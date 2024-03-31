// ChildProcess.java

import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class ChildProcess {
    public static void main(String[] args) {
        try {
            // ソケット通信のセットアップ
            Socket socket = new Socket("localhost", 12345); // 接続先のホストとポート番号を適宜変更してください

            // データの受信
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputData = in.readLine();

            // 受信したデータをbase64エンコード
            String encodedData = Base64.getEncoder().encodeToString(inputData.getBytes());

            // エンコードされたデータを親プロセスに送信
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(encodedData);

            // ソケットおよびストリームのクローズ
            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
