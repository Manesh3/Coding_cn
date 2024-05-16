import java.io.*;
import java.net.*;

public class Receiver {

    private static final int PACKET_SIZE = 1024;
    private static final int PORT = 12345;

    public static void main(String[] args) {
        // Create UDP socket
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            // Receive script file
            receiveFile(socket, "received_script.txt");
            // Receive text file
            receiveFile(socket, "received_text.txt");
            // Receive audio file
            receiveFile(socket, "received_audio.mp3");
            // Receive video file
            receiveFile(socket, "received_video.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveFile(DatagramSocket socket, String filename) throws IOException {
        File file = new File(filename);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        byte[] buffer = new byte[PACKET_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        boolean fileEnded = false;
        while (!fileEnded) {
            socket.receive(packet);
            if (packet.getLength() == 0) {
                fileEnded = true;
            } else {
                fileOutputStream.write(buffer, 0, packet.getLength());
            }
        }

        fileOutputStream.close();
        System.out.println("File " + filename + " received successfully.");
    }
}
