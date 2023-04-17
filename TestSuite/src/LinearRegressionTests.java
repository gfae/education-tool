import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LinearRegressionTest {

    static LinearRegression lr;
    static ArrayList<Integer> xCords;
    static ArrayList<Integer> yCords;

    @BeforeAll
    public static void beforeAll() throws Exception {
        System.out.println("Setting up test class");
        xCords = new ArrayList<>();
        yCords = new ArrayList<>();

        xCords.add(1);
        xCords.add(2);
        xCords.add(3);
        xCords.add(4);
        xCords.add(5);

        yCords.add(2);
        yCords.add(4);
        yCords.add(6);
        yCords.add(8);
        yCords.add(10);

        lr = new LinearRegression(xCords, yCords);
    }

    @Test
    public void testMeanOfPoints() {
        System.out.println("Testing meanOfPoints method");
        assertEquals(3.0, lr.meanOfPoints(xCords), 0.0);
    }

    @Test
    public void testGradient() {
        System.out.println("Testing gradient method");
        assertEquals(2.0, lr.gradient(), 0.0);
    }

    @Test
    public void testGetLinearCords() {
        System.out.println("Testing getLinearCords method");
        int[][] expectedCords = {{0, 0}, {100, 200}};
        assertArrayEquals(expectedCords, lr.getLinearCords());
    }
}