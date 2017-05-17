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


            double r = Param.rStart + Param.rInterval * i;

            //homo
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
//                if(j % 100 ==0) System.out.println("homograph,_period: "+j+" result: "+Util.getFracOfCoop(homoGraph));
            }

            rArray[i] = r;
            homoResult[i] = Util.getFracOfCoop(homoGraph);


            //hetero1
            Graph heteroGraph = null;
            try {
                heteroGraph = originHeteroGraph.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }


            for (int j = 0; j < Param.periods; j++) {
                heteroGraph = PGG.round(heteroGraph, r);
                heteroGraph = Evolution.evolve(heteroGraph, r);
                heteroGraph.initializePayoff();
//                if(j % 100 ==0) System.out.println("heterograph1,_period: "+j+" result: "+Util.getFracOfCoop(heteroGraph));
            }

            heteroResult[i] = Util.getFracOfCoop(heteroGraph);


            //hetero2: limited
            Graph heteroGraph2 = null;
            try {
                heteroGraph2 = originHeteroGraph.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < Param.periods; j++) {
                heteroGraph2 = PGG.roundLimited(heteroGraph2, r);
                heteroGraph2 = Evolution.evolveLimited(heteroGraph2, r);
                heteroGraph2.initializePayoff();
//                if(j % 100 ==0) System.out.println("heterograph2,_period: "+j+" result: "+Util.getFracOfCoop(heteroGraph2));
            }

            heteroLimitedResult[i] = Util.getFracOfCoop(heteroGraph2);
        }

        System.out.println("r_val__: "+Util.arrayToString(rArray));
        System.out.println("homo___: "+Util.arrayToString(homoResult));
        System.out.println("hetero1: "+Util.arrayToString(heteroResult));
        System.out.println("hetero2: "+Util.arrayToString(heteroLimitedResult));

    }
}
