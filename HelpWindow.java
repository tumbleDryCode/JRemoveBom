import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class HelpWindow extends JFrame implements ActionListener {

    JScrollPane scrollPaneEditor;
    JScrollPane scrollPaneMenu;
    BasicUtils basicUtils;
    MediaTracker tracker;
    String stringDocsDir;
    private JEditorPane editorpane;
    private JEditorPane menuPane;
    private URL helpURL;

    public HelpWindow(String title, String helpURL) {
        super(title);
        basicUtils = new BasicUtils();
        stringDocsDir = basicUtils.getWorkingDir() + "/docs/";
        this.setEnabled(true);
        tracker = new MediaTracker(this);
        Image iconHelpImg = Toolkit.getDefaultToolkit().getImage(basicUtils.getUfile("docs/images/iconHelp.gif"));
        tracker.addImage(iconHelpImg, 1);
        try {
            tracker.waitForAll();
        } catch (Exception interruptedexception) {
            System.out.println(interruptedexception.toString());

        }
        setIconImage(iconHelpImg);


        editorpane = new JEditorPane();
        scrollPaneEditor = new JScrollPane(editorpane);
        editorpane.setEditable(false);
        HTMLEditorKit kit = (HTMLEditorKit) editorpane.getEditorKitForContentType("text/html");
        editorpane.setEditorKit(kit);
        editorpane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent ev) {
                try {
                    if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        doHEventUrlAction(ev.getDescription());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        menuPane = new JEditorPane();
        scrollPaneMenu = new JScrollPane(menuPane);

        menuPane.setEditable(false);
        HTMLEditorKit kitA = (HTMLEditorKit) menuPane.getEditorKitForContentType("text/html");
        menuPane.setEditorKit(kitA);
        menuPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent ev) {
                try {
                    if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        doHEventUrlAction(ev.getDescription());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        JSplitPane splitPaneHelp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPaneMenu, scrollPaneEditor);
        Border emptyBdr = BorderFactory.createEmptyBorder(2, 2, 2, 2);
        splitPaneHelp.setOneTouchExpandable(true);
        splitPaneHelp.setContinuousLayout(false);
        splitPaneHelp.setBorder(emptyBdr);
        splitPaneHelp.setDividerLocation(178);


        JPanel panelHWmain = new JPanel(new BorderLayout());
        panelHWmain.add("Center", splitPaneHelp);
        getContentPane().add(panelHWmain, BorderLayout.CENTER);


        addButtons();
        // no need for close listener just dispose
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        calculateLocation();
        setVisible(true);
        requestFocus();
        try {
            doHEventUrlAction(helpURL);
        } catch (Exception ex) {
            editorpane.setText(getTemplate("docs/index_body.html"));
        }
        try {
            menuPane.setText(getTemplate("docs/index_menu.html"));
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public static void main(String[] args) {
        try {
            if ((args[0] != null) && (args[1] != null)) {
                new HelpWindow(args[0], args[1]);
            } else {
                new HelpWindow("help", "docs/index_body.html");
            }
        } catch (Exception e) {
            new HelpWindow("help", "docs/index_body.html");
        }
    }

    /**
     * An Actionlistener so must implement this method
     */
    public void actionPerformed(ActionEvent e) {
        String strAction = e.getActionCommand();
        try {
            if (Objects.equals(strAction, "Contents")) {
                editorpane.setPage(helpURL);
            }
            if (Objects.equals(strAction, "Close")) {
                processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        } catch (IOException ex) {
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


    private void calculateLocation() {
        Dimension screendim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new Dimension(screendim.width - 200, screendim.height - 200));
        int locationx = (screendim.width - (screendim.width - 200)) / 2;
        int locationy = (screendim.height - (screendim.height - 200)) / 2;
        setLocation(locationx, locationy);
    }

    public String getTemplate(String s) {
        String cleanTplateString = "noQvalue";
        try {
            String tplateString = basicUtils.readFileAsString(s);
            String repTplateUri = "src=\"file:///" + stringDocsDir;
            String cleanFileString = basicUtils.replaceString(repTplateUri, "\\", "/");
            cleanTplateString = basicUtils.replaceString(tplateString, "src=\"", cleanFileString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cleanTplateString;
    }

    public void doHEventUrlAction(String urlString) {
        if (urlString.startsWith("http")) {
            try {
                BrowserControl.displayURL(urlString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            editorpane.setText(getTemplate(basicUtils.getUfile("docs/" + urlString)));
            editorpane.repaint();
            editorpane.setCaretPosition(0);
        }

    }
}