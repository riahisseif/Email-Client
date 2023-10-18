package com.stageProject;

import com.stageProject.controller.persistence.PersistenceAccess;
import com.stageProject.controller.persistence.ValidAccount;
import com.stageProject.controller.services.LoginService;
import com.stageProject.model.EmailAccount;
import com.stageProject.view.ViewFactory;
import javafx.application.Application;

import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Launch extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    private PersistenceAccess persistenceAccess=new PersistenceAccess();
    private EmailManager emailManager=new EmailManager();
    @Override
    public void start(Stage primayStage) throws Exception {
        ViewFactory viewFactory = new ViewFactory(emailManager);
        List<ValidAccount> validAccountList = persistenceAccess.loadFromPersistence();
        if(validAccountList.size() > 0) {//there is more then password
            viewFactory.showMainWindow();
            for (ValidAccount validAccount: validAccountList){
                EmailAccount emailAccount = new EmailAccount(validAccount.getAddress(), validAccount.getPassword());
                LoginService loginService = new LoginService(emailAccount, emailManager);
                loginService.start();
            }
        } else {
            viewFactory.showLoginWindow();
        }
        //the persisitance should be early
    }



    @Override
    public void stop() throws Exception {
        List<ValidAccount> validAccountList = new ArrayList<ValidAccount>();
        for (EmailAccount emailAccount: emailManager.getEmailAccounts()){
            validAccountList.add(new ValidAccount(emailAccount.getAddress(), emailAccount.getPassword()));
        }
        persistenceAccess.saveToPersistence(validAccountList);
        //when the preograme is stoped we get the login and save it
    }
}
