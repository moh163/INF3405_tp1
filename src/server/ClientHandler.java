package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
public class ClientHandler extends Thread { // pour traiter la demande de chaque client sur un socket particulier
	private Socket socket; 
	private int clientNumber; 
	private String root = System.getProperty("user.dir")+File.separator+"src"+File.separator+"server"+File.separator+"res";
	private File currentFile = new File(root);
	
	public ClientHandler(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber; 
		System.out.println();
		System.out.println(root);
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
				System.out.println("full : "+commandFull);
				
				indexSpace=commandFull.indexOf(" ");
				commandName= indexSpace==-1 ? commandFull : commandFull.substring(0, commandFull.indexOf(" "))  ;
				commandOption= indexSpace==-1 ? null:commandFull.substring(indexSpace+1);
				System.out.println("name : "+commandName);
				System.out.println("options : "+commandOption);
				
				switch(commandName) {
				case "cd":
					cd(commandOption);
					out.writeUTF("Vous êtes dans le dossier "+currentFile+".");
					break;
				case "ls":
					out.writeUTF(ls());
					break;
				case "mkdir":
					boolean isCreated = mkdir(commandOption);
					out.writeUTF("Le dossier "+commandOption+(isCreated? " a" : " n'a pas")+" été créé.");
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
	private String ls() {
		File[] enfantFiles = currentFile.listFiles();
		String returnString= "";
		for (int i=0;i<enfantFiles.length;i++){
			if(enfantFiles[i].isDirectory()) {
				returnString+="[Folder] "+enfantFiles[i].getName()+"\n";
			}else if (enfantFiles[i].isFile()) {
				returnString+="[File] "+enfantFiles[i].getName()+"\n";
			}
		}
		return returnString;
	}
	private boolean mkdir(String commandOption) {
		File tempFile = new File(currentFile, commandOption);
		return tempFile.mkdir();
	}
	private void upload() {
		
	}
	private void download() {
		
	}
		
}

