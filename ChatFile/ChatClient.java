package ChatFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ChatClient extends JFrame {

    private JButton sendButton;
    private JTextField inputText;
    private JTextArea chat;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private static String username;
    /**
     * Send button handler
     */
    private final ActionListener sendAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            out.write(username + " " + inputText.getText() + "\n");
            out.flush();
            inputText.setText("");
        }
    };

    public void init() {

        initComponents();

        connect();

        startServerListener();
    }

    private void startServerListener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try (BufferedReader bin = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                        String messageFromServer = ""; // some message from server
                        while ((messageFromServer = bin.readLine()) != null) {
                            addMessageFromServer(messageFromServer);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * Adds String message to GUI component
     * @param message
     */
    private void addMessageFromServer(String message) {
        chat.append(message + "\n");
    }

    private void initComponents() {
        JPanel container = new JPanel();
        container.setPreferredSize(new Dimension(300, 200));
        container.setLayout(null);
        setContentPane(container);

        JLabel inputLabel = new JLabel("Write text:");
        inputLabel.setSize(150, 20);
        inputLabel.setLocation(5, 105);
        container.add(inputLabel);

        sendButton = new JButton("Send");
        sendButton.setSize(80, 30);
        sendButton.setLocation(105, 125);
        container.add(sendButton);

        sendButton.addActionListener(sendAction);

        inputText = new JTextField();
        inputText.setSize(100, 20);
        inputText.setBorder(BorderFactory.createLineBorder(Color.black));
        inputText.setLocation(5, 128);
        inputText.addActionListener(sendAction);
        container.add(inputText);

        chat = new JTextArea();
        chat.setSize(200, 200);
        chat.setEditable(false);
        chat.setFont(new Font("Arial", Font.BOLD, 12));
        chat.setBorder(BorderFactory.createEtchedBorder(Color.green, Color.black));

        JScrollPane scroll = new JScrollPane(chat);
        scroll.setSize(200, 100);
        scroll.setLocation(5, 5);
        container.add(scroll);

        pack();
        setLocation(200, 150);
        setVisible(true);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void connect() {
        String hostName = "localhost";
        int portNumber = 7777;
        try {
            socket = new Socket(hostName, portNumber);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        if (args.length == 0) {
            username = "";
        } else {
            username = args[0];
        }
        ChatClient cc = new ChatClient();
        cc.init();
    }
}