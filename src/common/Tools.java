package common;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
	
	public static String readAddress(Scanner input) {
		String serverAddress;
		do {
			System.out.println("Enter server address to connect to");
			serverAddress = input.nextLine();		
		}while(!ipValidation(serverAddress));
		return serverAddress;
	}
	
	public static String readPort(Scanner input) {
		String serverPort;
		do {
			System.out.println("Enter port to connect to");
			serverPort = input.nextLine();	
		}while (!Tools.portValidation(serverPort));
		return serverPort;
	}
	
	public static boolean ipValidation(String ipAddress) {
		final String IP_ADDRESS_PATTERN = 
		        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		
		return Pattern.compile(IP_ADDRESS_PATTERN).matcher(ipAddress).matches();
	}
	
	public static boolean portValidation(String port) {
		final String PORT_PATTERN = "^(50[0-4]\\d|5050)$";
		return Pattern.compile(PORT_PATTERN).matcher(port).matches();
	}

}
