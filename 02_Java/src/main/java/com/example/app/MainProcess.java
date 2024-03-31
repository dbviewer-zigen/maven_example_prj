package com.example.app;

import java.io.*;
import java.nio.channels.*;
import java.nio.file.*;

public class MainProcess {
    public static void main(String[] args) {
        try {
            // Create a named pipe
            String pipeName = "/tmp/my_pipe";
            Files.deleteIfExists(Paths.get(pipeName)); // Ensure the pipe does not already exist
            Files.createFile(Paths.get(pipeName));

            // Start the child process
            ProcessBuilder processBuilder = new ProcessBuilder("java", "ChildProcess", pipeName);
            Process childProcess = processBuilder.start();

            // Open the named pipe for writing
            Pipe pipe = Pipe.open(Paths.get(pipeName), StandardOpenOption.WRITE);
            Pipe.SinkChannel sinkChannel = pipe.sink();

            // Write data to the named pipe
            String data = "123";
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(data.getBytes());
            buffer.flip();
            sinkChannel.write(buffer);

            // Close the named pipe
            sinkChannel.close();

            // Wait for the child process to exit
            int exitCode = childProcess.waitFor();
            if (exitCode != 0) {
                System.err.println("Child process exited with error: " + exitCode);
                System.exit(1);
            }

            // Read data size from the named pipe
            Pipe.SourceChannel sourceChannel = pipe.source();
            buffer.clear();
            int dataSize = sourceChannel.read(buffer);
            buffer.flip();
            int length = buffer.getInt();

            // Print the data size received from the child process
            System.out.println("Data size received from child process: " + length);

            // Close resources
            sourceChannel.close();

            // Delete the named pipe
            Files.deleteIfExists(Paths.get(pipeName));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
