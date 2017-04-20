package client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.validator.routines.EmailValidator;

public class Controller implements ActionListener {
    private ChatMessangerAppl parent;
    private Command command;
    // Необходимо для демонстрации работы без сервера
    private AtomicInteger id;

    public Controller(ChatMessangerAppl chatMessangerAppl) {
        parent = chatMessangerAppl;
        id = new AtomicInteger(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            doAction(e);
        } catch (ParseException e1) {
            return;
        }
        command.execute();
    }

    private void doAction(ActionEvent e) throws ParseException {
        String comm = e.getActionCommand();
        if ("login".equals(comm)){
            LoginPanelView view = ((LoginPanelView)Utility.findParent((Component)e.getSource(), LoginPanelView.class));
            if (! EmailValidator.getInstance().isValid(view.getUserNameField().getText())){
                command = new LoginErrorCommand(view);
            } else {
                parent.getModel().setCurrentUser(view.getUserNameField().getText());
                command = new ShowChatViewCommand(parent, view);
            }
        } else
        if ("send".equals(comm)) {
            ChatPanelView view = ((ChatPanelView)Utility.findParent((Component)e.getSource(), ChatPanelView.class));
            parent.getModel().setLastMessageText(view.getTextMessageField().getText());
            command = new SendMessageCommand(parent, view, id);
        } else
        if ("logout".equals(comm)) {
            ChatPanelView view = ((ChatPanelView)Utility.findParent((Component)e.getSource(), ChatPanelView.class));
            parent.setModel(new Model());
            command = new ShowLoginViewCommand(parent, view);
        } 
        else 
            throw new ParseException("No command", 0);
    }  
}
