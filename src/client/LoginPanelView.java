package client;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginPanelView extends AbstractView{
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

    /**
     * @return the mainPanel
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
    
    /**
     * @param mainPanel the mainPanel to set
     */
    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }
    
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
            loginButton.addActionListener(parent.getController());
        }
        return loginButton;
    }   
    
    JTextField getUserNameField() {
        if (userNameField == null) {
            userNameField = new JTextField(12);
            userNameField.setName("userNameField");
        }
        return userNameField;
    }
    JLabel getErrorLable() {
        if (errorLable == null){
            errorLable = new JLabel("Имя не введено или введено не верно");
            errorLable.setForeground(Color.red);
        }
        return errorLable;
    }

    public void initModel() {
        parent.getModel().setCurrentUser("");
    }
    
    public void clearFields() {
        getErrorLable().setVisible(false);
        getUserNameField().setText("");
    }

}
