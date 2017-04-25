package client;

public class ShowLoginViewCommand implements Command {
    private ChatMessengerAppl appl;
    private AbstractView view;

    public ShowLoginViewCommand(ChatMessengerAppl parent, ChatPanelView panel) {
        appl = parent;
        view = panel;
    }

    @Override
    public void execute() {
        
        view.clearFields();
        view.setVisible(false);
        appl.getTimer().cancel();
        appl.showLoginPanelView();
    }
}
