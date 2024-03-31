import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Hello3 {
    public native boolean callC(String pipeName, ByteBuffer inputData, ByteBuffer outputData);

    static {
        System.loadLibrary("hello3");
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("[Java] start");
        if (args.length != 1) {
            System.out.println("Usage: java Hello3 <pipe_name>");
            return;
        }        
        
        long start = System.currentTimeMillis();
        String pipeName = args[0];

        try{
            Hello3 hello = new Hello3();
            ByteBuffer inputData = ByteBuffer.wrap("Input data from Java".getBytes());
            System.out.println("ByteBuffe:" + inputData.limit());

            ByteBuffer outputData = ByteBuffer.allocateDirect(1024);
            boolean b = hello.callC(pipeName, inputData, outputData);
            System.out.println("result " + b);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            System.out.println("[Java] end");
            System.out.println("[java] response time :" + (System.currentTimeMillis()-start) + "ms");
        }


    }
}
