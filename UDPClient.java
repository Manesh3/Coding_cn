import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            // Create a socket to send and receive data
            socket = new DatagramSocket();
            
            String message = "Hello";
            byte[] buffer = message.getBytes();
            
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 65432;
            
            // Create a packet to send data
            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
            System.out.println("Sending message: " + message);
            socket.send(sendPacket);
            
            // Create a buffer to receive response
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            
            // Wait for the response
            socket.receive(receivePacket);
            String receivedResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received response: " + receivedResponse);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
