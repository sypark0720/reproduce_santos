import java.util.List;

public class PGG {

    //모든 노드에 대해 payoff 계산
    //all cooperators contribute the same cost c per game
    public static Graph round(Graph graph, double r){

        double[] newPayoff = graph.getPayoff().clone();

        for(int i=0; i<graph.getnNode(); i++){
            newPayoff[i] = participateAllPGGs(i, graph, r);
        }

        graph.setPayoff(newPayoff);
        return graph;
    }

    //all cooperators contribute the same cost c per game
    public static Graph roundLimited(Graph graph, double r){
        double[] newPayoff = graph.getPayoff().clone();

        for(int i=0; i<graph.getnNode(); i++){
            newPayoff[i] = participateAllPGGsLimited(i, graph, r);
        }

        graph.setPayoff(newPayoff);
        return graph;
    }

    //모든 node를 y로 두고 그 neighbor를 x로 두고 모든 x에 대해 pgg 계산
    public static double participateAllPGGs(int node, Graph graph, double r){

        List<Integer> neighbors = Util.getNeighbors(node, graph);
        double nodePayoff = 0;

        nodePayoff += participatePGG(node, node, graph, r);

        for (int x=0; x<neighbors.size(); x++) {
            nodePayoff += participatePGG(node, neighbors.get(x), graph, r);
        }

        return nodePayoff;
    }

    public static double participateAllPGGsLimited(int node, Graph graph, double r){

        List<Integer> neighbors = Util.getNeighbors(node, graph);
        double nodePayoff = 0;

        nodePayoff += participatePGGLimited(node, node, graph, r);

        for (int x=0; x<neighbors.size(); x++) {
            nodePayoff += participatePGGLimited(node, neighbors.get(x), graph, r);
        }

        return nodePayoff;
    }

    //중심이 x인 PGG에 y가 참여
    public static double participatePGG(int y, int x, Graph graph, double r){

        //x 미포함.
        List<Integer> xneighbors = Util.getNeighbors(x, graph);
        //x 포함
        xneighbors.add(x);


        int[] strategy = graph.getStrategy();
        int nc = 0;

        //get Nc
        for (int i=0; i<xneighbors.size(); i++){
            int s = xneighbors.get(i);
            nc += strategy[s];
        }

        //y가 cooperator, defector일 때 구분
        double cost = 0;
        if(strategy[y]==Param.COOPERATOR) cost = Param.c;

        return (Param.c)*(r)*nc/(xneighbors.size()) - cost;
    }

    //중심이 x인 PGG에 y가 참여
    public static double participatePGGLimited(int y, int x, Graph graph, double r){

        //x 미포함.
        List<Integer> xneighbors = Util.getNeighbors(x, graph);
        //x 포함
        xneighbors.add(x);

        int[] strategy = graph.getStrategy();
        int[] degree = graph.getDegree();
        double incomes = 0;

        //get incomes
        for (int i=0; i<xneighbors.size(); i++){
            int j = xneighbors.get(i);
            incomes += Param.c*strategy[j]/(degree[j]+1);
        }

        //y가 cooperator, defector일 때 구분
        double cost = 0;
        if(strategy[y]==Param.COOPERATOR) cost = Param.c;

        return (r)*incomes/(degree[x]+1) - cost/(degree[y]+1);
    }
}
