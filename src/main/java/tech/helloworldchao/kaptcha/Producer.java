package tech.helloworldchao.kaptcha;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Responsible for creating captcha image with a text drawn on it.
 */
public interface Producer {
    /**
     * Create an image which will have written a distorted text.
     *
     * @param text the distorted characters
     * @return image with the text
     */
    public BufferedImage createImage(String text);

    /**
     * Create a Base64 image which will have written a distorted text.
     *
     * @param text the distorted characters
     * @return Base64 image with the text
     */
    public String createBase64Image(String text) throws IOException;

    /**
     * @return the text to be drawn
     */
    public abstract String createText();
}