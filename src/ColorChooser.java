import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by samhaves on 15-05-21.
 */
public class ColorChooser extends JFrame {

    JColorChooser colorChooser = new JColorChooser();
    JPanel colorPanel = new JPanel(),buttonPanel = new JPanel();
    JButton select, cancel;

    public ColorChooser(){
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        colorPanel.add(colorChooser);
        add(colorPanel);
        buttonPanel.setBackground(Color.darkGray);
        buttonPanel.setSize(new Dimension(706, 40));
        buttonPanel.setLayout(new FlowLayout());
        select = new JButton("Ok");
        cancel = new JButton("Cancel");
        buttonPanel.add(select);
        buttonPanel.add(cancel);
        add(buttonPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        //assigne a chaque fractale la couleur choisi si on pousse "ok"
        select.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Fractal temp : Window.fractals) {
                    temp.color = colorChooser.getColor();
                }
                dispose();
            }

        });

        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }

        });
    }
}
