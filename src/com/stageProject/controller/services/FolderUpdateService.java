package com.stageProject.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import java.util.List;

public class FolderUpdateService extends Service {
    private List<Folder> folderList;//have a liste of folder

    public FolderUpdateService(List<Folder> folderList) {
        this.folderList = folderList;
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                //this service start when out program start and will never end
                for (;;){
                    //for a linfinie
                    try {
                        Thread.sleep(5000);//sleep the task for 5s then ask for a new update
                        for(Folder folder:folderList){
                            if(folder.getType()!=Folder.HOLDS_FOLDERS && folder.isOpen()){//check
                                folder.getMessageCount();

                            }
                        }
                    }catch (Exception e){

                    }
                }

            }
        };
    }
}
