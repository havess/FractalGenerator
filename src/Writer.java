import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

public class Writer implements Runnable {
    private String threadName, filePath;
    private static boolean running;

    public Writer(String threadName, String filePath) {
        this.threadName = threadName;
        this.filePath = filePath;
    }

    @Override
    public void run() {
            System.out.println("created file");
            createFile();
    }

    private boolean createFile(){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), "utf-8"))) {
            //write file
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
