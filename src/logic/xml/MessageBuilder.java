package logic.xml;

import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import logic.Message;

public class MessageBuilder {
    public static String buildDocument(Document document, Message[] messagesList) throws DOMException, LSException {
        Element rootElement = document.createElement("messages");
        document.appendChild(rootElement);
        
        for (Message message : messagesList) {
            Element messageElement = document.createElement("message");
            rootElement.appendChild(messageElement);            
            if (message.getId() != null){
                messageElement.setAttribute("id", message.getId().toString());
            }
            messageElement.setAttribute("sender", message.getUserNameFrom());
            messageElement.setAttribute("receiver", message.getUserNameTo());
            messageElement.setAttribute("moment", (new SimpleDateFormat("HH:mm:ss dd-MM-yyyy")).format(message.getMoment().getTime()));
            messageElement.appendChild(
                    document.createTextNode(message.getText()));
        }

        // Форматирование строки xml документа и установка кодировки
        DOMImplementation impl = document.getImplementation();
        DOMImplementationLS implLS = (DOMImplementationLS) impl.getFeature("LS", "3.0");
        LSSerializer ser = implLS.createLSSerializer();
        ser.getDomConfig().setParameter("format-pretty-print", true);
        
        LSOutput lsOutput = implLS.createLSOutput();
        lsOutput.setEncoding("UTF-8");
        
        Writer stringWriter = new StringWriter();
        lsOutput.setCharacterStream(stringWriter);
        
        ser.write(document, lsOutput);        
        return stringWriter.toString();
    }
}
