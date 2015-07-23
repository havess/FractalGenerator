import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

/**
 * Created by samhaves on 15-05-12.
 */
public class Window extends JFrame{

	private static final long serialVersionUID = 1L;
	JPanel container, optionsPanel;
    JButton draw, colorChooserButton, printButton, plus, minus;
    JComboBox<String> fractalType, iterations, randomColors;
    JLabel fractalLabel, iterLabel, randomColorLabel,emptySpace, zoomLabel, xLabel, yLabel;
    JSlider zoomSlider, xSlider, ySlider;
    private DrawingPanel drawingPanel;
    private ID curId ;
    private int iter, xShift = 0, yShift = 0;
    private int zoom = 1;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Color defaultColor = new Color(255,255,255), previousColor;
    private boolean random = false;
    public static LinkedList<Fractal> fractals = new LinkedList<Fractal>();

    String[] fractalTypes = {
            "Tree",
            "Circles"
    },

    iterationNums = {
            "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"
    },
    randomColorsTypes = {
            "Solid Color",
            "Pastels",
            "Forest",
            "Ocean"
    };


    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Window().setVisible(true);
            }
        });
    }

   public Window(){
       super("Fractal Generator");

       Color optionsPanelColor = new Color(2,2,2);
       
       //GUI
       setLookAndFeel();
       setSize(screenSize);
       setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       setResizable(false);

       container = new JPanel();
       container.setSize(screenSize);
       container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));

       drawingPanel = new DrawingPanel();
       drawingPanel.setPreferredSize(new Dimension(getWidth() - 200, getHeight()));
       drawingPanel.setBackground(new Color(20, 20, 20));
       container.add(drawingPanel);

       optionsPanel = new JPanel();
       optionsPanel.setPreferredSize(new Dimension(200, getHeight()));
       optionsPanel.setMinimumSize(new Dimension(200, getHeight()));
       optionsPanel.setMaximumSize(new Dimension(200, getHeight()));
       optionsPanel.setBackground(optionsPanelColor);
       optionsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
       optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));

       fractalLabel = new JLabel("Fractal type");
       fractalLabel.setForeground(Color.lightGray);
       fractalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       optionsPanel.add(fractalLabel);

       fractalType  = new JComboBox<>();
       fractalType.setPreferredSize(new Dimension(190, 20));
       for(int i = 0; i < fractalTypes.length; i++){
           fractalType.addItem(fractalTypes[i]);
       }
       optionsPanel.add(fractalType);


       iterLabel = new JLabel("Iterations");
       iterLabel.setForeground(Color.lightGray);
       iterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       optionsPanel.add(iterLabel);

       iterations  = new JComboBox<>();
       iterations.setPreferredSize(new Dimension(190, 20));
       for(int i = 0; i < iterationNums.length; i++){
           iterations.addItem(iterationNums[i]);
       }
       optionsPanel.add(iterations);

       randomColorLabel = new JLabel("Fractal Color");
       randomColorLabel.setForeground(Color.lightGray);
       randomColorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
       optionsPanel.add(randomColorLabel);

       randomColors  = new JComboBox<>();
       randomColors.setPreferredSize(new Dimension(190, 20));
       for(int i = 0; i < randomColorsTypes.length; i++){
           randomColors.addItem(randomColorsTypes[i]);
       }
       randomColors.addActionListener(new ActionListener() {

           public void actionPerformed(ActionEvent e) {
               JComboBox temp = (JComboBox) e.getSource();
               String selectedItem = (String) temp.getSelectedItem();
               if (!selectedItem.equals(randomColorsTypes[0])) {
                   colorChooserButton.setEnabled(false);
                   for (Fractal tempFrac : fractals) {
                       tempFrac.random = true;
                       previousColor = tempFrac.getColor();//pour retourner a la couleur choisi auparavant
                       //les couleurs initiales pour certains types de couleurs "random"
                       if (selectedItem.equals(randomColorsTypes[1])) {
                           tempFrac.color = new Color(255, 255, 255); //pastels
                       } else if (selectedItem.equals(randomColorsTypes[2])) {
                           tempFrac.color = new Color(57, 255, 15); //forest
                       } else if (selectedItem.equals(randomColorsTypes[3])) {
                           tempFrac.color = new Color(3, 128, 255); //ocean
                       }
                   }
               } else {
                   colorChooserButton.setEnabled(true);
                   for (Fractal tempFrac : fractals) {
                       tempFrac.random = false;
                       tempFrac.color = previousColor;
                   }
               }

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
       //minus.addActionListener();
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
       ySlider.addChangeListener(new ChangeListener() {
           @Override
           public void stateChanged(ChangeEvent e) {
               drawingPanel.paintComponent(drawingPanel.getGraphics());
          
               for (Fractal temp : fractals) {
                   temp.yShift = ySlider.getValue();
               }

               drawingPanel.repaint();
           }
       });
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
       xSlider.addChangeListener(new ChangeListener() {
           @Override
           public void stateChanged(ChangeEvent e) {
               drawingPanel.paintComponent(drawingPanel.getGraphics());
               for (Fractal temp : fractals) {
                   temp.xShift = xSlider.getValue();
               }
               drawingPanel.repaint();
           }
       });
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
       add(container);

       init();
       
       //MouseListeners et ChangeListeners
       colorChooserButton.addMouseListener(new MouseAdapter() {

           @Override
           public void mouseClicked(MouseEvent e) {
               if (colorChooserButton.isEnabled()) {
                   new ColorChooser();
               }
           }

       });

       plus.addMouseListener(new MouseAdapter() {

           @Override
           public void mouseClicked(MouseEvent e) {
               drawingPanel.paintComponent(drawingPanel.getGraphics());
               
               for (Fractal temp : fractals) {
            	   
                   temp.zoom += 2;
                   zoomSlider.setValue(temp.zoom);
               }
               drawingPanel.repaint();
           }
       });

       minus.addMouseListener(new MouseAdapter() {

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
       });
       
       zoomSlider.addChangeListener(new ChangeListener() {
           @Override
           public void stateChanged(ChangeEvent e) {
               drawingPanel.paintComponent(drawingPanel.getGraphics());
               
               for (Fractal temp : fractals) {
                   temp.zoom = zoomSlider.getValue();
               }
               drawingPanel.repaint();
           }
       });

       draw.addMouseListener(new MouseAdapter() {

           @Override
           public void mouseClicked(MouseEvent e) {
               drawingPanel.paintComponent(drawingPanel.getGraphics());

               checkComboBox();

               for (Fractal temp : fractals) {
                   temp.iter = Integer.parseInt((String)iterations.getSelectedItem()) ;
               }
               drawingPanel.repaint();
           }
       });

   }
   	//ajoute les fractales a la liste
    private void init(){
        fractals.add(new Tree(this.getWidth() - 200,this.getHeight() - 200, iter, zoom, xShift, yShift,defaultColor,random, drawingPanel, ID.Tree));
        fractals.add(new Circles(this.getWidth() - 200,this.getHeight() - 200, iter, zoom, xShift, yShift,defaultColor,random, drawingPanel, ID.Circles));
    }
    
    private void setLookAndFeel() {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");}
		catch(Exception e){
			//ignore error
		}
		
	}

    private void checkComboBox(){

        String chosenFrac = fractalType.getSelectedItem().toString();
        
        switch(chosenFrac){
            case "Circles":
                this.curId = ID.Circles;
                break;
            case "Tree":
                this.curId = ID.Tree;
                break;
        }

    }
    
    //JPanel ou on dessine les fractales
    private class DrawingPanel extends JPanel {

        public DrawingPanel(){
            setDoubleBuffered(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.darkGray);
        }

        @Override
        public void repaint(){
            for(Fractal temp: fractals){
                if (temp.getID() == curId) temp.paintComponent(this.getGraphics());
            }

        }

    }
}
