package com.stageProject;

import com.stageProject.controller.services.FetchFoldersService;
import com.stageProject.controller.services.FolderUpdateService;
import com.stageProject.model.EmailAccount;
import com.stageProject.model.EmailMessage;
import com.stageProject.model.EmailTreeItem;
import com.stageProject.view.IconResolver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import javax.mail.Flags;
import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {//hold the information about progrmae state
    private EmailMessage selectedMessage;//get the selected message
    private EmailTreeItem<String> selectedFolder;
    private IconResolver iconResolver = new IconResolver();
    private ObservableList<EmailAccount> emailAccounts = FXCollections.observableArrayList();
    //we need a getters to use this fields
    public  ObservableList<EmailAccount> getEmailAccounts(){
        return  emailAccounts;
    }


    //we need getters and setters to use them in main window control
    public EmailMessage getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(EmailMessage selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public EmailTreeItem<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }


    private FolderUpdateService folderUpdateService;
    //Folder holding
   //here we have access to ll the folder
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<String>("");//treeitem to hold our folder
    public EmailTreeItem<String> getFoldersRoot(){return foldersRoot;
    }
    private List<Folder> folderList=new ArrayList<>();
    //this information will pass to mainwindow

    public List<Folder> getFolderList(){
        return this.folderList;
    }

    public EmailManager(){
        folderUpdateService=new FolderUpdateService(folderList);
        folderUpdateService.start();
    }
    public void addEmailAccount(EmailAccount emailAccount){//this methode recive an email account argements
        emailAccounts.add(emailAccount);
        EmailTreeItem<String> treeItem = new EmailTreeItem<String>(emailAccount.getAddress());
        treeItem.setGraphic(iconResolver.getIconForFolder(emailAccount.getAddress()));
        FetchFoldersService fetchFoldersService = new FetchFoldersService(emailAccount.getStore(), treeItem,folderList);
        fetchFoldersService.start();
        foldersRoot.getChildren().add(treeItem);
    }


    public void setRead() {
        try {
            selectedMessage.setRead(true);//is read
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);//set the message read
            selectedFolder.decrementMessagesCount();//the number of message dicrement
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public void setUnRead() {
        try {
            selectedMessage.setRead(false);//is unread
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, false);//set the message unread
            selectedFolder.incrementMessagesCount();//the number of message incriment
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public void deleteSelectedMessage() {
        try {

            selectedMessage.getMessage().setFlag(Flags.Flag.DELETED, true);//get the deleted mail
            selectedFolder.getEmailMessages().remove(selectedMessage);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
