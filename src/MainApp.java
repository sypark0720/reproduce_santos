public class MainApp {

    public static void main(String[] args) {

        double[] rArray = new double[Param.nSample];

        double[] homoResult = new double[Param.nSample];
        double[] heteroResult = new double[Param.nSample];
        double[] heteroResultLimited = new double[Param.nSample];

        double[] avgHomoResult = new double[Param.nSample];
        double[] avgHeteroResult = new double[Param.nSample];
        double[] avgHeteroResultLimited = new double[Param.nSample];

        GraphGenerator gg = new GraphGenerator();

        for (int realization = 0; realization < Param.REALIZATION; realization++) {

            //Homo
            Graph homoGraph = gg.generateHomoGraph(Param.m0, Param.nNodes);
            homoGraph.initializeStrategy(Param.rc);

            //Hetero: Fixed cost per game.
            Graph heteroGraph = gg.generateHeteroGraph(Param.m0, Param.nNodes);
            heteroGraph.initializeStrategy(Param.rc);

            for (int run = 0; run < Param.RUNFORREALIZATION; run++) {

                for (int i = 0; i < Param.nSample; i++) {
                    double r = Param.rStart + Param.rInterval * i;
                    rArray[i] = r;

                    //homo
                    for (int period = 0; period < Param.periods; period++) {
                        homoGraph = PGG.round(homoGraph, r);
                        homoGraph = Evolution.evolve(homoGraph, r);
                        homoGraph.initializePayoff();
                    }
                    homoResult[i] += Util.getFracOfCoop(homoGraph);

                    //hetero
                    for (int period = 0; period < Param.periods; period++) {
                        heteroGraph = PGG.round(heteroGraph, r);
                        heteroGraph = Evolution.evolve(heteroGraph, r);
                        heteroGraph.initializePayoff();
                    }
                    heteroResult[i] += Util.getFracOfCoop(heteroGraph);

                    //hetero-limited
                    for (int period = 0; period < Param.periods; period++) {
                        heteroGraph = PGG.roundLimited(heteroGraph, r);
                        heteroGraph = Evolution.evolve(heteroGraph, r);
                        heteroGraph.initializePayoff();
                    }
                    heteroResultLimited[i] += Util.getFracOfCoop(heteroGraph);
                }
            }

            for (int i = 0; i < Param.nSample; i++) {


                avgHomoResult[i] += homoResult[i];
                avgHeteroResult[i] += heteroResult[i];
                avgHeteroResultLimited[i] += heteroResultLimited[i];
//
//                avgHomoResult[i] += homoResult[i] / Param.RUNFORREALIZATION;
//                avgHeteroResult[i] += heteroResult[i] / Param.RUNFORREALIZATION;
//                avgHeteroResultLimited[i] += heteroResultLimited[i] / Param.RUNFORREALIZATION;
            }

            for (int i = 0; i < Param.nSample; i++) {
                avgHomoResult[i] = avgHomoResult[i] / Param.REALIZATION;
                avgHeteroResult[i] = avgHeteroResult[i] / Param.REALIZATION;
                avgHeteroResultLimited[i] = avgHeteroResultLimited[i] / Param.REALIZATION;
            }

            System.out.println("r value: " + Util.arrayToString(rArray));
            System.out.println("Homo---: " + Util.arrayToString(avgHomoResult));
            System.out.println("Hetero : " + Util.arrayToString(avgHeteroResult));
            System.out.println("Hetero2: " + Util.arrayToString(avgHeteroResultLimited));
        }
    }
}

