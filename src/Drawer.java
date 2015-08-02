import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;

public class Drawer implements Runnable{

    private Utilities util = new Utilities();

    private String threadName;
    private Graphics g;
    private ID curId;
    private int iter,zoom,xShift,yShift, width, height;
    private Color color;
    private JPanel panel;
    private LinkedList<Point> coords = new LinkedList<>();
    private static boolean running;



    public Drawer(String threadName, int iter, int zoom, int xShift, int yShift, Graphics g, Color color, JPanel panel, ID id){
        this.threadName = threadName;
        this.g = g;
        this.iter = iter;
        this.zoom = zoom;
        this.xShift = xShift;
        this.yShift = yShift;
        this.color = color;
        this.panel = panel;
        this.width = this.panel.getWidth();
        this.height = this.panel.getHeight();
        this.curId = id;
        running = true;
    }

    public void update(Graphics g,int iter, int zoom, int xShift, int yShift, Color color, ID id) {
        this.g = g;
        this.iter = iter;
        this.zoom = zoom;
        this.xShift = xShift;
        this.yShift = yShift;
        this.color = color;
        this.curId = id;
        running = true;
    }

    @Override
    public void run() {
        if(running){
            drawFractal();
        }

    }

    public static boolean isRunning(){
        return running;
    }

    public static void terminate(){
        running = false;
    }

    private void drawFractal(){
        g.setColor(color);
        File f = new File("coords.txt");
        if(curId == ID.Tree){
            drawTree(width / 2, height - 300, -90, iter);
        }else if(curId == ID.Circles){
            drawCircle(width / 2 - (zoom * zoom) / 2, height / 2 - (zoom * zoom) / 2, iter, zoom * zoom);
        }

        //after done rendering write coordinates to file,
        // TODO: integrate replace file logic and shtuff

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("coords" + iter + ".txt"), "utf-8"))) {
            for(Point p: coords){
                writer.write(p.x + " " + p.y);
                writer.newLine();
            }

        }catch (IOException e){
            System.out.println("file was not created");
        }
    }

    private void drawTree(int x1, int y1, double angle, int iter) {
        if(iter >= 0 && Drawer.isRunning()){

            if(Window.getInstance().isRandom()) {
                Color iterationColor = util.generateRandom(color);
                g.setColor(iterationColor);
            }

            int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * iter * zoom);
            int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * iter * zoom);

            coords.add(new Point(x2,y2));

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setStroke(new BasicStroke(0.5f * iter));

            //don't draw line if outside of visible area, speeds up zoomed scrolling
            if(!((x1 + xShift < 0 && x2 + xShift < 0) || (x1 + xShift > width && x2 + xShift > width)
                    || (y1 + yShift < 0 && y2 + yShift < 0) || (y1 + yShift > height && y2 + yShift > height))) {
                g2d.drawLine(x1 + xShift, y1 + yShift, x2 + xShift, y2 + yShift);
            }

            drawTree(x2, y2, angle + 30, iter - 1);
            drawTree(x2, y2, angle - 30, iter - 1);

        }
    }

    private void drawCircle( int x1, int y1, int iter, int radius) {
        if(iter >= 0 && Drawer.isRunning()) {

            if(Window.getInstance().isRandom()) {
                Color iterationColor = util.generateRandom(color);
                g.setColor(iterationColor);
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setStroke(new BasicStroke(0.1f * radius));
            if(!((x1 + xShift + radius < 0 ) || (x1 + xShift - radius > panel.getWidth())
                    || (y1 + yShift + radius < 0 ) || (y1 + yShift - radius > panel.getHeight()))){
                g2d.drawOval(x1 + xShift, y1 + yShift, radius, radius);
            }
            drawCircle(x1 + radius / 4, y1 - radius / 4, iter - 1, radius / 2);
            drawCircle(x1 + 3 * (radius / 4), y1 + radius / 4, iter - 1, radius / 2);
            drawCircle(x1 - radius / 4, y1 + radius / 4, iter - 1, radius / 2);
            drawCircle(x1 + radius / 4, y1 + 3 * (radius / 4), iter - 1, radius / 2);
        }
    }
}
