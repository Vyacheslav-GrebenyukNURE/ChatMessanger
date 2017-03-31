package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import logic.Message;

public class ChatPanelView extends JPanel implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = 5849245903242954304L;
    private ChatMessangerAppl parent;
    private JScrollPane messagesListPanel;
    private JTextPane messagesTextPane;
    private JPanel textMessagePanel;
    private JButton sendMessageButton;    
    private JTextField textMessageField;
    private JButton logoutButton;
    
    private AtomicInteger id;

    public ChatPanelView(ChatMessangerAppl chatMessageAppl) {
        this.parent = chatMessageAppl;
        id = new AtomicInteger(0);
        initialize();
    }

    private void initialize() {
        this.setName("chatPanelView");
        this.setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.add(new JLabel("Привет, " + parent.getModel().getCurrentUser() + "!"), BorderLayout.WEST);
        header.add(getLogoutButton(), BorderLayout.EAST);
        this.add(header, BorderLayout.NORTH);
        this.add(getMessagesListPanel(), BorderLayout.CENTER);
        this.add(getTextMessagePanel(), BorderLayout.SOUTH);
    }

    private JButton getLogoutButton() {
        if (logoutButton == null) {
            logoutButton = new JButton();
            logoutButton.setText("Выйти");
            logoutButton.setName("logoutButton");
            logoutButton.setActionCommand("logout");
            logoutButton.addActionListener(this);
        }
        return logoutButton;
    }

    private JPanel getTextMessagePanel() {
        if (textMessagePanel == null) {
            textMessagePanel = new JPanel();
            textMessagePanel.setLayout(new BorderLayout());
            addLabeledField(textMessagePanel, "Введите сообщение", getTextMessageField());
            textMessagePanel.add(getSendMessageButton(), BorderLayout.EAST);
        }
        return textMessagePanel;
    }

    private void addLabeledField(JPanel panel, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setLabelFor(textField);
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
    }

    private JTextField getTextMessageField() {
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
            sendMessageButton.addActionListener(this);
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
//            messagesTextArea
//                    .setText("\n\n\n\n\n\n1\n\n\n\n\n\n\n4\n\n\n\n\n\n\n\n\n\n\n\n\n9\n\n\n\n\n\n\n\n\n\n\n\n\n\n10\n");
        }
        return messagesTextPane;
    }

    public void initModel() {
        parent.getModel().setCurrentMessageText("");
        // получить с сервера начальный список сообщений
        getMessagesTextPane().setText(parent.getModel().getMessages());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            doAction(e);
        } catch (ParseException e1) {
            return;
        }
        clearFields();
        this.setVisible(false);
        parent.showLoginPanelView();
    }

    private void clearFields() {
        getMessagesTextPane().setText("");
        getTextMessageField().setText("");
    }

    private void doAction(ActionEvent e) throws ParseException {
        if ("send".equals(e.getActionCommand())) {
            parent.getModel().setCurrentMessageText(this.getTextMessageField().getText());
            // отправить на сервер
            parent.getModel().addMessage(new Message(new Long(id.incrementAndGet()), this.getTextMessageField().getText(), parent.getModel().getCurrentUser(), "", Calendar.getInstance()));
            // сообщение вьюхе об изменении
            getMessagesTextPane().setText("<html>" + parent.getModel().getMessages() + "</html>");
            getTextMessageField().setText("");
            throw new ParseException("send op", 0);
        } else
        if ("logout".equals(e.getActionCommand())) {
            parent.setModel(new Model());
        } else
            throw new ParseException("no operation", 0);
    }
}
