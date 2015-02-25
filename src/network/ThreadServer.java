package network;

import java.io.*;
import java.net.*;

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
        // Try to open a server socket on the given port
        // Note that we can't choose a port less than 1024 if we are not
        // privileged users (root)

        try {
            echoServer = new ServerSocket(port);
        }
        catch (IOException e) {
            System.out.println(e);
        }

        System.out.println( "Server is started and is waiting for connections." );
        System.out.println( "With multi-threading, multiple connections are allowed." );
        System.out.println( "Any client can send -1 to stop the server." );

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
        String line;
        try {
            boolean serverStop = false;

            while (true) {
                line = is.readLine();
                System.out.println( "Received " + line + " from Connection " + id + "." );
                int n = Integer.parseInt(line);
                if ( n == -1 ) {
                    serverStop = true;
                    break;
                }
                if ( n == 0 ) break;
                os.println("" + n*n );
            }

            System.out.println( "Connection " + id + " closed." );
            is.close();
            os.close();
            clientSocket.close();

            if ( serverStop ) server.stopServer();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}