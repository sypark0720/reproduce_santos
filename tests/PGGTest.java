import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PGGTest {

    public static GraphGenerator gg;

    @BeforeClass
    public static void makeInstance()throws Exception  {
        gg = new GraphGenerator();
    }

    @Test
    public void participateAllPGGs() throws Exception {

    }

    @Test
    public void participateAllLimitedPGGs() throws Exception {

    }

    @Test
    public void participatePGG() throws Exception {

        double expected = -0.5;

        List<List<Integer>> adjacencyList = Util.generateDoubleList(4);
        adjacencyList.get(0).add(1);
        adjacencyList.get(0).add(2);
        adjacencyList.get(0).add(3);

        adjacencyList.get(1).add(0);
        adjacencyList.get(2).add(0);
        adjacencyList.get(3).add(0);

        double actual = PGG.participatePGG(1,0,
                new Graph(4,adjacencyList, new int[]{1,1,1,3}, new int[]{1,1,0,0}, new double[]{0,0,0,0}), 1);

        assertEquals(expected, actual, 0.0000000000000001);
    }

    @Test
    public void participateLimitedPGG() throws Exception {

    }


}