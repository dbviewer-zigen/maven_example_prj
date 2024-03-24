import com.example.app.*;

public class Main {
    public static void main(String[] args) {
        System.loadLibrary("Hello");
        Hello inst = new Hello();
        System.out.println("java start");
        inst.hello();
        System.out.println("java end");
    }
}

