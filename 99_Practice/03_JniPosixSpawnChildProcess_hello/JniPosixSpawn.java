import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class JniPosixSpawn {
    public native boolean callC(String pipeName);

    static {
        System.loadLibrary("jniposixspawn");
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
            JniPosixSpawn hello = new JniPosixSpawn();
            boolean b = hello.callC(pipeName);
            System.out.println("result " + b);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            System.out.println("[Java] end");
            System.out.println("[java] response time :" + (System.currentTimeMillis()-start) + "ms");
        }


    }
}
