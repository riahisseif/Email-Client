package com.stageProject.controller;

import com.stageProject.EmailManager;
import com.stageProject.controller.services.EmailSenderService;
import com.stageProject.model.EmailAccount;
import com.stageProject.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComposeMessageController extends BaseController implements Initializable {

    private List<File> attachments = new ArrayList<File>();// array liste of files

    @FXML
    private TextField recipientTextFIeld;

    @FXML
    private TextField subjectTextField;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private Label errorLabe;

    @FXML
    private ChoiceBox<EmailAccount> emailAccountChoice;//ChoiceBox with emailaccount

    @FXML
    void attachBtnAction() {

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null){
            attachments.add(selectedFile);
        }

    }

    @FXML
    void sendButtonAction() {
        //here we have should evrey thing
        //we will build an email sender service
        EmailSenderService emailSenderService = new EmailSenderService(
                emailAccountChoice.getValue(),
                subjectTextField.getText(),
                recipientTextFIeld.getText(),
                htmlEditor.getHtmlText(),//for the content
                attachments
        );
        emailSenderService.start();//start the service
        emailSenderService.setOnSucceeded(e->{
            EmailSendingResult emailSendingResult = emailSenderService.getValue();//get the value for the test
            switch (emailSendingResult){
                case SUCCESS:
                    Stage stage = (Stage) recipientTextFIeld.getScene().getWindow();//get the stage
                    viewFactory.closeStage(stage);//close it
                    break;
                case FAILED_BY_PROVIDER:
                    errorLabe.setText("provide error ");
                    break;
                case FAILED_BY_UNEXPECTED_ERROR:
                    errorLabe.setText("Unexpected error!");
                    break;

            }



        });//where it succeded
    }
    public ComposeMessageController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailAccountChoice.setItems(emailManager.getEmailAccounts());
        emailAccountChoice.setValue(emailManager.getEmailAccounts().get(0));//get the first emil in the choisebox
    }
}
