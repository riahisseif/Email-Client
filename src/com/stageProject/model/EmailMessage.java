package com.stageProject.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.mail.Message;
import javax.mail.internet.MimeBodyPart;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmailMessage {
    //remplir all the fileds (send, date , size ....)
    //le probleme que le table view ne support pas the string mais il support les simple objet
    private SimpleStringProperty subject;
    private SimpleStringProperty sender;
    private SimpleStringProperty recipient;
    private SimpleObjectProperty<SizeInteger> size; //type size integer
    private SimpleObjectProperty<Date> date;
    private boolean isRead;//indicateur
    private Message message;
    private List<MimeBodyPart> attachmentList = new ArrayList<MimeBodyPart>();//array liste of mimebodypart
    private boolean hasAttachments = false;//indicateur

    //we write the constracteur but not implemented because it is a little bit complicated
    public EmailMessage(String subject, String sender, String recipient, int size, Date date, boolean isRead, Message message){
        this.subject = new SimpleStringProperty(subject);
        this.sender = new SimpleStringProperty(sender);
        this.recipient = new SimpleStringProperty(recipient);
        this.size = new SimpleObjectProperty<SizeInteger>(new SizeInteger(size));
        this.date = new SimpleObjectProperty<Date>(date);
        this.isRead = isRead;
        this.message = message;
    }
    //getters and setters
    public boolean hasAttachments(){
        return hasAttachments;
    }
    public List<MimeBodyPart> getAttachmentList(){
        return attachmentList;
    }

    public String getSubject(){
        return this.subject.get();
    }
    public  String getSender(){
        return this.sender.get();
    }
    public String getRecipient(){
        return this.recipient.get();
    }
    public SizeInteger getSize(){
        return this.size.get();
    }
    public Date getDate(){
        return this.date.get();
    }

    public boolean isRead() {
        return isRead;
    }
    public void setRead(boolean read) {
        isRead = read;
    }
    public Message getMessage(){
        return this.message;
    }

    public void addAttachment(MimeBodyPart mbp) {
        hasAttachments = true;//we has attachment
        attachmentList.add(mbp);
        try {
            System.out.println("Added attach: " + mbp.getFileName());//test
        } catch ( Exception e) {
            e.printStackTrace();
        }

    }
    }

