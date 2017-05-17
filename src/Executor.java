public class Executor {

    public double[] roundAccRate;
    public double[] roundAvgRate;
    public double[] graphAccRate;
    public double[] graphAvgRate;

    public double[] execute(Graph originGraph){

        //IN ONE GRAPH - 하나의 그래프당 n번 처음 strategy를 변화시켜가며 rate 측정
        graphAccRate= new double[Param.nSample];

        for(int run=0; run < Param.runsPerGraph; run++){
            originGraph.initializeStrategy(Param.rc);

            //IN ONE ROUND - r을 변화시켜가며 cooperator의 rate측정
            roundAccRate = new double[Param.nSample];

            for (int rOrder = 0; rOrder < Param.nSample; rOrder++) {

                double r = Param.rStart + Param.rInterval * rOrder;

                //IN ONE r VALUE
                Graph graph = null;
                try {
                    graph = originGraph.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                //periods만큼 진행
                for (int period = 0; period < Param.periods; period++) {
                    graph = PGG.round(graph, r);
                    graph = Evolution.evolve(graph, r);
                    graph.initializePayoff();
                }

                //그 후 n번의 periods를 평균
                for (int period = 0; period < Param.periodsAvged; period++){
                    graph = PGG.round(graph, r);
                    graph = Evolution.evolve(graph, r);
                    graph.initializePayoff();
                    roundAccRate[rOrder] += Util.getFracOfCoop(graph);
                }
            }

            roundAvgRate = Util.averageArray(roundAccRate, Param.periodsAvged);
            graphAccRate = Util.sumArray(graphAccRate, roundAvgRate);
        }

        graphAvgRate = Util.averageArray(graphAccRate, Param.runsPerGraph);
        return graphAvgRate;
    }

    public double[] executeLimited(Graph originGraph){

        //IN ONE GRAPH - 하나의 그래프당 n번 처음 strategy를 변화시켜가며 rate 측정
        graphAccRate= new double[Param.nSample];

        for(int run=0; run < Param.runsPerGraph; run++){
            originGraph.initializeStrategy(Param.rc);

            //IN ONE ROUND - r을 변화시켜가며 cooperator의 rate측정
            roundAccRate = new double[Param.nSample];

            for (int rOrder = 0; rOrder < Param.nSample; rOrder++) {

                double r = Param.rStart + Param.rInterval * rOrder;

                //IN ONE r VALUE
                Graph graph = null;
                try {
                    graph = originGraph.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                //periods만큼 진행
                for (int period = 0; period < Param.periods; period++) {
                    graph = PGG.roundLimited(graph, r);
                    graph = Evolution.evolveLimited(graph, r);
                    graph.initializePayoff();
                }

                //그 후 n번의 periods를 평균
                for (int period = 0; period < Param.periodsAvged; period++){
                    graph = PGG.roundLimited(graph, r);
                    graph = Evolution.evolveLimited(graph, r);
                    graph.initializePayoff();
                    roundAccRate[rOrder] += Util.getFracOfCoop(graph);
                }
            }

            roundAvgRate = Util.averageArray(roundAccRate, Param.periodsAvged);
            graphAccRate = Util.sumArray(graphAccRate, roundAvgRate);
        }

        graphAvgRate = Util.averageArray(graphAccRate, Param.runsPerGraph);
        return graphAvgRate;
    }
}
