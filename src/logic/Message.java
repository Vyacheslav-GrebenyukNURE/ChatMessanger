package logic;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Message implements Serializable, Comparable<Message> {
    /**
     * 
     */
    private static final long serialVersionUID = 5521915137203818381L;
    private Long id;
    private String text;
    private String userNameFrom, userNameTo;
    private Calendar moment;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the userNameFrom
     */
    public String getUserNameFrom() {
        return userNameFrom;
    }

    /**
     * @param userNameFrom
     *            the userNameFrom to set
     */
    public void setUserNameFrom(String userNameFrom) {
        this.userNameFrom = userNameFrom;
    }

    /**
     * @return the userNameTo
     */
    public String getUserNameTo() {
        return userNameTo;
    }

    /**
     * @param userNameTo
     *            the userNameTo to set
     */
    public void setUserNameTo(String userNameTo) {
        this.userNameTo = userNameTo;
    }

    /**
     * @return the moment
     */
    public Calendar getMoment() {
        return moment;
    }

    /**
     * @param moment
     *            the moment to set
     */
    public void setMoment(Calendar moment) {
        this.moment = moment;
    }

    public Message(Long id, String text, String userNameFrom, String userNameTo, Calendar moment) {
        super();
        this.id = id;
        this.text = text;
        this.userNameFrom = userNameFrom;
        this.userNameTo = userNameTo;
        this.moment = moment;
    }

    public Message(String text, String userNameFrom, String userNameTo) {
        super();
        this.id = null;
        this.text = text;
        this.userNameFrom = userNameFrom;
        this.userNameTo = userNameTo;
        this.moment = Calendar.getInstance();
    }

    public Message() {
        super();
        this.id = 0L;
        this.text = "";
        this.userNameFrom = "";
        this.userNameTo = "";
        this.moment = Calendar.getInstance();
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
        return "<b>" + userNameFrom 
                + ((userNameTo.length() != 0) ? (" -> " + userNameTo) : "")
                + ":</b><br><message>"
                + text + "</message><br><div align=right style='font-size:small'>"
                + (new SimpleDateFormat("HH:mm:ss dd-MMM-yyyy")).format(moment.getTime())
                + "</div><br>";
    }

    @Override
    public int compareTo(Message o) {
        if (getMoment().equals(o.getMoment()))
            return getId().compareTo(o.getId());
        return getMoment().compareTo(o.getMoment());
    }

}
