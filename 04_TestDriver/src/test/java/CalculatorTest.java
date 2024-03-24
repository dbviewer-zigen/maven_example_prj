import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.example.app.*;
public class CalculatorTest {

    @Test
    public void testAddition() {
        int result = Calculator.add(3, 5);
        assertEquals(8, result, "3 + 5 should equal 8");

        // ローカルリポジトリからクラスを取得できていることを確認する
        Simple simple = new Simple();
        System.out.println(simple.hello());
        assertEquals("Hello, World!", simple.hello());


    }

    @Test
    public void testSubtraction() {
        int result = Calculator.subtract(10, 4);
        assertEquals(6, result, "10 - 4 should equal 6");
    }
}
