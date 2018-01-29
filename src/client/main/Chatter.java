package client.main;

public abstract class Chatter {
	
	public String username;
	
	public abstract void connect(String ip, int port); //connecting 
	public abstract void testUsername(String username); //sending a username
	public abstract void sendMessage(String s); //Sending messages 
	public abstract void exit(String s); //exiting without client request
	public abstract void disconnect(); //client hitting disconnect button

}
