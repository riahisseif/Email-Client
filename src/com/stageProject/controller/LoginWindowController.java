package com.stageProject.controller;

import com.stageProject.EmailManager;
import com.stageProject.controller.services.LoginService;
import com.stageProject.model.EmailAccount;
import com.stageProject.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController extends BaseController implements Initializable {

    @FXML
    private Label errorLabel;

    @FXML
    private TextField emailAddressFied;

    @FXML
    private PasswordField passwordField;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void loginButtonAction() {
        if(fieldsAreValid()){
            System.out.println("cliK!!");
            EmailAccount emailAccount = new EmailAccount(emailAddressFied.getText(), passwordField.getText());
            LoginService loginService = new LoginService(emailAccount, emailManager);
            loginService.start();//this methode do all the tasks
            loginService.setOnSucceeded(event -> {//called when the task is succeded
                EmailLoginResult emailLoginResult= loginService.getValue();//we get the values from the servecies

                switch (emailLoginResult) {
                    case SUCCESS:
                        System.out.println("login succesfull!!!" + emailAccount);
                        if(!viewFactory.isMainViewInitialized()) {//if it is not initialezed
                            viewFactory.showMainWindow();
                        }
                        Stage stage =(Stage) errorLabel.getScene().getWindow();
                        viewFactory.closeStage(stage);
                        return;
                    case FAILED_BY_CREDENTIALS:
                        errorLabel.setText("invalid credentials!");
                        return;
                    case FAILED_BY_UNEXPECTED_ERROR:
                        errorLabel.setText("unexpected error!");
                        return;
                    default:
                        return;
                }

            });


        }



    }

    private boolean fieldsAreValid() {
        if(emailAddressFied.getText().isEmpty()) {
            errorLabel.setText("Please fill email");
            return false;
        }
        if(passwordField.getText().isEmpty()) {
            errorLabel.setText("Please fill password");
            return false;
        }

        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailAddressFied.setText("seifbvb36@gmail.com");
        passwordField.setText("azertyuiop1920");
    }
}