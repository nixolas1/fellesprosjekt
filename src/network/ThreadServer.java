package network;
import java.net.*;
import java.io.*;

public class ThreadServer {

    static class ServerThread implements Runnable {
        Socket client = null;
        public ServerThread(Socket c) {
            this.client = c;
        }
        public void run() {
            try {
                System.out.println("Connected to client : "+client.getInetAddress().getHostName());
                client.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
    public static void main(String args[]) {
        try {
            ServerSocket server = new ServerSocket(7000);
            while (true) {
                Socket p = server.accept();
                new Thread(new ServerThread(p)).start();
            }
        } catch (Exception ex) {
            System.err.println("Error : " + ex.getMessage());
        }
    }
}

/*
    Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                clientSocket.getInputStream()));
        String inputLine, outputLine;
        KnockKnockProtocol kkp = new KnockKnockProtocol();

        outputLine = kkp.processInput(null);
        out.println(outputLine);

        while ((inputLine = in.readLine()) != null) {
             outputLine = kkp.processInput(inputLine);
             out.println(outputLine);
             if (outputLine.equals("Bye."))
                break;
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
 */