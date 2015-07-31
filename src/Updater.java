import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Updater implements Runnable{
    private Thread t;
    private String threadName;
    private Object var;
    private String varN;
    public static ArrayList<Fractal> vFractals;

    public Updater(String threadName, String varN, Object var){
        this.threadName = threadName;
        this.varN = varN;
        this.var = var;
        vFractals = new ArrayList<>(Window.fractals.size());

    }

    @Override
    public void run() {
        System.out.println("Running: " + threadName);
        if(Window.updateRequested){
            updateValue(varN, var);
        }

    }

    private void updateValue(String varN, Object var) {
        for(Fractal f: vFractals){

            switch(varN){
                case "color":
                    f.color = (Color) var;
                    break;
                case "iter":
                    f.iter = (Integer) var;
                    break;
                case "zoom":
                    f.zoom = (Integer) var;
                    break;
            }
            System.out.println("Updated" + var.getClass().toString());
        }
    }
}
