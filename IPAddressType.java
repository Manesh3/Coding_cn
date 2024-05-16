import java.util.Scanner;

public class IPAddressType {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter an IP address: ");
        String ipAddress = scanner.nextLine();
        scanner.close();
        
        try {
            String ipClass = getIPClass(ipAddress);
            String ipType = getIPType(ipAddress);
            
            System.out.println("IP Address: " + ipAddress);
            System.out.println("Class: " + ipClass);
            System.out.println("Type: " + ipType);
        } catch (Exception e) {
            System.out.println("Invalid IP address");
        }
    }
    
    private static String getIPClass(String ipAddress) throws Exception {
        String[] parts = ipAddress.split("\\.");
        if (parts.length != 4) {
            throw new Exception("Invalid IP address format");
        }
        
        int firstOctet = Integer.parseInt(parts[0]);
        
        if (firstOctet >= 0 && firstOctet <= 127) {
            return "A";
        } else if (firstOctet >= 128 && firstOctet <= 191) {
            return "B";
        } else if (firstOctet >= 192 && firstOctet <= 223) {
            return "C";
        } else if (firstOctet >= 224 && firstOctet <= 239) {
            return "D";
        } else if (firstOctet >= 240 && firstOctet <= 255) {
            return "E";
        } else {
            throw new Exception("Invalid IP address range");
        }
    }
    
    private static String getIPType(String ipAddress) throws Exception {
        String[] parts = ipAddress.split("\\.");
        if (parts.length != 4) {
            throw new Exception("Invalid IP address format");
        }
        
        int firstOctet = Integer.parseInt(parts[0]);
        int secondOctet = Integer.parseInt(parts[1]);
        
        if ((firstOctet == 10) ||
            (firstOctet == 172 && (secondOctet >= 16 && secondOctet <= 31)) ||
            (firstOctet == 192 && secondOctet == 168)) {
            return "Private";
        } else if (firstOctet == 127) {
            return "Loopback";
        } else if (firstOctet == 169 && secondOctet == 254) {
            return "Link-local";
        } else if (firstOctet >= 224 && firstOctet <= 239) {
            return "Multicast";
        } else if (firstOctet >= 240 && firstOctet <= 255) {
            return "Reserved";
        } else {
            return "Public";
        }
    }
}
