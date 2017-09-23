package jpalcut.imageprocessing;

import static org.imgscalr.Scalr.*;

import javafx.application.Platform;
import jpalcut.imageprocessing.controller.Controller;
import org.imgscalr.Scalr;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageResize extends Thread {

    private Controller controller = (Controller) Main.getFXMLresizeStage().getController();
    private int width, height;
    private List<File> images;

    public ImageResize(List<File> images, int width, int height){
        this.width = width;
        this.height = height;
        this.images = images;
    }
    //Resize list of images
    public boolean resizeImages() {

        File directory = createDirectory(images.get(0));
        for (File file : images) {
            try {
                //Read image
                BufferedImage image = ImageIO.read(file);
                //Change image size
                image = changeImageSize(image, width, height);
                //Path for new resized image
                String path = directory.getAbsolutePath() + File.separator + file.getName();

                //Initialization for writing file
                ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // Needed see javadoc
                param.setCompressionQuality(1.0F);
                ImageOutputStream ios = ImageIO.createImageOutputStream(new File(path));
                writer.setOutput(ios);

                //Write file in directory
                writer.write(null, new IIOImage(image, null, null), param);

                //Loading animation
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            controller.statusLabel.setText("Processing image " + (images.indexOf(file)+1));
                        }
                        catch (Exception e){
                            System.out.println("Platform runLater exception");
                        }
                    }
                });


            } catch (IOException e) {
                return false;
            }
        }

        //Prepare UI for next action
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    controller.selectImages.setDisable(false);
                    controller.heightField.setDisable(false);
                    controller.widthField.setDisable(false);
                    controller.imageCount.setText("No images");
                    controller.statusLabel.setText("Images resized successfully");
                }
                catch (Exception e){
                    System.out.println("Platform runLater exception");
                }
            }
        });

        return true;
    }

    //Resize image
    public BufferedImage changeImageSize(BufferedImage image, int width, int height) {
        int newWidth, newHeight;
        double ratio;

        //not changing photo size
        if (image.getWidth() <= width && image.getHeight() <= height) {
            return image;
        }

        //for changing photo size
        if (image.getWidth() > image.getHeight()) {
            newWidth = width;
            ratio = (double) image.getWidth() / (double) width;
            newHeight = (int) ((double) image.getHeight() / ratio);
        } else {
            newHeight = height;
            ratio = (double) image.getHeight() / (double) height;
            newWidth = (int) ((double) image.getWidth() / ratio);
        }

        //Resize image using Scalr class for better photo quality
        BufferedImage resizedImage = Scalr.resize(image, Method.ULTRA_QUALITY, newWidth, newHeight);

        return resizedImage;
    }

    //Create new directory for saving images
    public File createDirectory(File file) {
        //Parent directory absolute path
        String path = file.getParentFile().getAbsolutePath();
        //Define new directory
        File directory = new File(path + File.separator + "resize");
        //Create new directory
        directory.mkdir();
        return directory;
    }

    @Override
    public void run(){
        resizeImages();
    }

}
