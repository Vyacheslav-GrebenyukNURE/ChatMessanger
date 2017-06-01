package client;

import java.awt.BorderLayout;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatMessengerAppl extends JFrame{
    final static Logger LOGGER = LogManager.getLogger();
    
    private static final long serialVersionUID = 6486409443796464545L;
    private static final int FRAME_HEIGHT = 600;
    private static final int FRAME_WIDTH = 400;
    static final int PORT = 7070;
    static final int PERIOD = 1000;
    static final int DELAY = 100;

    private static final Model MODEL;
    private static final Controller CONTROLLER;
    private static final ViewFactory VIEWS;
    private Timer timer;
    
    static {
        MODEL = Model.getInstance();
        CONTROLLER = Controller.getInstance();
        VIEWS = ViewFactory.getInstance();
        LOGGER.trace("MVC instantiated", MODEL, CONTROLLER, VIEWS);
    }

    public ChatMessengerAppl() {
        super();
        initialize();
    }
    
    public static void main(String[] args) {
        JFrame frame = new ChatMessengerAppl();
        frame.setVisible(true);
        frame.repaint();
    }

    private void initialize() {
        AbstractView.setParent(this);
        MODEL.setParent(this);
        MODEL.initialize();
        CONTROLLER.setParent(this);
        VIEWS.viewRegister("login", LoginPanelView.getInstance());
        VIEWS.viewRegister("chat", ChatPanelView.getInstance());
        timer = new Timer("Server request for update messages");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setTitle("Chat Messenger");
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(getLoginPanelView(), BorderLayout.CENTER);
        this.setContentPane(contentPanel);
    }

    public Model getModel() {
        return MODEL;
    }

    public Controller getController() {
        return CONTROLLER;
    }

    public Timer getTimer() {
        return timer;
    }
    
    public void setTimer(Timer timer) {
        this.timer = timer;
    }    

    private JPanel getLoginPanelView() {        
        LoginPanelView loginPanelView = VIEWS.getView("login");
        loginPanelView.initModel();
        return loginPanelView;
    }
    
    JPanel getChatPanelView(boolean doGetMessages) {
        ChatPanelView chatPanelView = VIEWS.getView("chat");
        chatPanelView.initModel(doGetMessages);
        return chatPanelView;
    }

    private void showPanel(JPanel panel) {
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setVisible(true);
        panel.repaint();        
    }

    public void showChatPanelView() {
        showPanel(getChatPanelView(true));        
    }
    public void showLoginPanelView() {
        showPanel(getLoginPanelView());        
    }
 }
