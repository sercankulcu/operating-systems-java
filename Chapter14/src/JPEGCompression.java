import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.util.Iterator;

/**
 * This class demonstrates JPEG image compression using the javax.imageio package.
 * It provides functionality to load an image and compress it with a specified quality level.
 */
public class JPEGCompression {
    
    // Constants for default values
    private static final String DEFAULT_INPUT = "input.jpg";
    private static final String DEFAULT_OUTPUT = "output.jpg";
    private static final float DEFAULT_QUALITY = 0.5f;

    /**
     * Main method to demonstrate image compression functionality
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Load the original image from file
            BufferedImage originalImage = loadImage(DEFAULT_INPUT);
            
            // Validate image loading
            if (originalImage == null) {
                throw new IOException("Failed to load input image");
            }
            
            // Compress the image with default quality
            compressImage(originalImage, DEFAULT_QUALITY, DEFAULT_OUTPUT);
            
            // Print success message
            System.out.println("Image compression completed successfully");
            System.out.println("Output saved as: " + DEFAULT_OUTPUT);
            
        } catch (IOException e) {
            // Handle and report any errors during processing
            System.err.println("Error during image compression: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads an image from the specified file path
     * @param inputPath Path to the input image file
     * @return BufferedImage object containing the loaded image
     * @throws IOException if the image cannot be read
     */
    private static BufferedImage loadImage(String inputPath) throws IOException {
        File inputFile = new File(inputPath);
        // Check if file exists before attempting to read
        if (!inputFile.exists()) {
            throw new IOException("Input file not found: " + inputPath);
        }
        return ImageIO.read(inputFile);
    }

    /**
     * Compresses an image using JPEG compression with specified quality
     * @param originalImage The source image to compress
     * @param quality Compression quality (0.0f to 1.0f)
     * @param outputPath Path where the compressed image will be saved
     * @throws IOException if compression or file writing fails
     * @throws IllegalArgumentException if quality is out of range
     */
    public static void compressImage(BufferedImage originalImage, float quality, String outputPath) 
            throws IOException {
        // Validate input parameters
        if (originalImage == null) {
            throw new IllegalArgumentException("Original image cannot be null");
        }
        if (quality < 0.0f || quality > 1.0f) {
            throw new IllegalArgumentException("Quality must be between 0.0 and 1.0");
        }

        // Get available JPEG writer
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) {
            throw new IOException("No JPEG writers available");
        }
        ImageWriter writer = writers.next();

        try {
            // Configure compression parameters
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            // Write the compressed image to file
            File outputFile = new File(outputPath);
            try (FileImageOutputStream fios = new FileImageOutputStream(outputFile)) {
                writer.setOutput(fios);
                IIOImage iioImage = new IIOImage(originalImage, null, null);
                writer.write(null, iioImage, param);
                fios.flush();
            }
        } finally {
            // Clean up resources
            writer.dispose();
        }
    }
}