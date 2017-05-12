import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphGenerator {


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

        for(int i=m0; i<nNodes; i++) degree[i] = m0; //degree를 [1,1,1,1,m0,m0,...]로 초기화 (하나의 노드를 붙일 때마다 m0개의 edge가 추가되므로)

        Random rd = new Random();

        //Add nodes
        for (int i=1;i<nNodes-m0;i++)
        {
            int totalDegree = 2*i*m0;
            List<Integer> selected = new ArrayList<>(m0); //이미 선택된 node
            int newlySelected = 0; //현재 노드 중 새 노드가 연결될 노드
            int countSelect = 0;

            // m0/2개의 새로운 edge를 만들 때까지 반복
                while(countSelect< m0/2){

                float probe = rd.nextFloat(); //random한 소수 (0~1사이)를 하나 뽑는다
                float partSum = 0;

                //preferential attachment. j: 현재 있는 node
                for(int j=0; j<m0+i-1; j++) {
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
                    insertEdge(m0 + i, newlySelected, adjacencyList);
                    insertEdge( newlySelected, m0 + i , adjacencyList);

                    totalDegree -= degree[newlySelected]; //남은 것들 중에서 뽑기 위해
                    degree[newlySelected]++;
                    selected.add(newlySelected);

                    countSelect++;
                }
                }

            }

        return new Graph(nNodes, adjacencyList, degree, new int[nNodes], new double[nNodes]);
    }

}
