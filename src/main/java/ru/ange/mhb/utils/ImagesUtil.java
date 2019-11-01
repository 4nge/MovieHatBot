package ru.ange.mhb.utils;

import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImagesUtil {


    public static BufferedImage writeWatermark(BufferedImage sourceImage, String text) {

        Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();
        try {
            // initializes necessary graphic properties
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
            g2d.setComposite(alphaChannel);
            g2d.setColor(Color.BLUE);
            g2d.setFont(new Font("Arial", Font.BOLD, 64));
            FontMetrics fontMetrics = g2d.getFontMetrics();
            Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);

            // calculates the coordinate where the String is painted
            int centerX = (sourceImage.getWidth() - (int) rect.getWidth()) / 2;
            int centerY = sourceImage.getHeight() / 2;

            // paints the textual watermark
            g2d.drawString(text, centerX, centerY);
            return sourceImage;

        } finally {
            g2d.dispose();
        }
    }

    public static InputStream getIsFromBm(BufferedImage img) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(img, "png", os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }


    public static BufferedImage addImageWatermark(BufferedImage sourceImage, String resourcesFilePath) {
        Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();

        try {

            File resourceFile = new ClassPathResource(resourcesFilePath).getFile();
            BufferedImage watermarkImage = ImageIO.read(resourceFile);
            // initializes necessary graphic properties
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
            g2d.setComposite(alphaChannel);

            // calculates the coordinate where the image is painted
            int topLeftX = (sourceImage.getWidth() - watermarkImage.getWidth()) / 2;
            int topLeftY = (sourceImage.getHeight() - watermarkImage.getHeight()) / 2;

            // paints the image watermark
            g2d.drawImage(watermarkImage, topLeftX, topLeftY, null);
            return sourceImage;

        } catch (IOException e) {
            return null;
        } finally {
            g2d.dispose();
        }
    }


    public static void main(String[] args) {
        //writeWatermark("Потрачено" );
        //writeWatermark2("/home/fedor-m/Изображения/5eb590be55a46d9cb11c70131e075c98.jpg", "Потрачено", "/home/fedor-m/Изображения/imgg.png" );
    }

}
