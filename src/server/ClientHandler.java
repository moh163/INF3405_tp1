package server;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import common.Tools;
public class ClientHandler extends Thread { // pour traiter la demande de chaque client sur un socket particulier
	private Socket socket; 
	private int clientNumber; 
	
	private Path root = Path.of(System.getProperty("user.dir"), "src", "server", "res");
	
	private File currentFile = new File(root.toString());
	
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
				String[] commandParts = Tools.readCommand(commandFull);
				
				commandName = commandParts[0];
				commandOption = commandParts[1];
				
				switch(commandName) {
				case "cd":
					cd(commandOption);
					out.writeUTF("Vous êtes dans le dossier "+currentFile);
					break;
				case "ls":
					out.writeUTF(ls());
					break;
				case "mkdir":
					boolean isCreated = mkdir(commandOption);
					out.writeUTF("Le dossier "+commandOption+(isCreated? " a" : " n'a pas")+" été créé");
					break;
				case "upload":
					
					if(!commandOption.isEmpty()) {

						String nameAndFormat = in.readUTF();
						
						//int length = Integer.parseInt(in.readUTF());
						
						//in.read(buffer);

						//System.out.println("server data length : "+length);
						System.out.println("nameAndFormat : "+nameAndFormat);
						
						boolean uploaded = upload(nameAndFormat,in);

						out.writeUTF("Le fichier "+commandOption+(uploaded?" a":" n'a pas")+" été bien téléverser");
					}
					
					break;
				case "download":
					out.writeUTF("Le fichier "+commandOption+" a bien été téléchargé");
					break;
				case "exit":
					System.out.println(commandName);
					out.writeUTF("Vous avez été déconnecté avec succès");
					break;
				default:
					out.writeUTF("Commande non reconnu. Veuillez réessayer");
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
		File[] childrenFiles = currentFile.listFiles();
		String returnString= "";
		for (int i=0;i<childrenFiles.length;i++){
			if(childrenFiles[i].isDirectory()) {
				returnString+="[Folder] "+childrenFiles[i].getName()+"\n";
			}else if (childrenFiles[i].isFile()) {
				returnString+="[File] "+childrenFiles[i].getName()+"\n";
			}
		}
		return returnString;
	}
	
	private boolean mkdir(String commandOption) {
		File tempFile = new File(currentFile, commandOption);
		return tempFile.mkdir();
	}
	private boolean upload(String nameAndFormat, DataInputStream in) {
	    Path filePath = Path.of(currentFile.toString(), nameAndFormat);
	    File outputFile = filePath.toFile();

	    int size;
	    try {
	        byte[] buffer = new byte[socket.getReceiveBufferSize()];
	        size=socket.getReceiveBufferSize();
	        OutputStream fileStream = new FileOutputStream(outputFile);
	        int count;
	        socket.setSoTimeout(5000); // set a timeout of 5 seconds
	        while(true){
	            try {
	                count = in.read(buffer);
	                if (count < 0) {
	                    break; // end of stream
	                }
	                fileStream.write(buffer, 0, count);
	            } catch (SocketTimeoutException e) {
	                System.out.println("Timeout reached.");
	                break;
	            }
	        }
	        System.out.println("Sortie de boucle");
	        socket.setSoTimeout(0);
	        fileStream.close();
	        return true;
	    } catch(IOException e) {
	        return false;
	    }
	}

	
//	private boolean upload(String nameAndFormat, DataInputStream in) {
//		
//		Path filePath = Path.of(currentFile.toString(), nameAndFormat);
//		File outputFile = filePath.toFile();
//		try {
//			byte[] buffer = new byte[socket.getReceiveBufferSize()];
//			OutputStream fileStream = new FileOutputStream(outputFile);
//			int count;
//			 while((count = in.read(buffer)) >=1){
//		            fileStream.write(buffer, 0, count);
//		            System.out.println(count);
//		     }
//			 System.out.println("Sortie de boucle");
//			fileStream.close();
//			
//			return true;
//			
//		}catch(IOException e) {
//			
//			return false;
//		}
//	}
	private void download() {
		
	}
		
}









