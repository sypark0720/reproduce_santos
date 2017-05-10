import java.util.ArrayList;
import java.util.List;

public class Util {


    public static List<List<Integer>> generateDoubleList(int nNodes){
        List<List<Integer>> doubleList = new ArrayList<>();
        for(int i=0; i<nNodes; i++) doubleList.add(new ArrayList<>());
        return doubleList;
    }


    //고생함... list는 deep copy를 합시다!
    public static List<Integer> getNeighbors(int index, Graph graph){
        List<List<Integer>> mAdjacencyList = graph.getAdjacencyList();
        List<Integer> neighbors = mAdjacencyList.get(index);

        List<Integer> newList = new ArrayList<>();
        for(int i=0; i<neighbors.size(); i++){
            newList.add(neighbors.get(i));
        }

        return newList;
    }

    public static double getFracOfCoop(Graph graph){
        int[] strategy = graph.getStrategy();
        double sum=0;

        for(int i=0;i<graph.getnNode();i++){
            sum += strategy[i];
        }

        return sum/graph.getnNode();
    }


    public static double maxMinDiff(int nNodes, double[] payoff){
        double min = payoff[0];
        double max = payoff[0];

        for(int i=1; i < nNodes; i++){
            if(payoff[i]<min) min = payoff[i];
            if(payoff[i]>max) max = payoff[i];
        }

        return max-min;
    }

    public static String arrayToString(double[] array){
        String text= "";
        for(int i=0; i<array.length; i++){
            text += array[i]+" | ";
        }
        return text;
    }

    public static String arrayToString(int[] array){
        String text= "";
        for(int i=0; i<array.length; i++){
            text += array[i]+" ";
        }
        return text;
    }
}
