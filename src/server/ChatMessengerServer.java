package server;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import logic.Message;
import logic.xml.MessageParser;

public class ChatMessengerServer {
    final static Logger logger = LogManager.getLogger(ChatMessengerServer.class);
    private final static int PORT = 7070;
    private static AtomicInteger id = new AtomicInteger(0);
    private static Map<Long, Message> messagesDB = Collections.synchronizedSortedMap(new TreeMap<Long, Message>());
    private static volatile boolean stop = false;

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        // Зарузить файл с сообщениями
        loadMessageDBFile();
        
        // Запустить поток для обработки команды выхода
        quitCommandThread();

        // Создаем новый серверный сокет
        ServerSocket s = new ServerSocket(PORT);
        logger.info("Server started...");
        
        // Слушаем запросы с тайм-аутом 500мс (для обработки выхода)
        // и создаем новую нить для обработки запроса
        while (!stop) {
            s.setSoTimeout(500);
            Socket socket;
            try {
                socket = s.accept();
                try {
                    new ServerThread(socket, id, messagesDB);
                } catch (IOException e) {
                    logger.error("IO error");
                    socket.close();
                }
            } catch (SocketTimeoutException e) {                
            }                
        }
        // Запись бд в файл
        
        logger.info("Server stoped.");
        s.close();               
    }

    private static void quitCommandThread() {
        new Thread(){
            public void run(){
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                while (true){
                    String buf;
                    try {
                        buf = br.readLine();
                        if ("quit".equals(buf)){
                            stop = true;
                            break;
                        } else {
                            System.out.println("Неберите 'quit' для завершения работы сервера.");
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
        MessageParser saxp = new MessageParser(id, messagesDB);
        InputStream is = new ByteArrayInputStream(Files.readAllBytes(Paths.get("resources/messagesDB.xml")));
        parser.parse(is, saxp);
        id.incrementAndGet();
    }
} 