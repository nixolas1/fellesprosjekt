package network;

import java.io.*;
import java.net.*;

public class ThreadClient {


    static String hostname = "localhost";
    static int port = 7777;

    // declaration section:
    // clientSocket: our client socket
    // os: output stream
    // is: input stream
    static Socket clientSocket = null;
    static DataOutputStream os = null;
    static BufferedReader is = null;

    public static void main(String[] args) {

        // Initialization section:
        // Try to open a socket on the given port
        // Try to open input and output streams

        try {
            clientSocket = new Socket(hostname, port);
            os = new DataOutputStream(clientSocket.getOutputStream());
            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + hostname);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
        }

        // If everything has been initialized then we want to write some data
        // to the socket we have opened a connection to on the given port

        if (clientSocket == null || os == null || is == null) {
            System.err.println( "Something is wrong. One variable is null." );
            return;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String data = null;
        try {
            data = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String response = send(data);
        System.out.println(response);


    }

    public static String send(String data){
        try {

            //System.out.print( "Enter an integer (0 to stop connection, -1 to stop server): " );
            os.writeBytes( data + "\n" );

            String responseLine = is.readLine();
            return responseLine;

        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }

        return "";
    }

    public static boolean close(){
        try {
            os.close();
            is.close();
            clientSocket.close();
            return true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
            return false;
        }
    }
}