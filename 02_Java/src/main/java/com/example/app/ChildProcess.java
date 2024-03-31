package com.example.app;
import java.io.*;
import java.nio.channels.*;
import java.nio.file.*;

public class ChildProcess {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java ChildProcess <pipeName>");
            System.exit(1);
        }

        String pipeName = args[0];
        try {
            // Open the named pipe for reading
            Pipe pipe = Pipe.open(Paths.get(pipeName), StandardOpenOption.READ);
            Pipe.SourceChannel sourceChannel = pipe.source();

            // Read data from the named pipe
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = sourceChannel.read(buffer);
            if (bytesRead == -1) {
                System.err.println("No data received");
                System.exit(1);
            }

            // Calculate data size
            int dataSize = bytesRead;

            // Close the named pipe
            sourceChannel.close();

            // Write data size to the named pipe
            Pipe.SinkChannel sinkChannel = pipe.sink();
            buffer.clear();
            buffer.putInt(dataSize);
            buffer.flip();
            sinkChannel.write(buffer);

            // Close resources
            sinkChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
