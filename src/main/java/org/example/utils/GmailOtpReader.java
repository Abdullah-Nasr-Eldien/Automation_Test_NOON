package org.example.utils;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.ComparisonTerm;
import jakarta.mail.search.ReceivedDateTerm;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GmailOtpReader {
    private static final Duration TIMEOUT = Duration.ofSeconds(90);
    private static final Pattern OTP_PATTERN = Pattern.compile(
            "(?:otp|verification\\s+code|one[- ]time\\s+(?:password|code)|security\\s+code|login\\s+code)"
                    + "[^\\d]{0,50}(\\d{4,8})",
            Pattern.CASE_INSENSITIVE);

    private GmailOtpReader() {
    }

    public static String waitForNoonOtp(Instant requestedAt)
            throws MessagingException, IOException, InterruptedException {
        String email = requiredEnvironmentVariable("GMAIL_EMAIL").trim();
        String appPassword = requiredEnvironmentVariable("GMAIL_APP_PASSWORD")
                .replaceAll("\\s", "");
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");
        properties.put("mail.imaps.connectiontimeout", "30000");
        properties.put("mail.imaps.timeout", "30000");
        properties.put("mail.imaps.writetimeout", "30000");

        Session session = Session.getInstance(properties);
        Instant deadline = Instant.now().plus(TIMEOUT);
        try (Store store = session.getStore("imaps")) {
            store.connect(email, appPassword);
            try (Folder inbox = store.getFolder("INBOX")) {
                inbox.open(Folder.READ_ONLY);
                while (Instant.now().isBefore(deadline)) {
                    inbox.getMessageCount();
                    Message[] messages = inbox.search(new ReceivedDateTerm(
                            ComparisonTerm.GE, Date.from(requestedAt)));
                    for (int i = messages.length - 1; i >= 0; i--) {
                        Message message = messages[i];
                        Date receivedAt = message.getReceivedDate();
                        if (receivedAt == null || receivedAt.toInstant().isBefore(requestedAt)) {
                            continue;
                        }

                        String body = readText(message);
                        jakarta.mail.Address[] senders = message.getFrom();
                        String sender = senders == null || senders.length == 0
                                ? "" : senders[0].toString();
                        String content = message.getSubject() + " " + sender + " " + body;
                        if (!content.toLowerCase(Locale.ROOT).contains("noon")) {
                            continue;
                        }

                        Matcher matcher = OTP_PATTERN.matcher(content);
                        if (matcher.find()) {
                            return matcher.group(1);
                        }
                    }
                    Thread.sleep(5000);
                }
            }
        }
        throw new IllegalStateException("No Noon OTP email arrived in Gmail within 90 seconds.");
    }

    private static String readText(Part part) throws MessagingException, IOException {
        if (part.isMimeType("text/*")) {
            return String.valueOf(part.getContent());
        }
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            StringBuilder text = new StringBuilder();
            for (int i = 0; i < multipart.getCount(); i++) {
                text.append(readText(multipart.getBodyPart(i))).append(' ');
            }
            return text.toString();
        }
        return "";
    }

    private static String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Set the " + name + " environment variable before running this test.");
        }
        return value;
    }
}
