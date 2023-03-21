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


public class Walk {

    private final byte[] errorHash;
    private final byte[] buffer;
    private final MessageDigest digest;

    public Walk(int buffer_size) {
        buffer = new byte[buffer_size];
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new WalkException("Not found algorithm", e);
        }
        errorHash = new byte[digest.getDigestLength()];
    }

    public static void main(String[] args) {
        try {
            Walk walk = new Walk(4096);
            walk.run(args);
        } catch (WalkException e) {
            System.err.println(e.getMessage());
        }
    }

    private void run(String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            throw new WalkException("Bad args", null);
        }

        Path inputPath = getPath(args[0], "input");
        Path outputPath = getPath(args[1], "output");

        try {
            if (outputPath.getParent() != null) {
                Files.createDirectories(outputPath.getParent());
            }
        } catch (IOException e) {
            throw new WalkException("Fail make dirs", e);
        }

        try (BufferedReader bufferedReader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8)) {
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
                String file_path;
                // :NOTE: bufferedReader.readLine() error message
                while ((file_path = bufferedReader.readLine()) != null) {
                    byte[] hash = hashigFile(file_path);
                    bufferedWriter.write(convertHash(hash) + " " + file_path);
                    bufferedWriter.newLine();
                }
            } catch (IOException e) {
                System.err.println("Fail write output file:" + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Fail open input file: " + e.getMessage());
        }
    }

    private static Path getPath(String path, String name) {
        try {
            return Path.of(path);
        } catch (InvalidPathException e) {
            throw new WalkException("Fail convert " + name + " path", e);
        }
    }

    private byte[] hashigFile(String fileName) {
        Path path;
        try {
            path = getPath(fileName, "");
        } catch (Exception e) {
            System.err.println("Fail open file: " + e.getMessage());
            return errorHash;
        }

        digest.reset();

        try (InputStream fis = Files.newInputStream(path)) {
            int count;
            while ((count = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, count);
            }
        } catch (IOException e) {
            System.err.println("Fail read file: " + e.getMessage());
            return errorHash;
        }

        return digest.digest();
    }

    private String convertHash(byte[] hash) {
        BigInteger sum = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(sum.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    private static class WalkException extends RuntimeException {
        public WalkException(String message, Throwable e) {
            super(message, e);
        }
    }
}
