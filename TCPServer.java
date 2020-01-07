import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Yongbing Hu
 * @version 0.0.0
 * @time 2019-10-14 11:36 a.m.
 * @description
 */
public class TCPServer {
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private Map<String, PrintWriter> info;

    public TCPServer() {
        try {
            serverSocket = new ServerSocket(9999);
            executorService = Executors.newCachedThreadPool();
            info = new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //start TCPServer
    public void start() {
        try {
            while (true) {
                System.out.println("waiting for the client...");
                Socket socket = this.serverSocket.accept();
                InetAddress inetAddress = socket.getInetAddress();
                System.out.println("client : " + inetAddress.getHostAddress() + " connected success");
                //let thread to deal with the connected client
                executorService.execute(new Listener(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class Listener implements Runnable {
        private Socket socket;
        private String name;

        public  Listener(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            

            /**
             * get the information of the client and store in the map
             * 
            */
            try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true)) {
                this.name = getName();
                store(this.name,printWriter);
                Thread.sleep(100);

                //send message to all the client
                sendToAll("[System message] " + name + " is online");


                //receive message from the client
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(),"UTF-8"));
                while(true) {
                    String message = bufferedReader.readLine();
                    if (message.length() != 0 && message != null)
                        sendToAll(this.name + ":" + message);

                    // a simple way to detect that whether the client is still on
                    if (message == null) {
                        break;
                        }
                    }
                } catch (Exception e) {
                // e.printStackTrace();
                    System.out.println(this.name + " is offline");
            } finally {

                // handle client disconnect
                remove(this.name);
                if (name != null)
                    sendToAll("[System message] " + name + " is offline");
            }

        }

        //remove the client when it disconnect
        private void remove(String name) {
            info.remove(name);
        }

        // send message to all client that are connected to the server
        public synchronized void sendToAll(String message) {
            for (PrintWriter printWriter : info.values()) {
                printWriter.println(message);
            }
        }

        //store the information of client in the map
        public synchronized void store(String name, PrintWriter printWriter) {
            info.put(name, printWriter);
        }

        private String getName() {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
                //true inorder the check whether the name is right
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                while (true) {
                    String name = bufferedReader.readLine();
                    if (name.trim().length() == 0 || info.containsKey(name)) {
                        //name is not right
                        printWriter.println("fail");
                    } else {
                        printWriter.println("success");
                        return  name;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return  null;
        }
    }

    // run the server
    public static void main(String[] args) {
        try {
            TCPServer server = new TCPServer();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

