package network;

import java.io.*;
import java.net.*;
import java.util.Hashtable;

import server.Logic;

public class ThreadServer {
    public static void main(String args[]) {
        int port = 7777;
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
        System.out.print("Server start");
        try {
            echoServer = new ServerSocket(port);
        }
        catch (IOException e) {
            System.out.println(e);
        }
        System.out.println( "ed" );

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
    InputStream is;
    OutputStream os;
    Socket clientSocket;
    int id;
    ThreadServer server;

    public ServerConnection(Socket clientSocket, int id, ThreadServer server) {
        this.clientSocket = clientSocket;
        this.id = id;
        this.server = server;
        System.out.println( "Connection " + id + " established with: " + clientSocket );
        try {
            is = clientSocket.getInputStream();
            os = clientSocket.getOutputStream();
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    public void run() {
        String function;

        try {
            boolean serverStop = false;

            while (true) {

                //convert input stream from socket to Query object
                ObjectInputStream ois = new ObjectInputStream(is);
                Query query = (Query)ois.readObject();
                function = query.function;
                Hashtable data = query.data;

                System.out.println( "Received " + function + " query from Connection " + id + "." );

                //Send function request and data to Server logic processing
                Query reply = Logic.process(function, data);

                //reply to client

                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(reply);
                System.out.println( "Replied '" + reply.function+", "+reply.data.toString() + "' to query from Connection " + id + "." );

                if(reply==null)break;
            }

            System.out.println( "Connection " + id + " closed." );
            is.close();
            os.close();
            clientSocket.close();


        } catch (IOException e) {
            System.out.println("Connection to client killed: "+e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}