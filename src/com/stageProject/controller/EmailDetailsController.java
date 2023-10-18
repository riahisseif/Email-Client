package com.stageProject.controller;

import com.stageProject.EmailManager;
import com.stageProject.controller.services.MessageRendererService;
import com.stageProject.model.EmailMessage;
import com.stageProject.view.ViewFactory;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import java.awt.Desktop;
import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import java.net.URL;
import java.util.ResourceBundle;

public class EmailDetailsController extends BaseController implements Initializable {
    private String LOCATION_OF_DOWNLOADS = System.getProperty("E:\\") ;

    @FXML
    private Label attachmentLabel;

    @FXML
    private WebView webView;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label sendertLabel;

    @FXML
    private HBox hBoxDownloads;

    public EmailDetailsController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {//the methode where evreything is happen
        EmailMessage emailMessage = emailManager.getSelectedMessage();//the current message
        subjectLabel.setText(emailMessage.getSubject());
        sendertLabel.setText(emailMessage.getSender());
        loadAttachments(emailMessage);

        MessageRendererService messageRendererService = new MessageRendererService(webView.getEngine());
        messageRendererService.setEmailMessage(emailMessage);
        messageRendererService.restart();


    }

    private void loadAttachments(EmailMessage emailMessage){
        if (emailMessage.hasAttachments()){
            for (MimeBodyPart mimeBodyPart: emailMessage.getAttachmentList()){
                try {
                    AttachmentButton button = new AttachmentButton (mimeBodyPart);
                    hBoxDownloads.getChildren().add(button);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            attachmentLabel.setText("");
        }
    }

    private class AttachmentButton extends Button {
        private MimeBodyPart mimeBodyPart;
        private String downloadedFilePath;

        public  AttachmentButton (MimeBodyPart mimeBodyPart) throws MessagingException {//constracteur
            this.mimeBodyPart = mimeBodyPart;
            this.setText(mimeBodyPart.getFileName());
            this.downloadedFilePath = LOCATION_OF_DOWNLOADS + mimeBodyPart.getFileName();

            this.setOnAction( e -> downloadAttachment());//when we clique
        }
        private void downloadAttachment(){
            colorBlue();
            Service service=new Service() {
                @Override
                protected Task createTask() {
                    return new Task() {
                        @Override
                        protected Object call() throws Exception {
                            mimeBodyPart.saveFile(downloadedFilePath);
                            return null;
                        }
                    };
                }
            };

            service.restart();
            service.setOnSucceeded(e->{
                colorGreen();
                this.setOnAction( e2->{
                    File file = new File(downloadedFilePath);
                    Desktop desktop = Desktop.getDesktop();
                    if(file.exists()){
                        try {
                            desktop.open(file);
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                    }
                });
            });

        }
        private void colorBlue(){
            this.setStyle("-fx-background-color: Blue");
        }
        private void colorGreen(){
            this.setStyle("-fx-background-color: Green");
        }

    }


}
