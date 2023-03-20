import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import static java.nio.file.Files.*;

public class Walk {
    public static void main(String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            return;
        }
        ArrayList<String> paths = readFile(args[0]);
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
        try {
            file.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
            return output;
        }
        try (BufferedReader bufferedReader = newBufferedReader(Path.of(fileName), StandardCharsets.UTF_8)) {
            String line;
            while  ((line = bufferedReader.readLine()) != null) {
                output.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    private static void writeFile(String fileName, ArrayList<String> paths) {
        File file = new File(fileName);
        if (file.getParent() != null) {
            File outPath = new File(file.getParent());
            outPath.mkdirs();
        }
        try {
            file.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try (BufferedWriter bufferedWriter = newBufferedWriter(Path.of(fileName), StandardCharsets.UTF_8)) {
            for (String path : paths) {
                bufferedWriter.write(path);
                bufferedWriter.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] readPaths(String fileName) {
        ArrayList<Byte> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(fileName)){
            byte[] buffer = new byte[256];
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
            return "0000000000000000000000000000000000000000000000000000000000000000";
        }
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
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
