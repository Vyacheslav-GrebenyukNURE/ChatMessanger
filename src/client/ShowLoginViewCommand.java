package client;

public class ShowLoginViewCommand implements Command {
    private ChatMessangerAppl appl;

    public ShowLoginViewCommand(ChatMessangerAppl parent) {
        appl = parent;
    }

    @Override
    public void execute() {
        appl.showLoginPanelView();
    }
}
