import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;


public class detector {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static double[] compareFrames(String videoFile, int no_of_comparisons) {

        VideoCapture capture = new VideoCapture(videoFile);

        long kaadreid = (long) capture.get(Videoio.CAP_PROP_FRAME_COUNT);
        double[] tulem = new double[(int) kaadreid / no_of_comparisons];

        if (!capture.isOpened()) {
            System.out.println("Error: Cannot open the video file.");
            return null;
        }
        for (int i = 1; i < no_of_comparisons; i++) {
            capture.set(Videoio.CAP_PROP_POS_FRAMES, ((double) (i * kaadreid) / no_of_comparisons));
            Mat frame1 = new Mat();
            capture.read(frame1);

            capture.set(Videoio.CAP_PROP_POS_FRAMES, ((double) (i - 1) * (int) kaadreid / no_of_comparisons));
            Mat frame2 = new Mat();
            capture.read(frame2);

            Mat diff = new Mat();
            Core.absdiff(frame1, frame2, diff);

            double score = Core.sumElems(diff).val[0] / (frame1.rows() * frame1.cols() * frame1.channels());
            System.out.println(score);
            tulem[i] = score;

            frame1.release();
            frame2.release();
            diff.release();
        }

        capture.release();

        return tulem;
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }



    public static void copyFrames(String inputVideo, String outputVideo, long startFrameIndex, long endFrameIndex) {
        // Open the input video file
        VideoCapture capture = new VideoCapture(inputVideo);

        if (!capture.isOpened()) {
            System.out.println("Error: Cannot open the input video file.");
            return;
        }

        // Get the frame count
        long frameCount = (long) capture.get(Videoio.CAP_PROP_FRAME_COUNT);

        // Check if the specified frames are within the range
        if (startFrameIndex < 0 || startFrameIndex >= frameCount || endFrameIndex < 0 || endFrameIndex >= frameCount) {
            System.out.println("Error: Frame indices are out of range.");
            return;
        }

        // Set the position to the start frame
        capture.set(Videoio.CAP_PROP_POS_FRAMES, startFrameIndex);

        // Create a VideoWriter for the output file
        int fourcc = VideoWriter.fourcc('X', '2', '6', '4'); // Codec for MP4
        double fps = capture.get(Videoio.CAP_PROP_FPS);
        int frameWidth = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        int frameHeight = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        VideoWriter writer = new VideoWriter(outputVideo, fourcc, fps, new org.opencv.core.Size(frameWidth, frameHeight), true);

        // Write frames to the output file
        Mat frame = new Mat();
        for (long i = startFrameIndex; i <= endFrameIndex; i++) {
            capture.read(frame);
            writer.write(frame);
        }

        // Release resources
        frame.release();
        capture.release();
        writer.release();
    }


    public static void main(String[] args) {
        String videoFile = "C:\\Users\\pertk\\IdeaProjects\\rndm\\vid\\ralli.mp4.mp4";
        // String video = args[0];

        double[] erinevused = compareFrames(videoFile, 10);

        //copyFrames(videoFile, "C:\\Users\\pertk\\IdeaProjects\\rndm\\vid\\ralli_koopia.mp4.mp4", 100, 200);

        //System.out.println(Arrays.toString(erinevused));
    }
}
