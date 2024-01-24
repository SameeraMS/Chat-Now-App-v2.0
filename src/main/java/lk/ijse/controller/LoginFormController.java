package lk.ijse.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.client.Client;
import lk.ijse.dto.UserDto;
import lk.ijse.model.UserModel;
import lk.ijse.server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class LoginFormController {
    public JFXTextField txtName;

    public static String name;
    public ImageView imageView;
    public JFXPasswordField txtPassword;

    private File file;

    public void initialize() throws IOException {
        startServer();
    }

    public void txtUsernameOnAction(ActionEvent actionEvent) throws IOException {
        txtPassword.requestFocus();
    }

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {

        if (!txtName.getText().isEmpty() || !txtPassword.getText().isEmpty()) {
            String username = txtName.getText();
            String password = txtPassword.getText();

            boolean isExists = false;
            try {
                isExists = UserModel.existsUser(username);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (isExists) {
                try {
                    UserDto userDto = UserModel.userDetails(username);
                    if (!userDto.getPassword().equals(password)) {
                        new Alert(Alert.AlertType.WARNING, "Invalid username or password", ButtonType.OK).show();
                    } else {

                        if (userDto.getImage() != null){
                            imageView = new ImageView(new Image(userDto.getImage()));
                        } else {
                            imageView = new ImageView(new Image("/asserts/images/images.png"));
                        }

                        load();
                        txtName.clear();
                        txtPassword.clear();
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }else{
                new Alert(Alert.AlertType.WARNING, "Invalid username or password", ButtonType.OK).show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING, "Username and password are required", ButtonType.OK).show();
        }



    }

    private void load() throws IOException {
            Client client = new Client(txtName.getText(), imageView);
            Thread thread = new Thread(client);
            thread.start();
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
