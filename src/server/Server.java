package server;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;
import common.Tools;

public class Server {
	
	private static ServerSocket Listener;

	public static void main(String[] args) throws Exception {
		
		int clientNumber = 0;

		Scanner input = new Scanner(System.in);
//		String serverAddress = Tools.readAddress(input);
//		String serverPort = Tools.readPort(input);
		String serverAddress = "127.0.0.1";
		String serverPort = "5000";	

		Listener = new ServerSocket();
		
		Listener.setReuseAddress(true);
		
		InetAddress serverIp = InetAddress.getByName(serverAddress);
		
		Listener.bind(new InetSocketAddress(serverIp, Integer.parseInt(serverPort)));
		
		System.out.format("The server is running on %s:%s", serverAddress, serverPort);
		
		try {
			while(true) {
				new ClientHandler(Listener.accept(), clientNumber++).start();
			}
		}finally {
			input.close();
			Listener.close();
		}
		
	}
	
}

























