import javax.rmi.CORBA.Util;
import javax.swing.*;
import java.awt.*;

public class Circles extends Fractal{
    Utilities util = new Utilities();

    public Circles(int width, int height, int iter, int zoom,int xShift,int  yShift, Color color, JPanel panel, ID id) {
        super(width, height, iter, zoom, xShift, yShift,color, panel, id);

    }

    public void drawCircle(Graphics g, int x1, int y1, int iter, int radius) {
        if(iter >= 0) {

            if(Window.getInstance().isRandom()) {
                Color iterationColor = util.generateRandom(color);
                g.setColor(iterationColor);
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setStroke(new BasicStroke(0.1f * radius));
            if(!((x1 + xShift + radius < 0 ) || (x1 + xShift - radius> panel.getWidth())
            		|| (y1 + yShift + radius < 0 ) || (y1 + yShift - radius > panel.getWidth()))){
            	g2d.drawOval(x1 + xShift, y1 + yShift, radius, radius);
           }

            drawCircle(g, x1 + radius / 4, y1 - radius / 4, iter - 1, radius / 2);
            drawCircle(g, x1 + 3 * (radius / 4), y1 + radius / 4, iter - 1, radius / 2);
            drawCircle(g, x1 - radius / 4, y1 + radius / 4, iter - 1, radius / 2);
            drawCircle(g, x1 + radius / 4, y1 + 3 * (radius / 4), iter - 1, radius / 2);
        }

    }


    protected void drawFractal(Graphics g){
        g.setColor(color);
        drawCircle(g, width / 2 - (zoom*zoom)/2, height/2 - (zoom*zoom)/2, iter , zoom * zoom);
    }


}
