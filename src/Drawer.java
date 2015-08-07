
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class Drawer implements Runnable{

    private Utilities util = new Utilities();

    private String threadName;
    private Graphics g;
    private ID curId;
    private int iter,zoom,angle,xShift,yShift, width, height, lineCount = 0;
    private Color color;
    private JPanel panel;
    private ArrayList<Color> colors;
    private static boolean newColors;
    private static boolean running;



    public Drawer(String threadName, int iter, int zoom,int angle, int xShift, int yShift, Graphics g, Color color, JPanel panel, ID id){
        this.threadName = threadName;
        this.g = g;
        this.iter = iter;
        this.zoom = zoom;
        this.angle = angle;
        this.xShift = xShift;
        this.yShift = yShift;
        this.color = color;
        this.panel = panel;
        this.width = this.panel.getWidth();
        this.height = this.panel.getHeight();
        this.curId = id;
        running = true;
    }

    public void update(Graphics g,int iter, int zoom,int angle, int xShift, int yShift, Color color, ID id) {
        this.g = g;
        this.iter = iter;
        this.zoom = zoom;
        this.angle = angle;
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
        if(isNewColors() || colors == null) {
            colors = new ArrayList<>();
        }else{
            lineCount = 0;
        }
        if(curId == ID.Tree){
            drawTree(width / 2 + xShift, height - 300 + yShift, -90, iter);
        }else if(curId == ID.Circles){
            drawCircle(width / 2 - (zoom * zoom) / 2 + xShift, height / 2 - (zoom * zoom) / 2 + yShift, iter, zoom * zoom);
        }else if(curId == ID.Fern){
            drawFern();
        }
    }

    private void drawTree(int x1, int y1, double angle, int iter) {
        if(iter >= 0 && isRunning()){

            checkRandom();

            int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * iter * zoom);
            int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * iter * zoom);


            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setStroke(new BasicStroke(0.5f * iter));

            //don't draw line if outside of visible area, speeds up zoomed scrolling
            if(!((x1 < 0 && x2 < 0) || (x1 > width && x2> width)
                    || (y1  < 0 && y2 < 0) || (y1 > height && y2 > height))) {
                g2d.drawLine(x1, y1, x2 , y2 );
            }

            drawTree(x2, y2, angle + this.angle, iter - 1);
            drawTree(x2, y2, angle - this.angle, iter - 1);
        }


    }

    private void drawFern(){
        float   a[]={0F,0.2F,-0.15F,0.85F},
                b[]={0F,-0.26F,0.28F,0.04F},
                c[]={0F,0.23F,0.26F,-0.04F},
                d[]={0.16F,0.22F,0.24F,0.85F},
                e[]={0F,0F,0F,0F},
                f[]={0F,1.6F,0.44F,1.6F},
                rand,NewX,NewY;

        int index = 0;
        int xScale,yScale,xOffset,screenX,screenY;

        xOffset = panel.getWidth()/2;
        xScale = ((int)((panel.getWidth() - xOffset )/2.6556F)) * zoom;
        yScale = (panel.getHeight()/11) * zoom;

        float x = 0,y = 0, n = 0;
        while(n < 500000 && isRunning()){

            checkRandom();

            rand=(float)Math.random(); // from 0 to 1
            if (rand<=0.01F){
                index = 0;
            }else if (rand<=0.08F){
                index = 1;
            }else if (rand<=0.15F){
                index = 2;
            }else if(rand<1){
                index = 3;
            }

            NewX = a[index] * x + b[index] * y + e[index];
            NewY = c[index] * x + d[index] * y + f[index];
            x = NewX;
            y = NewY;

            screenX = (int)(x * xScale + xOffset) + xShift;
            screenY = (int)(y * yScale) + yShift;
            if(!((screenX < 0  ) || (screenX > panel.getWidth())
                    || (screenY < 0 ) || (screenY > panel.getHeight()))){
                g.fillRect(screenX,screenY,1,1);
                n++;
            }

        }

    }


    private void drawCircle( int x1, int y1, int iter, int radius) {
        if(iter >= 0 && isRunning()) {

            checkRandom();

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setStroke(new BasicStroke(0.1f * radius));

            if(!((x1 + radius < 0 ) || (x1 - radius > panel.getWidth())
                    || (y1 + radius < 0 ) || (y1  - radius > panel.getHeight()))){
                g2d.drawOval(x1, y1, radius, radius);
            }
            drawCircle(x1 + radius / 4, y1 - radius / 4, iter - 1, radius / 2);
            drawCircle(x1 + 3 * (radius / 4), y1 + radius / 4, iter - 1, radius / 2);
            drawCircle(x1 - radius / 4, y1 + radius / 4, iter - 1, radius / 2);
            drawCircle(x1 + radius / 4, y1 + 3 * (radius / 4), iter - 1, radius / 2);
        }
    }

    private void checkRandom(){
        if(Window.getInstance().isRandom() && isNewColors()) {
            Color iterationColor = util.generateRandom(color);
            colors.add(iterationColor);
            g.setColor(iterationColor);

        }else if (Window.getInstance().isRandom() && !isNewColors()){
            if (lineCount < colors.size()) {
                g.setColor(colors.get(lineCount));
                lineCount++;
            }else if (lineCount >= colors.size()){
                Color iterationColor = util.generateRandom(color);
                colors.add(iterationColor);
                g.setColor(iterationColor);
            }
        }
    }

    public static void setNewColors(boolean value) {
        newColors = value;
    }

    public static boolean isNewColors(){
        return newColors;
    }
}
