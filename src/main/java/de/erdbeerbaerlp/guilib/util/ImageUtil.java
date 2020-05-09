package de.erdbeerbaerlp.guilib.util;

import net.minecraft.client.renderer.texture.NativeImage;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class ImageUtil {
    /**
     * This method resizes the image and returns the BufferedImage object that can be drawn
     */
    public static BufferedImage scaleImage(final BufferedImage img, int width, int height) {
        int w = img.getWidth();
        int h = img.getHeight();
        if (w == width && h == height) return img;
        BufferedImage dimg = new BufferedImage(width, height, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, width, height, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

    /**
     * This method resizes the image and returns the BufferedImage object that can be drawn<br>This method keeps aspect ratio
     */
    public static BufferedImage scaleImageKeepAspectRatio(final BufferedImage img, int width, int height) {
        int w = img.getWidth();
        int h = img.getHeight();
        if (w == width && h == height) return img;
        final Dimension scaled = getScaledDimension(new Dimension(w, h), new Dimension(width, height));
        final int newWidth = (int) scaled.getWidth();
        final int newHeight = (int) scaled.getHeight();
        final BufferedImage dimg = new BufferedImage(newWidth, newHeight, img.getType());
        final Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, newWidth, newHeight, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

    private static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;
        if (original_width > bound_width) {
            new_width = bound_width;
            new_height = (new_width * original_height) / original_width;
        }
        if (new_height > bound_height) {
            new_height = bound_height;
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    private static byte[] toByteArray(final InputStream is) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        final byte[] out = os.toByteArray();
        os.close();
        return out;
    }

    public static ByteArrayInputStream getInputStreamFromImageURL(String url) throws IOException {
        final HttpURLConnection httpcon = (HttpURLConnection) new URL(url).openConnection();
        httpcon.addRequestProperty("User-Agent", "Minecraft");
        final ByteArrayInputStream is = new ByteArrayInputStream(toByteArray(httpcon.getInputStream()));
        httpcon.disconnect();
        return is;
    }

    /**
     * Downloads an image as {@link NativeImage}
     */
    public static NativeImage loadImageFromURL(String url, boolean keepAspectRatio, int width, int height) throws IOException {
        final ByteArrayInputStream is = getInputStreamFromImageURL(url);
        final BufferedImage img = ImageIO.read(is);
        is.close();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        if (keepAspectRatio)
            ImageIO.write(ImageUtil.scaleImageKeepAspectRatio(img, width, height), "png", os);
        else
            ImageIO.write(ImageUtil.scaleImage(img, width, height), "png", os);
        final ByteArrayInputStream is2 = new ByteArrayInputStream(os.toByteArray());
        final NativeImage imgo = NativeImage.read(is2);
        is2.close();
        return imgo;
    }

    public static boolean isURLGif(String url) throws IOException {
        final InputStream is = getInputStreamFromImageURL(url);
        ImageInputStream iis = ImageIO.createImageInputStream(is);
        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
        while (imageReaders.hasNext()) {
            ImageReader reader = imageReaders.next();
            System.out.printf("formatName: %s%n", reader.getFormatName());
            if (reader.getFormatName().endsWith("gif")) {
                iis.close();
                return true;
            }
        }
        iis.close();
        return false;
    }
}
