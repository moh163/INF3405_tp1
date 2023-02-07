import java.util.regex.Pattern;

public class Tools {
	
	public static boolean ipValidation(String ipAddress) {
		final String IP_ADDRESS_PATTERN = 
		        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		
		return Pattern.compile(IP_ADDRESS_PATTERN).matcher(ipAddress).matches();
	}
	
	public static boolean portValidation(String port) {
		final String PORT_PATTERN = "^50[0-0]{1,2}$";
		return Pattern.compile(PORT_PATTERN).matcher(port).matches();
	}

}
