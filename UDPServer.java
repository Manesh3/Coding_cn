import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            // Create a socket to listen on the port.
            socket = new DatagramSocket(65432);
            System.out.println("Server is up and listening on port 65432");
            
            byte[] receiveBuffer = new byte[1024];
            
            while (true) {
                // Create a packet to receive data into the buffer
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                
                // Wait to receive a packet
                socket.receive(receivePacket);
                String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                
                System.out.println("Received message: " + receivedData);
                
                // Prepare and send an acknowledgment
                String response = "Hello received";
                byte[] responseData = response.getBytes();
                
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);
                socket.send(responsePacket);
                
                System.out.println("Sent acknowledgment to " + clientAddress + ":" + clientPort);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
