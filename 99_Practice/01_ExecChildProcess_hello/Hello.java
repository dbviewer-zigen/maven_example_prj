import java.io.*;

public class Hello {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("jdk.lang.Process.launchMechanism:"+System.getProperty("jdk.lang.Process.launchMechanism"));

        long start = System.currentTimeMillis();
        String cProgramPath = "./hello";
      
        Process process = Runtime.getRuntime().exec(cProgramPath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        //System.out.println("C program exited with code: " + exitCode);
        //System.out.println("[java] response time :" + (System.currentTimeMillis()-start) + "ms");
        long end = System.currentTimeMillis(); // after create instance
        System.out.println("[java] response time :" + (end-start) + "ms");

    }
}
