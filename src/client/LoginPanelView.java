package client;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginPanelView extends AbstractView{
    final static Logger LOGGER = LogManager.getLogger(LoginPanelView.class);
    
    private static final long serialVersionUID = -6308995713965796115L;    
    private JPanel loginPanel;
    private JPanel mainPanel;
    private JButton loginButton;
    private JTextField userNameField;
    private JLabel errorLable;

    private LoginPanelView() {
        super();
        initialize();        
    }
    
    public static LoginPanelView getInstance() {
        return LoginPanelViewHolder.INSTANCE;
    }

    private static class LoginPanelViewHolder {
        private static final LoginPanelView INSTANCE = new LoginPanelView();
    }
    
    @Override public void initialize() {
        this.setName("loginPanelView");
        this.setLayout(new BorderLayout());
        this.add(getLoginPanel(), BorderLayout.CENTER);
        InputMap im = getLoginButton().getInputMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
        im.put(KeyStroke.getKeyStroke("released ENTER"), "released");
    }
    
    /**
     * @param mainPanel the mainPanel to set
     */
    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }
    
    private JPanel getLoginPanel() {
        if (loginPanel == null) {
            loginPanel = new JPanel();
            loginPanel.setLayout(new BorderLayout());
            loginPanel.add(getMainPanel(), BorderLayout.NORTH);
            // здесь можно добавить пары полей ввода дополнительных данных и меток,
            // например, поле ввода адреса сервера             
            addLabeledField(getMainPanel(), "Введите имя (адрес е-почты)", getUserNameField()); 
            loginPanel.add(getLoginButton(), BorderLayout.SOUTH);            
        }
        return loginPanel;
    }
    
    JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        }
        return mainPanel;
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
    
    @SuppressWarnings("unused")
    private void setErrorLableText(String text){
        getErrorLable().setText(text);
    }

    public void initModel() {
        parent.getModel().setCurrentUser("");
        getUserNameField().requestFocusInWindow();
        parent.getRootPane().setDefaultButton(getLoginButton());
    }
    
    public void clearFields() {
        getErrorLable().setVisible(false);
        getUserNameField().setText("");
    }

}
