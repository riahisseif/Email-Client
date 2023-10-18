package com.stageProject.controller.services;

import com.stageProject.controller.EmailSendingResult;
import com.stageProject.model.EmailAccount;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.List;

public class EmailSenderService extends Service<EmailSendingResult>  {

    //objet pour envoyer les message
    private EmailAccount emailAccount;
    private String subject;
    private String recipient;
    private String content;
    private List<File> attachments;

    //constacteur
    public EmailSenderService(EmailAccount emailAccount, String subject, String recipient, String content,List<File> attachments ) {
        this.emailAccount = emailAccount;
        this.subject = subject;
        this.recipient = recipient;
        this.content = content;
        this.attachments=attachments;
    }

    @Override
    protected Task<EmailSendingResult> createTask() {
        return new Task<EmailSendingResult>() {
            @Override
            protected EmailSendingResult call()  {

                try {
                    //Create the message:
                    MimeMessage mimeMessage = new MimeMessage(emailAccount.getSession());
                    mimeMessage.setFrom(emailAccount.getAddress());//set the adress
                    mimeMessage.addRecipients(Message.RecipientType.TO, recipient);//add the recepient
                    mimeMessage.setSubject(subject);//set the subject
                    //Set the content:
                    Multipart multipart = new MimeMultipart();
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(content, "text/html");
                    multipart.addBodyPart(messageBodyPart);
                    mimeMessage.setContent(multipart);
                    //adding  the attahments
                    if(attachments.size()>0){
                        for ( File file: attachments){
                            MimeBodyPart mimeBodyPart = new MimeBodyPart();
                            DataSource source = new FileDataSource(file.getAbsolutePath());
                            mimeBodyPart.setDataHandler(new DataHandler(source));
                            mimeBodyPart.setFileName(file.getName());
                            multipart.addBodyPart(mimeBodyPart);
                        }
                    }


                    //Sending the message:
                    Transport transport = emailAccount.getSession().getTransport();//use transpoet object
                    transport.connect(
                            emailAccount.getProperties().getProperty("outgoingHost"),
                            emailAccount.getAddress(),//get user
                            emailAccount.getPassword()
                    );
                    transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                    transport.close();
                    return EmailSendingResult.SUCCESS;
                } catch (MessagingException e) {
                    e.printStackTrace();
                    return EmailSendingResult.FAILED_BY_PROVIDER;
                }  catch (Exception e) {
                    e.printStackTrace();
                    return EmailSendingResult.FAILED_BY_UNEXPECTED_ERROR;
                }
            }
        };
    }
}
