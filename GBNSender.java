import java.io.*;
import java.net.*;
import java.util.*;

public class GBNSender {

    private static final int WINDOW_SIZE = 4; // Window size for Go-Back-N protocol
    private static final int PORT = 12345;
    private static final String RECEIVER_IP = "localhost";

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket(RECEIVER_IP, PORT);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        final int[] base = {0};
        final int[] nextSeqNum = {0};
        String[] packets = { "Packet1", "Packet2", "Packet3", "Packet4", "Packet5", "Packet6", "Packet7", "Packet8" };

        Timer timer = new Timer();
        TimerTask timeoutTask = new TimerTask() {
            public void run() {
                try {
                    System.out.println("Timeout! Resending packets from base " + base[0]);
                    for (int i = base[0]; i < nextSeqNum[0]; i++) {
                        out.println(packets[i]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        while (base[0] < packets.length) {
            while (nextSeqNum[0] < base[0] + WINDOW_SIZE && nextSeqNum[0] < packets.length) {
                out.println(packets[nextSeqNum[0]]);
                System.out.println("Sent: " + packets[nextSeqNum[0]]);
                if (base[0] == nextSeqNum[0]) {
                    timer.schedule(timeoutTask, 2000, 2000);
                }
                nextSeqNum[0]++;
            }

            String ack = in.readLine();
            if (ack != null && ack.startsWith("ACK")) {
                int ackNum = Integer.parseInt(ack.split(" ")[1]);
                System.out.println("Received: " + ack);
                if (ackNum >= base[0]) {
                    base[0] = ackNum + 1;
                    timer.cancel();
                    timer = new Timer();
                    timeoutTask = new TimerTask() {
                        public void run() {
                            try {
                                System.out.println("Timeout! Resending packets from base " + base[0]);
                                for (int i = base[0]; i < nextSeqNum[0]; i++) {
                                    out.println(packets[i]);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                }
            }
        }

        out.println("END");
        socket.close();
    }
}
