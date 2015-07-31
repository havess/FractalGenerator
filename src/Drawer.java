import java.awt.*;

public class Drawer implements Runnable{
    private String threadName;
    private Graphics g;
    private ID curId;
    private static boolean running;

    public Drawer(String threadName, Graphics g, ID id) {
        this.threadName = threadName;
        this.g = g;
        this.curId = id;
        this.running = true;
    }

    @Override
    public void run() {
        System.out.println("Running: " + threadName);
        if(running){
            drawFractal(curId);
        }
    }

    public static boolean isRunning(){
        return running;
    }

    public static void terminate(){
        running = false;
        System.out.println("Draw Thread has terminated");
    }

    private void drawFractal(ID curId){
        Window.fractals.stream().filter(temp -> temp.getID() == curId).forEach(temp -> temp.drawFractal(g));
    }
}
