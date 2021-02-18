package Client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread{

    protected int port;
    protected String hostName = "", host;
    protected Scanner socketReader;
    protected BufferedWriter socketWriter;

    public Client(String host, int port) throws IOException {
        this.port = port;
        Socket socket = new Socket(host, port);

        socketReader = new Scanner(socket.getInputStream());
        socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        setDaemon(true);
        start();
    }

    public void clientStop(){
        interrupt();
    }

    public void send(String message) throws IOException {
        socketWriter.write(message + "\n");
        socketWriter.flush();
    }

    @Override
    public void run() {
        while (!isInterrupted()){
            if(socketReader.hasNextLine()){
                System.out.println("Сообщение от сервера: " +  socketReader.nextLine());
            }
        }
    }
}
