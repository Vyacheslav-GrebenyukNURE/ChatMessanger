package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import logic.Message;
import logic.xml.MessageBuilder;
import logic.xml.MessageParser;

class ServerThread extends Thread {
    final static Logger LOGGER = LogManager.getLogger(ServerThread.class);

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    private Map<Long, Message> messagesDB;
    private AtomicInteger messageId;

    public ServerThread(Socket socket, AtomicInteger messageId, Map<Long, Message> messagesDB) throws IOException {
        this.socket = socket;
        this.messageId = messageId;
        this.messagesDB = messagesDB; 
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run() {
        try {
            LOGGER.debug("New thread starting...");
            String requestLine = in.readLine();
            LOGGER.debug(requestLine);
            switch (requestLine) {
            case "GET":
                LOGGER.debug("get");
                Long lastId = Long.valueOf(in.readLine());
                LOGGER.debug(lastId);
                // Отфильтровать Java 8 Обработка коллекций лямбда-выражениями
                Message[] newMessages = messagesDB.values().stream()
                        .filter(message -> message.getId().compareTo(lastId) > 0)
                        .collect(Collectors.toList())
                        .toArray(new Message[0]);
                LOGGER.debug(Arrays.asList(newMessages));
                // Сформировать и отправить в out xml с сообщениями
                DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = docBuildFactory.newDocumentBuilder();
                Document document = builder.newDocument();        
                String xmlContent = MessageBuilder.buildDocument(document, newMessages);
                LOGGER.trace("Echoing: " + xmlContent);
                out.println(xmlContent);
                out.println("END");
                out.flush();               
                break;
            case "PUT":
                LOGGER.debug("put");
                requestLine = in.readLine();
                StringBuilder mesStr = new StringBuilder();
                while (! "END".equals(requestLine)) {
                    mesStr.append(requestLine);
                    requestLine = in.readLine();
                }                   
                LOGGER.debug(mesStr);
                // Распарсить xml документ с сообщениями и добавить в карту БД
                SAXParserFactory parserFactory = SAXParserFactory.newInstance();
                SAXParser parser = parserFactory.newSAXParser();
                List<Message> messages = new ArrayList<>();
                MessageParser saxp = new MessageParser(messageId, messages);
                InputStream is = new ByteArrayInputStream(mesStr.toString().getBytes());
                parser.parse(is, saxp);
                for (Message message : messages) {
                    messagesDB.put(message.getId(), message);
                }
                LOGGER.trace("Echoing: " + messages);
                out.println("OK");
                out.flush();
                out.close();
                break;
            default:
                LOGGER.info("Unknown request: " + requestLine);
                out.println("BAD REQUEST");
                out.flush();
                break;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            out.println("ERROR");
            out.flush();
        } finally {
            try {
                LOGGER.debug("Socket closing...");
                LOGGER.debug("Close object stream");
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                LOGGER.error("Socket not closed");
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