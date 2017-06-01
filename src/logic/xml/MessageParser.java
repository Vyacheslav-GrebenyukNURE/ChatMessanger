package logic.xml;

import org.xml.sax.helpers.DefaultHandler;

import logic.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.*;

public class MessageParser extends DefaultHandler {
    final static Logger LOGGER = LogManager.getLogger(MessageParser.class);
    private String thisElement = "";
    private Message message;
    private AtomicInteger id;
    private List<Message> messages;

    public MessageParser(AtomicInteger id, List<Message> messages) {
        this.id = id;
        this.messages = messages;
    }

    @Override
    public void startDocument() throws SAXException {
        LOGGER.debug("startDocument");
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        thisElement = qName;
        LOGGER.debug("startElement");
        LOGGER.trace("<" + qName);
        if ("message".equals(qName)){
            message = Message.newMessage().build();
            for (int i = 0; i < atts.getLength(); i++) {
                String attrName = atts.getLocalName(i);
                String value = atts.getValue(i);
                LOGGER.trace(attrName + "=" + value);
                switch (attrName) {
                case "sender":
                    message.setUserNameFrom(value);
                    break;
                case "receiver":
                    message.setUserNameTo(value);
                    break;
                case "id":
                    message.setId(Long.valueOf(value));
                    break;
                case "moment":
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                    try {
                        cal.setTime(sdf.parse(value));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    message.setMoment(cal);
                }            
            }
        }
        LOGGER.trace(">");
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if ("message".equals(qName)){
            Long newId = Long.valueOf(id.getAndIncrement());
            if (message.getId() == null){
                message.setId(newId);
            } else {
                newId = message.getId();
                id.set(newId.intValue());
            }
            LOGGER.debug("id = " + newId);
            messages.add(message);
        }
        thisElement = "";
        LOGGER.debug("endElement");
        LOGGER.trace("</" + qName + ">");
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if ("message".equals(thisElement)){
            String messBody = new String(ch, start, length).trim();
            LOGGER.trace(messBody);
            message.setText(messBody);
        }
    }

    @Override
    public void endDocument() {
        LOGGER.debug("endDocument");
    }
}
