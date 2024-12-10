/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Asus
 */
public class EmailService {
    private static String emailFrom = "sonicyabsol@gmail.com";
    private static String passwordFrom = "uahpccbqyjmwhsyd";

    private Properties mProperties;
    private Session mSession;
    private MimeMessage mCorreo;

    public EmailService() {
        mProperties = new Properties();
    }

    private void createEmail(String emailTo, String subject, String content, String attachmentPath) {
        // Configuración de las propiedades SMTP
        mProperties.put("mail.smtp.host", "smtp.gmail.com");
        mProperties.put("mail.smtp.port", "587");
        mProperties.put("mail.smtp.auth", "true");
        mProperties.put("mail.smtp.starttls.enable", "true");
        mProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        mSession = Session.getInstance(mProperties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, passwordFrom);
            }
        });

        try {
            mCorreo = new MimeMessage(mSession);
            mCorreo.setFrom(new InternetAddress(emailFrom));
            mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            mCorreo.setSubject(subject);

            // Crear la parte del cuerpo del mensaje
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html; charset=utf-8");

            // Crear un multipart para agregar el cuerpo y el adjunto
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Adjuntar el archivo PDF
            if (attachmentPath != null && !attachmentPath.isEmpty()) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachmentPath);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                attachmentBodyPart.setFileName(attachmentPath);
                multipart.addBodyPart(attachmentBodyPart);
            }

            // Establecer el contenido del correo
            mCorreo.setContent(multipart);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    public void sendEmail(String destinatario, String asunto, String cuerpo, String rutaAdjunto) {
        createEmail(destinatario, asunto, cuerpo, rutaAdjunto);
        try {
            Transport.send(mCorreo);
            System.out.println("Correo enviado satisfactoriamente");
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
}
