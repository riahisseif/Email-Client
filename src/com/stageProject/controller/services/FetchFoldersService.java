package com.stageProject.controller.services;

import com.stageProject.model.EmailTreeItem;
import com.stageProject.view.IconResolver;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.util.List;


public class FetchFoldersService extends Service<Void>{//this classe have the acess to all the folder
    private Store store;
    private EmailTreeItem<String> foldersRoot;
    private List<Folder> folderList;
    private IconResolver iconResolver = new IconResolver();

    public FetchFoldersService(Store store, EmailTreeItem<String> foldersRoot, List<Folder> folderList) {
        this.store = store;
        this.foldersRoot = foldersRoot;
        this.folderList=folderList;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                fetchFolders();
                return null;
            }
        };
    }

    private void fetchFolders() throws MessagingException {
        Folder[] folders = store.getDefaultFolder().list();//stocker les folder
        handleFolders(folders, foldersRoot);//create les elements
    }
    private void handleFolders(Folder[] folders, EmailTreeItem<String> foldersRoot) throws MessagingException {
        for(Folder folder: folders){
            folderList.add(folder);//add the folder
            EmailTreeItem<String> emailTreeItem = new EmailTreeItem<String>(folder.getName());
            emailTreeItem.setGraphic(iconResolver.getIconForFolder(folder.getName()));
            foldersRoot.getChildren().add((emailTreeItem));
            foldersRoot.setExpanded(true);
            fetchMessagesFolder(folder,emailTreeItem);//use the folder // here we have access to the folderand the emailTreeItem
            addMessageListnerToFolder(folder,emailTreeItem);//this methode help us to add the message while the programme is running 
            if(folder.getType() == Folder.HOLDS_FOLDERS) {
                Folder[] subFolders =  folder.list();
                handleFolders(subFolders, emailTreeItem);//trie les element de messagerie(spam , corbeille ....)
                //now our programe support many acount and il sont separer
            }
        }

    }

    private void addMessageListnerToFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
        folder.addMessageCountListener(new MessageCountListener() {
            @Override
            public void messagesAdded(MessageCountEvent e) {
                //create an array because for each 5 seconde we can hava more an eamil
                for (int i=0;i<e.getMessages().length;i++){
                    try {
                        Message message=folder.getMessage(folder.getMessageCount()-i);//stocker les message dans les folder
                        //now we have the message so we need to add in our program
                        emailTreeItem.addEmailToTop(message);//emailTreeItem.addEmail() add the email in the buttom of our liste
                    } catch (MessagingException messagingException) {
                        messagingException.printStackTrace();
                    }
                }


            }

            @Override
            public void messagesRemoved(MessageCountEvent e) {
                System.out.println("message removed " + e);

            }
        });
    }

    private void fetchMessagesFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
        //create a sercices that get the message from each server
        Service fetchMessagesService=new Service() {
            @Override
            protected Task createTask() {//nar argument because it is not return anything
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        //start to write email geting logic
                        if (folder.getType() !=Folder.HOLDS_FOLDERS){
                            folder.open(Folder.READ_WRITE);//open the folder with read and write permesion
                            int folderSize=folder.getMessageCount();//get the size
                            for(int i=folderSize;i>0;i--){//from the big to the small
                                //System.out.println(folder.getMessage(i).getSubject());//test
                                emailTreeItem.addEmail(folder.getMessage(i));//add the email message from the folder
                            }
                        }
                        return null;
                    }
                };
            }
        };
        fetchMessagesService.start();//start the srvice
    }

}
