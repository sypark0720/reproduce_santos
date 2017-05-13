public class MainApp {

    public static void main(String[] args) {

        double[] rArray = new double[Param.nSample];
        double[] homoWholeAccRate = new double[Param.nSample];
        double[] homoLimitedWholeAccRate = new double[Param.nSample];
        double[] heteroWholeAccRate = new double[Param.nSample];
        double[] heteroLimitedWholeAccRate = new double[Param.nSample];

        double[] graphAvgRate;

        GraphGenerator gg = new GraphGenerator();
        Executor executor = new Executor();

        //rArray
        for (int rOrder = 0; rOrder < Param.nSample; rOrder++) {
            double r = Param.rStart + Param.rInterval * rOrder;
            rArray[rOrder] = r;
        }

        //IN WHOLE PROCESS - 총 n개의 그래프에 대해서 실행
        for (int num=0; num < Param.realization; num++){

            //HOMOGENEOUS GRAPH
            Graph homoGraph = gg.generateHomoGraph(Param.m0, Param.nNodes);

            graphAvgRate = executor.execute(homoGraph);
            homoWholeAccRate = Util.sumArray(homoWholeAccRate, graphAvgRate);

            graphAvgRate = executor.executeLimited(homoGraph);
            homoLimitedWholeAccRate = Util.sumArray(homoLimitedWholeAccRate, graphAvgRate);

            //HETEROGENEOUS GRAPH
            Graph heteroGraph = gg.generateHeteroGraph(Param.m0, Param.nNodes);

            graphAvgRate = executor.execute(heteroGraph);
            heteroWholeAccRate = Util.sumArray(heteroWholeAccRate, graphAvgRate);

            graphAvgRate = executor.executeLimited(heteroGraph);
            heteroLimitedWholeAccRate = Util.sumArray(heteroLimitedWholeAccRate, graphAvgRate);
        }

        System.out.println("r_val___: "+Util.arrayToString(rArray));
        System.out.println("homo____: "+Util.arrayToString(Util.averageArray(homoWholeAccRate, Param.realization)));
        System.out.println("homo_lim: "+Util.arrayToString(Util.averageArray(homoLimitedWholeAccRate, Param.realization)));
        System.out.println("hete____: "+Util.arrayToString(Util.averageArray(heteroWholeAccRate, Param.realization)));
        System.out.println("hete_lim: "+Util.arrayToString(Util.averageArray(heteroLimitedWholeAccRate, Param.realization)));
    }
}
