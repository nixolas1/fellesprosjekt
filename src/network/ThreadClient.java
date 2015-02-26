package network;

import java.io.*;
import java.net.*;
import java.util.Hashtable;

public class ThreadClient {


    static String hostname = "localhost";
    static int port = 7777;

    // declaration section:
    // clientSocket: our client socket
    // os: output stream
    // is: input stream
    static Socket clientSocket = null;
    static OutputStream os = null;
    static InputStream is = null;

    public static void main(String[] args) {

        // Initialization section:
        // Try to open a socket on the given port
        // Try to open input and output streams
        System.out.print("Starting client socket... ");

        try {
            clientSocket = new Socket(hostname, port);
            os = clientSocket.getOutputStream();
            is = clientSocket.getInputStream();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + hostname);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
        }

        // If everything has been initialized then we want to write some data
        // to the socket we have opened a connection to on the given port

        if (clientSocket == null || os == null || is == null) {
            System.err.println( "Connection setup error. A var is null." );
            return;
        }
        System.out.println("started!");

        Hashtable data = new Hashtable<String, String>(){{
            put("username","nicolaat");
            put("pass", "hello");
            put("domain", "stud.ntnu.no");
        }};

        Query reply = send(new Query("login", data));
        Hashtable<String, Boolean> response = reply.data;
        if(response.get("reply")){
            //success
        }
        else{
            if(response.get("data")){
                //wrong username or domain
            }else{
                //wrong password
            }
        }

    }

    public static Query send(Query query){
        try {

            System.out.println("Sent "+query.function);

            //os.writeBytes();

            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(query);

            ObjectInputStream ois = new ObjectInputStream(is);
            Query response = (Query)ois.readObject();
            System.out.println("Server replied: "+response.function+", "+response.data.toString());
            return response;

        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new Query("error", false);
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