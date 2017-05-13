public class Param {

    //Parameters
    //r 관련
    public static final int nSample = 19; //paper: 19 - the number of measured r
    public static final double rInterval = 0.25; //paper: 0.25 -interval of r //r started from 1
    public static final double rStart = 2.5; //paper: 1 (1~5, 0.25 intervals)

    //in one graph, one round
    public static final int periods = 100; //paper: 100000 -generations to be in equalibrium
    public static final int periodsAvged = 5; //paper: 2000 -위의 periods가 지난 다음 얼마만큼의 generation을 평균낼 것인지


    //in one graph, total rounds
    public static final int runsPerGraph = 3; //paper: 100 -한 그래프당 몇 번 돌릴 건지

    //in whole process
    public static final int realization = 3; //paper: 10 -몇 개의 그래프를 돌릴 껀지


    public static final int nNodes = 1000; //total number of nodes
    public static final int m0 = 4; //z
    public static final double rc = 0.5; //initial rate of cooperators
    public static final double c = 1; //cost

    public static final int DEFECTOR = 0;
    public static final int COOPERATOR = 1;


}
