package client;
import java.io.DataInputStream;
import common.Tools;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

// Application client
public class Client {
	
	private static Socket socket;
	
	public static void main(String[] args) throws Exception {
		
		// Adresse et port du serveur

		Scanner input = new Scanner(System.in);
//		String serverAddress = Tools.readAddress(input);
//		String serverPort = Tools.readPort(input);
		String serverAddress = "127.0.0.1";
		String serverPort = "5000";	
		
		// Création d'une nouvelle connexion avec le serveur
		socket = new Socket(serverAddress, Integer.parseInt(serverPort));
		System.out.format("Serveur lancé sur [%s:%s]", serverAddress, serverPort);
		
		// Création d'un canal entrant et sortant pour recevoir les messages envoyés, par le serveur
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		
		// Attente de la réception d'un message envoyé par le serveur, server sur le canal
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		boolean isClosed=false;
		String command;
		do {
			command=input.nextLine();
			if(command.equals("exit")) {
				isClosed=true;
				System.out.println(isClosed);
			}
			out.writeUTF(command);
			
			// afficher la réponse du serveur
			System.out.println(in.readUTF());
			
		}while(!isClosed);
		// Fermeture de la connexion avec le serveur
		System.out.println("Connexion interrompu avec le serveur");
		input.close();
		socket.close();
	}
}