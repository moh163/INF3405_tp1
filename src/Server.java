import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
	
	private static ServerSocket Listener;

	public static void main(String[] args) throws Exception {
		
		int clientNumber = 0;
		
		
		Scanner input = new Scanner(System.in);
		String serverAddress;
		
		do {
			System.out.println("Enter server address");
			serverAddress = input.nextLine();			
			System.out.println(serverAddress);
		}while(!Tools.ipValidation(serverAddress));
		
		
		input = new Scanner(System.in);

		String serverPort;
		
		do {

			System.out.println("Enter port");
			serverPort = input.nextLine();
			
		}while (Tools.portValidation(serverPort));
		
		
		System.out.println(serverPort);
		
		
		
		
		
		Listener = new ServerSocket();
		
		Listener.setReuseAddress(true);
		
		InetAddress serverIp = InetAddress.getByName(serverAddress);
		
		Listener.bind(new InetSocketAddress(serverIp, serverPort));
		
		System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);
		
		try {
			while(true) {
				new ClientHandler(Listener.accept(), clientNumber++).start();
			}
		}finally {
			Listener.close();
		}
		
	}
	
}

























