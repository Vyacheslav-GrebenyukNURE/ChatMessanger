package client;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChatMessangerAppl extends JFrame{
    /**
     * 
     */
    private static final long serialVersionUID = 6486409443796464545L;
    private static final int FRAME_HEIGHT = 600;
    private static final int FRAME_WIDTH = 400;

    private Model model;
    private Controller controller;
    private JPanel contentPanel;
    private JPanel loginPanelView;
    private JPanel chatPanelView;

    public ChatMessangerAppl() {
        super();
        initialize();
    }
    
    public static void main(String[] args) {
        JFrame frame = new ChatMessangerAppl();
        frame.setVisible(true);
        frame.repaint();
    }

    private void initialize() {
        model = new Model();
        controller = new Controller(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setTitle("Chat Messanger"); 
        this.setContentPane(getContentPanel());
    }

    /**
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * @return the controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    private Container getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(getLoginPanelView(), BorderLayout.CENTER);
        }
        return contentPanel;
    }

    private JPanel getLoginPanelView() {
        if (loginPanelView == null) {
            loginPanelView = new LoginPanelView(this);
        }
        ((LoginPanelView) loginPanelView).initModel();
        return loginPanelView;
    }
    
    private JPanel getChatPanelView() {
        if (chatPanelView == null) {
            chatPanelView = new ChatPanelView(this);
        }
        ((ChatPanelView) chatPanelView).initModel();
        return chatPanelView;
    }
    
    private void showPanel(JPanel panel) {
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setVisible(true);
        panel.repaint();        
    }

    public void showChatPanelView() {
        showPanel(getChatPanelView());        
    }
    public void showLoginPanelView() {
        showPanel(getLoginPanelView());        
    }    
 }
