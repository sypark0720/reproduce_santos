import java.util.Calendar;

public class MainApp_tunable_hetero {

    static Calendar calendar;

    public static void main(String[] args) {

        calendar = Calendar.getInstance();
        System.out.println("time----: " + calendar.get(Calendar.HOUR_OF_DAY )+ ":" + calendar.get(Calendar.MINUTE) + "\n" );

        double[] rArray = new double[Param.nSample];
        double[] heteroWholeAccRate;
        double[] heteroLimitedWholeAccRate;
        double[] graphAvgRate;

        Graph originHeteroGraph;
        Graph heteroGraph;
        Graph heteroGraph2;

        GraphGenerator gg = new GraphGenerator();
        Executor executor = new Executor();

        //rArray
        for (int rOrder = 0; rOrder < Param.nSample; rOrder++) {
            double r = Param.rStart + Param.rInterval * rOrder;
            rArray[rOrder] = r;
        }
        System.out.println("r_val___: " + Util.arrayToString(rArray));


        //q 변경해가면서 실행
        for(int i=0; i<7; i++) {

            double q = 0.15 * i;
            heteroWholeAccRate = new double[Param.nSample];
            heteroLimitedWholeAccRate = new double[Param.nSample];

            //IN WHOLE PROCESS - 총 n개의 그래프에 대해서 실행
            for (int num = 0; num < Param.realization; num++) {

                //HETEROGENEOUS GRAPH
                originHeteroGraph = gg.generateTunableHeteroGraph(Param.m0, Param.nNodes, q);

                try {
                    heteroGraph = originHeteroGraph.clone();
                    graphAvgRate = executor.execute(heteroGraph);
                    heteroWholeAccRate = Util.sumArray(heteroWholeAccRate, graphAvgRate);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                try {
                    heteroGraph2 = originHeteroGraph.clone();
                    graphAvgRate = executor.executeLimited(heteroGraph2);
                    heteroLimitedWholeAccRate = Util.sumArray(heteroLimitedWholeAccRate, graphAvgRate);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }

            calendar = Calendar.getInstance();
            System.out.println("time----: " + calendar.get(Calendar.HOUR_OF_DAY )+ ":" + calendar.get(Calendar.MINUTE) );
            System.out.println("q-------: " + q );
            System.out.println("hete____: " + Util.arrayToString(Util.averageArray(heteroWholeAccRate, Param.realization)));
            System.out.println("hete_lim: " + Util.arrayToString(Util.averageArray(heteroLimitedWholeAccRate, Param.realization))+"\n");
        }
    }
}
