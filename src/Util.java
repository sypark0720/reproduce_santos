import java.util.ArrayList;
import java.util.List;

public class Util {

    public static double[] sumArray (double[] accmulatedArray, double[] targetArray){
        for(int i=0; i<accmulatedArray.length; i++)
            accmulatedArray[i] += targetArray[i];
        return accmulatedArray;
    }

    public static double[] averageArray(double[] array, int num){
        double[] newArray = new double[array.length];
        for(int i=0; i<array.length; i++){
            newArray[i] = array[i]/num;
        }
        return newArray;
    }


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


    public static List<Integer> getNeighbors(int index, List<List<Integer>> adjacencyList){
        List<Integer> neighbors = adjacencyList.get(index);

        List<Integer> newList = new ArrayList<>();
        for(int i=0; i<neighbors.size(); i++){
            newList.add(neighbors.get(i));
        }

        return newList;
    }

    public static boolean isAdjacent(int index1, int index2, List<List<Integer>> adjacencyList){
        List<Integer> neighborOfIndex1 = adjacencyList.get(index1);
        if(neighborOfIndex1.contains(index2)) return true;
        else return false;
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
            text += array[i]+" ";
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

    //get clustering coefficient
    public static double calculateCC(Graph graph){

        List<List<Integer>> adjacentList = graph.getAdjacencyList();
        int nNodes = graph.getnNode();

        double clusterCoef = 0;
        List<Integer> neighbors;

        for (int index=0; index<nNodes; index++)
        {
            neighbors = Util.getNeighbors(index, adjacentList);
            int degree = neighbors.size();
            int sum = 0;

//            System.out.println("degree: " + degree);
            if(degree > 1){

                for (int i=0; i<degree; i++)
                    for (int j=i; j<degree; j++)
                    {
                        if(Util.isAdjacent(neighbors.get(i), neighbors.get(j), adjacentList)) sum++;
                    }
//                System.out.println("120: "+ sum);
                clusterCoef += sum / ( 0.5*(degree*(degree-1)) );
//                System.out.println("121: "+ clusterCoef);
            }
        }
        return clusterCoef/nNodes;
    }
}
