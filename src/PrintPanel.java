
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.print.*;

public class PrintPanel implements Printable, ActionListener {

    JPanel panelToPrint;

    public int print(Graphics g, PageFormat pf, int page) throws
            PrinterException {

        if (page > 0) { 
        	// si la page est hors du document arrete 
            return NO_SUCH_PAGE;
        }

        //pour qu'il n'est pas hors page
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        //ici on imprime
        panelToPrint.printAll(g);

        //retourne la page fait partie du document
        return PAGE_EXISTS;
    }

    public void actionPerformed(ActionEvent e) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
              /* The job did not successfully complete */
            }
        }
    }

    public PrintPanel(JPanel p) {
        panelToPrint = p;
    }
}
