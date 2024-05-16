import java.util.Scanner;

public class CRC {

    // Generator polynomial (CRC-8)
    private static final String GENERATOR = "100000111"; // CRC-8-ATM

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the 7/8-bit ASCII data: ");
        String data = scanner.nextLine();
        scanner.close();

        String dataWithCRC = generateCRC(data);
        System.out.println("Data with CRC: " + dataWithCRC);

        boolean isValid = verifyCRC(dataWithCRC);
        System.out.println("Data with CRC is " + (isValid ? "valid" : "invalid"));

        // Introduce an error for demonstration
        String erroneousData = introduceError(dataWithCRC);
        System.out.println("Erroneous data: " + erroneousData);

        boolean isErroneousDataValid = verifyCRC(erroneousData);
        System.out.println("Erroneous data with CRC is " + (isErroneousDataValid ? "valid" : "invalid"));
    }

    private static String generateCRC(String data) {
        String paddedData = data + "00000000"; // Append 8 zeros for CRC-8
        String crc = divide(paddedData, GENERATOR);
        return data + crc;
    }

    private static boolean verifyCRC(String dataWithCRC) {
        String remainder = divide(dataWithCRC, GENERATOR);
        return remainder.equals("00000000");
    }

    private static String divide(String dividend, String divisor) {
        int len = divisor.length();
        String temp = dividend.substring(0, len);

        while (len < dividend.length()) {
            if (temp.charAt(0) == '1') {
                temp = xor(temp, divisor) + dividend.charAt(len);
            } else {
                temp = xor(temp, "000000000") + dividend.charAt(len);
            }
            temp = temp.substring(1);
            len++;
        }

        if (temp.charAt(0) == '1') {
            temp = xor(temp, divisor);
        } else {
            temp = xor(temp, "000000000");
        }

        return temp.substring(1);
    }

    private static String xor(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return result.toString();
    }

    private static String introduceError(String dataWithCRC) {
        char[] chars = dataWithCRC.toCharArray();
        int pos = (int) (Math.random() * dataWithCRC.length());
        chars[pos] = chars[pos] == '0' ? '1' : '0';
        return new String(chars);
    }
}
