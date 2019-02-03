
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class JremoveBOM extends JFrame implements ActionListener {


    JMenuBar mb;
    JMenu mFile;
    JMenu jmnuHelp;
    JMenuItem mOpen;
    JMenuItem mExit;
    JMenuItem mnuiHelpTopics;
    JMenuItem mnuiAbout;
    JFileChooser fc;
    JScrollPane scrollPaneEditor;
    BasicUtils basicUtils;
    MediaTracker tracker;
    String stringDocsDir;
    String fullStatString;
    private JEditorPane editorpane;
    private URL helpURL;
    public JremoveBOM(String title, String[] bomFiles) {
        super(title);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        basicUtils = new BasicUtils();

        fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);

        stringDocsDir = basicUtils.getWorkingDir() + "/docs/";
        fullStatString = "JremoveBOM loaded with args[]: " + Arrays.toString(bomFiles) + "<br>";
        this.setEnabled(true);
        tracker = new MediaTracker(this);
        Image iconHelpImg = Toolkit.getDefaultToolkit().getImage(basicUtils.getUfile("docs/JremoveBOM.gif"));
        tracker.addImage(iconHelpImg, 1);
        try {
            tracker.waitForAll();
        } catch (Exception interruptedexception) {
            System.out.println(interruptedexception.toString());

        }
        setIconImage(iconHelpImg);

        mb = new JMenuBar();
        // mb.setBorderPainted(true);
        mFile = new JMenu("File");

        mFile.getPopupMenu().setLightWeightPopupEnabled(false);
        mOpen = new JMenuItem("Open");
        mExit = new JMenuItem("Exit");
        mFile.add(mOpen);
        mFile.addSeparator();
        mFile.add(mExit);

        jmnuHelp = new JMenu("Help");
        jmnuHelp.getPopupMenu().setLightWeightPopupEnabled(false);
        jmnuHelp.setForeground(new Color(0, 0, 255)); //XXX windows lnf?
        mnuiAbout = new JMenuItem("About");
        mnuiHelpTopics = new JMenuItem("Help", new ImageIcon(basicUtils.getUfile("cbox/images/iconHelp.gif")));


        jmnuHelp.add(mnuiHelpTopics);
        jmnuHelp.addSeparator();
        jmnuHelp.add(mnuiAbout);


        mb.add(mFile);
        mb.add(jmnuHelp);


        mOpen.addActionListener(this);
        mnuiHelpTopics.addActionListener(this);
        mnuiAbout.addActionListener(this);
        mExit.addActionListener(this);


        editorpane = new JEditorPane();
        scrollPaneEditor = new JScrollPane(editorpane);
        editorpane.setEditable(false);
        HTMLEditorKit kit = (HTMLEditorKit) editorpane.getEditorKitForContentType("text/html");
        editorpane.setEditorKit(kit);
        //anonymous inner listener
        editorpane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent ev) {
                try {
                    if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        System.out.println("URL getSourceElement: " + ev.getSourceElement());
                        System.out.println("URL: getDescription" + ev.getDescription());
                        doHEventUrlAction(ev.getDescription());
                    }
                } catch (Exception ex) {
                    //put message in window
                    ex.printStackTrace();
                }
            }
        });


        JPanel panelHWmain = new JPanel(new BorderLayout());
        panelHWmain.add("Center", scrollPaneEditor);
        getContentPane().add(panelHWmain, BorderLayout.CENTER);

        addButtons();
        // no need for close listener just dispose
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // dynamically set location
        calculateLocation();
        setVisible(true);
        requestFocus();
        try {
            for (String bomFile : bomFiles) {
                editorpane.setText(getTemplate(bomFile));
            }
        } catch (Exception ex) {
            editorpane.setText(ex.toString());
        }
        setJMenuBar(mb);
    }

    public static void main(String[] args) {
        try {
            new JremoveBOM("JremoveBOM", args);
        } catch (Exception e) {
            String[] gg = new String[2];
            gg[0] = "error";
            gg[1] = e.toString();
            gg[2] = Arrays.toString(args);
            new JremoveBOM("JremoveBOM", gg);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        try {
            if (obj == mOpen) {
                int returnVal = fc.showOpenDialog(JremoveBOM.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] file = fc.getSelectedFiles();
                    for (File aFile : file) {
                        editorpane.setText(getTemplate(aFile.toString()));
                    }
                } else {
                    // canceled
                }
            }


            if (obj == mnuiHelpTopics) {
                new HelpWindow("JremoveBOM Help", "index_body.html");
            }

            if (obj == mnuiAbout) {
                new AboutDialog(this, "JremoveBOM");
            }
            if (obj == mExit) {
                System.exit(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addButtons() {
        JButton btncontents = new JButton("Contents");
        btncontents.addActionListener(this);
        JButton btnclose = new JButton("Close");
        btnclose.addActionListener(this);
        JPanel panebuttons = new JPanel();
        getContentPane().add(panebuttons, BorderLayout.SOUTH);
    }

    /**
     * locate in middle of screen
     */
    private void calculateLocation() {
        Dimension screendim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(730, 300);
        int locationx = (screendim.width - (screendim.width - 100)) / 2;
        int locationy = (screendim.height - (screendim.height - 100)) / 2;
        setLocation(locationx, locationy);
    }

    public String getTemplate(String s) {
        String cleanTplateString;
        try {
            File file = new File(s);
            FileInputStream fis = new FileInputStream(file);
            long size = file.length();
            byte[] b = new byte[(int) size];
            int bytesRead = fis.read(b, 0, (int) size);
            if (bytesRead != size) {
                throw new IOException("cannot read file");
            } else {
                fis.close();
            }
            int b0 = b[0] & 0xff;
            int b1 = b[1] & 0xff;
            int b2 = b[2] & 0xff;
            if (b0 == 0xef && b1 == 0xbb && b2 == 0xbf) {
                System.out.println("Hint: the file starts with a UTF-8 BOM.");
                cleanTplateString = new String(b, 3, b.length - 3, "UTF8");
                fullStatString += "<b>BOM found in</b>: " + s + "<br>";
                basicUtils.saveTextString(cleanTplateString, s);
            } else {
                fullStatString += "no BOM found in: " + s + "<br>";
            }
        } catch (Exception ex) {
            fullStatString += ex.toString();
        }
        return fullStatString;
    }

    public void doHEventUrlAction(String urlString) {
        if (urlString.startsWith("http")) {
            try {
                BrowserControl.displayURL(urlString);
            } catch (Exception ex) {
                editorpane.setText(ex.toString());
            }
        } else {
            editorpane.setText(getTemplate(basicUtils.getUfile("docs/" + urlString)));
            editorpane.repaint();
            editorpane.setCaretPosition(0);
        }

    }
}