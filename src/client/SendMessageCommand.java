package client;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import logic.Message;

public class SendMessageCommand implements Command {
    ChatMessangerAppl parent;
    ChatPanelView view;
    AtomicInteger id;

    public SendMessageCommand(ChatMessangerAppl parent, ChatPanelView view, AtomicInteger id) {
        this.parent = parent;
        this.view = view;
        this.id = id;
    }

    @Override
    public void execute() {
      // отправить сообщение на сервер
      parent.getModel().addMessage(
              new Message(Long.valueOf(id.incrementAndGet()), view.getTextMessageField().getText(), parent.getModel().getCurrentUser(), "", Calendar.getInstance()));
      // получить обновления и вызвать нотификацию
      view.getMessagesTextPane().setText("<html>" + parent.getModel().getMessages() + "</html>");
      view.getTextMessageField().setText("");
    }
}
