package client;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import logic.Message;

public class Model {
    private ChatMessengerAppl parent;
    private String currentUser;
    private String lastMessageText;
    private Set<Message> messages;
    private Long lastMessageId;
//    Замените адрес для тестов в аудитории во время занятий
    private String serverIPAddress="127.0.0.1";
//    private String serverIPAddress="10.13.30.1";
    
    private Model() {     
    }

    public static Model getInstance() {
        return ModelHolder.INSTANCE;
    }

    private static class ModelHolder {
        private static final Model INSTANCE = new Model();
    }
    
    public void setParent(ChatMessengerAppl parent) {
        this.parent = parent;
    }
    
    public void initialize() {
        setMessages(new TreeSet<Message>() {
            private static final long serialVersionUID = 1L;

            @Override public String toString() {
                StringBuilder result = new StringBuilder("<html><body id='body'>");
                Iterator<Message> i = iterator();
                while (i.hasNext()) {
                    result.append(i.next().toString()).append("\n");
                }
                return result.append("</body></html>").toString();
            }
        });
        lastMessageId = 0L;
        currentUser = "";
        lastMessageText = "";
    }
    
    public Long getLastMessageId() {
        return lastMessageId;
    }
    
    public void setLastMessageId(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }
    
    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getLastMessageText() {
        return lastMessageText;
    }

    public void setLastMessageText(String currentMessageText) {
        this.lastMessageText = currentMessageText;
    }

    public String getServerIPAddress() {
        return serverIPAddress;
    }

    public void setServerIPAddress(String serverIPAddress) {
        this.serverIPAddress = serverIPAddress;
    }

    public String messagesToString() {
        return getMessages().toString();
    }

    public void addMessages(List<Message> messages) {
        this.getMessages().addAll(messages);
        ((ChatPanelView)parent.getChatPanelView(false)).modelChangedNotification(messages.toString());
    }

//    public void addMessage(Message message) {
//        this.getMessages().add(message);        
//    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }       
}
