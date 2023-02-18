package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
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

				switch (commandName) {
				case "cd":
					cd(commandOption);
					out.writeUTF("Vous êtes dans le dossier " + currentFile);
					break;
				case "ls":
					out.writeUTF(ls());
					break;
				case "mkdir":
					boolean isCreated = mkdir(commandOption);
					out.writeUTF("Le dossier " + commandOption + (isCreated ? " a" : " n'a pas") + " été créé");
					break;
				case "upload":

					if (!commandOption.isEmpty()) {

						int length = Integer.parseInt(in.readUTF());
						String nameAndFormat = in.readUTF();
						
						boolean uploaded = upload(nameAndFormat, length,in);

						out.writeUTF("Le fichier " + commandOption + (uploaded ? " a" : " n'a pas")
								+ " été bien téléverser");
					}

					break;
				case "download":
					if(!commandOption.isEmpty()) {
						download(commandOption, out);
						out.writeUTF("Le fichier " + commandOption + " a bien été téléchargé");
					}
					break;
				case "exit":
					System.out.println(commandName);
					out.writeUTF("Vous avez été déconnecté avec succès");
					break;
				default:
					out.writeUTF("Commande non reconnu. Veuillez réessayer");
					break;
				}

			} while (!commandName.equals("exit"));
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
		if(commandOption.equals("..")) {
			currentFile = new File(currentFile.getParent());
		}else {
			currentFile = new File(Path.of(currentFile.toString(), commandOption).toString());
		}

	}

	private String ls() {
		File[] childrenFiles = currentFile.listFiles();
		String returnString = "";
		for (int i = 0; i < childrenFiles.length; i++) {
			if (childrenFiles[i].isDirectory()) {
				returnString += "[Folder] " + childrenFiles[i].getName() + "\n";
			} else if (childrenFiles[i].isFile()) {
				returnString += "[File] " + childrenFiles[i].getName() + "\n";
			}
		}
		return returnString;
	}

	private boolean mkdir(String commandOption) {
		File tempFile = new File(currentFile, commandOption);
		return tempFile.mkdir();
	}
	
	private boolean upload(String nameAndFormat, int length, DataInputStream in) {
	    Path filePath = Path.of(currentFile.toString(), nameAndFormat);
	    File outputFile = filePath.toFile();

	    int size;
	    try {
	        byte[] buffer = new byte[socket.getReceiveBufferSize()];
	        size=socket.getReceiveBufferSize();
	        OutputStream fileStream = new FileOutputStream(outputFile);
	        int count = 0;
	        while((length -= count) > 0){
	        	count = in.read(buffer);
                fileStream.write(buffer, 0, count);
	        }
	        fileStream.close();
	        return true;
	    } catch(IOException e) {
	        return false;
	    }
	}

	private void download(String commandName, DataOutputStream out) {
		try {
			if (!commandName.isEmpty()) {
				File file = new File(Path.of(root.toString(), commandName).toString());
				byte[] data = new byte[8192]; // Files.readAllBytes(dataPath);
				FileInputStream fileIn = new FileInputStream(file);
				DataInputStream dataIn = new DataInputStream(new BufferedInputStream(fileIn));

				int count;
				out.writeUTF(commandName);
				while ((count = dataIn.read(data)) >= 0) {
					out.write(data, 0, count);
				}
				System.out.println("sortie de boucle client");

				System.out.println("client name: {" + commandName + "}");
				System.out.println("client data length : " + data.length);
			} else {
				System.out.println("no data");
			}
		} catch (IOException e) {
			System.out.println("Une erreur est surevenue: " + e);
		}

	}

}
