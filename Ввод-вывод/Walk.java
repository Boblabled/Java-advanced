import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import static java.nio.file.Files.*;

public class Walk {

    private static final String ERROR_HASH = "0".repeat(64);

    public Walk() {
        // :NOTE: init
    }

    public static void main(String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            return;
        }

//        new Walk().run(args)

        // :NOTE: all files readed
        ArrayList<String> paths = readFile(args[0]);
        // :NOTE: all writed file, BufferedWriter
        ArrayList<String> output = new ArrayList<>();
        for (String path : paths) {
            StringBuilder element = new StringBuilder();
            element.append(encode(readPaths(path)));
            element.append(" ");
            element.append(path);
            output.add(element.toString());
        }
        writeFile(args[1], output);
    }

    private static ArrayList<String> readFile(String fileName) {
        ArrayList<String> output = new ArrayList<>();
        File file = new File(fileName);
        if (!file.canRead()) {
            return output;
        }
        // filename = "> \0"
        // :NOTE: содержательные ошибки
        // :NOTE: Path.of cat throw InvalidPathException
        try (BufferedReader bufferedReader = newBufferedReader(Path.of(fileName), StandardCharsets.UTF_8)) {
            String line;
            while  ((line = bufferedReader.readLine()) != null) {
                output.add(line);
            }
        } catch (IOException e) {
            System.err.println("Fail open output file: " + e.getMessage());
        }
        return output;
    }

    private static void writeFile(String fileName, ArrayList<String> paths) {
        File file = new File(fileName);
        if (file.getParent() != null) {
            File outPath = new File(file.getParent());
//            Files.createDirectories(outPath)
            outPath.mkdirs();
        }
        try {
            file.createNewFile();
        }
        catch (IOException e) {
            // :NOTE: error message
            e.printStackTrace();
            return;
        }
        try (BufferedWriter bufferedWriter = newBufferedWriter(Path.of(fileName), StandardCharsets.UTF_8)) {
            for (String path : paths) {
                bufferedWriter.write(path);
                // :NOTE: don't use \n
                bufferedWriter.write("\n");
            }
        } catch (IOException e) {
            // :NOTE: содержательные ошибки
            e.printStackTrace();
        }
    }

    private static byte[] readPaths(String fileName) {
        ArrayList<Byte> list = new ArrayList<>();
        // :NOTE: io and nio, best usage nio
        // :NOTE: don't read all file in RAM
        try (FileInputStream fis = new FileInputStream(fileName)){
            // :NOTE: best way create buffer in constructor
            byte[] buffer = new byte[4096];
            int count;
            while((count=fis.read(buffer))!=-1) {
                for (int i = 0; i < count; i++) {
                     list.add(buffer[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        byte[] output = new byte[list.size()];
        for(int i = 0; i < list.size(); i++) {
            output[i] = list.get(i);
        }
        return output;
    }

    private static String encode(byte[] input){
        if (input == null) {
            // :NOTE: constant
            return ERROR_HASH;
        }
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // :NOTE: error
            e.printStackTrace();
        }
        assert digest != null;
        byte[] hash = digest.digest(input);
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

}
