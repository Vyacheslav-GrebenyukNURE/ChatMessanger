package client;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import logic.Message;

public class Model {
    private String currentUser = "";
    private String lastMessageText = "";
    private Set<Message> messages;
    private Long lastMessageId;
    private String serverIPAddress="127.0.0.1";
    
    public Model() {
        super();
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
    }
    
    /**
     * @return the lastMessageId
     */
    public Long getLastMessageId() {
        return lastMessageId;
    }
    
    /**
     * @param lastMessageId the lastMessageId to set
     */
    public void setLastMessageId(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }
    
    /**
     * @return the currentUser
     */
    public String getCurrentUser() {
        return currentUser;
    }
    /**
     * @param currentUser the currentUser to set
     */
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    /**
     * @return the currentMessageText
     */
    public String getLastMessageText() {
        return lastMessageText;
    }
    /**
     * @param currentMessageText the currentMessageText to set
     */
    public void setLastMessageText(String currentMessageText) {
        this.lastMessageText = currentMessageText;
    }

    public String getServerIPAddress() {
        return serverIPAddress;
    }

    public void setServerIPAddress(String serverIPAddress) {
        this.serverIPAddress = serverIPAddress;
    }

    /**
     * @return the messages in String
     */
    public String messagesToString() {
        return getMessages().toString();
    }
    /**
     * @param messages the messages to add
     */
    public void addMessages(List<Message> messages) {
        this.getMessages().addAll(messages);
    }

    public void addMessage(Message message) {
        this.getMessages().add(message);        
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }       
}
