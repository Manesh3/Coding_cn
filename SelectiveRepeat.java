import java.util.*;

class Frame {
    int seqNum;
    String data;

    Frame(int seqNum, String data) {
        this.seqNum = seqNum;
        this.data = data;
    }
}

class Sender {
    private int windowSize;
    private int nextSeqNum;
    private int base;
    private int numFrames;
    private List<Frame> frames;
    private Map<Integer, Boolean> ackReceived;

    Sender(int windowSize, List<Frame> frames) {
        this.windowSize = windowSize;
        this.frames = frames;
        this.base = 0;
        this.nextSeqNum = 0;
        this.numFrames = frames.size();
        this.ackReceived = new HashMap<>();
    }

    void sendFrames(Receiver receiver) {
        while (base < numFrames) {
            while (nextSeqNum < base + windowSize && nextSeqNum < numFrames) {
                sendFrame(receiver, frames.get(nextSeqNum));
                nextSeqNum++;
            }
            receiveAcknowledgments(receiver);
            slideWindow();
        }
    }

    private void sendFrame(Receiver receiver, Frame frame) {
        System.out.println("Sender: Sending frame with sequence number " + frame.seqNum);
        receiver.receiveFrame(frame);
    }

    private void receiveAcknowledgments(Receiver receiver) {
        List<Integer> acks = receiver.getAcks();
        for (int ack : acks) {
            if (!ackReceived.containsKey(ack)) {
                System.out.println("Sender: Received acknowledgment for sequence number " + ack);
                ackReceived.put(ack, true);
            }
        }
    }

    private void slideWindow() {
        while (ackReceived.containsKey(base) && ackReceived.get(base)) {
            base++;
        }
    }
}

class Receiver {
    private int windowSize;
    private int expectedSeqNum;
    private Map<Integer, Frame> buffer;
    private List<Integer> acks;

    Receiver(int windowSize) {
        this.windowSize = windowSize;
        this.expectedSeqNum = 0;
        this.buffer = new HashMap<>();
        this.acks = new ArrayList<>();
    }

    void receiveFrame(Frame frame) {
        System.out.println("Receiver: Received frame with sequence number " + frame.seqNum);
        if (frame.seqNum >= expectedSeqNum && frame.seqNum < expectedSeqNum + windowSize) {
            buffer.put(frame.seqNum, frame);
            acks.add(frame.seqNum);
            deliverFrames();
        } else {
            System.out.println("Receiver: Discarding frame with sequence number " + frame.seqNum);
        }
    }

    private void deliverFrames() {
        while (buffer.containsKey(expectedSeqNum)) {
            System.out.println("Receiver: Delivering frame with sequence number " + expectedSeqNum + " and data: " + buffer.get(expectedSeqNum).data);
            buffer.remove(expectedSeqNum);
            expectedSeqNum++;
        }
    }

    List<Integer> getAcks() {
        List<Integer> ackCopy = new ArrayList<>(acks);
        acks.clear();
        return ackCopy;
    }
}

public class SelectiveRepeat {
    public static void main(String[] args) {
        int windowSize = 4;
        List<Frame> frames = Arrays.asList(
                new Frame(0, "Data0"),
                new Frame(1, "Data1"),
                new Frame(2, "Data2"),
                new Frame(3, "Data3"),
                new Frame(4, "Data4"),
                new Frame(5, "Data5"),
                new Frame(6, "Data6"),
                new Frame(7, "Data7")
        );

        Sender sender = new Sender(windowSize, frames);
        Receiver receiver = new Receiver(windowSize);

        sender.sendFrames(receiver);
    }
}
