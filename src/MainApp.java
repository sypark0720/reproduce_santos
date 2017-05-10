public class MainApp {

    public static void main(String[] args) {

        double[] rArray = new double[Param.nSample];
        double[] heteroResult = new double[Param.nSample];
//        double[] homoResult = new double[nSample];

        GraphGenerator gg = new GraphGenerator();


        for (int i = 0; i < Param.nSample; i++) {
            double r = Param.rStart + Param.rInterval * i;

            Graph heteroGraph = gg.generateHeteroGraph(Param.m0, Param.nNodes);
            heteroGraph.initializeStrategy(Param.rc);

//            Graph homoGraph = gg.generateHomoGraph(m0, nNodes);
//            gg.initializeStrategy(homoGraph, rc);

            for (int j = 0; j < Param.periods; j++) {

//                System.out.println("***********************************************************");
//                System.out.println("PERIOD: "+j);
//                System.out.println("[INITIALIZED]");
//                System.out.println("STRATEGY: " + Util.arrayToString(heteroGraph.getStrategy()));


                //1. HeteroGraph
                heteroGraph = PGG.round(heteroGraph, r);
//                System.out.println("***********************************************************");
//                System.out.println("[PGG ROUNDED]");
//                System.out.println("STRATEGY: " + Util.arrayToString(heteroGraph.getStrategy()));

                heteroGraph = Evolution.evolve(heteroGraph, r);
//                System.out.println("***********************************************************");
//                System.out.println("[EVOLVED]");
//                System.out.println("STRATEGY: " + Util.arrayToString(heteroGraph.getStrategy()));

                heteroGraph.initializePayoff();


//                //2. HomoGraph
//                participatePGG(homoGraph, r);
//                evolution(homoGraph);

            }

            rArray[i] = r;
            heteroResult[i] = Util.getFracOfCoop(heteroGraph);
//            homoResult[i] = getFracOfCoop(homoGraph);
        }

        System.out.println("r value: "+Util.arrayToString(rArray));
        System.out.println("hetero : "+Util.arrayToString(heteroResult));
    }


}
