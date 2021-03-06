package server;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import logic.Message;
import logic.xml.MessageBuilder;
import logic.xml.MessageParser;

public class ChatMessengerServer {
    final static Logger LOGGER = LogManager.getLogger(ChatMessengerServer.class);
    private static final int SERVER_TIMOUT = 500;
    private static final int PORT = 7070;
    private static final String DB_FILE_NAME = "resources/messagesDB.xml";
    private static AtomicInteger id = new AtomicInteger(0);
    private static Map<Long, Message> messagesDB = Collections.synchronizedSortedMap(new TreeMap<Long, Message>());
    private static volatile boolean stop = false;

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        // Загрузить файл с сообщениями
        loadMessageDBFile();
        
        // Запустить поток для обработки команды выхода
        quitCommandThread();

        // Создаем новый серверный сокет
        ServerSocket serverSocket = new ServerSocket(PORT);
        LOGGER.info("Server started...");
        
        // Слушаем запросы с тайм-аутом 500мс (для обработки выхода)
        // и создаем новую нить для обработки запроса
        while (!stop) {
            serverSocket.setSoTimeout(SERVER_TIMOUT);
            Socket socket;
            try {
                socket = serverSocket.accept();
                try {
                    new ServerThread(socket, id, messagesDB);
                } catch (IOException e) {
                    LOGGER.error("IO error");
                    socket.close();
                }
            } catch (SocketTimeoutException e) {                
            }                
        }
        // Запись бд в файл
        saveMessageDBFile();
        
        LOGGER.info("Server stoped.");
        serverSocket.close();               
    }

    private static void quitCommandThread() {
        new Thread(){
            @Override public void run(){
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                while (true){
                    String buf;
                    try {
                        buf = br.readLine();
                        if ("quit".equals(buf)){
                            stop = true;
                            break;
                        } else {
                            LOGGER.warn("Неберите 'quit' для завершения работы сервера.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }               
            }
        }.start();
    }

    private static void loadMessageDBFile() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        List<Message> messages = new ArrayList<>();
        MessageParser saxp = new MessageParser(id, messages);
        InputStream is = new ByteArrayInputStream(Files.readAllBytes(Paths.get(DB_FILE_NAME)));
        parser.parse(is, saxp);
        for (Message message : messages) {
            messagesDB.put(message.getId(), message);
        }
        id.incrementAndGet();
        is.close();
    }
    
    private static void saveMessageDBFile() throws ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();        
        String xmlContent = MessageBuilder.buildDocument(document, messagesDB.values());

        OutputStream stream = new FileOutputStream(new File(DB_FILE_NAME));
        OutputStreamWriter out = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
        out.write(xmlContent + "\n");
        out.flush();
        out.close();
    }
} 
