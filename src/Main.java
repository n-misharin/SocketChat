import Client.Client;
import Server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Server server = new Server("hello", 2021, "1");
        ArrayList<Client> clients = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (true){
            String s = scanner.nextLine();
            switch (s){
                case "stop":
                    server.stopServer();
                    break;
                case "start":
                    try {
                        server.startServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "add":
                    try {
                        clients.add(new Client("localhost", 2021));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "cnt":
                    System.out.println(server.clientsCount());
                    break;
                case "del":
                    clients.remove(Integer.parseInt(scanner.nextLine()));
                    break;
                case "msg":
                    try {
                        clients.get(Integer.parseInt(scanner.nextLine())).send(scanner.nextLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "int":
                    System.out.println(server.isInterrupted() + " " + server.isAlive());
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("error");
            }
        }
    }
}
