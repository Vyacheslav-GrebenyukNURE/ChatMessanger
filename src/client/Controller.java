package client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import org.apache.commons.validator.routines.EmailValidator;

public class Controller implements ActionListener {
    private ChatMessengerAppl parent;
    private Command command;

    private Controller() {     
    }

    public static Controller getInstance() {
        return ControllerHolder.INSTANCE;
    }

    private static class ControllerHolder {
        private static final Controller INSTANCE = new Controller();
    }
    
    public void setParent(ChatMessengerAppl parent) {
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            action(e);
        } catch (ParseException e1) {
            return;
        }
        command.execute();
    }

    private void action(ActionEvent e) throws ParseException {
        String comm = e.getActionCommand();
        switch (comm) {
        case "login": {
            LoginPanelView view = Utility.findParent((Component) e.getSource(), LoginPanelView.class);
            if (!EmailValidator.getInstance().isValid(view.getUserNameField().getText())) {
                command = new LoginErrorCommand(view);
            } else {
                parent.getModel().setCurrentUser(view.getUserNameField().getText());
                command = new ShowChatViewCommand(parent, view);
            }
        }
            break;
        case "send": {
            ChatPanelView view = Utility.findParent((Component) e.getSource(), ChatPanelView.class);
            parent.getModel().setLastMessageText(view.getTextMessageField().getText());
            command = new SendMessageCommand(parent, view);
        }
            break;
        case "logout": {
            ChatPanelView view = Utility.findParent((Component) e.getSource(), ChatPanelView.class);
            parent.getModel().initialize();
            command = new ShowLoginViewCommand(parent, view);
        }
            break;
        default:
            throw new ParseException("No command", 0);
        }
    }
}
