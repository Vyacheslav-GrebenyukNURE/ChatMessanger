package client;

public class ShowChatViewCommand implements Command {
    private ChatMessengerAppl appl;
    private AbstractView view;

    public ShowChatViewCommand(ChatMessengerAppl parent, LoginPanelView panel) {
        appl = parent;
        view = panel;
    }

    @Override
    public void execute() {
        view.clearFields();
        view.setVisible(false);
        appl.showChatPanelView();
    }
}
