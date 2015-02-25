package network;

import java.io.*;
import java.net.*;
import server.Logic;

public class ThreadServer {
    public static void main(String args[]) {
        int port = 6789;
        ThreadServer server = new ThreadServer( port );
        server.startServer();
    }

    // declare a server socket and a client socket for the server;
    // declare the number of connections

    ServerSocket echoServer = null;
    Socket clientSocket = null;
    int numConnections = 0;
    int port;

    public ThreadServer(int port) {
        this.port = port;
    }

    public void stopServer() {
        System.out.println( "Server cleaning up." );
        System.exit(0);
    }

    public void startServer() {

        try {
            echoServer = new ServerSocket(port);
        }
        catch (IOException e) {
            System.out.println(e);
        }


        // Whenever a connection is received, start a new thread to process the connection
        // and wait for the next connection.

        while ( true ) {
            try {
                clientSocket = echoServer.accept();
                numConnections ++;
                ServerConnection oneconnection = new ServerConnection(clientSocket, numConnections, this);
                new Thread(oneconnection).start();
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

class ServerConnection implements Runnable {
    BufferedReader is;
    PrintStream os;
    Socket clientSocket;
    int id;
    ThreadServer server;

    public ServerConnection(Socket clientSocket, int id, ThreadServer server) {
        this.clientSocket = clientSocket;
        this.id = id;
        this.server = server;
        System.out.println( "Connection " + id + " established with: " + clientSocket );
        try {
            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    public void run() {
        String request;
        String ret;
        try {
            boolean serverStop = false;

            while (true) {
                request = is.readLine();

                System.out.println( "Received " + request + " from Connection " + id + "." );
                String reply = Logic.process(request);
                os.println(reply);

                if(reply==null)break;
            }

            System.out.println( "Connection " + id + " closed." );
            is.close();
            os.close();
            clientSocket.close();

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}