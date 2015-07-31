import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.*;

public class Tree extends Fractal{
    Utilities util = new Utilities();

    public Tree(int width, int height, int iter, int zoom,int xShift,int  yShift,Color color, JPanel panel, ID id) {
        super(width,height,iter,zoom,xShift,yShift,color, panel,id);

    }

    public void drawTree(Graphics g, int x1, int y1, double angle, int iter) {
        if(iter >= 0 && Drawer.isRunning()){

            if(Window.getInstance().isRandom()) {
                Color iterationColor = util.generateRandom(color);
                g.setColor(iterationColor);
            }

            int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * iter * zoom);
            int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * iter * zoom);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setStroke(new BasicStroke(0.5f * iter));

            //don't draw line if outside of visible area, speeds up zoomed scrolling
            if(!((x1 + xShift < 0 && x2 + xShift < 0) || (x1 + xShift > panel.getWidth() && x2 + xShift > panel.getWidth())
            		|| (y1 + yShift < 0 && y2 + yShift < 0) || (y1 + yShift > panel.getWidth() && y2 + yShift > panel.getWidth()))){

            	g2d.drawLine(x1 + xShift, y1 + yShift, x2 + xShift, y2 + yShift);
            }

            drawTree(g, x2, y2, angle + 30, iter - 1);
            drawTree(g, x2, y2, angle - 30, iter - 1);

        }
    }

    protected void drawFractal(Graphics g){
            g.setColor(color);
            drawTree(g, width / 2, height - 75, -90, iter);
    }
}