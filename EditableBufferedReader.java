import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class EditableBufferedReader extends BufferedReader {

    private static final int RIGHT_ARROW = 1000;
    private static final int LEFT_ARROW = 1001;
    private static final int HOME = 1002;
    private static final int END = 1003;
    private static final int INSERT_MODE = 1004;
    private static final int DELETE = 1005;
    private static final int BACKSPACE = 1006;

    private Line line;

    public EditableBufferedReader(Reader in, Line line) {
        super(in);
        this.line = line;
    }

    public void setRaw() {
        executeCommand("stty -echo raw </dev/tty");
    }

    public void unsetRaw() {
        executeCommand("stty echo cooked </dev/tty");
    }

    private void executeCommand(String command) {
        try {
            String[] cmd = {"/bin/sh", "-c", command};
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int read() throws IOException {
        int readChar = super.read();
        if (readChar == 27) {
            return handleEscapeSequence();
        } else if (readChar == 127) {
            return BACKSPACE;
        }else if (readChar == 9){ //Ctrl + I
            return INSERT_MODE;
        }
        return readChar;
    }

    private int handleEscapeSequence() throws IOException {
        super.read(); // discard
        int command = super.read();
        switch (command) {
            case 67:
                return RIGHT_ARROW;
            case 68:
                return LEFT_ARROW;
            case 65:
                return HOME;
            case 66:
                return END;
            case 50:
                super.read();
                return INSERT_MODE;
            case 51:
                super.read();
                return DELETE;
            default:
                return -1;
        }
    }

    @Override
    public String readLine() throws IOException {
        setRaw();
        int intRead;
        while (true) {
            intRead = read();
            handleKeyInput(intRead);
            if (intRead == '\r' || intRead == '\n') {
                unsetRaw();
                return line.getLineContent();
            }
        }
    }

    private void handleKeyInput(int key) {
        switch (key) {
            case RIGHT_ARROW:
                line.moveCursorRight();
                break;
            case LEFT_ARROW:
                line.moveCursorLeft();
                break;
            case HOME:
                line.setCursorPosition(0);
                break;
            case END:
                line.setCursorPosition(line.getLineContent().length());
                break;
            case INSERT_MODE:
                line.toggleInsertMode();
                break;
            case DELETE:
                line.removeCharacterAt(line.getCursorPosition());
                break;
            case BACKSPACE:
                line.removeCharacterAt(line.getCursorPosition() - 1);
                break;
            default:
                line.addCharacterAt(line.getCursorPosition(), (char) key);
        }
    }
}
