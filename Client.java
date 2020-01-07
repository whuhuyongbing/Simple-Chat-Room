import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Yongbing Hu
 * @version 0.0.0
 * @time 2019-10-14 11:51 a.m.
 * @description Client 
 */
public class Client {

    // a chat GUI and a login GUI
    private GUI gui;
    private LoginGUI loginGui;
    private Socket socket;


    //need the ip to Contruxt the Client
    public Client(String ip) {
        this.gui = new GUI();
        this.loginGui = new LoginGUI();
        try {
            this.socket = new Socket(ip,9999);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //start the client socket
    public void start() throws Exception {

        //handle login
        loginGui.getFrame().setVisible(true);
        JTextField UserName = loginGui.getTxtUserName();
        JButton btnLogin = loginGui.getBtnLogin();
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (setName(UserName.getText())) {
                    ExecutorService executorService = Executors.newCachedThreadPool();
                    executorService.execute(new MessageReceiver());
                    loginGui.getFrame().dispose();
                    gui.getFrame().setVisible(true);
                }
            }
        });

        loginGui.getBtnCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginGui.getFrame().dispose();
                Thread.currentThread().interrupt();
                System.exit(0);
            }
        });


        //send messages
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(),"UTF-8"),true);
        JButton send = gui.getSend();
        JTextField textField = gui.getTextField();
        JButton connect = gui.getConnect();
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField.getText();
                System.out.println(message);
                if (message != null) {
                    printWriter.println(message);
                    gui.getTextField().setText("");
                }

            }
        });

        //disconnect 
        gui.getConnect().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.getFrame().dispose();
                Thread.currentThread().interrupt();
                System.exit(0);
            }
        });

    }
    private boolean setName(String userName) {

        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(),"UTF-8"),true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(),"UTF-8"));
            if (userName.trim().length() == 0) {
                loginGui.getMention().setText("the name can't be null");
                return false;
            } else {
                printWriter.println(userName);
                String isSuccess = bufferedReader.readLine();
                if (isSuccess != null && !"success".equals(isSuccess)) {
                    loginGui.getMention().setText(userName + " already exist. Change another name");
                    loginGui.getTxtUserName().setText("");
                    return false;
                } else {
                    System.out.println("Login!  you can chat with other guys");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // Thread that is to receive the message and display on the screen
    class MessageReceiver implements Runnable {
        @Override
        public void run() {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
                String message = null;
                while ((message = bufferedReader.readLine()) != null) {
                    gui.getTextArea().append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //main function
    public static void main(String[] args) throws Exception {
        Client client = new Client("localhost");
        client.start();
    }
}
