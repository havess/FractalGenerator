import javax.swing.*;
import java.awt.*;

/**
 * Created by samhaves on 15-05-12.
 */
public abstract class Fractal{

    protected int width, height, iter, xShift, yShift;
    protected Color color;
    protected boolean random;
    protected ID id;
    protected JPanel panel;
    protected int zoom;

    protected abstract void paintComponent(Graphics g);//methode requis par chaque classe enfant

    //Contrsucteur "super"
    public Fractal(int width, int height, int iter, int zoom,int xShift,int  yShift, Color color,boolean random, JPanel panel, ID id) {
        this.width = width;
        this.height = height;
        this.iter = iter;
        this.zoom = zoom;
        this.xShift = xShift;
        this.yShift = yShift;
        this.color = color;
        this.random = random;
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
