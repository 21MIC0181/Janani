import java.util.Scanner;

public class PrimeSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of threads (1-10): ");
        int numOfThreads = scanner.nextInt();
        scanner.close();

        long start = System.currentTimeMillis();
        long sum = parallelSum(numOfThreads);
        long end = System.currentTimeMillis();

        System.out.println("Sum of prime numbers from 1 to 20,000: " + sum);
        System.out.println("Execution time with " + numOfThreads + " thread(s): " + (end - start) + " ms");
    }

    private static long parallelSum(int numOfThreads) {
        PrimeSumWorker[] workers = new PrimeSumWorker[numOfThreads];
        for (int i = 0; i < numOfThreads; i++) {
            workers[i] = new PrimeSumWorker(i, numOfThreads);
            workers[i].start();
        }

        long sum = 0;
        for (int i = 0; i < numOfThreads; i++) {
            try {
                workers[i].join();
                sum += workers[i].getSum();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return sum;
    }

    private static class PrimeSumWorker extends Thread {
        private final int id;
        private final int numOfThreads;
        private long sum;

        public PrimeSumWorker(int id, int numOfThreads) {
            this.id = id;
            this.numOfThreads = numOfThreads;
            this.sum = 0;
        }

        public void run() {
            for (int i = id + 1; i <= 20000; i += numOfThreads) {
                if (isPrime(i)) {
                    sum += i;
                }
            }
        }

        public long getSum() {
            return sum;
        }

        private boolean isPrime(int n) {
            if (n <= 1) {
                return false;
            }
            for (int i = 2; i <= Math.sqrt(n); i++) {
                if (n % i == 0) {
                    return false;
                }
            }
            return true;
        }
    }
}

