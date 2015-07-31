import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Window{

    private Thread tUp = null;
    private Thread tDraw = null;
    private Drawer d = null;

	private static final long serialVersionUID = 1L;
    private static Window instance;
    public static boolean updateRequested;
    final static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();


    JFrame frame;
    JPanel container, optionsPanel;
    JButton draw, colorChooserButton, printButton, plus, minus;
    JComboBox<String> fractalType, iterations;
    JComboBox<String> randomColors;
    JLabel fractalLabel, iterLabel, randomColorLabel,emptySpace, zoomLabel, xLabel, yLabel;
    JSlider zoomSlider, xSlider, ySlider;
    private DrawingPanel drawingPanel;

    private ID curId ;


    private final Color defaultColor = new Color(255,255,255);
    private Color previousColor = defaultColor;

    public static ArrayList<Fractal> fractals = new ArrayList<>();

    String[] fractalTypes = {
            "Tree",
            "Circles"
    }, randomColorsTypes = {
            "Solid Color",
            "Pastels",
            "Forest",
            "Ocean"
    };

    int [] iterationNums =  {
            1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,40
    };

   public Window(){
       frame = new JFrame("Fractal Generator");

       Color optionsPanelColor = new Color(2,2,2);
       
       //GUI
       setLookAndFeel();
       frame.setSize(SCREEN_SIZE);
       frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       frame.setResizable(false);

       container = new JPanel();
       container.setSize(SCREEN_SIZE);
       container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));

       drawingPanel = new DrawingPanel();
       drawingPanel.setPreferredSize(new Dimension(frame.getWidth() - 200, frame.getHeight()));
       drawingPanel.setBackground(new Color(20, 20, 20));
       container.add(drawingPanel);

       optionsPanel = new JPanel();
       optionsPanel.setPreferredSize(new Dimension(200, frame.getHeight()));
       optionsPanel.setMinimumSize(new Dimension(200, frame.getHeight()));
       optionsPanel.setMaximumSize(new Dimension(200, frame.getHeight()));
       optionsPanel.setBackground(optionsPanelColor);
       optionsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
       optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));

       fractalLabel = new JLabel("Fractal type");
       fractalLabel.setForeground(Color.lightGray);
       fractalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       optionsPanel.add(fractalLabel);

       fractalType  = new JComboBox<>();
       fractalType.setPreferredSize(new Dimension(190, 20));
       for (String fractalType1 : fractalTypes) {
           fractalType.addItem(fractalType1);
       }
       optionsPanel.add(fractalType);


       iterLabel = new JLabel("Iterations");
       iterLabel.setForeground(Color.lightGray);
       iterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       optionsPanel.add(iterLabel);

       iterations  = new JComboBox<>();
       iterations.setPreferredSize(new Dimension(190, 20));
       for (int iterationNum : iterationNums) {
           iterations.addItem(Integer.toString(iterationNum));
       }
       optionsPanel.add(iterations);

       randomColorLabel = new JLabel("Fractal Color");
       randomColorLabel.setForeground(Color.lightGray);
       randomColorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       optionsPanel.add(randomColorLabel);

       randomColors  = new JComboBox<>();
       randomColors.setPreferredSize(new Dimension(190, 20));
       for (String randomColorsType : randomColorsTypes) {
           randomColors.addItem(randomColorsType);
       }
       randomColors.addActionListener(e -> {

           String selectedItem = (String) randomColors.getSelectedItem();
           Color newColor;

           if (!selectedItem.equals(randomColorsTypes[0])) {
               colorChooserButton.setEnabled(false);
               previousColor = fractals.get(0).color;

               if (selectedItem.equals(randomColorsTypes[1])) {
                   newColor = new Color(255, 255, 255); //pastels
               } else if (selectedItem.equals(randomColorsTypes[2])) {
                   newColor = new Color(57, 255, 15); //forest
               } else if (selectedItem.equals(randomColorsTypes[3])) {
                   newColor = new Color(3, 128, 255); //ocean
               }else{
                   newColor = null;
               }

           } else {
               colorChooserButton.setEnabled(true);
               newColor = previousColor;
           }

           updateRequested = true;
           Updater update = new Updater("color update", "color", newColor);
           tUp = new Thread(update, "color update");
           tUp.start();
           System.out.println("Update successful");

       });
       optionsPanel.add(randomColors);

       emptySpace = new JLabel("  ");
       emptySpace.setPreferredSize(new Dimension(190, 2));
       optionsPanel.add(emptySpace);

       colorChooserButton = new JButton("Color Chooser");
       colorChooserButton.setPreferredSize(new Dimension(190, 25));
       colorChooserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
       colorChooserButton.setFont(colorChooserButton.getFont().deriveFont(Font.BOLD));
       optionsPanel.add(colorChooserButton);

       emptySpace = new JLabel("  ");
       emptySpace.setPreferredSize(new Dimension(190, 2));
       optionsPanel.add(emptySpace);

       zoomLabel = new JLabel("Zoom");
       zoomLabel.setForeground(Color.lightGray);
       zoomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       optionsPanel.add(zoomLabel);

       JPanel zoomPanel = new JPanel();
       zoomPanel.setBackground(optionsPanelColor);
       zoomPanel.setPreferredSize(new Dimension(200, 50));
       zoomPanel.setLayout(new FlowLayout());
       minus = new JButton("-");
       minus.setPreferredSize(new Dimension(40, 40));
       minus.setFont(minus.getFont().deriveFont(Font.BOLD));
       minus.setAlignmentX(Component.CENTER_ALIGNMENT);
       zoomPanel.add(minus);

       plus = new JButton("+");
       plus.setPreferredSize(new Dimension(40, 40));
       plus.setFont(plus.getFont().deriveFont(Font.BOLD));
       plus.setAlignmentX(Component.CENTER_ALIGNMENT);
       zoomPanel.add(plus);

       optionsPanel.add(zoomPanel);

       zoomLabel = new JLabel("Fine Zoom");
       zoomLabel.setForeground(Color.lightGray);
       zoomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       optionsPanel.add(zoomLabel);

       zoomSlider = new JSlider();
       zoomSlider.setPreferredSize(new Dimension(190, 20));
       zoomSlider.setBackground(Color.black);
       zoomSlider.setMaximum(30);
       zoomSlider.setMinimum(1);
       zoomSlider.setMajorTickSpacing(29);
       zoomSlider.setMinorTickSpacing(1);
       zoomSlider.setPaintTicks(true);
       zoomSlider.setPaintLabels(false);
       zoomSlider.setValue(1);
       optionsPanel.add(zoomSlider);

       emptySpace = new JLabel("  ");
       emptySpace.setPreferredSize(new Dimension(190, 2));
       optionsPanel.add(emptySpace);

       yLabel = new JLabel("Y Shift");
       yLabel.setForeground(Color.lightGray);
       yLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       optionsPanel.add(yLabel);

       ySlider = new JSlider(JSlider.VERTICAL);
       ySlider.setBackground(Color.lightGray);
       ySlider.setMinimum(-3000);
       ySlider.setMaximum(3000);
       ySlider.setMajorTickSpacing(1000);
       ySlider.setMinorTickSpacing(200);
       ySlider.setPaintTicks(true);
       ySlider.setPaintLabels(false);
       ySlider.setValue(0);
       optionsPanel.add(ySlider);

       xLabel = new JLabel("X shift");
       xLabel.setForeground(Color.lightGray);
       xLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       optionsPanel.add(xLabel);

       xSlider = new JSlider();
       xSlider.setBackground(Color.lightGray);
       xSlider.setPreferredSize(new Dimension(190, 20));
       xSlider.setMinimum(-3000);
       xSlider.setMaximum(3000);
       xSlider.setMajorTickSpacing(1000);
       xSlider.setMinorTickSpacing(200);
       xSlider.setPaintTicks(true);
       xSlider.setPaintLabels(false);
       xSlider.setValue(0);
       optionsPanel.add(xSlider);

       JPanel buttonPanel = new JPanel();
       buttonPanel.setBackground(optionsPanelColor);
       buttonPanel.setPreferredSize(new Dimension(200, 50));
       buttonPanel.setLayout(new FlowLayout());
       draw = new JButton("Draw");
       draw.setPreferredSize(new Dimension(80, 30));
       draw.setFont(draw.getFont().deriveFont(Font.BOLD));
       draw.setAlignmentX(Component.CENTER_ALIGNMENT);
       buttonPanel.add(draw);

       printButton = new JButton("Print");
       printButton.setPreferredSize(new Dimension(80, 30));
       printButton.setFont(printButton.getFont().deriveFont(Font.BOLD));
       printButton.setAlignmentX(Component.CENTER_ALIGNMENT);
       printButton.addActionListener(new PrintPanel(drawingPanel));
       buttonPanel.add(printButton);
       optionsPanel.add(buttonPanel);

       JLabel filler = new JLabel(new ImageIcon("carbon.png"));
       optionsPanel.add(filler);

       container.add(optionsPanel);
       frame.add(container);
       frame.setVisible(true);
       init();

       colorChooserButton.addMouseListener(new MouseAdapter() {

           @Override
           public void mouseClicked(MouseEvent e) {
               if (colorChooserButton.isEnabled()) {
                   new ColorChooser();
               }
           }

       });

       ySlider.addChangeListener(e -> {
           drawingPanel.paintComponent(drawingPanel.getGraphics());

           for (Fractal temp : fractals) {
               temp.yShift = ySlider.getValue();
           }
           drawingPanel.repaint();
       });

       xSlider.addChangeListener(e -> {
           drawingPanel.paintComponent(drawingPanel.getGraphics());
           for (Fractal temp : fractals) {
               temp.xShift = xSlider.getValue();
           }
           drawingPanel.repaint();
       });

       /*plus.addMouseListener(new MouseAdapter() {

           @Override
           public void mouseClicked(MouseEvent e) {
               drawingPanel.paintComponent(drawingPanel.getGraphics());
               
               for (Fractal temp : fractals) {
            	   
                   temp.zoom += 2;
                   zoomSlider.setValue(temp.zoom);
               }
               drawingPanel.repaint();
           }
       });*/

       /*minus.addMouseListener(new MouseAdapter() {

           @Override
           public void mouseClicked(MouseEvent e) {
               drawingPanel.paintComponent(drawingPanel.getGraphics());
               
               for (Fractal temp : fractals) {
                   if(temp.zoom >= 2){
                       temp.zoom -= 2;
                   }
                   zoomSlider.setValue(temp.zoom);
               }
               drawingPanel.repaint();
           }
       });*/
       
       zoomSlider.addChangeListener(e -> {
           if(tDraw != null){
               try{
                   Drawer.terminate();
                   tDraw.join();
               }catch (InterruptedException exc){
                   //ignore
               }
           }
           drawingPanel.paintComponent(drawingPanel.getGraphics());

           for (Fractal temp : fractals) {
               temp.zoom = zoomSlider.getValue();
           }
           drawingPanel.repaint();
       });

       draw.addMouseListener(new MouseAdapter() {


           @Override
           public void mousePressed(MouseEvent e) {
               if(tDraw != null){
                   try{
                       Drawer.terminate();
                       tDraw.join();
                   }catch (InterruptedException exc){
                       //ignore
                   }
               }
               drawingPanel.paintComponent(drawingPanel.getGraphics());
               checkComboBox();

               for (Fractal temp : fractals) {
                   temp.iter = Integer.parseInt((String)iterations.getSelectedItem()) ;
               }

               Drawer d = new Drawer("Fractal Drawer", drawingPanel.getGraphics(),curId);
               tDraw = new Thread(d, "Fractal Drawer");
               tDraw.start();
               System.out.println("Created thread ready to be clicked again");
           }
       });

   }
    private void init(){
        fractals.add(new Tree(frame.getWidth() - 200,frame.getHeight() - 200, 0, 1, 0, 0,defaultColor, drawingPanel, ID.Tree));
        fractals.add(new Circles(frame.getWidth() - 200,frame.getHeight() - 200, 0, 1, 0, 0,defaultColor, drawingPanel, ID.Circles));
    }

    public static Window getInstance() {
        if(instance == null) {
            instance = new Window();
        }
        return instance;
    }
    
    private void setLookAndFeel() {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");}
		catch(Exception e){
			//ignore error
		}
		
	}

    public boolean isRandom(){
        return randomColors.getSelectedIndex() != 0;
    }

    private void checkComboBox(){

        int chosenFractal = fractalType.getSelectedIndex();
        
        switch(chosenFractal){
            case 0:
                this.curId = ID.Tree;
                break;
            case 1:
                this.curId = ID.Circles;
                break;
        }

    }

    private class DrawingPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);
            g.setColor(Color.darkGray);
        }

        @Override
        public void repaint(){
            if(Updater.vFractals != null && !Drawer.isRunning()){
                fractals = new ArrayList<>(Updater.vFractals);
                System.out.println("cloned vFractals");
            }
            Drawer d = new Drawer("Fractal Drawer", this.getGraphics(),curId);
            tDraw = new Thread(d, "Fractal Drawer");
            tDraw.start();
        }

    }
}
