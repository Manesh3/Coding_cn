import java.util.Scanner;

public class SubnetCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Input IP address
        System.out.print("Enter IP address (format: xxx.xxx.xxx.xxx): ");
        String ipAddress = scanner.nextLine();
        
        // Input subnet mask prefix length
        System.out.print("Enter subnet mask prefix length: ");
        int prefixLength = scanner.nextInt();
        
        scanner.close();

        // Validate IP address
        if (!isValidIpAddress(ipAddress)) {
            System.out.println("Invalid IP address format.");
            return;
        }

        // Validate prefix length
        if (prefixLength < 0 || prefixLength > 32) {
            System.out.println("Invalid prefix length. It should be between 0 and 32.");
            return;
        }

        // Calculate subnet mask
        String subnetMask = calculateSubnetMask(prefixLength);
        
        // Output subnet mask
        System.out.println("Subnet mask: " + subnetMask);
    }

    // Method to validate IP address format
    private static boolean isValidIpAddress(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        if (octets.length != 4) {
            return false;
        }
        for (String octet : octets) {
            int num;
            try {
                num = Integer.parseInt(octet);
            } catch (NumberFormatException e) {
                return false;
            }
            if (num < 0 || num > 255) {
                return false;
            }
        }
        return true;
    }

    // Method to calculate subnet mask
    private static String calculateSubnetMask(int prefixLength) {
        int[] mask = new int[4];
        for (int i = 0; i < 4; i++) {
            if (prefixLength >= 8) {
                mask[i] = 255;
                prefixLength -= 8;
            } else {
                mask[i] = 256 - (int)Math.pow(2, 8 - prefixLength);
                prefixLength = 0;
            }
        }
        return mask[0] + "." + mask[1] + "." + mask[2] + "." + mask[3];
    }
