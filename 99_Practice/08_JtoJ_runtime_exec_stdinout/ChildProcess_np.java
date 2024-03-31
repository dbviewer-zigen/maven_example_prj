// ChildProcess.java

import java.io.*;
import java.util.Base64;

public class ChildProcess_np {
    public static void main(String[] args) {
        try {
            // 受け取るデータの長さを引数から取得
            int dataLength = Integer.parseInt(args[0]);

            // // データの読み取り
            // BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            // char[] buffer = new char[dataLength];
            // reader.read(buffer, 0, dataLength);

            // // データのbase64エンコード
            // String encodedData = Base64.getEncoder().encodeToString(new String(buffer).getBytes());

            // エンコードされたデータの出力
            System.out.println("argument:"+args[0]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
