module group1.library_management_system_java {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires net.synedra.validatorfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
  requires eu.hansolo.tilesfx;
  requires com.almasb.fxgl.all;
    requires javafx.graphics;
    requires java.sql;
  requires java.desktop;
  requires com.h2database;

  opens LMS to javafx.fxml;
  exports LMS;
}