package Server;

import javax.print.DocFlavor;
import java.io.IOException;

public class MessageHandler {

    public static final String MESSAGE_SEPARATOR = "#";
    public static final String AUTHORIZATION = "0";
    public static final String NEW_MESSAGE = "1";
    public static final String EXIT = "2";
    public static final String CHECK = "3";
    public static final String SUCCESS = "4";
    public static final String ERROR = "5";

    protected ClientHandler clientHandler;

    public MessageHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    private void send(String message){
        try {
            clientHandler.send(message);
        } catch (IOException e) {
            exit();
        }
    }

    private void sendAll(String message){
        clientHandler.server.sendAll(message);
    }

    public void newMessage(String message){
        if (clientHandler.isAuth()){
            sendAll(NEW_MESSAGE + MESSAGE_SEPARATOR + clientHandler.getName() + MESSAGE_SEPARATOR + message);
        }else {
            send(NEW_MESSAGE + MESSAGE_SEPARATOR + ERROR);
        }
    }

    public void exit(){
        clientHandler.server.removeClient(clientHandler);
    }

    public void check(){
        send(CHECK + MESSAGE_SEPARATOR + clientHandler.server.name);
    }

    public void auth(String login, String password){
        try {
            if (clientHandler.server.password.equals(password) && login.length() != 0){
                clientHandler.setPassword(password);
                clientHandler.setClientName(login);
                clientHandler.send(AUTHORIZATION + MESSAGE_SEPARATOR + SUCCESS);
            }else {
                clientHandler.send(AUTHORIZATION + MESSAGE_SEPARATOR + ERROR);
            }
        }catch (IOException e){
            exit();
        }
    }


    public void messageHandler(String message){
        String[] data = message.split(MESSAGE_SEPARATOR);
        if (data.length == 0)
            return;
        switch (data[0]){
            case AUTHORIZATION:
                System.out.println("AUTH " + message);
                if (data.length == 3){
                    auth(data[1], data[2]);
                }else {
                    send(AUTHORIZATION + MESSAGE_SEPARATOR + ERROR);
                }
                break;
            case EXIT:
                clientHandler.server.removeClient(clientHandler);
                break;
            case NEW_MESSAGE:
                if (data.length == 2){
                    newMessage(data[1]);
                }else {
                    send(NEW_MESSAGE + MESSAGE_SEPARATOR + ERROR);
                }
                break;
            case CHECK:
                check();
                break;
        }
    }
}
