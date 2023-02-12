import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
	
	private static ServerSocket Listener;

	public static void main(String[] args) throws Exception {
		
		System.out.println("hello");
		
		int clientNumber = 0;
		
		
		Scanner input = new Scanner(System.in);
		String serverAddress;
		
		do {
			System.out.println("Enter server address");
			serverAddress = input.nextLine();			
			System.out.println(serverAddress);

		}while(!Tools.ipValidation(serverAddress));

		String serverPort;
		
		do {

			System.out.println("Enter port");
			serverPort = input.nextLine();
			
		}while (!Tools.portValidation(serverPort));
		
		
		System.out.println(serverPort);
		
		
		

		Listener = new ServerSocket();
		
		Listener.setReuseAddress(true);
		
		InetAddress serverIp = InetAddress.getByName(serverAddress);
		
		Listener.bind(new InetSocketAddress(serverIp, Integer.parseInt(serverPort)));
		
		System.out.format("The server is running on %s:%d%n", serverAddress, Integer.parseInt(serverPort));
		
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

























