public class MainApp_test {

    public static void main(String args[]){

        /** generateTunableHeteroGraph 확인용 **/
        GraphGenerator gg = new GraphGenerator();
        Graph graph;

        for(int i=0; i<7; i++){
            double q = 0.15*i;

            graph = gg.generateTunableHeteroGraph(4,1000, q);

            //probability & clustering coefficient
            System.out.println("q&cc: "+q+" "+Util.calculateCC(graph));
        }



    }

}
