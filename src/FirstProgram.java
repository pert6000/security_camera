public class FirstProgram {
    public static void main(String[] args) {
        // Your first program's code here

        // Call the second Java program without blocking


        // Continue with the first program's code
        for (int i = 0; i < 10; i++) {
            System.out.println("First Program is running: " + i);
            try {
                Thread.sleep(1000); // Sleep for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void callErinevused(String fail) {
        try {
            // Command to run the second Java program
            String command = "java -cp . SecondProgram " + fail;

            // Start a new process for the second program
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
            pb.inheritIO(); // Redirect input/output/error streams to the current process
            pb.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}