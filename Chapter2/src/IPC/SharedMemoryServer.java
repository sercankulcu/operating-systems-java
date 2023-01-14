package IPC;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/*
 * In Java, inter-process communication (IPC) using shared memory can be achieved using the 
 * MappedByteBuffer class from the java.nio package. The MappedByteBuffer class allows you to 
 * map a region of a file to memory and access it as if it were an array of bytes. This can be 
 * used to create shared memory that can be accessed by multiple processes. 
 * 
 * Here is an example of a Java program that demonstrates how to use shared memory for IPC 
 * between two separate processes:
 * 
 * */

public class SharedMemoryServer {
    public static void main(String[] args) {
        try (RandomAccessFile memoryFile = new RandomAccessFile("shared_memory.bin", "rw")) {
            FileChannel fc = memoryFile.getChannel();
            MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
            buffer.put("Hello, client!".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
