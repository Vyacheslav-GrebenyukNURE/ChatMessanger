package client;

public class ShowChatViewCommand implements Command {
    private ChatMessangerAppl appl;

    public ShowChatViewCommand(ChatMessangerAppl parent) {
        appl = parent;
    }

    @Override
    public void execute() {
        appl.showChatPanelView();
    }
}
