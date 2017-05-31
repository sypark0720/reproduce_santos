import java.util.List;

public class MainApp_test {

    public static void main(String args[]){

        GraphGenerator gg = new GraphGenerator();

        for(int n=0; n<6; n++){
            double q = 0.15*n;

            Graph graph = gg.generateTunableHeteroGraph(6,2000, 0.6);

    //        System.out.println(graph.toString());

            double gcc = 0;
            for (int index=0; index < graph.getnNode(); index++){

                List<Integer> neighbors = Util.getNeighbors(index, graph);

                if(neighbors.size() > 1){
                    int sum=0;

                    for(int i=0; i<neighbors.size()-1; i++){
                        for(int j=i+1; j<neighbors.size(); j++){
                            if(Util.isAdjacent(neighbors.get(i), neighbors.get(j), graph.getAdjacencyList())) sum++;
                        }
                    }

                    double lcc = sum/(neighbors.size()*(neighbors.size()-1)/2);
                    gcc += lcc;
                }
            }

            System.out.println("probability:"+q);
            System.out.println("clusting coefficient:"+gcc/graph.getnNode());
        }

    }
}
