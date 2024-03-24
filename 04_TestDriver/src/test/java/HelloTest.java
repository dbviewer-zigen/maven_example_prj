import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.example.app.*;

public class HelloTest {

    @Test
    public void testHello() {

        System.loadLibrary("Hello");
        Hello inst = new Hello();
        System.out.println("java start");
        inst.hello();
        System.out.println("java end");

        assertEquals(1, 1);
    
    }

}
