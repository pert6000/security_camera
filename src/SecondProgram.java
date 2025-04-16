public class SecondProgram {
    public static void main(String[] args) {
        // Your second program's code here
        for (int i = 0; i < 10; i++) {
            System.out.println("Second Program is running: " + i);
            try {
                Thread.sleep(1000); // Sleep for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}