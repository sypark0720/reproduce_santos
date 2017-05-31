import java.util.ArrayList;
import java.util.List;

public class GraphGenerator {

    public MersenneTwisterFast rd = new MersenneTwisterFast();

    //m0이 star(0,1,2,...m0로 이루어진)의 중심
    public Graph makeStar(int nNodes, int m0){

        List<List<Integer>> adjacencyList = Util.generateDoubleList(nNodes);
        int[] degree = new int[nNodes];

        for (int i=0;i<m0;i++)
        {
            degree[i]  = 1;
            adjacencyList = insertEdge(i, m0, adjacencyList);
            adjacencyList = insertEdge(m0, i, adjacencyList);
        }
        degree[m0] = m0;
        return new Graph(nNodes, adjacencyList, degree);
    }

    public  List<List<Integer>> insertEdge(int from, int to, List<List<Integer>> adjacencyList){
        adjacencyList.get(from).add(to);
        return adjacencyList;
    }


    //regular graph - degree: 4
    public Graph generateHomoGraph(int m0, int nNodes){

        List<List<Integer>> adjacencyList = Util.generateDoubleList(nNodes);
        int[] degree = new int[nNodes];

        //Initialize
        for(int i=0; i<nNodes; i++) degree[i] = m0;

        //todo m0 이용해서 바꾸기
        for(int i=0; i<nNodes; i++){
            adjacencyList = insertEdge(i, (i+nNodes-2)%nNodes, adjacencyList);
            adjacencyList = insertEdge(i, (i+nNodes-1)%nNodes, adjacencyList);
            adjacencyList = insertEdge(i, (i+nNodes+1)%nNodes, adjacencyList);
            adjacencyList = insertEdge(i, (i+nNodes+2)%nNodes, adjacencyList);
        }
        return new Graph(nNodes, adjacencyList, degree, new int[nNodes], new double[nNodes]);
    }


    public Graph generateHeteroGraph(int m0, int nNodes){

        //Initialize
        Graph graph = makeStar(nNodes, m0);
        List<List<Integer>> adjacencyList = graph.getAdjacencyList();
        int[] degree = graph.getDegree();

        for(int i=m0+1; i<nNodes; i++) degree[i] = m0/2; //degree를 [1,1,1,1,m0,m0/2,m0/2...]로 초기화 (하나의 노드를 붙일 때마다 m0/2개의 edge가 추가되므로)

        //Add nodes
        for (int i=1;i<nNodes-m0;i++)
        {

            int newV = m0+i;
            int totalDegree = 2*m0 + m0*i; //기존 star degree [ 1,1,1,1,m0 ] 갯수 + 한 번에 m0/2개씩 붙여지니까 degree는 한번에 m0개씩 늘어남

            List<Integer> selected = new ArrayList<>(m0); //이미 선택된 node
            int newlySelected = 0; //현재 노드 중 새 노드가 연결될 노드
            int countSelect = 0;

            // m0/2개의 새로운 edge를 만들 때까지 반복
                while(countSelect< m0/2){

                float probe = rd.nextFloat(); //random한 소수 (0~1사이)를 하나 뽑는다
                float partSum = 0;

                //preferential attachment. j: 현재 있는 node
                for(int j=0; j<newV-1; j++) {
                    if (!selected.contains(j)) {
                        partSum += (float) degree[j] / totalDegree;

                        if (probe < partSum) {
                            newlySelected = j;
                            break;
                        }
                    }
                }

                //partSum < probe일 때: 연결 불가
                if (partSum < probe) continue;

                else{
                    //connect
                    insertEdge(newV, newlySelected, adjacencyList);
                    insertEdge(newlySelected, newV , adjacencyList);

                    totalDegree -= degree[newlySelected]; //남은 것들 중에서 뽑기 위해
                    degree[newlySelected]++;
                    selected.add(newlySelected);

                    countSelect++;
                }
                }

            }

        return new Graph(nNodes, adjacencyList, degree, new int[nNodes], new double[nNodes]);
    }


    /**
     * @param q : probability that affects clustering
     */
    public Graph generateTunableHeteroGraph(int m0, int nNodes, double q){

        //Initialize
        Graph graph = makeStar(nNodes, m0);
        List<List<Integer>> adjacencyList = graph.getAdjacencyList();
        int[] degree = graph.getDegree();

        for(int i=m0+1; i<nNodes; i++) degree[i] = m0/2; //degree를 [1,1,1,1,m0,m0/2,m0/2...]로 초기화 (하나의 노드를 붙일 때마다 m0/2개의 edge가 추가되므로)

        //Add nodes
        for (int i=1;i<nNodes-m0;i++)
        {

            int newV = m0+i;
            int totalDegree = 2*m0 + m0*i; //기존 star degree [ 1,1,1,1,m0 ] 갯수 + 한 번에 m0/2개씩 붙여지니까 degree는 한번에 m0개씩 늘어남

            int u = -1; //일단 하나 뽑히는 애

            int newlySelected = 0; //현재 노드 중 새 노드가 연결될 노드
            List<Integer> selected = new ArrayList<>(m0); //이미 선택된 node
            int countSelect = 0; //몇 번이나 골랐는지

            //STEP2. 일단 하나 뽑아서 Preferential Attachment (PA)
            while (true) {
                float probe = rd.nextFloat(); //random한 소수 (0~1사이)를 하나 뽑는다
                float partSum = 0;

                //preferential attachment. j: 현재 있는 node
                for (int j = 0; j < newV - 1; j++) {
                    if (!selected.contains(j)) {
                        partSum += (float) degree[j] / totalDegree;}

                    if (probe < partSum) {
                        newlySelected = j;
                        break;
                    }
                }


                //partSum < probe일 때: 연결 불가
                if (partSum < probe) continue;

                else {
                    //connect
                    insertEdge(newV, newlySelected, adjacencyList);
                    insertEdge(newlySelected, newV, adjacencyList);

                    totalDegree -= degree[newlySelected]; //남은 것들 중에서 뽑기 위해
                    degree[newlySelected]++;
                    selected.add(newlySelected);
                    countSelect++;

                    u = newlySelected;
                    break;
                }
            }


            while (countSelect < m0/2) {

                //q의 확률로 Triad formation
                if(isTF(q)){
                    List<Integer> neighborsOfU = Util.getNeighbors(u, adjacencyList);

                    //select w : newlySelected의 neighbor이면서 vNew와 연결되지 않은 것
                    int w = -1;
                    for (int n = 0; n < neighborsOfU.size(); n++) {
                        if ( !selected.contains(neighborsOfU.get(n)) & !Util.isAdjacent(newV, neighborsOfU.get(n), adjacencyList)) {
                            w = neighborsOfU.get(n);
                            break;
                        }
                    }

                    if ( w >= 0 ) {
                        //connect
                        insertEdge(newV, w, adjacencyList);
                        insertEdge(w, newV, adjacencyList);

                        totalDegree -= degree[w]; //남은 것들 중에서 뽑기 위해
                        degree[w]++;
                        selected.add(w);

                        countSelect++;
                    }else{
                        //PA
                        while (true) {
                            float probe = rd.nextFloat(); //random한 소수 (0~1사이)를 하나 뽑는다
                            float partSum = 0;

                            //preferential attachment. j: 현재 존재하는 node
                            for (int j = 0; j < newV - 1; j++) {
                                if (!selected.contains(j)) {
                                    partSum += (float) degree[j] / totalDegree;}

                                if (probe < partSum) {
                                    newlySelected = j;
                                    break;
                                }
                            }

                            //partSum < probe일 때: 연결 불가
                            if (partSum < probe) continue;

                            else {
                                //connect
                                insertEdge(newV, newlySelected, adjacencyList);
                                insertEdge(newlySelected, newV, adjacencyList);

                                totalDegree -= degree[newlySelected]; //남은 것들 중에서 뽑기 위해
                                degree[newlySelected]++;
                                selected.add(newlySelected);
                                countSelect++;

                                u = newlySelected;
                                break;
                            }
                        }

                    }
                }//그 외의 확률로 PA
                else{
                    //PA
                    while (true) {
                        float probe = rd.nextFloat(); //random한 소수 (0~1사이)를 하나 뽑는다
                        float partSum = 0;

                        //preferential attachment. j: 현재 존재하는 node
                        for (int j = 0; j < newV - 1; j++) {
                            if (!selected.contains(j)) {
                                partSum += (float) degree[j] / totalDegree;}

                            if (probe < partSum) {
                                newlySelected = j;
                                break;
                            }
                        }

                        //partSum < probe일 때: 연결 불가
                        if (partSum < probe) continue;

                        else {
                            //connect
                            insertEdge(newV, newlySelected, adjacencyList);
                            insertEdge(newlySelected, newV, adjacencyList);

                            totalDegree -= degree[newlySelected]; //남은 것들 중에서 뽑기 위해
                            degree[newlySelected]++;
                            selected.add(newlySelected);
                            countSelect++;

                            u = newlySelected;
                            break;
                        }
                    }
                }
            }
        }
        return new Graph(nNodes, adjacencyList, degree, new int[nNodes], new double[nNodes]);
    }





    /**
     *  determine whether using Triad formation(TF)
     */
    public boolean isTF(double q){
        float probe = rd.nextFloat(); //random한 소수 (0~1사이)를 하나 뽑는다.
        if(q >= probe) return true;
        return false;
    }


}
