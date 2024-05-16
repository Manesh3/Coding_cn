import java.io.*;
import java.net.*;

public class GBNReceiver {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        int expectedSeqNum = 0;

        while (true) {
            String packet = in.readLine();
            if (packet == null || packet.equals("END")) {
                break;
            }
            System.out.println("Received: " + packet);

            if (packet.equals("Packet" + (expectedSeqNum + 1))) {
                System.out.println("Packet " + (expectedSeqNum + 1) + " is correct");
                expectedSeqNum++;
                out.println("ACK " + expectedSeqNum);
            } else {
                System.out.println("Packet " + (expectedSeqNum + 1) + " is incorrect. Sending ACK " + expectedSeqNum);
                out.println("ACK " + expectedSeqNum);
            }
        }

        socket.close();
        serverSocket.close();
    }
}
