package client;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import common.Tools;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.net.SocketOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

// Application client
public class Client {
	
	private static Socket socket;
	
	private static Path root = Path.of(System.getProperty("user.dir"), "src", "client", "res");
	
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
		
		// Création d'un canal entrant et sortant
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		
		// Attente de la réception d'un message envoyé par le serveur
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		String command;
		do {
			System.out.println("Entrez une commande : ");
			
			command = input.nextLine();
			out.writeUTF(command);
			
			String[] commandParts = Tools.readCommand(command);
			
			switch(commandParts[0]){
			case "upload" : 
				if(!commandParts[1].isEmpty()) {
					Path filePath = Path.of(root.toString(), commandParts[1]);
					File file= new File(filePath.toString());
					byte[] data = new byte[8192]; //Files.readAllBytes(dataPath);
					FileInputStream fileIn = new FileInputStream(file);
					DataInputStream dataIn = new DataInputStream(new BufferedInputStream(fileIn));
					
					out.writeUTF(Files.readAllBytes(filePath).length+"");
					
					int count;
					out.writeUTF(commandParts[1]);
					while ((count = dataIn.read(data)) >= 0)
					{
					  out.write(data, 0, count);
					}
				}else {
					System.err.println("Aucune donnée");
				}
				break;
			case "download" : 
				if(!commandParts[1].isEmpty()) {
					
				}
				break;
			}
			
			// afficher la réponse du serveur
			System.out.println(in.readUTF());
			
		}while(!command.equals("exit"));
		
		// Fermeture de la connexion avec le serveur
		System.out.println("Connexion interrompu avec le serveur");
		input.close();
		socket.close();
	}
}