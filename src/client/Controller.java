package client;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.validator.routines.EmailValidator;

import logic.Message;

public class Controller implements ActionListener {
    private ChatMessangerAppl parent;
    private Command command;
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
        AbstractView view = ((AbstractView)findParent((Component)e.getSource(), AbstractView.class)); 
        view.clearFields();
        view.setVisible(false);
        command.execute();
    }

    private void doAction(ActionEvent e) throws ParseException {
        String comm = e.getActionCommand();
        if ("login".equals(comm)){
            LoginPanelView view = ((LoginPanelView)findParent((Component)e.getSource(), LoginPanelView.class));
            if (! EmailValidator.getInstance().isValid(view.getUserNameField().getText())){
                view.setVisible(false);
                view.getMainPanel().add(view.getErrorLable());
                view.getErrorLable().setVisible(true);
                view.setVisible(true);
                view.repaint();
                throw new ParseException("No valid user name", 0);
            } else {
                parent.getModel().setCurrentUser(view.getUserNameField().getText());
                command = new ShowChatViewCommand(parent);
            }
        } else
        if ("send".equals(comm)) {
            ChatPanelView view = ((ChatPanelView)findParent((Component)e.getSource(), ChatPanelView.class));
            parent.getModel().setCurrentMessageText(view.getTextMessageField().getText());
            // отправить на сервер
            parent.getModel().addMessage(
                    new Message(Long.valueOf(id.incrementAndGet()), view.getTextMessageField().getText(), parent.getModel().getCurrentUser(), "", Calendar.getInstance()));
            // сообщение вьюхе об изменении
            view.getMessagesTextPane().setText("<html>" + parent.getModel().getMessages() + "</html>");
            view.getTextMessageField().setText("");
            throw new ParseException("send op", 0);
        } else
        if ("logout".equals(comm)) {
            parent.setModel(new Model());
            command = new ShowLoginViewCommand(parent);
        } 
        else 
            throw new ParseException("No command", 0);
    }
    
    private static <T extends Container> T findParent(Component comp, Class<T> clazz)  {
        if (comp == null)
           return null;
        if (clazz.isInstance(comp))
           return (clazz.cast(comp));
        else
           return findParent(comp.getParent(), clazz);     
    }
}
