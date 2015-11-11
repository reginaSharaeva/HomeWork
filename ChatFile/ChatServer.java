package ChatFile;

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
    static List<String> history;
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
                histRead(client);
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
                        String name = inputLine.substring(0, inputLine.indexOf(" "));
                        inputLine = inputLine.replaceFirst(name + " ", "");
                        if (name.equals("")) {
                            name = cl.getName();
                        }
                        for (int i = 0; i < list.size(); i++) {
                            PrintWriter out = new PrintWriter(new OutputStreamWriter(list.get(i).getSocket().getOutputStream()));
                            out.println(name + ": " + inputLine + "\n");
                            out.flush();
                        }
                        history = readList();
                        if (history == null) {
                            history = new ArrayList<>();
                        }
                        history.add(name + ": " + inputLine + "\n");
                        writeList(history);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void histRead(Client client) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getSocket().getOutputStream()));

                    try {
                        history = readList();
                        if (history != null) {
                            if (history.size() != 0 && history.size() > 5) {
                                for (int i = history.size() - 5; i < history.size(); i++) {
                                    out.println(history.get(i));
                                    out.flush();
                                }
                            }
                            if (history.size() != 0 && history.size() <= 5) {
                                for (int i = 0; i < history.size(); i++) {
                                    out.println(history.get(i));
                                    out.flush();
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }} catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static List<String> readList() throws IOException, ClassNotFoundException {
        List<String> result = null;
        try(FileInputStream fin = new FileInputStream("history.txt")) {
            try {
                ObjectInputStream in = new ObjectInputStream(fin);
                result = (List<String>) in.readObject();
            } catch (Exception e) {

            }
        }
        return result;
    }
    public static void writeList(List<String> list) throws IOException {
        try (FileOutputStream fout = new FileOutputStream("history.txt");
             ObjectOutputStream out = new ObjectOutputStream(fout)) {
            out.writeObject(list);
        }
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
