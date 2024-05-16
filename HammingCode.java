import java.util.Scanner;

public class HammingCode {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a 7/8-bit ASCII data: ");
        String data = scanner.nextLine();
        scanner.close();

        // Encode data using Hamming Code
        String encodedData = encode(data);
        System.out.println("Encoded data: " + encodedData);

        // Introduce an error for demonstration
        String erroneousData = introduceError(encodedData);
        System.out.println("Erroneous data: " + erroneousData);

        // Detect and correct the error
        String correctedData = decodeAndCorrect(erroneousData);
        System.out.println("Corrected data: " + correctedData);
    }

    private static String encode(String data) {
        int[] bits = new int[12];
        int[] dataBits = new int[8];

        // Convert data to bits array
        for (int i = 0; i < data.length(); i++) {
            dataBits[i] = data.charAt(i) - '0';
        }

        // Place data bits in the correct positions
        bits[2] = dataBits[0];
        bits[4] = dataBits[1];
        bits[5] = dataBits[2];
        bits[6] = dataBits[3];
        bits[8] = dataBits[4];
        bits[9] = dataBits[5];
        bits[10] = dataBits[6];
        bits[11] = dataBits[7];

        // Calculate parity bits
        bits[0] = bits[2] ^ bits[4] ^ bits[6] ^ bits[8] ^ bits[10];
        bits[1] = bits[2] ^ bits[5] ^ bits[6] ^ bits[9] ^ bits[10];
        bits[3] = bits[4] ^ bits[5] ^ bits[6] ^ bits[11];
        bits[7] = bits[8] ^ bits[9] ^ bits[10] ^ bits[11];

        // Convert bits array to string
        StringBuilder encodedData = new StringBuilder();
        for (int bit : bits) {
            encodedData.append(bit);
        }

        return encodedData.toString();
    }

    private static String introduceError(String encodedData) {
        char[] chars = encodedData.toCharArray();
        int pos = (int) (Math.random() * encodedData.length());
        chars[pos] = chars[pos] == '0' ? '1' : '0';
        return new String(chars);
    }

    private static String decodeAndCorrect(String data) {
        int[] bits = new int[12];
        for (int i = 0; i < data.length(); i++) {
            bits[i] = data.charAt(i) - '0';
        }

        int p1 = bits[0] ^ bits[2] ^ bits[4] ^ bits[6] ^ bits[8] ^ bits[10];
        int p2 = bits[1] ^ bits[2] ^ bits[5] ^ bits[6] ^ bits[9] ^ bits[10];
        int p4 = bits[3] ^ bits[4] ^ bits[5] ^ bits[6] ^ bits[11];
        int p8 = bits[7] ^ bits[8] ^ bits[9] ^ bits[10] ^ bits[11];

        int errorPosition = p1 + (p2 * 2) + (p4 * 4) + (p8 * 8);

        if (errorPosition != 0) {
            System.out.println("Error detected at position: " + errorPosition);
            bits[errorPosition - 1] ^= 1;
        } else {
            System.out.println("No error detected.");
        }

        // Extract data bits
        StringBuilder correctedData = new StringBuilder();
        correctedData.append(bits[2]).append(bits[4]).append(bits[5])
                     .append(bits[6]).append(bits[8]).append(bits[9])
                     .append(bits[10]).append(bits[11]);

        return correctedData.toString();
    }
}
