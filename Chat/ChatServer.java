package Chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Регина on 05.11.2015.
 */
public class ChatServer {
    static List<Client> list = new ArrayList<>();

    public static void main(String... args) {
        int portNumber = 7777;
        try (ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
            System.out.println("Started Chat Server");
            int id = 0;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Client client = new Client("user" + id++, clientSocket);
                list.add(client);
                clientThread(client);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    private static void clientThread(Client cl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("New client connected: " + cl.getName() + " "
                        + cl.getSocket().getInetAddress().getHostName());
                    BufferedReader in = new BufferedReader(new InputStreamReader(cl.getSocket().getInputStream()));
                    while (true) {
                        String inputLine = in.readLine();
                        for (int i = 0; i < list.size(); i++) {
                            PrintWriter out = new PrintWriter(new OutputStreamWriter(list.get(i).getSocket().getOutputStream()));
                            out.println(cl.getName() + ": " + inputLine + "\n");
                            out.flush();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static class Client {
        String name;
        Socket socket;

        public Client(String name, Socket socket) {
            this.name = name;
            this.socket = socket;
        }


        public Socket getSocket() {
            return socket;
        }

        public void setSocket(Socket socket) {
            this.socket = socket;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}