public class Param {

    //Parameters
    public static final int nSample = 4; //paper: 19 - the number of measured r
    public static final double rInterval = 1; //paper: 0.25 -interval of r //r started from 1
    public static final double rStart = 1; //paper: 1 (1~5, 0.25 intervals)

    public static final int REALIZATION = 1; //paper: 10 -몇 개의 그래프를 돌릴 껀지
    public static final int RUNS = 5; //paper: 100 -한 그래프당 몇 번 돌릴 건지
    public static final int periods = 10; //paper: 100000 -generations to be in equalibrium
    public static final int genAvged = 2000; //paper: 2000 -위의 periods가 지난 다음 얼마만큼의 generation을 평균낼 것인지

    public static final int nNodes = 1000; //total number of nodes
    public static final int m0 = 4; //z
    public static final double rc = 0.5; //initial rate of cooperators
    public static final double c = 1; //cost

    public static final int DEFECTOR = 0;
    public static final int COOPERATOR = 1;



}
