package client;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

public class ChatPanelView extends AbstractView {
    /**
     * 
     */
    private static final long serialVersionUID = 5849245903242954304L;
    private JScrollPane messagesListPanel;
    private JTextPane messagesTextPane;
    private JPanel textMessagePanel;
    private JButton sendMessageButton;    
    private JTextField textMessageField;
    private JButton logoutButton;
    
    public ChatPanelView(ChatMessengerAppl chatMessageAppl) {
        super(chatMessageAppl);
        initialize();
    }

    @Override public void initialize() {
        this.setName("chatPanelView");
        this.setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.add(new JLabel("Привет, " + parent.getModel().getCurrentUser() + "!"), BorderLayout.WEST);
        header.add(getLogoutButton(), BorderLayout.EAST);
        this.add(header, BorderLayout.NORTH);
        this.add(getMessagesListPanel(), BorderLayout.CENTER);
        this.add(getTextMessagePanel(), BorderLayout.SOUTH);
        InputMap im = sendMessageButton.getInputMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
        im.put(KeyStroke.getKeyStroke("released ENTER"), "released");
        getTextMessageField().requestFocus();
        parent.getRootPane().setDefaultButton(sendMessageButton);
    }

    private JButton getLogoutButton() {
        if (logoutButton == null) {
            logoutButton = new JButton();
            logoutButton.setText("Выйти");
            logoutButton.setName("logoutButton");
            logoutButton.setActionCommand("logout");
            logoutButton.addActionListener(parent.getController());
        }
        return logoutButton;
    }

    private JPanel getTextMessagePanel() {
        if (textMessagePanel == null) {
            textMessagePanel = new JPanel();
            textMessagePanel.setLayout(new BoxLayout(textMessagePanel, BoxLayout.X_AXIS));
            addLabeledField(textMessagePanel, "Введите сообщение", getTextMessageField());
            textMessagePanel.add(getSendMessageButton(), BorderLayout.EAST);
        }
        return textMessagePanel;
    }

    JTextField getTextMessageField() {
        if (textMessageField == null) {
            textMessageField = new JTextField(12);
            textMessageField.setName("textMessageField");
        }
        return textMessageField;
    }

    private JButton getSendMessageButton() {
        if (sendMessageButton == null) {
            sendMessageButton = new JButton();
            sendMessageButton.setText("Отправить");
            sendMessageButton.setName("sendMessageButton");
            sendMessageButton.setActionCommand("send");            
            sendMessageButton.addActionListener(parent.getController());
        }
        return sendMessageButton;
    }

    private JScrollPane getMessagesListPanel() {
        if (messagesListPanel == null) {
            messagesListPanel = new JScrollPane(getMessagesTextPane());
            messagesListPanel.setSize(getMaximumSize());
            messagesListPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            // messagesListPanel.getVerticalScrollBar().addAdjustmentListener(new
            // AdjustmentListener() {
            // public void adjustmentValueChanged(AdjustmentEvent e) {
            // e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            // }
            // });
        }
        return messagesListPanel;
    }

    protected JTextPane getMessagesTextPane() {
        if (messagesTextPane == null) {
            messagesTextPane = new JTextPane();
            messagesTextPane.setEditable(false);
            messagesTextPane.setContentType("text/html");
            messagesTextPane.setName("messageTextArea");
            ((DefaultCaret) messagesTextPane.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        }
        return messagesTextPane;
    }

    public void initModel() {
        parent.getModel().setLastMessageText("");
        // Получить сообщение с сервера
        getMessagesTextPane().setText(parent.getModel().messagesToString());
    }

    public void clearFields() {
        getMessagesTextPane().setText("");
        getTextMessageField().setText("");
    }

    public void modelChangedNotification() {
        getMessagesTextPane().setText(parent.getModel().messagesToString());                
    }
}
