import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.util.concurrent.TimeUnit;
public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("jdk.lang.Process.launchMechanism:"+System.getProperty("jdk.lang.Process.launchMechanism"));

            // プロセスの実行時間の計測
            long startTime = System.currentTimeMillis();

            // 送信データの準備
            String inputData = "Hello, world!";
            int length = inputData.length();
            System.out.println("データサイズ：" + String.valueOf(length));

            // コマンドと引数を文字列の配列として指定する
            String[] command = {"./test", String.valueOf(length)};

            // Cプログラムの実行
           ProcessBuilder processBuilder = new ProcessBuilder(command);

            // Start the process
            Process childProcess = processBuilder.start();

            // 子プロセスのOutputStreamを取得する
            OutputStream outputStream = childProcess.getOutputStream();

            // BufferedWriterを作成してOutputStreamに書き込む
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(inputData);
            writer.newLine();
            writer.flush();
            writer.close();

            // // 親プロセスから子プロセスへのデータ送信
            // OutputStream out = childProcess.getOutputStream();
            // out.write(inputData.getBytes());
            // out.flush();
            // out.close();

            // 子プロセスからのデータ受信
            InputStream in = childProcess.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String resultData = reader.readLine();
            System.out.println("resultData Data: " + resultData);
            reader.close();


            // // Create a BufferedWriter to write to the OutputStream
            // BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

            // // Write "Hello, world!" to the child process
            // writer.write(inputData);
            // // writer.newLine(); // Add a newline character to signify the end of the line
            // writer.flush(); // Flush the buffer to ensure the data is sent

            // // Close the writer
            // writer.close();

            // Thread timeoutThread = new Thread(() -> {
            //     try {
            //         // Wait for the process to exit or timeout after 5 seconds
            //         if (!childProcess.waitFor(5, TimeUnit.SECONDS)) {
            //             // If the process did not exit within 5 seconds, destroy it forcibly
            //             childProcess.destroy();
            //         }
            //     } catch (InterruptedException e) {
            //         e.printStackTrace();
            //     }
            // });
            // timeoutThread.start();

            // Process p = Runtime.getRuntime().exec("./test", params);
            // 子プロセスの標準出力および標準エラー出力を入力するスレッドを起動
            // 出力ストリーム
            // new StreamThread(p.getInputStream(), "OUTPUT").start();
            // エラーストリーム
            new StreamThread(childProcess.getErrorStream(), "ERROR").start();

            // p.waitFor();  // 子プロセスの終了を待つ
            // p.destroy();  // 子プロセスを明示的に終了させ、資源を回収できるようにする
            // 標準入力へのデータ送信
            // process.getOutputStream().write(inputData.getBytes());
            // process.getOutputStream().flush();
            // process.getOutputStream().close();

            // プロセス実行結果を取得
            int result = childProcess.waitFor();// 子プロセスの終了を待つ

            // プロセスの実行時間を標準出力に出力
            long endTime = System.currentTimeMillis();
            System.out.println("Process execution time: " + (endTime - startTime) + " milliseconds result:" + result);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

       /**
     *子プロセスの出力ストリームから入力し、ファイルに出力するスレッド
     */
    static class StreamThread extends Thread {
        private InputStream in;
        private String type;

        public StreamThread(InputStream in, String type) {
            this.in = in;
            this.type = type;
        }
        @Override
        public void run() {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    // ログなど出力する
                    System.out.println(type + ">" + line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                try{in.close();} catch(IOException ex){}
            }
        }

    }
}

