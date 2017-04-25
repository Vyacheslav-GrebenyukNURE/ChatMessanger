package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import logic.Message;
import logic.xml.MessageParser;

class ServerThread extends Thread {
    final static Logger logger = LogManager.getLogger(ServerThread.class);

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ObjectOutputStream oos;
    
    private Map<Long, Message> messagesDB;
    private AtomicInteger messageId;

    public ServerThread(Socket s, AtomicInteger messageId, Map<Long, Message> messagesDB) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//        oos = new ObjectOutputStream(socket.getOutputStream());
        this.messageId = messageId;
        this.messagesDB = messagesDB; 
        start();
    }

    public void run() {
        try {
            logger.debug("starting...");
            String requestLine = in.readLine();
            logger.debug(requestLine);
            switch (requestLine) {
            case "GET":
                oos = new ObjectOutputStream(socket.getOutputStream());
                logger.debug("get");
                Long lastId = Long.valueOf(in.readLine());
                logger.debug(lastId);
                // Отфильтровать Java 8 Обработка коллекций лямбда-выражениями 
                List<Message> collect = messagesDB.entrySet().stream()
                        .filter(map -> map.getKey().compareTo(lastId) > 0)
                        .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()))
                        .values().stream().collect(Collectors.toList());
                logger.debug(collect);
                // Сформировать и отправить в out xml с сообщениями
                oos.writeObject(collect);
                oos.flush();
                oos.close();
//                out.println("\nOK");
//                out.flush();               
                break;
            case "PUT":
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                requestLine = in.readLine();
                StringBuilder mesStr = new StringBuilder();
                while (! "END".equals(requestLine)) {
                    mesStr.append(requestLine);
                    requestLine = in.readLine();
                }                   
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                MessageParser saxp = new MessageParser(messageId, messagesDB);
                InputStream is = new ByteArrayInputStream(mesStr.toString().getBytes());
                parser.parse(is, saxp);
                logger.debug("Echoing: " + mesStr.toString());
                out.println("OK");
                out.flush();
                out.close();
                break;
            default:
                logger.info("Unknown request: " + requestLine);
                out.println("BAD REQUEST");
                out.flush();
                break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            out.println("ERROR");
            out.flush();
        } finally {
            try {
                logger.debug("Socket closing...");
                logger.debug("Close object stream");
//                oos.close();
                in.close();
//                out.close();
                socket.close();
            } catch (IOException e) {
                logger.error("Socket not closed");
            }
        }
    }

    public Map<Long, Message> getMessagesDB() {
        return messagesDB;
    }

    public AtomicInteger getMessageId() {
        return messageId;
    }
}