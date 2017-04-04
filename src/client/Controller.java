package client;

import java.awt.BorderLayout;
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
        ((AbstractView)findParent((Component)e.getSource(), AbstractView.class)).clearFields();
        ((AbstractView)findParent((Component)e.getSource(), AbstractView.class)).setVisible(false);
        command.execute();
    }

    private void doAction(ActionEvent e) throws ParseException {
        if ("login".equals(e.getActionCommand())){
            LoginPanelView view = ((LoginPanelView)findParent((Component)e.getSource(), LoginPanelView.class));
            if (! EmailValidator.getInstance().isValid(view.getUserNameField().getText())){
                view.setVisible(false);
                view.getMainPanel().add(view.getErrorLable(), BorderLayout.SOUTH);
                view.getErrorLable().setVisible(true);
                view.setVisible(true);
                view.repaint();
                throw new ParseException("No valid user name", 0);
            } else {
                parent.getModel().setCurrentUser(view.getUserNameField().getText());
                command = new ShowChatViewCommand(parent);
            }
        } else
        if ("send".equals(e.getActionCommand())) {
            ChatPanelView view = ((ChatPanelView)findParent((Component)e.getSource(), ChatPanelView.class));
            parent.getModel().setCurrentMessageText(view.getTextMessageField().getText());
            // отправить на сервер
            parent.getModel().addMessage(new Message(new Long(id.incrementAndGet()), view.getTextMessageField().getText(), parent.getModel().getCurrentUser(), "", Calendar.getInstance()));
            // сообщение вьюхе об изменении
            view.getMessagesTextPane().setText("<html>" + parent.getModel().getMessages() + "</html>");
            view.getTextMessageField().setText("");
            throw new ParseException("send op", 0);
        } else
        if ("logout".equals(e.getActionCommand())) {
            parent.setModel(new Model());
            command = new ShowLoginViewCommand(parent);
        } 
        else 
            throw new ParseException("No command", 0);
    }
    
    public static <T extends Container> T findParent(Component comp, Class<T> clazz)  {
        if (comp == null)
           return null;
        if (clazz.isInstance(comp))
           return (clazz.cast(comp));
        else
           return findParent(comp.getParent(), clazz);     
    }
}
