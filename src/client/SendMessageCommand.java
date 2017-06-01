package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import logic.Message;
import logic.xml.MessageBuilder;

public class SendMessageCommand implements Command {
    final static Logger LOGGER = LogManager.getLogger(SendMessageCommand.class);
    private ChatMessengerAppl parent;
    private ChatPanelView view;
    private InetAddress addr;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public SendMessageCommand(ChatMessengerAppl parent, ChatPanelView view) {
        this.parent = parent;
        this.view = view;
    }

    @Override
    public void execute() {
        try {
            addr = InetAddress.getByName(parent.getModel().getServerIPAddress());
            socket = new Socket(addr, ChatMessengerAppl.PORT);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            LOGGER.error("Socket error: " + e.getMessage());
            return;
        }
        try {
            String result;
            do {
                out.println("PUT");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.newDocument();
                List<Message> messages = new ArrayList<>();
                messages.add(
                        Message.newMessage()
                        .text(view.getTextMessageField().getText())
                        .from(parent.getModel().getCurrentUser())
                        .to("")
                        .moment(Calendar.getInstance())
                        .build());
//                        new Message(view.getTextMessageField().getText(),
//                                parent.getModel().getCurrentUser(), ""));
                String xmlContent = MessageBuilder.buildDocument(document, messages);
                out.println(xmlContent);
                out.println("END");
                result = in.readLine();
            } while (! "OK".equals(result));
        } catch (ParserConfigurationException | IOException e) {
            LOGGER.error("Send message error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch  (IOException e) {
                LOGGER.error("Socket close error: " + e.getMessage());
            }
        }
        view.getTextMessageField().setText("");
        view.getTextMessageField().requestFocus();
    }
}