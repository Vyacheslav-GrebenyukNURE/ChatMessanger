package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class SendMessageCommand implements Command {
    private ChatMessengerAppl parent;
    private ChatPanelView view;
    

    public SendMessageCommand(ChatMessengerAppl parent, ChatPanelView view, AtomicInteger id) {
        this.parent = parent;
        this.view = view;
    }

    @Override
    public void execute() {
        // отправить сообщение на сервер
        InetAddress addr;
        Socket socket;
        PrintWriter out;
        BufferedReader in;
        try {
            addr = InetAddress.getByName(parent.getModel().getServerIPAddress());
            socket = new Socket(addr, ChatMessengerAppl.PORT);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            // Вывести сообщение об обрыве соединения с сервером
            return;
        }
        try {
            String result;
            do{
                out.println("PUT");
                StringBuilder buf = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
                buf.append("<messages>\n")
                .append("<message sender=\"")
                .append(parent.getModel().getCurrentUser())
                .append("\" receiver=\"")
                .append("") // User to
                .append("\">\n")
                .append(view.getTextMessageField().getText())
                .append("\n</message>")
                .append("\n</messages>");
                out.println(buf);
                out.println("END");
                // Получить ОК
                result = in.readLine();
            } while (! "OK".equals(result));            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        view.getTextMessageField().setText("");
        view.getTextMessageField().requestFocus();
    }
}
