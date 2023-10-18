package com.stageProject.controller.services;

import com.stageProject.model.EmailMessage;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;

public class MessageRendererService extends Service {
    //this class help us to read the messages and showing our messages
    private EmailMessage emailMessage;//this email to visulize
    private WebEngine webEngine;//here we will show our messages
    private StringBuffer stringBuffer;//that will hold the content

    public MessageRendererService(WebEngine webEngine) {//MessageRendererService will get only the webEngine
        this.webEngine = webEngine;
        this.stringBuffer=new StringBuffer();//the stringbuffer will be just initilazed
        this.setOnSucceeded(event -> {
            displayMessage();
        });

    }

    //here evrey time we will load the message we will set it
    public  void setEmailMessage(EmailMessage emailMessage){

        this.emailMessage=emailMessage;
    }
    private void displayMessage(){//load the content we we start the the function display
        webEngine.loadContent(stringBuffer.toString());
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    loadMessage();//try to load the message in our task
                } catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        };


    }
    private void loadMessage() throws MessagingException, IOException {//this methode will load the message
        stringBuffer.setLength(0);//we clean the stringbuffer
        Message message=emailMessage.getMessage();//we get the message
        String contentType = message.getContentType();//we get the content type
        //contentype can be simple or multipart type
        if(isSimpleType(contentType)){//simple case
            stringBuffer.append(message.getContent().toString());
        } else if(isMultipartType(contentType)){
            Multipart multipart = (Multipart) message.getContent();
            loadMultipart(multipart, stringBuffer);//load the message

        }
    }

    private void loadMultipart(Multipart multipart, StringBuffer stringBuffer) throws MessagingException, IOException {//this function need a multipaert object and string buffer
        //it is a recrusive function
        for (int i = multipart.getCount() - 1; i>=0; i--){
            BodyPart bodyPart = multipart.getBodyPart(i);
            String contentType = bodyPart.getContentType();
            if (isSimpleType(contentType)){
                stringBuffer.append(bodyPart.getContent().toString());
            } else if(isMultipartType(contentType)){
                Multipart multipart2 = (Multipart) bodyPart.getContent();//get othe multipart
                loadMultipart(multipart2, stringBuffer);
            }else if(!isTextPlain(contentType)){
                //here we get the attachments:
                MimeBodyPart mbp = (MimeBodyPart) bodyPart;//the attachment are of type MimeBodyPart
                emailMessage.addAttachment(mbp);//add to our email messsage

            }
        }
    }

    private boolean isTextPlain(String contentType){
        return  contentType.contains("TEXT/PLAIN");
    }

    private boolean isSimpleType(String contentType){
        if(contentType.contains("TEXT/HTML") ||
                contentType.contains("mixed")||
                contentType.contains("text")){
            return true;//simple type
        } else {
            return false;
        }
    }

    private boolean isMultipartType(String contentType){
        if(contentType.contains("multipart")){
            return true;//multipart type
        } else {
            return false;
        }
    }
}
