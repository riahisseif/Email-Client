package com.stageProject.controller.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceAccess {
    private String VALID_ACCOUNTS_LOCATION = System.getenv("E:\\") +File.separator+ "validAccounts.ser";//path to stor account
    public List<ValidAccount> loadFromPersistence(){
        //will be called when programme start
        List<ValidAccount> resultList = new ArrayList<ValidAccount>();
        try {
            FileInputStream fileInputStream = new FileInputStream(VALID_ACCOUNTS_LOCATION);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            List<ValidAccount> persistedList = (List<ValidAccount>) objectInputStream.readObject();
            resultList.addAll(persistedList);
        } catch ( Exception e){
            e.printStackTrace();
        }
        return resultList;

    }
    public void saveToPersistence(List<ValidAccount> validAccounts){
        try {
            File file = new File(VALID_ACCOUNTS_LOCATION);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(validAccounts);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
