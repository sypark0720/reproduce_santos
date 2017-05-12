public class MainApp
{
    public static void main(String[] args) {

        double[] rArray = new double[Param.nSample];
        double[] homoResult = new double[Param.nSample];
        double[] heteroResult = new double[Param.nSample];
        double[] heteroLimitedResult = new double[Param.nSample];

        GraphGenerator gg = new GraphGenerator();

        for (int num=0; num<1; num++){

            //Homo
            Graph homoGraph = gg.generateHomoGraph(Param.m0, Param.nNodes);

            //한 그래프당 100번씩 돌린다
            for (int runs=0; runs< Param.RUNS; runs++){
                homoGraph.initializeStrategy(Param.rc);

                //한 그래프당 한 번 돌릴 때 r을 변화시켜가며 돌린다
                for (int i = 0; i < Param.nSample; i++) {

                    double r = Param.rStart + Param.rInterval * i;

                    //periods만큼 진화시킨다
                    for (int j = 0; j < Param.periods; j++) {
                        homoGraph = PGG.round(homoGraph, r);
                        homoGraph = Evolution.evolve(homoGraph, r);
                        homoGraph.initializePayoff();
                    }

                    rArray[i] = r;
                    homoResult[i] += Util.getFracOfCoop(homoGraph);

                }

                System.out.println("after 1 cycle: "+Util.arrayToString(homoResult));


//                //Hetero
//                Graph heteroGraph = gg.generateHeteroGraph(Param.m0, Param.nNodes);
//                heteroGraph.initializeStrategy(Param.rc);
//
//                for (int j = 0; j < Param.periods; j++) {
//                    //1. HeteroGraph
//                    heteroGraph = PGG.round(heteroGraph, r);
//                    heteroGraph = Evolution.evolve(heteroGraph, r);
//                    heteroGraph.initializePayoff();
//                }
//
//                heteroResult[i] = Util.getFracOfCoop(heteroGraph);
//
//                for (int j = 0; j < Param.periods; j++) {
//                    //1. HeteroGraph
//                    heteroGraph = PGG.roundLimited(heteroGraph, r);
//                    heteroGraph = Evolution.evolve(heteroGraph, r);
//                    heteroGraph.initializePayoff();
//                }
//
//                heteroLimitedResult[i] = Util.getFracOfCoop(heteroGraph);
            }

            System.out.println("after total runs: "+Util.arrayToString(homoResult));

            //todo 평균 내기



            System.out.println("r val  : "+Util.arrayToString(rArray));
            System.out.println("homo   : "+Util.arrayToString(Util.averageArray(homoResult, Param.REALIZATION*Param.RUNS)));
            System.out.println("hetero1: "+Util.arrayToString(heteroResult));
            System.out.println("hetero2: "+Util.arrayToString(heteroLimitedResult));
        }
    }
}
