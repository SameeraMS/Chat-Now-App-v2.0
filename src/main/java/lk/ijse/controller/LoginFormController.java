package lk.ijse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.client.Client;
import lk.ijse.server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

public class LoginFormController {
    public JFXTextField txtName;

    public static String name;
    public ImageView imageView;
    public JFXTextField txtPassword;
    private File file;

    public void initialize() throws IOException {
        startServer();
    }

    public void txtUsernameOnAction(ActionEvent actionEvent) throws IOException {
        txtPassword.requestFocus();
    }

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        load();
        txtName.clear();
    }

    private void load() throws IOException {
        if (Pattern.matches("^[a-zA-Z\\s]+", txtName.getText())) {
            Client client = new Client(txtName.getText(), imageView);
            Thread thread = new Thread(client);
            thread.start();

            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/chatForm.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setTitle(txtName.getText()+"'s Chat");
            stage.setAlwaysOnTop(true);
        }
    }

    private void startServer() throws IOException {
        Server server = Server.getServerSocket();
        Thread thread = new Thread(server);
        thread.start();
    }


    public void btnSignupOnAction(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/signupForm.fxml"))));
        stage.setTitle("Sign up");
        stage.show();
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) throws IOException {
        btnLoginOnAction(actionEvent);
    }
}
