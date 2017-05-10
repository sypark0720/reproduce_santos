import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UtilTest {

    public static GraphGenerator gg;

    @BeforeClass
    public static void makeInstance()throws Exception {
        gg = new GraphGenerator();
    }

    @Test
    public void getNeighbors() throws Exception {

        Graph homo = gg.generateHomoGraph(4, 10);

        List<Integer> expected = new ArrayList<>();
        expected.add(8);
        expected.add(9);
        expected.add(1);
        expected.add(2);

        List<Integer> actual = Util.getNeighbors(0, homo);

        assertEquals(expected, actual);
    }

    @Test
    public void getFracOfCoop() throws Exception {

        Graph hetero = gg.generateHeteroGraph(4,250);
        int nCoops = hetero.initializeStrategy( 0.5);
        double expected = (double) nCoops/250;

        assertEquals(expected, Util.getFracOfCoop(hetero), 0.00000000000000001);

    }

    @Test
    public void maxMinDiff() throws Exception {
        double payoff[] = {1,2,3,4,5,6,7,8};

        assertEquals(7, Util.maxMinDiff(8, payoff), 0.00000000000000001);

    }
}