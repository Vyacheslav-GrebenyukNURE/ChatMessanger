package client;

public class LoginErrorCommand implements Command {
    ChatMessangerAppl parent;
    LoginPanelView view;

    public LoginErrorCommand(ChatMessangerAppl parent, LoginPanelView view) {
        this.parent = parent;
        this.view = view;
    }

    @Override
    public void execute() {
        view.setVisible(false);
        view.getMainPanel().add(view.getErrorLable());
        view.getErrorLable().setVisible(true);
        view.setVisible(true);
        view.repaint();
    }

}
