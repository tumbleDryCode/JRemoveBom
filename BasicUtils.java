
import java.io.*;

public class BasicUtils {


    public String udString;
    public String fsString;

    public BasicUtils() {
        udString = System.getProperty("user.dir");
        fsString = File.separator;
        System.out.println("udString: " + udString);
        System.out.println("fsString: " + fsString);
    }

    public static void main(String argv[]) {
        new BasicUtils();
    }

    public String getWorkingDir() {
        return System.getProperty("user.dir");
    }

    public String getUfile(String s) {

        String uFileString;
        String cleanFileString = replaceString(s, "/", fsString);
        cleanFileString = replaceString(cleanFileString, "\\", fsString);
        uFileString = udString + fsString + cleanFileString;
        return uFileString;
    }

    public String replaceString(String s, String s1, String s2) {
        String s3 = s;
        if (s3 != null && s3.length() > 0) {
            int i = 0;
            do {
                int j = s3.indexOf(s1, i);
                if (j == -1)
                    break;
                s3 = s3.substring(0, j) + s2 + s3.substring(j + s1.length());
                i = j + s2.length();
            } while (true);
        }
        return s3;
    }

    public void saveTextString(String theTextString, String fileNameString) {
        try {

            FileOutputStream tbeureekas_stream = new FileOutputStream(fileNameString, false);
            PrintStream tbeureekas_pstream = new PrintStream(tbeureekas_stream);
            tbeureekas_pstream.println(theTextString);
            tbeureekas_pstream.close();
            tbeureekas_stream.close();

        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public String readFileAsString(String filePath)
            throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

}