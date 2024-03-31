import java.nio.ByteBuffer;

public class Main {

    private native boolean executeNativeProcess(ByteBuffer inputData, ByteBuffer outputData);

    static {

        System.loadLibrary("nativeprocess");
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis(); // after create instance

        ByteBuffer inputData = ByteBuffer.allocateDirect(1024);
        ByteBuffer outputData = ByteBuffer.allocateDirect(1024);

        String input = "ABCDEFG";
        System.out.println("[java] Request Data : " + input);
        inputData.put(input.getBytes());
        inputData.flip(); 

        Main main = new Main();
        boolean result = main.executeNativeProcess(inputData, outputData);
        byte[] bytes = new byte[4];
        String value = null;
        if (result) {
            // System.out.println("[java] outputData.limit() --> " + outputData.limit());
            // System.out.println("[java] outputData.remaining() --> " + outputData.remaining());
            //outputData.flip();//
            // byte[] bytes = new byte[outputData.remaining()];
            // StringBuilder hexString = new StringBuilder();
            // while (outputData.hasRemaining()) {
            //     int b = outputData.get() & 0xFF;
            //     hexString.append(String.format("%02X:", b)); 
            // }
            // System.out.println(hexString.toString());
            // byte[] bytes = new byte[1024];
            // outputData.get(bytes);
            // System.out.println("[java] stdout --> " + new String(bytes));
            
            outputData.get(bytes);
            value = new String(bytes);

        } else {
            System.err.println("[java] Failed to execute native process.");
        }
        System.out.println("[java] Response Data :" + value);    // 50ms
        // System.out.println("[Java] end");
        long end = System.currentTimeMillis(); // after create instance
        System.out.println("[java] response time :" + (end-start) + "ms");

    }
}