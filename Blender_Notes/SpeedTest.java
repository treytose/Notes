public class SpeedTest
{
  public static void main(String[] args) {
    long startTime = System.nanoTime();
    long sum = 0;
    for(int i = 0; i < 10000000; i++) {
      sum += i;
    }
    long endTime = System.nanoTime();
    System.out.println(sum);
    System.out.printf("%1.5f", (double)(endTime - startTime) / 1000000000);
  }
}
