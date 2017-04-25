package client;

import java.awt.Component;
import java.awt.Container;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.TreeSet;

import logic.Message;

public class Utility {
    public static <T extends Container> T findParent(Component comp, Class<T> clazz)  {
        if (comp == null)
           return null;
        if (clazz.isInstance(comp))
           return (clazz.cast(comp));
        else
           return findParent(comp.getParent(), clazz);     
    }
    
    @SuppressWarnings("unchecked")
    public static void messagesUpdate(ChatMessengerAppl appl) {
        // Запрос на обновление списка сохраненных на сервере сообщений
        InetAddress addr;
        Socket socket;
        PrintWriter out;
//        BufferedReader in;
        ObjectInputStream ois;
        try {
            addr = InetAddress.getByName(appl.getModel().getServerIPAddress());
            socket = new Socket(addr, ChatMessengerAppl.PORT);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            System.err.println("Socket error");
            return;
        }
//        String result;
        try {
            Model model = appl.getModel();
            out.println("GET");
            out.println(model.getLastMessageId());
            out.flush();
            // Чтение и десериализация объекта
            // для чтения объекта списка. убрать после реализации генератора xml
            ois = new ObjectInputStream(socket.getInputStream());
            model.addMessages((List<Message>) ois.readObject());
            ois.close();
            model.setLastMessageId(((TreeSet<Message>) (model.getMessages())).last().getId());
//            result = in.readLine();
//            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
