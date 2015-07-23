import javax.swing.*;
import java.awt.*;

public abstract class Fractal{

    protected int width, height, iter, xShift, yShift;
    protected Color color;
    protected ID id;
    protected JPanel panel;
    protected int zoom;

    protected abstract void drawFractal(Graphics g);

    public Fractal(int width, int height, int iter, int zoom,int xShift,int  yShift, Color color, JPanel panel, ID id) {
        this.width = width;
        this.height = height;
        this.iter = iter;
        this.zoom = zoom;
        this.xShift = xShift;
        this.yShift = yShift;
        this.color = color;
        this.id = id;
        this.panel = panel;
    }

    public Color getColor() {
        return color;
    }

    public ID getID(){
        return id;
    }
}
