import java.util.List;
import java.util.Random;


public class Evolution {

    public static Graph evolve(Graph graph, double r){

//        System.out.println("---------- EVOLUTION GRAPH--------------- ");
//        System.out.println(graph.toString());

        Random rand = new Random();
        double[] payoff = graph.getPayoff();
        int[] strategy = graph.getStrategy();
//        System.out.println("ORG STRATEGY: "+Util.arrayToString(strategy));
        int[] newStrategy = strategy.clone();
//        System.out.println("NEW STRATEGY: "+Util.arrayToString(newStrategy)+"(should be same as STRATEGY)");


        for(int index=0; index<graph.getnNode(); index++){

            //select neighbor randomly
            List<Integer> neighbors = Util.getNeighbors(index, graph);
            int selected = neighbors.get(rand.nextInt(neighbors.size()));

//            System.out.println("----------EVOLUTION index: "+ index + "------------------------");
//            System.out.println("neighbors: "+neighbors);
//            System.out.println("selected: "+selected);
//            System.out.println("payoff of index: "+payoff[index]);
//            System.out.println("payoff of selected: "+payoff[selected]);

            if(payoff[index] >= payoff[selected]) {
//                System.out.println("payoff of index is bigger. no change");
//                System.out.println("STRATEGY: "+Util.arrayToString(newStrategy));
                continue;}
            else{
                float probe = rand.nextFloat();
//                System.out.println("probe: "+probe);

                double M = getM(selected, index ,graph, r);
//                System.out.println("M: "+M);
//                System.out.println("difference/M: "+ (payoff[selected] - payoff[index])/M);

                if( (payoff[selected] - payoff[index])/M >= probe){
                    newStrategy[index] = strategy[selected];
//                    System.out.println("changed");
//                    System.out.println("NEW STRATEGY: "+Util.arrayToString(newStrategy));
                }else {
//                    System.out.println("probe is bigger. no change");
                }
            }
        }

//        System.out.println("----------ALL UPDATED-------------------------------=");
//        System.out.println("ALL UPDATED. ORG STRATEGY: "+ Util.arrayToString(graph.getStrategy()));
//        System.out.println("ALL UPDATED. NEW STRATEGY: "+ Util.arrayToString(newStrategy));
        graph.setStrategy(newStrategy);
//        System.out.println("UPDAT GRAPH. NEW STRATEGY: "+Util.arrayToString(graph.getStrategy())+"(should be same as new strategy)");
        return graph;
    }

    //max.possible payoff of y - min.possible payoff of x.
    public static double getM(int y, int x, Graph graph, double r){
//        System.out.println("------GET M-----");
        return getMaxPayoff(y, graph, r) - getMinPayoff(x, graph, r);
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

//        System.out.println("---NEW STRATEGY:"+Util.arrayToString(newStrategy));

        double payoff =0;
        try {
            Graph maxGraph = graph.clone();
            maxGraph.setStrategy(newStrategy);
            payoff = PGG.participateAllPGGs(index, maxGraph, r);

        }catch (Exception e){
            e.printStackTrace();
        }

//        System.out.println("---MAX PAYOFF:"+payoff);
        return payoff;
    }

    //min.possible payoff of a node: surrounded by all defectors
    public static double getMinPayoff(int index, Graph graph, double r){

//        System.out.println("GET MIN PAYOFF");
//        System.out.println("updated:"+index);
        //create new Strategy array which is all defectors except index node.
        int indexStrategy = graph.getStrategy()[index];
        int[] newStrategy = new int[graph.getnNode()];
        newStrategy[index] = indexStrategy;

//        System.out.println("---NEW STRATEGY:"+Util.arrayToString(newStrategy));

        double payoff =0;
        try {
            Graph minGraph = graph.clone();
            minGraph.setStrategy(newStrategy);
            payoff = PGG.participateAllPGGs(index, minGraph, r);

        }catch (Exception e){
            e.printStackTrace();
        }

//        System.out.println("---MIN PAYOFF:"+payoff);
        return payoff;
    }

}
