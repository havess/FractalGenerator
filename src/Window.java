import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class Window{

    private Thread tDraw = null;
    private Drawer drawer = null;

	private static final long serialVersionUID = 1L;
    private static Window instance;
    private boolean firstDraw = true, shift;
    final static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();


    JFrame frame;
    JPanel container, optionsPanel;
    JButton draw, colorChooserButton, printButton, plus, minus;
    JComboBox<String> fractalType, iterations;
    JComboBox<String> randomColors;
    JLabel fractalLabel, iterLabel, randomColorLabel,emptySpace, zoomLabel, xLabel, yLabel;
    JSlider zoomSlider, xSlider, ySlider;
    private DrawingPanel drawingPanel = new DrawingPanel();

    private int iter = 1, drawIter = 1, zoom = 1, xShift = 0, yShift = 0;
    private ID curId ;


    private static final Color defaultColor = new Color(255,255,255);
    public static Color curColor = defaultColor, previousColor = defaultColor;

    String[] fractalTypes = {
            "Tree",
            "Circles",
            "Fern"
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
       fractalType.addActionListener(e -> {
           if(fractalType.getSelectedItem() == fractalTypes[2]){
               iterations.setEnabled(false);
           }else{
               iterations.setEnabled(true);
           }
       });
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
       iterations.addActionListener(e -> {
           this.drawIter = this.iter;
           this.iter = Integer.parseInt((String) iterations.getSelectedItem());
       });
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

           if (!selectedItem.equals(randomColorsTypes[0])) {
               colorChooserButton.setEnabled(false);
               previousColor = curColor;

               if (selectedItem.equals(randomColorsTypes[1])) {
                   curColor = new Color(255, 255, 255); //pastels
               } else if (selectedItem.equals(randomColorsTypes[2])) {
                   curColor = new Color(57, 255, 15); //forest
               } else if (selectedItem.equals(randomColorsTypes[3])) {
                   curColor = new Color(3, 128, 255); //ocean
               }

           } else {
               colorChooserButton.setEnabled(true);
               curColor = previousColor;
           }

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


       colorChooserButton.addMouseListener(new MouseAdapter() {

           @Override
           public void mouseClicked(MouseEvent e) {
               if (colorChooserButton.isEnabled()) {
                   new ColorChooser();
               }
           }
       });

       ySlider.addChangeListener(e -> {
           clear();
           this.yShift = ySlider.getValue();
           this.shift = true;
           drawingPanel.repaint();
       });

       xSlider.addChangeListener(e -> {
           clear();
           this.xShift = xSlider.getValue();
           this.shift = true;
           drawingPanel.repaint();
       });

       /*plus.addMouseListener(new MouseAdapter() {

           @Override
           public void mouseClicked(MouseEvent e) {
               drawingPanel.paintComponent(drawingPanel.getGraphics());

               drawingPanel.repaint();
           }
       });*/

       /*minus.addMouseListener(new MouseAdapter() {

           @Override
           public void mouseClicked(MouseEvent e) {
               drawingPanel.paintComponent(drawingPanel.getGraphics());
               

               drawingPanel.repaint();
           }
       });*/
       
       zoomSlider.addChangeListener(e -> {
           clear();
           this.zoom = zoomSlider.getValue();
           drawingPanel.repaint();
       });

       draw.addMouseListener(new MouseAdapter() {


           @Override
           public void mousePressed(MouseEvent e) {
               clear();
               checkComboBox();
               drawIter = iter;

               if(firstDraw){
                   drawer = new Drawer("Fractal Drawer", iter, zoom, xShift, yShift, drawingPanel.getGraphics(),
                           curColor,drawingPanel,curId);
                   firstDraw = false;
               }else{
                   System.out.println("updating.." + iter);
                   drawer.update(drawingPanel.getGraphics(), iter, zoom, xShift, yShift, curColor, curId);
               }

               tDraw = new Thread(drawer, "Fractal Drawer");
               tDraw.start();
           }
       });

   }

    public static Window getInstance() {
        if(instance == null) {
            instance = new Window();
        }
        return instance;
    }

    private void clear(){
        if(tDraw != null){
            try{
                Drawer.terminate();
                tDraw.join();
            }catch (InterruptedException exc){
                exc.printStackTrace();
            }
        }
        drawingPanel.paintComponent(drawingPanel.getGraphics());
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

    public boolean isShift(){
        return this.shift;
    }

    public void setShift(boolean value){
        this.shift = value;
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
            case 2:
                this.curId = ID.Fern;
        }

    }

    private class DrawingPanel extends JPanel{


        public DrawingPanel(){
            addMouseListener(ma);
            addMouseMotionListener(ma);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.darkGray);
        }

        @Override
        public void repaint(){
            // must keep a copy of the previous iteration number so a change
            //      in combobox without pressing draw wont affect current fractal
            if(!firstDraw) {
                drawer.update(drawingPanel.getGraphics(), drawIter, zoom, xShift, yShift, curColor, curId);
                tDraw = new Thread(drawer, "Fractal Drawer");
                tDraw.start();
            }
        }

        MouseAdapter ma = new MouseAdapter() {
            Point startPoint, endPoint= new Point (0,0);

            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                startPoint = null;
                endPoint = new Point(getWidth()/2 + xShift, getHeight()/2 + yShift);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = e.getPoint();
                int x = p.x - startPoint.x;
                int y = p.y - startPoint.x;
                xShift = endPoint.x + x;
                yShift = endPoint.y + y;
                clear();
                repaint();
            }

        };


    }

}
