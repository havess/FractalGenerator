import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

public class Writer implements Runnable {
    private String threadName;
    private ID id;
    private int iter;
    private LinkedList<Point> coords;
    private static boolean running;

    public Writer(String threadName, ID id, int iter, LinkedList<Point> coords) {
        this.threadName = threadName;
        this.id = id;
        this.iter = iter;
        this.coords = coords;
    }

    @Override
    public void run() {
            System.out.println("created file");
            createFile();
    }

    private boolean createFile(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("cache/coordinates/"+ id.name() + "/" + iter + ".txt"), "utf-8"))) {
            for(Point p: this.coords){
                writer.write(p.x + " " + p.y);
                writer.newLine();
            }
            return true;

        }catch (IOException e){
            e.printStackTrace();
            System.out.println("file was not created");
            return false;
        }
    }

    public static boolean isRunning(){
        return running;
    }
}
