public class MainApp_homo
{
    public static void main(String[] args) {

        double[] rArray = new double[Param.nSample];

        double[] homoRoundAccRate;
        double[] homoRoundAvgRate;
        double[] homoGraphAccRate;
        double[] homoGraphAvgRate;
        //-------
        double[] homoWholeAccRate;
        double[] homoWholeAvgRate;

        GraphGenerator gg = new GraphGenerator();

        //HOMOGENEOUS GRAPH
        //IN WHOLE PROCESS - 총 n개의 그래프에 대해서 실행
        homoWholeAccRate = new double[Param.nSample];

        for (int num=0; num < Param.realization; num++){
            Graph originHomoGraph = gg.generateHomoGraph(Param.m0, Param.nNodes);

            //IN ONE GRAPH - 하나의 그래프당 n번 처음 strategy를 변화시켜가며 rate 측정
            homoGraphAccRate= new double[Param.nSample];

            for(int run=0; run < Param.runsPerGraph; run++){
                originHomoGraph.initializeStrategy(Param.rc);

                //IN ONE ROUND - r을 변화시켜가며 cooperator의 rate측정
                homoRoundAccRate = new double[Param.nSample];

                for (int rOrder = 0; rOrder < Param.nSample; rOrder++) {

                    //IN ONE r VALUE
                    Graph homoGraph = null;
                    try {
                        homoGraph = originHomoGraph.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                    double r = Param.rStart + Param.rInterval * rOrder;
                    rArray[rOrder] = r;

                    //periods만큼 진행
                    for (int period = 0; period < Param.periods; period++) {
                        homoGraph = PGG.round(homoGraph, r);
                        homoGraph = Evolution.evolve(homoGraph, r);
                        homoGraph.initializePayoff();
                    }

                    //그 후 n번의 periods를 평균
                    for (int period = 0; period < Param.periodsAvged; period++){
                        homoGraph = PGG.round(homoGraph, r);
                        homoGraph = Evolution.evolve(homoGraph, r);
                        homoGraph.initializePayoff();
                        homoRoundAccRate[rOrder] += Util.getFracOfCoop(homoGraph);
                    }
                }

                homoRoundAvgRate = Util.averageArray(homoRoundAccRate, Param.periodsAvged);
                homoGraphAccRate = Util.sumArray(homoGraphAccRate, homoRoundAvgRate);
                }

            homoGraphAvgRate = Util.averageArray(homoGraphAccRate, Param.runsPerGraph);
            //--------
            homoWholeAccRate = Util.sumArray(homoWholeAccRate, homoGraphAvgRate);
        }

        homoWholeAvgRate = Util.averageArray(homoWholeAccRate, Param.realization);

        System.out.println("r val   : "+Util.arrayToString(rArray));
        System.out.println("homo avg: "+Util.arrayToString(homoWholeAvgRate));
    }
}
