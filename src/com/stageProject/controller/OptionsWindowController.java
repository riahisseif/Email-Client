package com.stageProject.controller;

import com.stageProject.EmailManager;
import com.stageProject.view.ColorTheme;
import com.stageProject.view.FontSize;
import com.stageProject.view.ViewFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;


public class OptionsWindowController extends BaseController implements Initializable {

    public OptionsWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

        @FXML
        private Slider fontSizePicker;

        @FXML
        private ChoiceBox<ColorTheme> themePicker;



    @FXML
        void applyButtonAction() {
        viewFactory.setColorTheme(themePicker.getValue());
        viewFactory.setFontSize(FontSize.values()[(int)(fontSizePicker.getValue())]);//acces to the font size and get it as an index
        System.out.println(viewFactory.getColorTheme());
        System.out.println(viewFactory.getFontSize());
        viewFactory.updateStyles();

        }

        @FXML
        void cancelButtonAction() {
            Stage stage = (Stage) fontSizePicker.getScene().getWindow();//get the scene and the window
            viewFactory.closeStage(stage);//just cloase the stage
        }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTthemePicker();
        setUpSizePicker();
    }

    private void setUpSizePicker() {
        fontSizePicker.setMin(0);
        fontSizePicker.setMax(FontSize.values().length-1);
        fontSizePicker.setValue(viewFactory.getFontSize().ordinal());
        fontSizePicker.setMajorTickUnit(1);
        fontSizePicker.setMinorTickCount(0);
        fontSizePicker.setBlockIncrement(1);
        fontSizePicker.setSnapToTicks(true);
        fontSizePicker.setShowTickMarks(true);
        fontSizePicker.setShowTickLabels(true);
        fontSizePicker.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double aDouble) {
                int i=aDouble.intValue();
                return FontSize.values()[i].toString();
            }

            @Override
            public Double fromString(String s) {
                return null;
            }
        });
        fontSizePicker.valueProperty().addListener((obs,oldval,newval)->{
            fontSizePicker.setValue(newval.intValue());
        });//lamda expression
    }

    private void setUpTthemePicker() {
        themePicker.setItems(FXCollections.observableArrayList(ColorTheme.values()));
        themePicker.setValue(viewFactory.getColorTheme());

    }
}
