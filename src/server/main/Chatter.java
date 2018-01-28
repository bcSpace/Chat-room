package server.main;

public abstract class Chatter {
	
	public abstract boolean usernameGood(String s, int id); 
	public abstract void sendMessage(String message);

}
