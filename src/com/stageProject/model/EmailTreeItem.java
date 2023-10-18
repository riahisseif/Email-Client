package com.stageProject.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailTreeItem<String> extends TreeItem<String> {

    private String name;
    private ObservableList<EmailMessage> emailMessages;
    private int unreadMessagesCount;//pour les message non liée
    public EmailTreeItem(String name) {
        super(name);
        this.name = name;
        this.emailMessages = FXCollections.observableArrayList();//FXCollections parceque le constracteur ne support pas les interfaces
    }
    //the best place to store our message is here
    public void addEmail(Message message) throws MessagingException {
        EmailMessage emailMessage=fetchMessage(message);//came the message
        emailMessages.add(emailMessage);
      //test   System.out.println("added to " + name + " " + message.getSubject());//add the mail to his folder

    }
    public void addEmailToTop(Message message) throws MessagingException {//this methode will be similaire to the other methode
        EmailMessage emailMessage=fetchMessage(message);//came the messgage // njibou el message ya3ni
        emailMessages.add(0,emailMessage);
    }

    private EmailMessage fetchMessage(Message message) throws MessagingException {
        boolean messageIsRead = message.getFlags().contains(Flags.Flag.SEEN);//return si le message was reading or not
        EmailMessage emailMessage = new EmailMessage(//we get all the message
                message.getSubject(),
                message.getFrom()[0].toString(),//return array
                message.getRecipients(MimeMessage.RecipientType.TO)[0].toString(),//return message type and array
                message.getSize(),
                message.getSentDate(),
                messageIsRead,
                message
        );
        emailMessages.add(emailMessage);//add the email message
        if(!messageIsRead){
            incrementMessagesCount();
        }
        return emailMessage;
    }
    public void incrementMessagesCount(){
        unreadMessagesCount++;//increment
        updateName();
    }
    public void decrementMessagesCount() {
        unreadMessagesCount--;
        updateName();//exemple inbox 2 ==>1
    }

    private void updateName(){
        if(unreadMessagesCount > 0){
            this.setValue((String)(name + "(" + unreadMessagesCount + ")"));//set the value for the unread message
        } else {
            this.setValue(name);
        }
    }

    public ObservableList<EmailMessage> getEmailMessages() {
        return emailMessages;
    }



}
