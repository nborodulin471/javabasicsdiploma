package ru.netology.graphics.converter;

import ru.netology.graphics.image.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {

    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    private TextColorSchema schema;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        BufferedImage img = ImageIO.read(new URL(url));

        int[] newSize = controlSize(img);
        int newWidth = newSize[0];
        int newHeight = newSize[1];

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();

        if (schema == null){schema = new ConverterColor();}

        char[][] chrs = new char[newWidth][newHeight];
        for (int w = 0; w < newWidth; w++) {
            for (int h = 0; h < newHeight; h++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                chrs[w][h] = c;
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                builder.append(chrs[w][h]);
                builder.append(chrs[w][h]);
            }
           builder.append("\n");
        }
        return builder.toString(); // Возвращаем собранный текст.
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    private int[] controlSize(BufferedImage img) {
        int width       = img.getWidth();
        int height      = img.getHeight();
        double ratio = calculateRatio(width, height);
        checkRatio(ratio);

        return calculateSize(ratio, width, height);
    }

    private int[] calculateSize(double attitude, int width, int height) {
        int[] newSize = new int[2];
        newSize[0] = (int) (width * attitude);
        newSize[1] = (int) (height * attitude);
        return newSize;
    }

    private void checkRatio(double ratio) {
        if (ratio > maxRatio && maxRatio != 0.0) {
            try {
                throw new BadImageSizeException(ratio, maxRatio);
            } catch (BadImageSizeException e) {
                e.printStackTrace();
            }
        }
    }

    private double calculateRatio(int width, int height) {
        if (width > height) {
            return maxWidth == 0 ? width : (double) maxWidth / width;
        } else{
            return maxHeight == 0 ? height: (double) maxHeight / height;
        }
    }

}
