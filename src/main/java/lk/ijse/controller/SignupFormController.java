package lk.ijse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.dto.UserDto;
import lk.ijse.model.UserModel;

import java.io.*;
import java.sql.SQLException;

public class SignupFormController {
    public ImageView imageView;
    public JFXTextField txtpw1;
    public JFXTextField txtname;
    public JFXTextField txtpw2;
    private File file;

    public void btnSignupOnAction(ActionEvent actionEvent) {

        if (!(txtpw1.getText().isEmpty() || txtname.getText().isEmpty() || txtpw2.getText().isEmpty())) {
            try {
                String username = txtname.getText();
                String password = txtpw1.getText();
                String confirmPassword = txtpw2.getText();

                if (!password.equals(confirmPassword)) {
                    new Alert(Alert.AlertType.WARNING, "Password does not match", ButtonType.OK).show();
                } else {
                    boolean isExists = UserModel.existsUser(username);
                    if (!isExists) {
                        boolean isSaved;
                        if (file != null) {
                            InputStream inputStream = new FileInputStream(file);
                            isSaved = UserModel.saveUser(new UserDto(username, password, inputStream));
                        } else {
                            isSaved = UserModel.saveUser(new UserDto(username, password, null));
                        }
                        if (isSaved) {
                            new Alert(Alert.AlertType.INFORMATION, "Saved", ButtonType.OK).show();
                            btnBackOnAction(actionEvent);
                        }
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Username already exists", ButtonType.OK).show();
                    }
                }

            }catch(FileNotFoundException | SQLException e){
                    e.printStackTrace();
                } catch(IOException e){
                    throw new RuntimeException(e);
                }
        } else {
            new Alert(Alert.AlertType.WARNING, "All fields are required", ButtonType.OK).show();
        }

    }

    public void btnImageChooserOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the image");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);
        file = fileChooser.showOpenDialog(txtname.getScene().getWindow());
        if (file != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                imageView.setImage(new Image(fileInputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) txtname.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/loginform.fxml"))));
        stage.setTitle("Login");
        stage.show();
    }
}
