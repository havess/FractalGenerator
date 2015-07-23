import java.awt.*;
import java.util.Random;

/**
 * Created by samhaves on 15-07-23.
 */
public class Utilities {

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
}
