// MainProcess.java

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

public class MainProcess {
    public static void main(String[] args) {
        try {

            // プロセスの実行時間の計測
            long startTime = System.currentTimeMillis();
            // 子プロセスの事前起動
            ProcessBuilder builder = new ProcessBuilder("java", "ChildProcess", "6"); // 第一引数はデータの長さ
            builder.redirectErrorStream(true);
            Process childProcess = builder.start();
            System.out.println("Child process start.");

            // ソケット通信のセットアップ
            ServerSocket serverSocket = new ServerSocket(12345); // ポート番号は適宜変更してください

            // クライアントからの接続待機
            Socket clientSocket = serverSocket.accept();

            // クライアントからのデータ受信
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputData = in.readLine();

            // 子プロセスへのデータ送信
            OutputStream out = childProcess.getOutputStream();
            out.write(inputData.getBytes());
            out.flush();

            // 子プロセスからのデータ受信
            InputStream childIn = childProcess.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(childIn));
            String encodedData = reader.readLine();
            reader.close();

            // base64デコード
            byte[] decodedBytes = Base64.getDecoder().decode(encodedData);

            // デコードされたデータを文字列に変換
            String decodedString = new String(decodedBytes);

            // デコードされたデータの出力
            System.out.println("Decoded Data: " + decodedString);

            // ソケットおよびストリームのクローズ
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();

            // 子プロセスの終了を待つ
            int exitCode = childProcess.waitFor();

            // 子プロセスの終了コードを確認
            if (exitCode == 0) {
                // 子プロセスが正常に終了した場合の処理
                System.out.println("Child process exited successfully.");
            } else {
                // 子プロセスが異常終了した場合の処理
                System.err.println("Child process exited with error code: " + exitCode);
            }
            // プロセスの実行時間を標準出力に出力
            long endTime = System.currentTimeMillis();
            System.out.println("Process execution time: " + (endTime - startTime) + " milliseconds.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
