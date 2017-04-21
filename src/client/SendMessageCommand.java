package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import logic.Message;

public class SendMessageCommand implements Command {
    private ChatMessengerAppl parent;
    private ChatPanelView view;
    private AtomicInteger id;
    private final int PORT = 7070;

    public SendMessageCommand(ChatMessengerAppl parent, ChatPanelView view, AtomicInteger id) {
        this.parent = parent;
        this.view = view;
        this.id = id;
    }

    @Override
    public void execute() {
        // отправить сообщение на сервер
        parent.getModel().addMessage(new Message(Long.valueOf(id.incrementAndGet()),
                view.getTextMessageField().getText(), parent.getModel().getCurrentUser(), "", Calendar.getInstance()));
        // получить обновления и вызвать нотификацию
        view.getMessagesTextPane().setText(parent.getModel().getMessages());
        view.getTextMessageField().setText("");
    }
}
