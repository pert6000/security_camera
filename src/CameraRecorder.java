import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.opencv.videoio.VideoWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CameraRecorder {

    public static void main(String[] args) throws IOException {
        // Load the OpenCV native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Open the default camera
        VideoCapture camera = new VideoCapture(0);

        // Check if camera opened successfully
        if (!camera.isOpened()) {
            System.out.println("Error: Could not open camera.");
            return;
        }

        // Define the codec and initial output file
        String outputFile = getOutputFileName();
        int fourcc = VideoWriter.fourcc('m', 'p', '4', 'v'); // Codec for MP4 format

        // Get camera properties (frame width, frame height, and frame rate)
        int frameWidth = (int) camera.get(Videoio.CAP_PROP_FRAME_WIDTH);
        int frameHeight = (int) camera.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        double frameRate = camera.get(Videoio.CAP_PROP_FPS);

        // Create VideoWriter object to write video to file
        VideoWriter videoWriter = new VideoWriter(outputFile, fourcc, frameRate, new org.opencv.core.Size(frameWidth, frameHeight));

        // Record start time
        LocalDateTime startTime = LocalDateTime.now();

        // Read and write frames until user interrupts
        Mat frame = new Mat();
        while (true) {
            // Read frame from camera
            camera.read(frame);

            // Check if frame is empty
            if (frame.empty()) {
                System.out.println("Error: Frame empty.");
                break;
            }

            // Write frame to output file
            videoWriter.write(frame);
            // Check if one hour has passed
            if (LocalDateTime.now().minusMinutes(1).isAfter(startTime)) { // siia pane kui suurt vahemikku tahad

                videoWriter.release();
                outputFile = getOutputFileName();
                videoWriter = new VideoWriter(outputFile, fourcc, frameRate, new org.opencv.core.Size(frameWidth, frameHeight));

                callErinevused(outputFile);

                startTime = LocalDateTime.now(); // Update start time
            }


            // Check for interrupt (user presses 'q' key)
            if (System.in.available() > 0 && System.in.read() == 'q') {
                break;
            }
        }

        // Release resources
        camera.release();
        videoWriter.release();
    }

    private static String getOutputFileName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return "recordings/output_" + now.format(formatter) + ".mp4";
    }

    public static void callErinevused(String fail) {
        try {
            // Command to run the second Java program
            String command = "java -cp . detector " + fail;

            // Start a new process for the second program
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
            pb.inheritIO(); // Redirect input/output/error streams to the current process
            pb.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
