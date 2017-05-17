import java.util.List;

public class Evolution {

    public static Graph evolve(Graph graph, double r){

        MersenneTwisterFast rand = new MersenneTwisterFast();
        double[] payoff = graph.getPayoff();
        int[] strategy = graph.getStrategy();
        int[] newStrategy = strategy.clone();


        for(int index=0; index<graph.getnNode(); index++){

            //select neighbor randomly
            List<Integer> neighbors = Util.getNeighbors(index, graph);
            int selected = neighbors.get(rand.nextInt(neighbors.size()));

            if(payoff[index] >= payoff[selected]) {
                continue;}
            else{
                float probe = rand.nextFloat();

                double M = getM(selected, index ,graph, r);

                if( (payoff[selected] - payoff[index])/M >= probe){
                    newStrategy[index] = strategy[selected];
                }else {
                }
            }
        }

        graph.setStrategy(newStrategy);
        return graph;
    }

    public static Graph evolveLimited(Graph graph, double r){

        MersenneTwisterFast rand = new MersenneTwisterFast();
        double[] payoff = graph.getPayoff();
        int[] strategy = graph.getStrategy();
        int[] newStrategy = strategy.clone();


        for(int index=0; index<graph.getnNode(); index++){

            //select neighbor randomly
            List<Integer> neighbors = Util.getNeighbors(index, graph);
            int selected = neighbors.get(rand.nextInt(neighbors.size()));

            if(payoff[index] >= payoff[selected]) {
                continue;}
            else{
                float probe = rand.nextFloat();

                double M = getMLimited(selected, index ,graph, r);

                if( (payoff[selected] - payoff[index])/M >= probe){
                    newStrategy[index] = strategy[selected];
                }else {
                }
            }
        }

        graph.setStrategy(newStrategy);
        return graph;
    }

    //max.possible payoff of y - min.possible payoff of x.
    public static double getM(int y, int x, Graph graph, double r){
        return getMaxPayoff(y, graph, r) - getMinPayoff(x, graph, r);
    }

    public static double getMLimited(int y, int x, Graph graph, double r){
        return getMaxPayoffLimited(y, graph, r) - getMinPayoffLimited(x, graph, r);
    }

    //max.possible payoff of a node: surrounded by all cooperators
    public static double getMaxPayoff(int index, Graph graph, double r){

        //create new Strategy array which is all cooperators except index node.
        int indexStrategy = graph.getStrategy()[index];
        int[] newStrategy = new int[graph.getnNode()];
        for(int i=0; i<newStrategy.length; i++){
            newStrategy[i] = 1;
        }
        newStrategy[index] = indexStrategy;

        double payoff =0;
        try {
            Graph maxGraph = graph.clone();
            maxGraph.setStrategy(newStrategy);
            payoff = PGG.participateAllPGGs(index, maxGraph, r);

        }catch (Exception e){
            e.printStackTrace();
        }

        return payoff;
    }

    public static double getMaxPayoffLimited(int index, Graph graph, double r){

        //create new Strategy array which is all cooperators except index node.
        int indexStrategy = graph.getStrategy()[index];
        int[] newStrategy = new int[graph.getnNode()];
        for(int i=0; i<newStrategy.length; i++){
            newStrategy[i] = 1;
        }
        newStrategy[index] = indexStrategy;

        double payoff =0;
        try {
            Graph maxGraph = graph.clone();
            maxGraph.setStrategy(newStrategy);
            payoff = PGG.participateAllPGGsLimited(index, maxGraph, r);

        }catch (Exception e){
            e.printStackTrace();
        }

        return payoff;
    }

    //min.possible payoff of a node: surrounded by all defectors
    public static double getMinPayoff(int index, Graph graph, double r){

        //create new Strategy array which is all defectors except index node.
        int indexStrategy = graph.getStrategy()[index];
        int[] newStrategy = new int[graph.getnNode()];
        newStrategy[index] = indexStrategy;

        double payoff =0;
        try {
            Graph minGraph = graph.clone();
            minGraph.setStrategy(newStrategy);
            payoff = PGG.participateAllPGGs(index, minGraph, r);

        }catch (Exception e){
            e.printStackTrace();
        }

        return payoff;
    }

    public static double getMinPayoffLimited(int index, Graph graph, double r){

        //create new Strategy array which is all defectors except index node.
        int indexStrategy = graph.getStrategy()[index];
        int[] newStrategy = new int[graph.getnNode()];
        newStrategy[index] = indexStrategy;

        double payoff =0;
        try {
            Graph minGraph = graph.clone();
            minGraph.setStrategy(newStrategy);
            payoff = PGG.participateAllPGGsLimited(index, minGraph, r);

        }catch (Exception e){
            e.printStackTrace();
        }

        return payoff;
    }

}
