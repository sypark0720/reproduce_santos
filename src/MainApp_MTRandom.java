public class MainApp_MTRandom {

    public static void main(String args[]){

        MersenneTwisterFast rand = new MersenneTwisterFast();
        System.out.println(rand.nextInt());
        System.out.println(rand.nextFloat());

    }
}
