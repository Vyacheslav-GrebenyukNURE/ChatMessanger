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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import logic.Message;
import logic.xml.MessageBuilder;

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
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.newDocument();
                
                String xmlContent = MessageBuilder.buildDocument(document,
                        new Message[]
                                {new Message(view.getTextMessageField().getText(),
                                        parent.getModel().getCurrentUser(),
                                        "")});
                out.println(xmlContent);
                out.println("END");
                // Получить ОК
                result = in.readLine();
            } while (! "OK".equals(result));            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
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
