public class MainApp_MTRandom {

    public static void main(String args[]){

        MTRandom rd = new MTRandom();

        int n = rd.next(32);
        System.out.println(n);

    }
}
