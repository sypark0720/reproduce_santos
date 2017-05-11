public class MainApp2
{
    public static void main(String[] args) {

        double[] rArray = new double[Param.nSample];
        double[] homoResult = new double[Param.nSample];
        double[] heteroResult = new double[Param.nSample];
        double[] heteroLimitedResult = new double[Param.nSample];

        GraphGenerator gg = new GraphGenerator();


        for (int num=0; num<10; num++){

            for (int i = 0; i < Param.nSample; i++) {
                double r = Param.rStart + Param.rInterval * i;

                //Homo
                Graph homoGraph = gg.generateHomoGraph(Param.m0, Param.nNodes);
                homoGraph.initializeStrategy(Param.rc);

                for (int j = 0; j < Param.periods; j++) {
                    homoGraph = PGG.round(homoGraph, r);
                    homoGraph = Evolution.evolve(homoGraph, r);
                    homoGraph.initializePayoff();
                }

                rArray[i] = r;
                homoResult[i] = Util.getFracOfCoop(homoGraph);


                //Hetero
                Graph heteroGraph = gg.generateHeteroGraph(Param.m0, Param.nNodes);
                heteroGraph.initializeStrategy(Param.rc);

                for (int j = 0; j < Param.periods; j++) {
                    //1. HeteroGraph
                    heteroGraph = PGG.round(heteroGraph, r);
                    heteroGraph = Evolution.evolve(heteroGraph, r);
                    heteroGraph.initializePayoff();
                }

                heteroResult[i] = Util.getFracOfCoop(heteroGraph);

                for (int j = 0; j < Param.periods; j++) {
                    //1. HeteroGraph
                    heteroGraph = PGG.roundLimited(heteroGraph, r);
                    heteroGraph = Evolution.evolve(heteroGraph, r);
                    heteroGraph.initializePayoff();
                }

                heteroLimitedResult[i] = Util.getFracOfCoop(heteroGraph);
            }

            System.out.println("r val  : "+Util.arrayToString(rArray));
            System.out.println("homo   : "+Util.arrayToString(homoResult));
            System.out.println("hetero1: "+Util.arrayToString(heteroResult));
            System.out.println("hetero2: "+Util.arrayToString(heteroLimitedResult));
        }
    }
}
