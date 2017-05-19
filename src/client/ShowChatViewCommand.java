package client;

import java.util.Timer;

public class ShowChatViewCommand implements Command {    
    private ChatMessengerAppl appl;
    private AbstractView view;

    public ShowChatViewCommand(ChatMessengerAppl parent, LoginPanelView panel) {
        appl = parent;
        view = panel;
    }
    
    @Override
    public void execute() {
        Utility.messagesUpdate(appl);
        view.clearFields();
        view.setVisible(false);
        appl.setTimer(new Timer());
        appl.getTimer().scheduleAtFixedRate(new UpdateMessageTask(appl), ChatMessengerAppl.DELAY, ChatMessengerAppl.PERIOD);
        appl.showChatPanelView();
    }  
}
