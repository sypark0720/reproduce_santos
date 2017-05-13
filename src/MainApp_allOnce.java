public class MainApp_allOnce
{
    public static void main(String[] args) {

        double[] rArray = new double[Param.nSample];
        double[] homoResult = new double[Param.nSample];
        double[] heteroResult = new double[Param.nSample];
        double[] heteroLimitedResult = new double[Param.nSample];

        GraphGenerator gg = new GraphGenerator();

        //Homo
        Graph originHomoGraph = gg.generateHomoGraph(Param.m0, Param.nNodes);
        originHomoGraph.initializeStrategy(Param.rc);

        //Hetero
        Graph originHeteroGraph = gg.generateHeteroGraph(Param.m0, Param.nNodes);
        originHeteroGraph.initializeStrategy(Param.rc);


        for (int i = 0; i < Param.nSample; i++) {

            //homo
            double r = Param.rStart + Param.rInterval * i;

            Graph homoGraph = null;
            try {
                homoGraph = originHomoGraph.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < Param.periods; j++) {
                homoGraph = PGG.round(homoGraph, r);
                homoGraph = Evolution.evolve(homoGraph, r);
                homoGraph.initializePayoff();
            }

            rArray[i] = r;
            homoResult[i] = Util.getFracOfCoop(homoGraph);


//            //hetero
//            Graph heteroGraph = null;
//            try {
//                heteroGraph = originHeteroGraph.clone();
//            } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//            }
//
//
//            for (int j = 0; j < Param.periods; j++) {
//                //1. HeteroGraph
//                heteroGraph = PGG.round(heteroGraph, r);
//                heteroGraph = Evolution.evolve(heteroGraph, r);
//                heteroGraph.initializePayoff();
//            }
//
//            heteroResult[i] = Util.getFracOfCoop(heteroGraph);
//
//            for (int j = 0; j < Param.periods; j++) {
//                //1. HeteroGraph
//                heteroGraph = PGG.roundLimited(heteroGraph, r);
//                heteroGraph = Evolution.evolve(heteroGraph, r);
//                heteroGraph.initializePayoff();
//            }
//
//            heteroLimitedResult[i] = Util.getFracOfCoop(heteroGraph);
        }

        System.out.println("r_val__: "+Util.arrayToString(rArray));
        System.out.println("homo___: "+Util.arrayToString(homoResult));
//        System.out.println("hetero1: "+Util.arrayToString(heteroResult));
//        System.out.println("hetero2: "+Util.arrayToString(heteroLimitedResult));

    }
}
