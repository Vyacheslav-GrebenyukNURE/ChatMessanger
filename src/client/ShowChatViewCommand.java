package client;

public class ShowChatViewCommand implements Command {
    private ChatMessangerAppl appl;
    private AbstractView view;

    public ShowChatViewCommand(ChatMessangerAppl parent, LoginPanelView panel) {
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
