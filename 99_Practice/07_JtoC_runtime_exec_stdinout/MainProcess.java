// MainProcess.java

import java.io.*;
import java.util.Base64;

public class MainProcess {
    public static void main(String[] args) {
        try {
            System.out.println("jdk.lang.Process.launchMechanism:"+System.getProperty("jdk.lang.Process.launchMechanism"));


            // プロセスの実行時間の計測
            long startTime = System.currentTimeMillis();

            // 子プロセスの起動
            ProcessBuilder builder = new ProcessBuilder("./test"); // 第一引数はデータの長さ
            builder.redirectErrorStream(true);
            Process childProcess = builder.start();

            // 親プロセスから子プロセスへのデータ送信
            // OutputStream out = childProcess.getOutputStream();
            // out.write("123ABC".getBytes());
            // out.flush();
            // out.close();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(childProcess.getOutputStream()));
            String inputData = "ABCDEFG";
            System.out.println("Request Data: " + inputData);
            writer.write(inputData);
            writer.newLine();
            writer.flush();
            writer.close();


            // 子プロセスからのデータ受信
            InputStream in = childProcess.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String encodedData = reader.readLine();
            System.out.println("Response Data: " + encodedData);
            reader.close();

            // base64デコード
            // byte[] decodedBytes = Base64.getDecoder().decode(encodedData);

            // デコードされたデータを文字列に変換
            // String decodedString = new String(decodedBytes);

            // デコードされたデータの出力
            // System.out.println("Decoded Data: " + decodedString);

            // プロセス実行結果を取得
            int result = childProcess.waitFor();// 子プロセスの終了を待つ

            // プロセスの実行時間を標準出力に出力
            long endTime = System.currentTimeMillis();
            System.out.println("Process execution time: " + (endTime - startTime) + " milliseconds result:" + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
