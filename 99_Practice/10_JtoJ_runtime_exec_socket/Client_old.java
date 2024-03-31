import java.io.IOException;
import java.io.OutputStream;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            // プロセスの実行時間の計測
            long startTime = System.currentTimeMillis();

            //1,接続したいサーバーの住所（IP、ポート）
            String serverIp = "localhost";
            int port = 8082;
            //2,接続作成
            socket = new Socket(serverIp, port);


            // プロセスの実行時間の計測
            long startTime2 = System.currentTimeMillis();

            //3,ioストリーム送る
            os = socket.getOutputStream();
            os.write("Hello world".getBytes());


            //Serverからのデータを読み込む
            is = socket.getInputStream();
            
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len=is.read(buffer))!=-1){
                data.write(buffer,0,len);
            }
            System.out.println("Client received " + data.toString());




            // プロセスの実行時間を標準出力に出力
            long endTime2 = System.currentTimeMillis();
            System.out.println("--[write] execution time: " + (endTime2 - startTime2) + " milliseconds");

            // プロセスの実行時間を標準出力に出力
            long endTime = System.currentTimeMillis();
            System.out.println("Client Process execution time: " + (endTime - startTime) + " milliseconds");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4,接続を遮断する
            if(socket !=null)
              socket.close();
            if(os !=null)
              os.close();
            if(is !=null)
              is.close();
        }
    }
}

