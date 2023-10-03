package bufferedreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class EditableBufferedReader extends BufferedReader {
 public static final int ESC_KEY = 27;
    public static final int SPACE_KEY = 32;
    public static final int BACKSPACE_KEY = 8;
    public static final int DELETE_KEY = 127;
    public static final int LEFT_ARROW_KEY = 68;
    public static final int RIGHT_ARROW_KEY = 67;
    public static final int BRACKET_KEY = 91;
    public static final int CR_KEY = 13;

    public EditableBufferedReader(Reader reader) {
        super(reader);
        enableRawMode();
    }

    public void enableRawMode() {
        try {
            String[] cmd = { "/bin/sh", "-c", "stty -echo raw </dev/tty" };
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableRawMode() {
        try {
            String[] cmd = { "/bin/sh", "-c", "stty echo cooked </dev/tty" };
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int read() throws IOException {
        int character = super.read();

        if (character == ESC_KEY) {
            int c1 = super.read();
            int c2 = super.read();

            if (c1 == BRACKET_KEY) {
                if (c2 == LEFT_ARROW_KEY) {
                    System.out.print("\033[D");
                } else if (c2 == RIGHT_ARROW_KEY) {
                    System.out.print("\033[C");
                }
            }
        } else if (character == DELETE_KEY || character == BACKSPACE_KEY) {
            System.out.print("\b \b");
        }

        return character;
    }

    @Override
    public String readLine() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int character;

        while ((character = read()) != -1 && character != '\n') {
            if (character == DELETE_KEY || character == BACKSPACE_KEY) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
            } else if (character == CR_KEY) {
                System.out.print("\r\n");
                break;
            } else {
                System.out.print((char) character);
                stringBuilder.append((char) character);
            }
        }
        disableRawMode();

        return stringBuilder.toString();
    }
}
