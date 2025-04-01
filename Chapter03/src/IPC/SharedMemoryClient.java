package IPC;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/*
 * here is an example of the client process that demonstrates how to use shared memory for IPC 
 * in Java:
 * 
 * In this example, the client process opens the shared memory file "shared_memory.bin" using a 
 * RandomAccessFile object and maps a region of it to memory using the MappedByteBuffer class. 
 * It then reads the data from the buffer and converts it to a string which prints out the 
 * message from the server.
 * */

public class SharedMemoryClient {
	
    public static void main(String[] args) {
    	
        try (RandomAccessFile memoryFile = new RandomAccessFile("shared_memory.bin", "rw")) {
            FileChannel fc = memoryFile.getChannel();
            MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, 1024);
            byte[] bytes = new byte[buffer.limit()];
            buffer.get(bytes);
            String message = new String(bytes);
            System.out.println("Server says: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
