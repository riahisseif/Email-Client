package com.stageProject.controller;

import com.stageProject.EmailManager;
import com.stageProject.controller.services.MessageRendererService;
import com.stageProject.model.EmailMessage;
import com.stageProject.model.EmailTreeItem;
import com.stageProject.model.SizeInteger;
import com.stageProject.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MainWindowController extends BaseController implements Initializable {
    //create menu items
    private MenuItem markUnreadMenuItem = new MenuItem("mark as unread");
    private MenuItem deleteMessageMenuItem = new MenuItem("delete message");
    private MenuItem showMessageDetailsMenuItem = new MenuItem("view details");

    @FXML
    private TreeView<String> emailsTreeView;//we change the parametre

    @FXML

    private TableView<EmailMessage> emailsTableView;

    @FXML
    private TableColumn<EmailMessage, String> senderCol;//EmailMessage and string sont les type ...

    @FXML
    private TableColumn<EmailMessage, String> subjectCol;

    @FXML
    private TableColumn<EmailMessage, String> recipientCol;

    @FXML
    private TableColumn<EmailMessage, SizeInteger> sizeCol;

    @FXML
    private TableColumn<EmailMessage, Date> dateCol;

    @FXML
    private WebView emailWebView;

    private MessageRendererService messageRendererService;

    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void optionsAction() {
        viewFactory.showOptionsWindow();

    }
    @FXML
    void addAccountAction() {
        viewFactory.showLoginWindow();

    }

    @FXML
    void ComposeMessageAction() {
        viewFactory.showComposeMessageWindow();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEmailsTreeView();
        setUpEmailsTableView();
        setUpFolderSelection();//choisir le folder ou on va importer les emails
        setUpBoldRows();//pour indiquer les message qui ne sont pas liée
        setUpMessageRendererService();
        setUpMessageSelection();
        setUpContextMenus();

    }


    private void setUpContextMenus() {
        markUnreadMenuItem.setOnAction(event -> {
            emailManager.setUnRead();//make market
        });
        deleteMessageMenuItem.setOnAction(event -> {
            emailManager.deleteSelectedMessage();//delete message
            emailWebView.getEngine().loadContent("");//clear our email view
        });
        showMessageDetailsMenuItem.setOnAction(event -> {//set up the menu item
            viewFactory.showEmailDetailsWindow();
        });
    }

    private void setUpMessageSelection() {
        emailsTableView.setOnMouseClicked(event -> {//lamda expression (we use the methode setOnMouseClicked
            EmailMessage emailMessage = emailsTableView.getSelectionModel().getSelectedItem();//get the selection then we get the item
            if(emailMessage != null){//if an emailsmessage not null
                emailManager.setSelectedMessage(emailMessage);
                if(!emailMessage.isRead()){
                    emailManager.setRead();
                }
                emailManager.setSelectedMessage(emailMessage);
                messageRendererService.setEmailMessage(emailMessage);//set the email on the messageRendererService
                messageRendererService.restart();//restart the event
                //rq! : the start methode use just one time
            }
        });
    }

    private void setUpMessageRendererService() {
        messageRendererService = new MessageRendererService(emailWebView.getEngine());//we set the message for show
        //we will call this message every time we select a message

    }

    private void setUpBoldRows() {
        emailsTableView.setRowFactory(new Callback<TableView<EmailMessage>, TableRow<EmailMessage>>() {
            @Override
            public TableRow<EmailMessage> call(TableView<EmailMessage> emailMessageTableView) {
                return new TableRow<EmailMessage>(){
                    @Override
                    protected void updateItem(EmailMessage item, boolean empty){
                        super.updateItem(item, empty);//supper constracteur
                        if(item != null) {
                            if (item.isRead()) {//if the item is read
                                setStyle("");
                            } else {
                                setStyle("-fx-font-weight: bold");
                            }
                        }
                    }
                };
            }
        });
    }


    private void setUpFolderSelection() {//indicate witch folder we see our messages
        emailsTreeView.setOnMouseClicked(e->{
            EmailTreeItem<String> item = (EmailTreeItem<String>)emailsTreeView.getSelectionModel().getSelectedItem();
            if (item != null) {//if we press in the items
                emailManager.setSelectedFolder(item);
                emailsTableView.setItems(item.getEmailMessages());//access to the emails
            }
        });
    }

    private void setUpEmailsTableView() {
        senderCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("sender")));//relieé le javafx avec java
        subjectCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("subject")));
        recipientCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("recipient")));
        sizeCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, SizeInteger>("size")));
        dateCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, Date>("date")));
        emailsTableView.setContextMenu(new ContextMenu(markUnreadMenuItem,deleteMessageMenuItem,showMessageDetailsMenuItem));//textMenu items

    }

    private void setUpEmailsTreeView() {
        emailsTreeView.setRoot(emailManager.getFoldersRoot());//need to change the parmaetre
        //root because it is an empty elements
        emailsTreeView.setShowRoot(false);
    }
}

