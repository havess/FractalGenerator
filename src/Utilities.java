import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Utilities {
    /* ----- CONSTANTS ----- */
    final static String DRAW_THREAD = "Fractal Drawer";

       final static double WIDTH = Window.SCREEN_SIZE.getWidth(),
               HEIGHT = Window.SCREEN_SIZE.getHeight();

    public Color generateRandom(Color rootColor){
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        // mix the color
        if (rootColor != null) {
            red = (red + rootColor.getRed()) / 2;
            green = (green + rootColor.getGreen()) / 2;
            blue = (blue + rootColor.getBlue()) / 2;
        }

        return new Color(red, green, blue);
    }

    public BufferedImage createImage(JPanel panel) {
        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.print(g);
        return bi;
    }

}
