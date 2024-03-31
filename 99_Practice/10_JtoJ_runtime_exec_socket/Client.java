import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
  public static void main(String[] args) {
    Client client = new Client();
    client.run();
  }

  void run() {

    Socket cSocket = null;
    BufferedReader csInput = null;
    PrintWriter writer = null;
    BufferedReader reader = null;

    try {
      // IPアドレスとポート番号を指定してクライアント側のソケットを作成
      cSocket = new Socket("127.0.0.1", 8083);

      // クライアント側での入力用
      csInput = new BufferedReader(new InputStreamReader(System.in));

      // クライアント側からサーバへの送信用
      writer = new PrintWriter(cSocket.getOutputStream(), true);

      // サーバ側からの受取用
      reader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));

      // 無限ループ byeの入力でループを抜ける
      String line = null;
      while (true) {
        System.out.println("-------------------");
        System.out.println("偶数の数値を入力して下さい");
        System.out.println("-------------------");

        line = csInput.readLine();

        // 送信用の文字を送信
        writer.println(line);

        // byeの入力でループを抜ける
        if (line.equals("bye")) {
          break;
        }

        // サーバ側からの受取の結果を表示
        System.out.println("サーバーからの回答：" + reader.readLine());
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
        if (writer != null) {
          writer.close();
        }
        if (csInput != null) {
          csInput.close();
        }
        if (cSocket != null) {
          cSocket.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println("クライアント側終了です");
    }
  }
}
