import java.io.IOException;
import java.io.InputStreamReader;

public class TestReadLine {
    public static void main(String[] args) {
        Line line = new Line();
        Console console = new Console(line);
        EditableBufferedReader in = new EditableBufferedReader(new InputStreamReader(System.in), line);
        String str = null;
        try {
            str = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nline is: " + str);
    }
}

