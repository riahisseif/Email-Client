module Seee {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;
    requires activation;
    requires java.mail;
    requires java.desktop;

    opens com.stageProject;
    opens com.stageProject.view;
    opens com.stageProject.controller;
    opens com.stageProject.model;
}