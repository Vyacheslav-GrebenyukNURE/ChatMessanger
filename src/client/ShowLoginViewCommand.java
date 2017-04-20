package client;

public class ShowLoginViewCommand implements Command {
    private ChatMessangerAppl appl;
    private AbstractView view;

    public ShowLoginViewCommand(ChatMessangerAppl parent, ChatPanelView panel) {
        appl = parent;
        view = panel;
    }

    @Override
    public void execute() {
        view.clearFields();
        view.setVisible(false);
        appl.showLoginPanelView();
    }
}
