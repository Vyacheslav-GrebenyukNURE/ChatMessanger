package logic;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Message build includes the text of the message, the users' name from and to,
 * the moment of message send and the unique id.
 * Id must be generated on the server side.
 * 
 * Message bean realizes the fluent builder patter. See inner static class Builder.
 *      @author Vyacheslav Grebenyuk
 */
public class Message implements Serializable, Comparable<Message> {
    private static final long serialVersionUID = 57618043172251503L;
    private Long id;
    private String text;
    private String userNameFrom, userNameTo;
    private Calendar moment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserNameFrom() {
        return userNameFrom;
    }

    public void setUserNameFrom(String userNameFrom) {
        this.userNameFrom = userNameFrom;
    }

    public String getUserNameTo() {
        return userNameTo;
    }

    public void setUserNameTo(String userNameTo) {
        this.userNameTo = userNameTo;
    }

    public Calendar getMoment() {
        return moment;
    }

    public void setMoment(Calendar moment) {
        this.moment = moment;
    }

    /**
     * Both constructors may be used only by inner static class Builder
     */
    private Message() {
    }
   
    private Message(Builder builder) {
        setId(builder.id);
        setText(builder.text);
        setUserNameFrom(builder.userNameFrom);
        setUserNameTo(builder.userNameTo);
        setMoment(builder.moment);
    }

    /**
     * Static builder method for fluent builder pattern
     */
    public static Builder newMessage() {
        return new Builder();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((moment == null) ? 0 : moment.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        result = prime * result + ((userNameFrom == null) ? 0 : userNameFrom.hashCode());
        result = prime * result + ((userNameTo == null) ? 0 : userNameTo.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Message other = (Message) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (moment == null) {
            if (other.moment != null)
                return false;
        } else if (!moment.equals(other.moment))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        if (userNameFrom == null) {
            if (other.userNameFrom != null)
                return false;
        } else if (!userNameFrom.equals(other.userNameFrom))
            return false;
        if (userNameTo == null) {
            if (other.userNameTo != null)
                return false;
        } else if (!userNameTo.equals(other.userNameTo))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuilder("<p><b>")
                .append( userNameFrom) 
                .append((userNameTo.length() != 0) ? (" -> " + userNameTo) : "")
                .append(":</b><br><message>")
                .append(text)
                .append("</message><br><div align=right style='font-size:small'>")
                .append((new SimpleDateFormat("HH:mm:ss dd-MMM-yyyy")).format(moment.getTime()))
                .append("</div><br></p>")
                .toString();
    }

    @Override
    public int compareTo(Message o) {
        if (getMoment().equals(o.getMoment()))
            return getId().compareTo(o.getId());
        return getMoment().compareTo(o.getMoment());
    }


    /**
     * Class Builder for Message bean is a fluent builder pattern
     * @author Vyacheslav Grebenyuk
     */
    public static final class Builder {
        private Long id;
        private String text;
        private String userNameFrom, userNameTo;
        private Calendar moment;
        
        private Builder(){            
        }
        
        public Builder id(Long id){
            this.id = id;
            return this;
        }
        
        public Builder text(String text) {
            this.text = text;
            return this;
        }
        
        public Builder from(String userNameFrom) {
            this.userNameFrom = userNameFrom;
            return this;            
        }
        
        public Builder to(String userNameTo) {
            this.userNameTo = userNameTo;
            return this;
        }
        
        public Builder moment(Calendar moment) {
            this.moment = moment;
            return this;
        }
        
        public Message build(){
            return new Message(this);
        }
    }
}
