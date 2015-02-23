/*package network;

import com.ui.ChatFrame;

import java.io.*;
import java.net.*;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.util.Arrays;


public class SocketClient implements Runnable{
    
    public int port;
    public String serverAddr;
    public java.net.Socket socket;
   // private String password = "";
    public BufferedReader In;
    public BufferedWriter Out;
    public int maxName = 15;
    public StringBuffer txtEnc = new StringBuffer();
    public StringBuffer txtDec = new StringBuffer();
   
    public SocketClient(ChatFrame frame) throws IOException{
        ui = frame; this.serverAddr = ui.serverAddr; this.port = ui.port;
        socket = new Socket(InetAddress.getByName(serverAddr), port);
        
        Out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream( )));
        In = new BufferedReader(new InputStreamReader(socket.getInputStream( )));
        Out.flush();
    }

    @Override
    public void run() {
        boolean keepRunning = true;
       
        while(keepRunning){
            try {
            	
            	String msg[] = In.readLine().split(":");
            	String type = msg[0];
            	 System.out.println("Incoming : "+Arrays.asList(msg).toString());
            	
            	switch(type){
            	case "PING ":
            		send("PONG", msg[1]);
            		break;
            	case "ERROR ":
            		ui.quit();
            	}
            	
            	if(msg.length>=3){
            		
            		String content = msg[2];
                	type=msg[1].split(" ")[1];
                	String sender = msg[1].split("!")[0];
                	System.out.println(type+"!!"+sender+"!!"+content);
            	
                if(type.equals("PRIVMSG") && ui.ready){
                	
                    String out = ui.crypt.decrypt(ui.crypt.unKek(content));
                    int spacing = (maxName-sender.length())<0 ? 0 : maxName-sender.length() ;
                    String begin = new String(new char[spacing]).replace('\0', ' ')+"<"+sender+"> ";

                	if(!out.equals("** ENCRYPTED **") && !out.equals("")){
                		ui.toFront();
                		//mac shit
                		if(!ui.isWin32()){
                		Application application = Application.getApplication();
                		application.requestUserAttention(false);
                		}
                		
                		if(out.equals("000"))out=content;

                	 	if(out.startsWith("/")){
                	 		String[] args = out.split(" ");
                     		String cmd = args[0].substring(1).toLowerCase();
                     		
                     		switch(cmd){
                     			case "me":
                     				begin="* "+sender+" ";
                     				out=out.substring(4);
                     				break;
                     			case "slap":
                     				begin="* "+sender+" ";
                     				out="slaps "+out.substring(6)+" with a large niggerdick";
                     				break;
                     				
                     			case "quote":
                     				out="'"+out.substring(8+args[1].length())+"' ~"+args[1];
                     				break;
                     				
                     		}
                	 	}
                	 	
	                	if(ui.hidden){
	                		 txtDec.append(begin+ out + "\n");
	                		 //content=content.replaceFirst("\\s+\\w+$", "");
	                		 out=content;
	                	}
	                	txtEnc.append(begin + content + "\n");
	                	ui.print(begin + out);
	                    
	                    ui.jScrollPane1.getVerticalScrollBar().setValue(ui.jScrollPane1.getVerticalScrollBar().getMaximum());
                	}
                }
                else if(type.equals("001")){ //login successful
                    	ui.model.addElement(ui.username);
                        ui.print("~ Login Successful");
                        //password = new String(ui.jPasswordField1.getPassword());
                }
                else if(type.equals("433")){ //nick taken
                	ui.print("~ Could not log in: nick '"+ui.username+"' already in use");
                	ui.jTextField3.setText(System.getProperty("user.name"));
                }
                else if(type.equals("020")){ //connect successful

                    ui.jPasswordField1.requestFocus();
                    ui.print("~ Connected to "+ui.jTextField1.getText());
                    ui.join();
                }
                else if(type.equals("353")){ //userlist
                	maxName=0;
                	String users[] = content.split(" ");
                	for(int i = 1; i < users.length; i++){
                		if(!ui.model.contains(users[i])){
	                		ui.model.addElement(users[i]); 
	                		if(users[i].length()> maxName)maxName=users[i].length();
                		}
                	}
                    
                }

                else if(type.equals("JOIN") && !sender.equals(ui.username)){
                	ui.print("~ "+ sender +" has joined");
                	
                	maxName=0;
                	for(int i = 0; i < ui.model.getSize(); i++)
                        if(ui.model.getElementAt(i).length()> maxName)maxName=ui.model.getElementAt(i).length();
                    
                    if(!content.equals(ui.username)){
                        boolean exists = false;
                        for(int i = 0; i < ui.model.getSize(); i++){
                            if(ui.model.getElementAt(i).equals(content)){
                                exists = true; break;
                            }
                            if(ui.model.getElementAt(i).length()> maxName)maxName=ui.model.getElementAt(i).length();
                        }
                        if(!exists){ ui.model.addElement(content); }
                    }
                    
                }
                else if(type.equals("QUIT")){
                    if(equals(ui.username)){
                        ui.print("<"+ sender +" > Me> : Bye");
                        ui.quit();
                    }
                    else{
                        ui.model.removeElement(content);
                        ui.print("~ "+ sender +" has signed out");
                    }
                }
                else{
                   // ui.print("[SERVER > Me] : Unknown message type\n");
                }
                
            	} //msg >2
            }
            catch( IOException | GeneralSecurityException e) {
                keepRunning = false;
                System.out.println(e);
                ui.print("~ Connection Failure :/");
               
                ui.enableLogin();
                for(int i = 1; i < ui.model.size(); i++){
                    ui.model.removeElementAt(i);
                }
                
                ui.clientThread.stop();
                
                System.out.println("Exception SocketClient run()");
                e.printStackTrace();
            }
        }
    }
    
    public void send(String cmd, String msg){
    	send(cmd, "", msg);
    }
    
    public void send(String cmd, String target, String msg){
    	String out = cmd.toUpperCase(), end="\r\n";
    	//NICK USER JOIN PING PRIVMSG QUIT
 		switch(cmd){
 			case "PRIVMSG": case "USER":
 				out+=" "+target+" :"+msg+end;
 				break;
 			case "NICK": case "JOIN": case "QUIT": case "PONG":
 				out+=" :"+msg+end;
 				break;
 		}
    	
    	
        try {
            Out.write(out);
            Out.flush();
            System.out.println("Outgoing : "+out);
        } 
        catch (IOException ex) { 
            System.out.println("Exception SocketClient send()");
        }
    }
    
    public void closeThread(Thread t){
        t = null;
    }
    
}
*/