import java.io.*;
import java.net.*;

public class Sender {

    private static final int PACKET_SIZE = 1024;
    private static final int PORT = 12345;
    private static final String RECEIVER_IP = "192.168.1.13"; // Change this to the IP address of the receiver

    public static void main(String[] args) {
        // Create UDP socket
        try (DatagramSocket socket = new DatagramSocket()) {
            // Send script file
            sendFile(socket, "script.txt");
            // Send text file
            sendFile(socket, "text.txt");
            // Send audio file
            sendFile(socket, "audio.mp3");
            // Send video file
            sendFile(socket, "video.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(DatagramSocket socket, String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File " + filename + " not found.");
            return;
        }

        InetAddress receiverAddress = InetAddress.getByName(RECEIVER_IP);
        byte[] buffer = new byte[PACKET_SIZE];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, receiverAddress, PORT);
                socket.send(packet);
            }
            // Send an empty packet to signal end of file
            DatagramPacket endPacket = new DatagramPacket(new byte[0], 0, receiverAddress, PORT);
            socket.send(endPacket);
            System.out.println("File " + filename + " sent successfully.");
        }
    }
}
