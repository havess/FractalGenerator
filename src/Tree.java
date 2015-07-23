/**
 * Created by samhaves on 15-05-12.
 */

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;
import javax.swing.*;

public class Tree extends Fractal{
	
	
    Color pastels = new Color(255,255,255);

    public Tree(int width, int height, int iter, int zoom,int xShift,int  yShift,Color color,boolean random, JPanel panel, ID id) {
        super(width,height,iter,zoom,xShift,yShift,color,random, panel,id);

    }

    public void drawTree(Graphics g, int x1, int y1, double angle, int iter) {
        if (iter == 0) {
        } else {

            if(random) {
                Color iterationColor = generateRandomColor(color);
                g.setColor(iterationColor);
            }

            int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * iter * zoom);
            int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * iter * zoom);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setStroke(new BasicStroke(0.5f * iter));
            if((x1 + xShift < 0 && x2 + xShift < 0) || (x1 + xShift > panel.getWidth() && x2 + xShift > panel.getWidth())
            		|| (y1 + yShift < 0 && y2 + yShift < 0) || (y1 + yShift > panel.getWidth() && y2 + yShift > panel.getWidth())){
            	//dont draw line if outside of visible area, speeds up zoomed scrolling
            }else{
            	g2d.drawLine(x1 + xShift, y1 + yShift, x2 + xShift, y2 + yShift);
            }
            //recursivite
            drawTree(g, x2, y2, angle + 30, iter - 1);
            drawTree(g, x2, y2, angle - 30, iter - 1);

        }
    }

    protected void paintComponent(Graphics g){
            g.setColor(color);
            drawTree(g, width / 2, height - 75, -90, iter);
    }
    
    //genere une couleur "random" base sur une couleur initiale
    public Color generateRandomColor(Color mix) {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        // mix the color
        if (mix != null) {
            red = (red + mix.getRed()) / 2;
            green = (green + mix.getGreen()) / 2;
            blue = (blue + mix.getBlue()) / 2;
        }

        Color color = new Color(red, green, blue);
        return color;
    }


}