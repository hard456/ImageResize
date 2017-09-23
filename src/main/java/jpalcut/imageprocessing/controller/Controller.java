package jpalcut.imageprocessing.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import jpalcut.imageprocessing.ImageResize;

import java.io.File;
import java.util.List;

public class Controller {

    @FXML
    public TextField widthField, heightField;
    @FXML
    public Label imageCount, statusLabel;
    @FXML
    public Button resizeImages, selectImages;

    List<File> fileList;

    public void selectImages() {
        //Filter file type
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files", "*.jpg", "*.jpeg");

        //Initialization fileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);

        //List of files
        fileList = fileChooser.showOpenMultipleDialog(null);

        //Show number of chosen files
        if (fileList == null) {
            imageCount.setText("No images");
            resizeImages.setDisable(true);
            statusLabel.setText("Choose files");
        }
        else {
            if (fileList.size() == 1) {
                imageCount.setText("1 image");
            } else {
                imageCount.setText(fileList.size() + " images");
            }
            resizeImages.setDisable(false);
            statusLabel.setText("");
        }
    }

    public void resizeImages(){

        selectImages.setDisable(true);
        resizeImages.setDisable(true);

        //New width and height
        int width = 0, height = 0;

        try {
            width = Integer.parseInt(widthField.getText());
            height = Integer.parseInt(heightField.getText());
        }
        catch (Exception e){
            //Catch the wrong file size
            statusLabel.setText("Fill the image size");
            selectImages.setDisable(false);
            resizeImages.setDisable(false);
            return;
        }

        widthField.setDisable(true);
        heightField.setDisable(true);

        ImageResize resize = new ImageResize(fileList, width, height);
        resize.start();

    }

}
