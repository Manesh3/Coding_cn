import java.io.*;
import java.net.*;

public class TCPClient {

    public static void main(String[] args) {
        final String SERVER_IP = "127.0.0.1";
        final int PORT = 12345;
        final String FILE_NAME = "text.txt";

        try {
            // Connect to server
            Socket clientSocket = new Socket(SERVER_IP, PORT);
            System.out.println("Connected to server.");

            // Create input and output streams
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            // Send file name to server
            outToServer.writeBytes(FILE_NAME + "\n");

            // Receive response from server
            String response = inFromServer.readLine();
            if (response.equals("FileExists")) {
                // Receive file from server
                FileOutputStream fileOutputStream = new FileOutputStream("received_" + FILE_NAME);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(clientSocket.getInputStream());
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                fileOutputStream.close();
                bufferedInputStream.close();
                System.out.println("File received successfully.");
            } else if (response.equals("FileNotExists")) {
                System.out.println("File does not exist on server.");
            }

            // Close connections
            inFromServer.close();
            outToServer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
