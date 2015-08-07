import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Window{

    private Thread tDraw = null;
    private Drawer drawer = null;

    private static Window instance;
    private boolean firstDraw = true;
    final static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();


    JFrame frame;
    JPanel container, optionsPanel;
    JButton draw, colorChooserButton, printButton, plus, minus, center;
    JComboBox<String> fractalType, iterations;
    JComboBox<String> randomColors;
    JLabel fractalLabel, iterLabel, randomColorLabel,emptySpace, zoomLabel, angleLabel;
    JSlider zoomSlider, angleSlider;
    private DrawingPanel drawingPanel = new DrawingPanel();

    private int iter = 1, drawIter = 1, zoom = 1, xShift = 0, yShift = 0, previousColorIndex = 0, lastChange = 0, angle;
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
           if (fractalType.getSelectedItem() == fractalTypes[2]) {
               iterations.setVisible(false);
               iterLabel.setVisible(false);
               angleSlider.setVisible(false);
               angleLabel.setVisible(false);
           } else {
               if (fractalType.getSelectedItem() == fractalTypes[0]) {
                   angleSlider.setVisible(true);
                   angleLabel.setVisible(true);
               } else {
                   angleSlider.setVisible(false);
                   angleLabel.setVisible(false);
               }
               iterations.setVisible(true);
               iterLabel.setVisible(true);
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

           int index = randomColors.getSelectedIndex();
           System.out.println(index);

           if (index != 0) {
               colorChooserButton.setEnabled(false);

               if (index == 1) {
                   curColor = new Color(255, 255, 255); //pastels
               } else if (index == 2) {
                   curColor = new Color(57, 255, 15); //forest
               } else if (index == 3) {
                   curColor = new Color(3, 128, 255); //ocean
               }

           } else {
               colorChooserButton.setEnabled(true);
           }

       });
       optionsPanel.add(randomColors);


       // makeshift margins
       emptySpace = new JLabel("  ");
       emptySpace.setPreferredSize(new Dimension(190, 2));
       optionsPanel.add(emptySpace);

       colorChooserButton = new JButton("Color Chooser");
       colorChooserButton.setPreferredSize(new Dimension(190, 25));
       colorChooserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
       colorChooserButton.setFont(colorChooserButton.getFont().deriveFont(Font.BOLD));
       optionsPanel.add(colorChooserButton);

       optionsPanel.add(emptySpace);

       angleLabel = new JLabel("Angle");
       angleLabel.setForeground(Color.lightGray);
       angleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       optionsPanel.add(angleLabel);

       angleSlider = new JSlider();
       angleSlider = new JSlider();
       angleSlider.setPreferredSize(new Dimension(190, 20));
       angleSlider.setBackground(Color.black);
       angleSlider.setMaximum(90);
       angleSlider.setMinimum(-90);
       angleSlider.setMinorTickSpacing(1);
       angleSlider.setPaintTicks(true);
       angleSlider.setPaintLabels(true);
       angleSlider.setValue(0);
       optionsPanel.add(angleSlider);

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
       zoomSlider.setMaximum(20);
       zoomSlider.setMinimum(-20);
       zoomSlider.setMinorTickSpacing(1);
       zoomSlider.setPaintTicks(true);
       zoomSlider.setPaintLabels(false);
       zoomSlider.setValue(0);
       optionsPanel.add(zoomSlider);

       emptySpace = new JLabel("  ");
       emptySpace.setPreferredSize(new Dimension(190, 2));
       optionsPanel.add(emptySpace);

       JPanel buttonPanel = new JPanel();
       buttonPanel.setBackground(optionsPanelColor);
       buttonPanel.setPreferredSize(new Dimension(200, 100));
       buttonPanel.setLayout(new FlowLayout());

       center = new JButton("Center");
       center.setPreferredSize(new Dimension(80, 30));
       center.setFont(center.getFont().deriveFont(Font.BOLD));
       center.setAlignmentX(Component.CENTER_ALIGNMENT);
       center.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               clear();
               xShift = 0;
               yShift = 0;
               drawingPanel.repaint();
           }
       });
       optionsPanel.add(center);

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

       angleSlider.addChangeListener(e -> {
           clear();
           this.angle = angleSlider.getValue();
           drawingPanel.repaint();
       });

       plus.addMouseListener(new MouseAdapter() {

           @Override
           public void mouseClicked(MouseEvent e) {
               drawingPanel.paintComponent(drawingPanel.getGraphics());
               zoom += 2;
               clear();
               drawingPanel.repaint();
           }
       });

       minus.addMouseListener(new MouseAdapter() {

           @Override
           public void mouseClicked(MouseEvent e) {
               drawingPanel.paintComponent(drawingPanel.getGraphics());
               if(zoom > 2){
                   zoom -= 2;
               }
               clear();
               drawingPanel.repaint();
           }
       });
       
       zoomSlider.addChangeListener(e -> {
           clear();
           int change = zoomSlider.getValue();
           int zoomChange = zoom + change;
           if(zoomChange > 0 && ((change > 0 && change > lastChange)
                    || (zoomSlider.getValue() < 0 && change < lastChange))){
               zoom = zoomChange;

           }
           drawingPanel.repaint();
           lastChange = change;
       });

       zoomSlider.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseReleased(MouseEvent e) {
               zoomSlider.setValue(0);
           }
       });

       draw.addMouseListener(new MouseAdapter() {


           @Override
           public void mousePressed(MouseEvent e) {
               clear();
               checkComboBox();
               Drawer.setNewColors(true);
               drawIter = iter;
               if(firstDraw){
                   drawer = new Drawer("Fractal Drawer", iter, zoom,angle, xShift, yShift, drawingPanel.getGraphics(),
                           curColor,drawingPanel,curId);
                   firstDraw = false;
               }else{
                   drawer.update(drawingPanel.getGraphics(), iter, zoom,angle, xShift, yShift, curColor, curId);
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

    private void setNewColors(){
        if(previousColorIndex == randomColors.getSelectedIndex()){
            Drawer.setNewColors(false);
        }else{
            Drawer.setNewColors(true);
            previousColorIndex = randomColors.getSelectedIndex();
        }
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
                setNewColors();
                if(!isRandom() || previousColorIndex == 0){
                    curColor = previousColor;
                }
                drawer.update(drawingPanel.getGraphics(), drawIter, zoom, angle, xShift, yShift, curColor, curId);
                tDraw = new Thread(drawer, "Fractal Drawer");
                tDraw.start();
            }
        }

        MouseAdapter ma = new MouseAdapter() {
            Point startPoint, shiftStart;

            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                shiftStart = new Point(xShift, yShift);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                startPoint = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = e.getPoint();
                int x = p.x - startPoint.x;
                int y = p.y - startPoint.y;
                xShift = shiftStart.x + x;
                yShift = shiftStart.y + y;
                clear();
                repaint();
            }

        };


    }

}
