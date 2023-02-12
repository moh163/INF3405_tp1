import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

// Application client
public class Client {
	
	private static Socket socket;
	
	public static void main(String[] args) throws Exception {
		
		// Adresse et port du serveur
//		String serverAddress = "127.0.0.1";
//		int port = 5000;
		Scanner input = new Scanner(System.in);
		String serverAddress;
		
		do {
			System.out.println("Enter server address to connect to");
			serverAddress = input.nextLine();			
			System.out.println(serverAddress);

		}while(!Tools.ipValidation(serverAddress));
		String serverPort;
		
		do {

			System.out.println("Enter port to connect to");
			serverPort = input.nextLine();
			
		}while (!Tools.portValidation(serverPort));
		
		// Création d'une nouvelle connexion aves le serveur
		socket = new Socket(serverAddress, Integer.parseInt(serverPort));
		System.out.format("Serveur lancé sur [%s:%d]", serverAddress, Integer.parseInt(serverPort));
		
		// Création d'un canal entrant pour recevoir les messages envoyés, par le serveur
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		// Attente de la réception d'un message envoyé par le, server sur le canal
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		boolean isClosed=false;
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		String command;
		do {
			command=input.nextLine();
			if(command.equals("exit")) {
				isClosed=true;
				System.out.println(isClosed);
			}
			out.writeUTF(command);
			
			
		}while(!isClosed);
		// Fermeture de la connexion avec le serveur
		System.out.println("Connexion interrompu avec le serveur");
		input.close();
		socket.close();
	}
}