import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
public class ClientHandler extends Thread { // pour traiter la demande de chaque client sur un socket particulier
	private Socket socket; 
	private int clientNumber; 
	private String root="C:\\";
	private File currentFile = new File(root);
	
	public ClientHandler(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber; 
		System.out.println("New connection with client #" + clientNumber + " at" + socket);
	}
	
	public void run() { // Création de thread qui envoi un message à un client
		try {
			
			DataInputStream in = new DataInputStream(socket.getInputStream());
			String commandFull, commandName, commandOption;
			int indexSpace;
			DataOutputStream out = new DataOutputStream(socket.getOutputStream()); // création de canal d’envoi 
			out.writeUTF("Hello from server - you are client#" + clientNumber); // envoi de message
			do {
				commandFull = in.readUTF();
				System.out.println(commandFull);
				
				indexSpace=commandFull.indexOf(" ");
				commandName= indexSpace==-1 ? commandFull : commandFull.substring(0, commandFull.indexOf(" "))  ;
				commandOption= indexSpace==-1 ? null:commandFull.substring(indexSpace+1);
				switch(commandName) {
				case "cd":
					cd(commandOption);
					out.writeUTF("Vous êtes dans le dossier "+commandOption+".");
					break;
				case "ls":
					String[] enfantStrings = ls();
					for (String enfant : enfantStrings) {
						out.writeUTF(enfant);
						System.out.println(enfant);
					}
					break;
				case "mkdir":
					mkdir(commandOption);
					out.writeUTF("Le dossier "+commandOption+" a été créé.");
					break;
				case "upload":
					out.writeUTF("Le fichier "+commandOption+" a bien été téléverser.");
					break;
				case "download":
					out.writeUTF("Le fichier "+commandOption+" a bien été téléchargé.");
					break;
				case "exit":
					System.out.println(commandName);
					out.writeUTF("Vous avez été déconnecté avec succès.");
					break;
				default:
					out.writeUTF("Commande non reconnu. Veuillez réessayer.");
					break;
				}
				
			}while(!commandName.equals("exit"));
		} catch (IOException e) {
			System.out.println("Error handling client# " + clientNumber + ": " + e);
		} finally {
			try {
				
				socket.close();
			} catch (IOException e) {
				System.out.println("Couldn't close a socket, what's going on?");
			}
			System.out.println("Connection with client# " + clientNumber + " closed");
		}
	}
	private void cd(String commandOption) {
		if(commandOption=="..") {
			// Ajoter le code pour revenir d'un niveau
		}else {
			currentFile = new File(currentFile.toString()+"\\"+commandOption);
		}
		
	}
	private String[] ls() {
		File[] enfantFiles = currentFile.listFiles();
		String[] returnString= new String[enfantFiles.length];
		for (int i=0;i<enfantFiles.length;i++){
			if(enfantFiles[i].isDirectory()) {
				returnString[i]="[Folder] "+enfantFiles[i].getName();
			}else if (enfantFiles[i].isFile()) {
				returnString[i]="[File] "+enfantFiles[i].getName();
			}
		}
		return returnString;
	}
	private void mkdir(String commandOption) {
		File tempFile = new File(currentFile, commandOption);
		tempFile.mkdir();
	}
	private void upload() {
		
	}
	private void download() {
		
	}
		
}

