import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ColorChooser{

    JColorChooser colorChooser = new JColorChooser();
    JPanel colorPanel = new JPanel(),buttonPanel = new JPanel();
    JButton select, cancel;

    public ColorChooser(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        colorPanel.add(colorChooser);
        frame.add(colorPanel);
        buttonPanel.setBackground(Color.darkGray);
        buttonPanel.setSize(new Dimension(706, 40));
        buttonPanel.setLayout(new FlowLayout());
        select = new JButton("Ok");
        cancel = new JButton("Cancel");
        buttonPanel.add(select);
        buttonPanel.add(cancel);
        frame.add(buttonPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        select.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Window.curColor = colorChooser.getColor();
                Window.previousColor = Window.curColor;
                frame.dispose();
            }

        });

        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
            }

        });
    }
}
