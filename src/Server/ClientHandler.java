package Server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread{

    protected Server server;
    protected Socket socket;
    protected Scanner socketReader;
    private String name;
    private String password = "";
    protected BufferedWriter socketWriter;
    private MessageHandler messageHandler;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.server = server;
        this.socket = socket;
        setClientName("unknown " + socket.getInetAddress());

        socketReader = new Scanner(socket.getInputStream());
        socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        messageHandler = new MessageHandler(this);
        setDaemon(true);
        start();
    }

    public void setClientName(String name){
        this.name = name;
    }

    public String getClientName(){
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void stopClient(){
        System.out.println("Клиент " + name + " отключен");
        try {
            send("Server stopped");
        } catch (IOException e) {
            //e.printStackTrace();
        }
        interrupt();
    }

    public void send(String message) throws IOException {
        socketWriter.write(message + "\n");
        socketWriter.flush();
    }

    public boolean isAuth(){
        return server.password.equals(getPassword());
    }

    @Override
    public void run() {
        System.out.println("Новый клиент " + name + " подключен");
        while (!isInterrupted()){
            if (socketReader.hasNextLine()){
                String message = socketReader.nextLine();
                System.out.println("Принято: " + message);
                messageHandler.messageHandler(message);
            }
        }
    }
}
