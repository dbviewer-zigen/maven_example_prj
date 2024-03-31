import java.io.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Hello2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        if (args.length != 1) {
            System.out.println("Usage: java Hello2 <pipe_name>");
            return;
        }
        String pipeName = args[0];
        String cProgramPath = "./hello2 " + pipeName;

        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            System.out.print("" + LocalDateTime.now().format(formatter) + " ");
            System.out.println("[java] start execute process");

            // Process execute
            Process process = Runtime.getRuntime().exec(cProgramPath);

            // first step write to named pipe
            FileChannel outputChannel = FileChannel.open(Paths.get(pipeName), StandardOpenOption.WRITE);
            String data = "Hello from Java!";
            ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
            outputChannel.write(buffer);
            System.out.print("" + LocalDateTime.now().format(formatter) + " ");
            System.out.println("[java] Data written to named pipe data = " + data);

            // second step read from named pipe
            FileChannel inputChannel = FileChannel.open(Paths.get(pipeName), StandardOpenOption.READ);
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int bytesRead = inputChannel.read(readBuffer);
            // System.out.println(bytesRead);
            readBuffer.flip();
            if (bytesRead != -1) {
                byte[] readData = new byte[bytesRead];
                readBuffer.get(readData);
                System.out.print("" + LocalDateTime.now().format(formatter) + " ");
                System.out.println("[Java] Data read from named pipe data = " + new String(readData) + ", data size = " + readData.length);
            } else {
                System.out.println("No data read from named pipe.");
            }


            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                System.out.println("[java] === Standard Output ===");
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                
                System.out.println("[java] === Standard Error ===");
                while ((line = errorReader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            System.out.print("" + LocalDateTime.now().format(formatter) + " ");
            System.out.println("[java] C program exited with code: " + exitCode);
            System.out.print("" + LocalDateTime.now().format(formatter) + " ");
            System.out.println("[java] response time :" + (System.currentTimeMillis()-start) + "ms");
            inputChannel.close();
            outputChannel.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }

    }
}
