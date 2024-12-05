package Amazon.utilities;


import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.checkerframework.checker.signature.qual.ClassGetSimpleName;

import Base.AmazonConfig;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailSendingUtils {

    private static final String FROM_EMAIL = "456e22e7d182aa";
    private static final String PASSWORD = "dca7b35f28501c";
    private static final String HOST = "sandbox.smtp.mailtrap.io";
    private static final int PORT = 2525;

    public static void sendEmail(String suiteName,String className, String releaseVersion, List<TestSummary> testSummaries, List<String> recipients) {
        try {
            // Configure email properties
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("tls", "yes");
            properties.put("ssl", "no");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", HOST);
            properties.put("mail.smtp.port", PORT);

            // Authenticate email
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL,PASSWORD);
                }
            });
            // Compose the email
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            for (String recipient : recipients) {
            	System.out.println(recipient);
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }
            message.setSubject("Test Suite Execution Report: " + suiteName + " - " + getCurrentDate());

            // Create email body with HTML content
            String emailBody = generateEmailBody(suiteName, releaseVersion, testSummaries);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(emailBody, "text/html");

            // Attach HTML report (optional)
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile(AmazonConfig.getSharedPath()+className +".html");

            // Combine all parts into one email
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachment);
            message.setContent(multipart);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully to recipients.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateEmailBody(String suiteName, String releaseVersion, List<TestSummary> testSummaries) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h2 style='background-color:green;color:white;padding:10px;'>")
          .append("Suite Name: ").append(suiteName)
          .append(" | Date: ").append(getCurrentDate())
          .append(" | Release Version: ").append(releaseVersion)
          .append("</h2>");
        sb.append("<table border='1' style='border-collapse:collapse;width:100%;'>");
        sb.append("<tr style='background-color:#f2f2f2;'>")
          .append("<th>Class Name</th>")
          .append("<th>Status</th>")
          .append("<th>Execution Time</th>")
          .append("</tr>");

        // Sort failed tests to appear at the top
        testSummaries.sort(Comparator.comparing(TestSummary::isPassed).reversed());
        for (TestSummary summary : testSummaries) {
            String rowColor = summary.isPassed() ? "background-color:green;color:white;" : "background-color:red;color:white;";
            sb.append("<tr>")
              .append("<td><a href='").append(summary.getReportLink()).append("' style='color:blue;'>")
              .append(summary.getClassName()).append("</a></td>")
              .append("<td style='").append(rowColor).append("'>")
              .append(summary.isPassed() ? "PASSED" : "FAILED").append("</td>")
              .append("<td>").append(summary.getExecutionTime()).append("</td>")
              .append("</tr>");
        }
        sb.append("</table>");
        sb.append("</body></html>");
        return sb.toString();
    }

    private static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

//    // TestSummary class to hold test data
//    public static class TestSummary {
//        private String className;
//        private boolean passed;
//        private String executionTime;
//        private String reportLink;
//
//        public TestSummary(String className, boolean passed, String executionTime, String reportLink) {
//            this.className = className;
//            this.passed = passed;
//            this.executionTime = executionTime;
//            this.reportLink = reportLink;
//        }
//
//        public String getClassName() {
//            return className;
//        }
//
//        public boolean isPassed() {
//            return passed;
//        }
//
//        public String getExecutionTime() {
//            return executionTime;
//        }
//
//        public String getReportLink() {
//            return reportLink;
//        }
//    }
}
