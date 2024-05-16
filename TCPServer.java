import java.io.*;
import java.net.*;

public class TCPServer {

    public static void main(String[] args) {
        final int PORT = 12345;

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Waiting for client...");

            // Accept client connection
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            // Create input and output streams
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

            // Receive file name from client
            String fileName = inFromClient.readLine();
            System.out.println("Received file name from client: " + fileName);

            // Read file and send to client
            File file = new File(fileName);
            if (file.exists()) {
                outToClient.writeBytes("FileExists\n");
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outToClient.write(buffer, 0, bytesRead);
                }
                fileInputStream.close();
                System.out.println("File sent successfully.");
            } else {
                outToClient.writeBytes("FileNotExists\n");
                System.out.println("File does not exist.");
            }

            // Close connections
            inFromClient.close();
            outToClient.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
