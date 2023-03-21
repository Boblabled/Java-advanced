import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.file.Files.*;

public class Walk {

    private static final String ERROR_HASH = "0".repeat(64);
    private static byte[] buffer;

    public Walk(int buffer_size) {
        buffer = new byte[buffer_size];
    }

    public static void main(String[] args) {
        Walk walk = new Walk(4096);
        walk.run(args);
    }

    private void run(String[] args){
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            return;
        }

        Path inputPath;
        try {
            inputPath = Path.of(args[0]);
        } catch (InvalidPathException e) {
            System.err.println("Fail open output file: " + e.getMessage());
            return;
        }

        Path outputPath;
        try {
            outputPath = Path.of(args[1]);
        } catch (InvalidPathException e) {
            System.err.println("Fail open output file: " + e.getMessage());
            return;
        }

        try {
            if (outputPath.getParent() != null) {
                Files.createDirectories(outputPath.getParent());
            }
        } catch (IOException e) {
            System.err.println("Fail make directories: " + e.getMessage());
        }

        try (BufferedReader bufferedReader = newBufferedReader(inputPath, StandardCharsets.UTF_8)) {
            try (BufferedWriter bufferedWriter = newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
                String file_path;
                while ((file_path = bufferedReader.readLine()) != null) {
                    bufferedWriter.write(hashigFile(file_path) + " " + file_path);
                    bufferedWriter.newLine();
                }
            } catch (IOException e) {
                System.err.println("Fail write output file:" + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Fail open input file: " + e.getMessage());
        }
    }

    private static String hashigFile(String fileName) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Wrong hash algorithm: " + e.getMessage());
        }
        try {
            Path path = Path.of(fileName);
        } catch (InvalidPathException e) {
            System.err.println("Fail open file: " + e.getMessage());
            return ERROR_HASH;
        }

        try (InputStream fis = newInputStream(Path.of(fileName))){
            int count;
            while((count=fis.read(buffer))!=-1) {
                for (int i = 0; i < count; i++) {
                    assert digest != null;
                    digest.update(buffer[i]);
                }
            }
        } catch (IOException e) {
            System.err.println("Fail read file: " + e.getMessage());
            return ERROR_HASH;
        }
        assert digest != null;
        byte[] hash = digest.digest();
        BigInteger sum =  new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(sum.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }
}
