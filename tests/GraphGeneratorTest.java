import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GraphGeneratorTest {

    public static GraphGenerator gg;

    @BeforeClass
    public static void makeInstance()throws Exception  {
        gg = new GraphGenerator();
    }

    @Test
    public void insertEdge() throws Exception {
        List<List<Integer>> expected = new ArrayList<>();
        expected .add(new ArrayList<>());
        expected .add(new ArrayList<>());
        expected .get(0).add(1);

        List<List<Integer>> actual = Util.generateDoubleList(2);
        actual = gg.insertEdge(0,1,expected);

        assertEquals(expected , actual);
    }

    @Test
    public void makeStar() throws Exception {

        List<List<Integer>> expectedAdjacencyList = Util.generateDoubleList(6);
        expectedAdjacencyList.get(0).add(3);
        expectedAdjacencyList.get(1).add(3);
        expectedAdjacencyList.get(2).add(3);
        expectedAdjacencyList.get(3).add(0);
        expectedAdjacencyList.get(3).add(1);
        expectedAdjacencyList.get(3).add(2);

        int[] expectedDegree = {1,1,1,3,0,0};


        Graph actual = gg.makeStar(6,3);
        assertEquals(expectedAdjacencyList, actual.getAdjacencyList());
        assertArrayEquals(expectedDegree, actual.getDegree());
    }

    @Test
    public void generateHomoGraph() throws Exception {
        Graph graph = gg.generateHomoGraph(4, 250);
        System.out.println(Util.arrayToString(graph.getDegree()));
    }

    @Test
    public void generateHeteroGraph() throws Exception {
        Graph graph = gg.generateHeteroGraph(4, 250);
        System.out.println(Util.arrayToString(graph.getDegree()));
    }


    @Test
    public void initializeStrategy() throws Exception {
        Graph graph = gg.generateHeteroGraph(4,250);
        assertEquals(125 , graph.initializeStrategy(0.5));
    }

}