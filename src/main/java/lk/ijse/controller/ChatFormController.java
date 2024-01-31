package lk.ijse.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.client.Client;
import lk.ijse.dto.ChatDto;
import lk.ijse.model.ChatModel;
import lombok.Setter;

import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChatFormController {
    public TextField txtMsg;
    public AnchorPane emojiPane;
    public VBox vBox;
    public ScrollPane scrollPane;
    public Pane header;
    public Label lblName;
    public ImageView imageView;
    public Pane textArea;
    public AnchorPane root;
    private Client client;
    @Setter
    private LoginFormController loginFormController;
    ChatModel model = new ChatModel();


    public void retrieveMsg() throws IOException, SQLException {
        ArrayList<ChatDto> chat = model.getChat(client.getName());
        System.out.println(chat);

        if (!(chat == null)) {
            for (ChatDto chatDto : chat) {
                if (chatDto.getId().equals("me") && chatDto.getImage() == null) {
                    appendText(chatDto.getMessage());
                } else if (chatDto.getId().equals("me") && chatDto.getMessage() == null){
                    setImage(chatDto.getImage().readAllBytes(), chatDto.getId());
                    } else if (!chatDto.getId().equals("me") && chatDto.getImage() == null) {
                        writeMessage(chatDto.getMessage());
                        } else if (!chatDto.getId().equals("me") && chatDto.getMessage() == null) {
                            setImage(chatDto.getImage().readAllBytes(), chatDto.getId());
                            }
            }
        }
    }
    public void setClient(Client client) throws IOException, SQLException {
        this.client = client;
       // retrieveMsg();
        String msg =" joined the chat";
        appendText(msg);
        client.sendMessage(msg);
    }

    public void grinningFaceEmojiOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE00");
        emojiPane.setVisible(false);
    }

    public void grinningSquintingOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE06");
        emojiPane.setVisible(false);
    }

    public void smilingFaceWithOpenHandsOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83E\uDD17");
        emojiPane.setVisible(false);
    }

    public void grinningFaceWithSweatOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE05");
        emojiPane.setVisible(false);
    }

    public void faceWithTearsOfJoyOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE02");
        emojiPane.setVisible(false);
    }

    public void cryingFaceOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE22");
        emojiPane.setVisible(false);
    }

    public void sunglassesFaceOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE0E");
        emojiPane.setVisible(false);
    }

    public void smilingFaceWithHeartEyesOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE0D");
        emojiPane.setVisible(false);
    }

    public void smilingFaceWithHornsOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE08");
        emojiPane.setVisible(false);
    }

    public void thumbsUpOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDC4D");
        emojiPane.setVisible(false);
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        try {
            String text = txtMsg.getText();
            if (text != null) {
                appendText(text);
                client.sendMessage(text);
                txtMsg.clear();
            } else {
                ButtonType ok = new ButtonType("Ok");
                ButtonType cancel = new ButtonType("Cancel");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Empty message. Is it ok?", ok, cancel);
                alert.showAndWait();
                ButtonType result = alert.getResult();
                if (result.equals(ok)) {
                    client.sendMessage(null);
                }
                txtMsg.clear();
            }

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void appendText(String message) throws SQLException {
       // print in my chat
        if (message.startsWith(" joined")) {
            HBox hBox = new HBox();
            hBox.setStyle("-fx-alignment: center;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
            Label messageLbl = new Label(message);
            messageLbl.setStyle("-fx-background-color: rgb(128,128,128);-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
            hBox.getChildren().add(messageLbl);
            vBox.getChildren().add(hBox);
            model.saveChat(new ChatDto(client.getName(),"me",message,null));
        //    System.out.println(message);   //my msg
        } else {
            HBox hBox = new HBox();
            hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
            Label messageLbl = new Label(message);
            messageLbl.setStyle("-fx-background-color:  purple;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
            hBox.getChildren().add(messageLbl);
            vBox.getChildren().add(hBox);
            model.saveChat(new ChatDto(lblName.getText(),"me",message,null));
         //   System.out.println(message);   //my msg

        }
    }

    public void btnEmojiOnAction(ActionEvent actionEvent) {
        emojiPane.setVisible(!emojiPane.isVisible());
    }

    public void txtMessageOnAction(ActionEvent actionEvent) {
        btnSendOnAction(actionEvent);
    }

    public void btnAttachmentOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg","*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                InputStream inputStream = new FileInputStream(selectedFile);
                byte[] bytes = Files.readAllBytes(selectedFile.toPath());
                HBox hBox = new HBox();
                hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center-right;");

                // Display the image in an ImageView or any other UI component
                ImageView imageView = new ImageView(new Image(new FileInputStream(selectedFile)));
                imageView.setStyle("-fx-padding: 10px;");
                imageView.setFitHeight(180);
                imageView.setFitWidth(100);

                hBox.getChildren().addAll(imageView);
                vBox.getChildren().add(hBox);

                client.sendImage(bytes);
                model.saveChat(new ChatDto(lblName.getText(),"me",null,inputStream));
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setImage(byte[] bytes, String sender) {
        HBox hBox = new HBox();
        Label messageLbl = new Label(sender);
        messageLbl.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");

        hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; " + (sender.equals(client.getName()) ? "-fx-alignment: center-right;" : "-fx-alignment: center-left;"));
        // Display the image in an ImageView or any other UI component
        Platform.runLater(() -> {
            ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(bytes)));
            imageView.setStyle("-fx-padding: 10px;");
            imageView.setFitHeight(180);
            imageView.setFitWidth(100);

            InputStream image = new ByteArrayInputStream(bytes);
            try {
                model.saveChat(new ChatDto(lblName.getText(),sender,null,image));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            hBox.getChildren().addAll(messageLbl, imageView);
            vBox.getChildren().add(hBox);
        });
    }

    public void writeMessage(String message) throws SQLException {
            //print msg on other clients
            HBox hBox = new HBox();
            hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
            Label messageLbl = new Label(message);
            messageLbl.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
            hBox.getChildren().add(messageLbl);
            model.saveChat(new ChatDto(lblName.getText(),null,message,null));
            Platform.runLater(() -> vBox.getChildren().add(hBox));
         //   System.out.println(message);  //with sender name
    }
}
