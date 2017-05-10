import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Graph implements Cloneable {

    private int nNode;
    private List<List<Integer>> adjacencyList;
    private int[] degree;
    private int[] strategy;
    private double[] payoff;

    public Graph(int nNode, List<List<Integer>> adjacencyList, int[] degree) {
        this.adjacencyList = adjacencyList;
        this.degree = degree;
        this.strategy = new int[nNode];
        this.payoff = new double[nNode];
    }

    public Graph(int nNode,List<List<Integer>> adjacencyList, int[] degree, int[] strategy, double[] payoff) {
        this.nNode = nNode;
        this.adjacencyList = adjacencyList;
        this.degree = degree;
        this.strategy = strategy;
        this.payoff = payoff;
    }


    public void initializePayoff(){
        this.payoff = new double[nNode];
    }

    public int initializeStrategy(double rc){
        int[] newStrategy = new int[this.getnNode()];

        Random rand = new Random();
        int numOfC = (int) (rc*this.getnNode());
        ArrayList<Integer> selected = new ArrayList<>();
        int counter = 0;

        while(counter<numOfC){
            int r = rand.nextInt(this.getnNode());
            if(selected.contains(r)) continue;

            selected.add(r);
            newStrategy[r]=Param.COOPERATOR;
            counter++;
        }

        this.setStrategy(newStrategy);

        return counter;
    }

    public Graph clone() throws CloneNotSupportedException {
        Graph graph = (Graph) super.clone();
        return graph;
    }

    public List<List<Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(List<List<Integer>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public int[] getDegree() {
        return degree;
    }

    public void setDegree(int[] degree) {
        this.degree = degree;
    }

    public int getnNode() {
        return nNode;
    }

    public void setnNode(int nNode) {
        this.nNode = nNode;
    }

    public int[] getStrategy() {
        return strategy;
    }

    public void setStrategy(int[] strategy) {
        this.strategy = strategy;
    }

    public double[] getPayoff() { return payoff; }

    public void setPayoff(double[] payoff) { this.payoff = payoff; }

    @Override
    public String toString() {
        return "Graph{" +
                "\nnNode=" + nNode +
                ",\nadjacencyList=" + adjacencyList +
                ",\ndegree=" + Arrays.toString(degree) +
                ",\nstrategy=" + Arrays.toString(strategy) +
                ",\npayoff=" + Arrays.toString(payoff) +
                '}';
    }
}
