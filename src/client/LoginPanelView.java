package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.validator.routines.EmailValidator;

public class LoginPanelView extends JPanel implements ActionListener{
    /**
     * 
     */
    private static final long serialVersionUID = -6308995713965796115L;
    private ChatMessangerAppl parent;
    private JPanel loginPanel;
    private JPanel mainPanel;
    private JButton loginButton;
    private JTextField userNameField;
    private JLabel errorLable;

    public LoginPanelView(ChatMessangerAppl chatMessageAppl) {
        this.parent = chatMessageAppl;
        initialize();        
    }
    
    private void initialize() {
        this.setName("loginPanelView");
        this.setLayout(new BorderLayout());
        this.add(getLoginPanel(), BorderLayout.CENTER);
    }
    
    private JPanel getLoginPanel() {
        if (loginPanel == null) {
            loginPanel = new JPanel();
            loginPanel.setLayout(new BorderLayout());
            addLabeledField(loginPanel, "Введите имя", getUserNameField()); 
            loginPanel.add(getLoginButton(), BorderLayout.SOUTH);            
        }
        return loginPanel;
    }

    private void addLabeledField(JPanel panel, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setLabelFor(textField);
        panel.add(label, BorderLayout.NORTH);
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(textField, BorderLayout.NORTH);
        panel.add(mainPanel, BorderLayout.CENTER);
    }

    private JButton getLoginButton() {
        if (loginButton == null) {
            loginButton = new JButton();
            loginButton.setText("Зарегистрироваться");
            loginButton.setName("loginButton");
            loginButton.setActionCommand("login");
            loginButton.addActionListener(this);
        }
        return loginButton;
    }   
    
    private JTextField getUserNameField() {
        if (userNameField == null) {
            userNameField = new JTextField(12);
            userNameField.setName("userNameField");
        }
        return userNameField;
    }
/**
 * Controller
 */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            doAction(e);
        } catch (ParseException e1) {
            return;
        }
        clearFields();
        this.setVisible(false);
        parent.showChatPanelView();
    }

    private void doAction(ActionEvent e) throws ParseException {
        if ("login".equals(e.getActionCommand())){
//            if (getUserNameField().getText().length() == 0) {
            if (! EmailValidator.getInstance().isValid(getUserNameField().getText())){
                this.setVisible(false);
                mainPanel.add(getErrorLable(), BorderLayout.SOUTH);
                this.setVisible(true);
                this.repaint();
                throw new ParseException("No valid user name", 0);
            } else {
                parent.getModel().setCurrentUser(getUserNameField().getText());
            }
        } else 
            throw new ParseException("No command", 0);
    }
    
    private JLabel getErrorLable() {
        if (errorLable == null){
            errorLable = new JLabel("Имя не введено или введено не верно");
            errorLable.setForeground(Color.red);
        }
        return errorLable;
    }

    public void initModel() {
        parent.getModel().setCurrentUser("");
    }
    
    private void clearFields() {
        getErrorLable().setVisible(false);
        getUserNameField().setText("");
    }

}
