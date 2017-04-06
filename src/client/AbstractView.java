package client;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class AbstractView extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1038391473871514638L;

    public abstract void initialize();
    public abstract void clearFields();

    protected void addLabeledField(JPanel panel, String labelText, Component field) {
        JLabel label = new JLabel(labelText);
        label.setLabelFor(field);
        panel.add(label);
        panel.add(field);
    }
}
