package tech.helloworldchao.kaptcha.impl;

import tech.helloworldchao.kaptcha.BackgroundProducer;
import tech.helloworldchao.kaptcha.GimpyEngine;
import tech.helloworldchao.kaptcha.Producer;
import tech.helloworldchao.kaptcha.text.TextProducer;
import tech.helloworldchao.kaptcha.text.WordRenderer;
import tech.helloworldchao.kaptcha.util.Configurable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Default {@link Producer} implementation which draws a captcha image using
 * {@link WordRenderer}, {@link GimpyEngine}, {@link BackgroundProducer}.
 * Text creation uses {@link TextProducer}.
 */
public class DefaultKaptcha extends Configurable implements Producer {
    private int width = 200;

    private int height = 50;

    /**
     * Create an image which will have written a distorted text.
     *
     * @param text the distorted characters
     * @return image with the text
     */
    public BufferedImage createImage(String text) {
        WordRenderer wordRenderer = getConfig().getWordRendererImpl();
        GimpyEngine gimpyEngine = getConfig().getObscurificatorImpl();
        BackgroundProducer backgroundProducer = getConfig().getBackgroundImpl();
        boolean isBorderDrawn = getConfig().isBorderDrawn();
        this.width = getConfig().getWidth();
        this.height = getConfig().getHeight();

        BufferedImage bi = wordRenderer.renderWord(text, width, height);
        bi = gimpyEngine.getDistortedImage(bi);
        bi = backgroundProducer.addBackground(bi);
        Graphics2D graphics = bi.createGraphics();
        if (isBorderDrawn) {
            drawBox(graphics);
        }
        return bi;
    }

    private void drawBox(Graphics2D graphics) {
        Color borderColor = getConfig().getBorderColor();
        int borderThickness = getConfig().getBorderThickness();

        graphics.setColor(borderColor);

        if (borderThickness != 1) {
            BasicStroke stroke = new BasicStroke((float) borderThickness);
            graphics.setStroke(stroke);
        }

        Line2D line1 = new Line2D.Double(0, 0, 0, width);
        graphics.draw(line1);
        Line2D line2 = new Line2D.Double(0, 0, width, 0);
        graphics.draw(line2);
        line2 = new Line2D.Double(0, height - 1, width, height - 1);
        graphics.draw(line2);
        line2 = new Line2D.Double(width - 1, height - 1, width - 1, 0);
        graphics.draw(line2);
    }

    /**
     * Create a Base64 image which will have written a distorted text.
     *
     * @param text the distorted characters
     * @return Base64 image with the text
     */
    public String createBase64Image(String text) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        ImageIO.write(createImage(text), "jpg", stream);
        byte[] bytes = Base64.getEncoder().encode(stream.toByteArray());
        String base64 = new String(bytes);

        stream.flush();
        stream.close();

        return "data:image/jpeg;base64," + base64;
    }

    /**
     * @return the text to be drawn
     */
    public String createText() {
        return getConfig().getTextProducerImpl().getText();
    }
}
