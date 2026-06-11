package com.trashcamp.frontend;

import com.trashcamp.frontend.controller.DashboardController;
import com.trashcamp.frontend.controller.LoginController;
import com.trashcamp.frontend.model.OfficerSession;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Entry point aplikasi TrashCamp JavaFX.
 */
public class MainApp extends Application {

    private static MainApp instance;
    private Stage primaryStage;

    public static MainApp getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        instance = this;
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TrashCamp — Manajemen Sampah Pendakian");
        this.primaryStage.setMinWidth(900);
        this.primaryStage.setMinHeight(600);
        showLogin(null);
    }

    /** Tampilkan halaman Login. */
    public void showLogin(OfficerSession session) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        if (controller != null && session != null) {
            controller.setSession(session);
        }

        Scene scene = new Scene(root, 1100, 680);
        scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /** Tampilkan halaman Dashboard setelah login berhasil. */
    public void showDashboard(OfficerSession session) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
        Parent root = loader.load();

        DashboardController controller = loader.getController();
        if (controller != null) {
            controller.setOfficerSession(session);
            controller.refresh();
        }

        Scene scene = new Scene(root, 1280, 780);
        scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
