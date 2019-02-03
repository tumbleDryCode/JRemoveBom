import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class AboutDialog extends JDialog
        implements ActionListener, MouseListener {
    JButton nogo;
    JLabel urlLabel;


    public AboutDialog(Frame frame, String theTitle) {
        super(frame, theTitle, true);
        getContentPane().setLayout(new BorderLayout());
        JLabel topLabel = new JLabel(theTitle, JLabel.CENTER);
        urlLabel = new JLabel("visit: tumbleDryCode", JLabel.CENTER);
        urlLabel.setForeground(new Color(0, 0, 180));
        urlLabel.addMouseListener(this);


        JPanel inputpan = new JPanel(new BorderLayout());
        inputpan.add("North", topLabel);
        inputpan.add("Center", urlLabel);


        nogo = new JButton("Close");
        nogo.setBackground(new Color(225, 180, 180));
        nogo.setForeground(new Color(0, 0, 180));
        nogo.addActionListener(this);


        JPanel mainpanel = new JPanel(new BorderLayout());
        mainpanel.add("Center", inputpan);
        mainpanel.add("South", nogo);

        getContentPane().add(mainpanel, "Center");
        addWindowListener(new AboutDialogWinListener());
        setSize(220, 100);
        setResizable(false);
        setLocation(200, 220);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent actionevent) {
        if (actionevent.getSource() == nogo) {
            dispose();
        }
    }

    public void mousePressed(MouseEvent mouseevent) {
        if (mouseevent.getSource() == urlLabel) {
            try {
                BrowserControl.displayURL("https://github.com/tumbleDryCode");
            } catch (Exception hexeception) {
                System.out.println("exeception: " + hexeception.toString());
            }
            dispose();
        }

    }

    public void mouseEntered(MouseEvent mouseevent) {
        if (mouseevent.getSource() == urlLabel) {
            setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        }
    }

    public void mouseClicked(MouseEvent mouseevent) {
    }

    public void mouseReleased(MouseEvent mouseevent) {
    }

    public void mouseExited(MouseEvent mouseevent) {
        if (mouseevent.getSource() == urlLabel) {
            setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }

    public class AboutDialogWinListener extends WindowAdapter {
        public AboutDialogWinListener() {
        }

        public void windowClosing(WindowEvent windowevent) {
            dispose();
        }
    }
}
