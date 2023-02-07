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
		System.out.println("Enter server address");
		
		boolean found = false;
		
		do {
			
			String serverAddress = input.nextLine();
			
			System.out.println(serverAddress);
			
			// verifier si presence de caractere non numeriques ou non point
			
			boolean goodChar = true;
			
			for(int c=0;c<serverAddress.length() && goodChar;c++) {
				int car = serverAddress.charAt(c);
				goodChar = (car <= 57 && car >= 48) || car == 46;
			}

			if(goodChar) {
				for(int p=0;p<2;p++) {
					
					int point = serverAddress.indexOf(".");
					
					found = point != -1;
					
					if(found) {
						int octet = Integer.parseInt(serverAddress.substring(0, point));
						found = octet >= 0 && octet <= 255;
						if(found) {
							serverAddress = serverAddress.substring(point+1, serverAddress.length());
						}
					}
				}
				
				int octet = Integer.parseInt(serverAddress.substring(2));
				found = octet >= 0 && octet <= 255;
			}
			
		}while(!found);
		

		
		input = new Scanner(System.in);

		int serverPort;
		
		do {

			System.out.println("Enter port");
			serverPort = Integer.parseInt(input.nextLine());
			
		}while (serverPort < 5000 || serverPort > 5050);
		
		
		System.out.println(serverPort);
		
		
		
		
		
/*		Listener = new ServerSocket();
		
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
		*/
	}
	
}

























