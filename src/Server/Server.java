package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Server extends Thread{

    protected ServerSocket serverSocket;
    protected String name;
    protected String password;
    private List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<ClientHandler>());
    protected int port;

    public Server(String name, int port, String password) {
        this.name = name;
        this.password = password;
        this.port = port;
        setDaemon(true);
    }

    @Override
    public void interrupt() {
        stopClients();
        super.interrupt();
    }

    public void stopClients(){
        for (ClientHandler client: clients){
            removeClient(client);
        }
    }

    public int clientsCount(){
        return clients.size();
    }

    public void stopServer(){
        interrupt();
        System.out.println("Сервер остановлен");
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(port);
        start();
    }

    public synchronized void removeClient(ClientHandler clientHandler){
        System.out.println("Клиент " + clientHandler.getClientName() + " вышел");
        clientHandler.stopClient();
        clients.remove(clientHandler);
    }

    public synchronized void sendAll(String message){
        ArrayList<ClientHandler> deadClients = new ArrayList<>();
        for (ClientHandler client: clients){
            if (!client.isAuth()) continue;
            try {
                client.send(message);
            } catch (IOException e) {
                //не получилось отправить сообщение. Удалить
                deadClients.add(client);
            }
        }
        for (ClientHandler client: deadClients){
            removeClient(client);
        }
    }

    @Override
    public void run() {
        System.out.println("Сервер старт");
        while (!isInterrupted()){
            try {
                Socket socket = serverSocket.accept();
                clients.add(new ClientHandler(socket, this));
            } catch (IOException e) {
                //ошибка подключения клиента
            }
        }
        //System.out.println("Сервер остановлен (Но сюда не дойдем)");
    }
}
