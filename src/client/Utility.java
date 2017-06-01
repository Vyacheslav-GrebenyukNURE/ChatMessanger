package client;

import java.awt.Component;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import logic.Message;
import logic.xml.MessageParser;

public class Utility {
    final static Logger LOGGER = LogManager.getLogger(Utility.class);
    public static <T extends Container> T findParent(Component comp, Class<T> clazz)  {
        if (comp == null)
           return null;
        if (clazz.isInstance(comp))
           return (clazz.cast(comp));
        else
           return findParent(comp.getParent(), clazz);     
    }
    
    public static void messagesUpdate(ChatMessengerAppl appl) {
        InetAddress addr;
        try {
            addr = InetAddress.getByName(appl.getModel().getServerIPAddress());
            try (Socket socket = new Socket(addr, ChatMessengerAppl.PORT);
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));){
                Model model = appl.getModel();
                out.println("GET");
                out.println(model.getLastMessageId());
                out.flush();
                String requestLine = in.readLine();
                StringBuilder mesStr = new StringBuilder();
                while (! "END".equals(requestLine)) {
                    mesStr.append(requestLine);
                    requestLine = in.readLine();
                }      
                SAXParserFactory parserFactory = SAXParserFactory.newInstance();
                try {
                    SAXParser parser = parserFactory.newSAXParser();
                    List<Message> messages = new ArrayList<Message>() {
                        private static final long serialVersionUID = 1L;

                        @Override public String toString(){
                            return this.stream().map(Message::toString).collect(Collectors.joining("\n"));
                        }
                    };
                    AtomicInteger id = new AtomicInteger(0);
                    MessageParser saxp = new MessageParser(id, messages);
                    parser.parse(new ByteArrayInputStream(mesStr.toString().getBytes()), saxp);
                    if (messages.size() > 0){
                        model.addMessages(messages);
                        model.setLastMessageId(id.longValue());
                        LOGGER.trace("List of new messages: " + messages.toString());
                    }
                } catch (ParserConfigurationException | SAXException e) {
                    LOGGER.error("Parser exeption: " + e.getMessage());
                }
                
            } catch (IOException e) {
                LOGGER.error("Socket error: " + e.getMessage());
                return;
            }
        } catch (UnknownHostException e1) {
            LOGGER.error("Unknown host address: " + e1.getMessage());
        }
    }
}