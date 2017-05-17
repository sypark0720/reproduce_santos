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
//            Graph originHomoGraph = gg.generateHomoGraph(Param.m0, Param.nNodes);
//
//            Graph homoGraph = null;
//            try {
//                homoGraph = originHomoGraph.clone();
//                graphAvgRate = executor.execute(homoGraph);
//                homoWholeAccRate = Util.sumArray(homoWholeAccRate, graphAvgRate);
//            } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//            }
//
//            //여긴 주석처리할 것
//            try {
//                Graph homoGraph2 = originHomoGraph.clone();
//                graphAvgRate = executor.executeLimited(homoGraph2);
//                homoLimitedWholeAccRate = Util.sumArray(homoLimitedWholeAccRate, graphAvgRate);
//
//            } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//            }

            //HETEROGENEOUS GRAPH
            Graph originHeteroGraph = gg.generateHeteroGraph(Param.m0, Param.nNodes);

//            Graph heteroGraph = null;
//            try {
//                heteroGraph = originHeteroGraph.clone();
//                graphAvgRate = executor.execute(heteroGraph);
//                heteroWholeAccRate = Util.sumArray(heteroWholeAccRate, graphAvgRate);
//            } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//            }

            try {
                Graph heteroGraph2 = originHeteroGraph.clone();
                graphAvgRate = executor.executeLimited(heteroGraph2);
                heteroLimitedWholeAccRate = Util.sumArray(heteroLimitedWholeAccRate, graphAvgRate);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("r_val___: "+Util.arrayToString(rArray));
        System.out.println("homo____: "+Util.arrayToString(Util.averageArray(homoWholeAccRate, Param.realization)));
        System.out.println("homo_lim: "+Util.arrayToString(Util.averageArray(homoLimitedWholeAccRate, Param.realization)));
        System.out.println("hete_lim: "+Util.arrayToString(Util.averageArray(heteroLimitedWholeAccRate, Param.realization)));
    }
}
