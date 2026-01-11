package com.stockwise;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Image icon = new Image(getClass().getResourceAsStream("/images/iconLogo.png"));
        stage.getIcons().add(icon);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/login.fxml"));

        Scene scene = new Scene(loader.load());

        scene.getStylesheets().add(
                getClass().getResource("/css/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("StockWise");
        stage.show();

        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }
}
