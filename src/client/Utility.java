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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import logic.Message;
import logic.xml.MessageParser;

public class Utility {
    public static <T extends Container> T findParent(Component comp, Class<T> clazz)  {
        if (comp == null)
           return null;
        if (clazz.isInstance(comp))
           return (clazz.cast(comp));
        else
           return findParent(comp.getParent(), clazz);     
    }
    
    public static void messagesUpdate(ChatMessengerAppl appl) {
        // Запрос на обновление списка сохраненных на сервере сообщений
        InetAddress addr;
        Socket socket;
        PrintWriter out;
        BufferedReader in;
        try {
            addr = InetAddress.getByName(appl.getModel().getServerIPAddress());
            socket = new Socket(addr, ChatMessengerAppl.PORT);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            System.err.println("Socket error");
            return;
        }
        String responseLine;
        try {
            Model model = appl.getModel();
            out.println("GET");
            out.println(model.getLastMessageId());
            out.flush();
            responseLine = in.readLine();
            StringBuilder mesStr = new StringBuilder();
            while (! "END".equals(responseLine)) {
                mesStr.append(responseLine);
                responseLine = in.readLine();
            }                   
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            List<Message> messages = new ArrayList<Message>(){
                private static final long serialVersionUID = 1L;
                @Override public String toString() {
                    return this.stream().map(Message::toString).collect(Collectors.joining("\n"));
                }
            };
            AtomicInteger id  = new AtomicInteger(0);
            MessageParser saxp = new MessageParser(id, messages);            
            parser.parse(new ByteArrayInputStream(mesStr.toString().getBytes()), saxp);
            if (messages.size() > 0){                
                model.addMessages(messages);
                model.setLastMessageId(id.longValue());
                System.out.println("List:" + messages.toString());                
                ((ChatPanelView) appl.getChatPanelView(false)).modelChangedNotification(messages.toString());
            }
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
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
    }
}
