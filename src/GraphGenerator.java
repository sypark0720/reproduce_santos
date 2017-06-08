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
            adjacencyList = insertEdgeBothSide(i, m0, adjacencyList);
        }
        degree[m0] = m0;
        return new Graph(nNodes, adjacencyList, degree);
    }

    public  List<List<Integer>> insertEdge(int from, int to, List<List<Integer>> adjacencyList){
        adjacencyList.get(from).add(to);
        return adjacencyList;
    }

    public  List<List<Integer>> insertEdgeBothSide(int from, int to, List<List<Integer>> adjacencyList){
        adjacencyList.get(from).add(to);
        adjacencyList.get(to).add(from);
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

            List<Integer> selected = new ArrayList<>(); //이미 선택된 node
            int newlySelected = 0; //현재 노드 중 새 노드가 연결될 노드
            int countSelect = 0;

            // m0/2개의 새로운 edge를 만들 때까지 반복
                while(countSelect< m0/2){

                    newlySelected = preferentialAttach(selected, newV, totalDegree, degree);
                    //partSum < probe일 때: 연결 불가
                    if (newlySelected < 0) continue;

                    else{
                        //connect
                        adjacencyList = insertEdgeBothSide(newV, newlySelected, adjacencyList);

                        totalDegree -= degree[newlySelected]; //남은 것들 중에서 뽑기 위해
                        degree[newlySelected]++;
                        selected.add(newlySelected);

                        countSelect++;
                    }
                }

            }

        return new Graph(nNodes, adjacencyList, degree, new int[nNodes], new double[nNodes]);
    }


    // preferential attachment
    // 어떤 node에 붙일 건지를 골라서 node 번호 리턴, 실패하면 return -1
    public int preferentialAttach(List selected, int newV, int totalDegree, int[] degree){

        float probe = rd.nextFloat(); //random한 소수 (0~1사이)를 하나 뽑는다
        float partSum = 0;

        //preferential attachment. j: 현재 있는 node
        for(int j=0; j<newV-1; j++) {
            if (!selected.contains(j)) {
                partSum += (float) degree[j] / totalDegree;

                if (probe < partSum) {
                    return j;
                }
            }
        }
        return -1;
    }


    //clusting coefficient 조정 가능한 heterograph.
    //q: edge 연결 시 Triad formation 을 할 확률.
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
            int newlySelected = 0; //현재 노드 중 새 노드가 연결될 노드
            List<Integer> selected = new ArrayList<>(); //이미 선택된 node
            int countSelect = 0; //몇 번이나 골랐는지

            int u; //Triad formation의 기준

            //STEP1. 일단 하나 뽑아서 Preferential Attachment
            while (true) {

                newlySelected = preferentialAttach(selected, newV, totalDegree, degree);
                if (newlySelected < 0) continue;

                else {
                    //connect
                    adjacencyList = insertEdgeBothSide(newV, newlySelected, adjacencyList);
                    totalDegree -= degree[newlySelected]; //남은 것들 중에서 뽑기 위해
                    degree[newlySelected]++;
                    selected.add(newlySelected);
                    countSelect++;

                    u = newlySelected;

                    break;
                }
            }

            //STEP2. 남은 edge수만큼 Triad formation(확률 q) 또는 preferential attachment(확률 1-q) 실시
            while (countSelect < m0/2) {

                //get available vertices: u의 neighbor 중 vNew와 연결되지 않은 것
                List<Integer> neighborsOfU = Util.getNeighbors(u, adjacencyList);
                List<Integer> available = getNodesTFavailable(newV, neighborsOfU, selected);

                int availableSize = available.size();

                //CASE1: Triad formation. q의 확률 && u에 neighbor중 연결할 node가 남아 있을 때
                if(isTF(q) && availableSize!=0){

                    int w=available.get(rd.nextInt(availableSize));

                    //connect
                    adjacencyList = insertEdgeBothSide(newV, w, adjacencyList);
                    totalDegree -= degree[w]; //남은 것들 중에서 뽑기 위해
                    degree[w]++;
                    selected.add(w);

                    countSelect++;

                }

                //CASE2: Preferential attachment. 그 외의 모든 경우 (1-q의 확률에 걸렸을 때 또는 Triad formation 해야 하지만 연결할 게 없을 때)
                //u변경
                else{

                    while (true) {
                        newlySelected = preferentialAttach(selected, newV, totalDegree, degree);
                        //partSum < probe일 때: 연결 불가
                        if (newlySelected < 0) continue;

                        else {
                            //connect
                            adjacencyList = insertEdgeBothSide(newV, newlySelected, adjacencyList);
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

    //determine whether using Triad formation(TF)
    public boolean isTF(double q){
        float probe = rd.nextFloat(); //random한 소수 (0~1사이)를 하나 뽑는다.
        if(q >= probe) return true;
        return false;
    }

    //neighborsOfU 중에서 newV와 연결되지 않은 neighbor 목록 return
    //(Triad formation이 가능한 u의 neighbor 목록)
    public List<Integer> getNodesTFavailable(int newV, List<Integer> neighborsOfU, List<Integer> selected){

        List<Integer> available = new ArrayList<>();

        for(int i=0; i<neighborsOfU.size(); i++){
            int neighbor = neighborsOfU.get(i);
            if( neighbor!=newV && !selected.contains(neighbor))
                available.add(neighbor);
        }
        return available;
    }

}
