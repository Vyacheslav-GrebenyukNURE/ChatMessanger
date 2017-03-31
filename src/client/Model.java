package client;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import logic.Message;

public class Model {
    private String currentUser = "";
    private String currentMessageText = "";
    private Set<Message> messages;
    private Long lastMessageId;
    
    public Model() {
        super();
        messages = new TreeSet<Message>()
        {
            private static final long serialVersionUID = 1L;

            @Override public String toString()
            {
                String result = "";
                Iterator<Message> i = iterator();
                while (i.hasNext()) {
                    result += i.next().toString() + "\n";
                }
                return result;
            }
        };
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
    public String getCurrentMessageText() {
        return currentMessageText;
    }
    /**
     * @param currentMessageText the currentMessageText to set
     */
    public void setCurrentMessageText(String currentMessageText) {
        this.currentMessageText = currentMessageText;
    }
    /**
     * @return the messages
     */
    public String getMessages() {
        return messages.toString();
    }
    /**
     * @param messages the messages to set
     */
    public void addMessages(List<Message> messages) {
        this.messages.addAll(messages);
    }

    public void addMessage(Message message) {
        this.messages.add(message);        
    }   
    
}
