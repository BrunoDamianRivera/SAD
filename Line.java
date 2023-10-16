import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Line {

    private StringBuilder lineContent;
    private PropertyChangeSupport support;
    private int cursorPosition;
    private boolean insertMode;



    public Line() {
        this.lineContent = new StringBuilder();
        this.support = new PropertyChangeSupport(this);
        this.cursorPosition = 0;
        this.insertMode = true;

    }

    public void toggleInsertMode(){
        this.insertMode = !this.insertMode;
        //System.out.println("Insert Mode: " + (insertMode ? "ON" : "OFF"));
    }

    public String getLineContent() {
        return lineContent.toString();
    }


    public int getCursorPosition() {
        return cursorPosition;
    }

    public void moveCursorLeft() {
        if (cursorPosition > 0) {
            cursorPosition--;
            // Notificar a los oyentes sobre el cambio en la posición del cursor.
            support.firePropertyChange("line", null, getLineContent());
        }
    }

    public void moveCursorRight() {
        if (cursorPosition < lineContent.length()) {
            cursorPosition++;
            // Notificar a los oyentes sobre el cambio en la posición del cursor.
            support.firePropertyChange("line", null, getLineContent());
        }
    }
    public void addCharacterAt(int pos, char c) {
        String oldLine = lineContent.toString();
        if(insertMode || pos == lineContent.length()){
            lineContent.insert(pos, c);
        } else {
            lineContent.setCharAt(pos, c);
        }
        cursorPosition = pos + 1;  // update cursor position
        support.firePropertyChange("line", oldLine, lineContent.toString());
    }

    public void removeCharacterAt(int pos) {
        if (lineContent.length() > 0 && pos < lineContent.length() && pos >= 0) {
            String oldLine = lineContent.toString();
            lineContent.deleteCharAt(pos);
            cursorPosition = pos;  // update cursor position
            support.firePropertyChange("line", oldLine, lineContent.toString());
        }
    }

    public void setCursorPosition(int newCursorPosition) {
        if (newCursorPosition >= 0 && newCursorPosition <= lineContent.length()) {
            int oldCursorPosition = this.cursorPosition;
            this.cursorPosition = newCursorPosition;

            support.firePropertyChange("cursorPosition", oldCursorPosition, newCursorPosition);
        }

    }


    public void setLine(String newLine) {
        String oldLine = lineContent.toString();
        lineContent = new StringBuilder(newLine);
        support.firePropertyChange("line", oldLine, newLine);
    }


    public void addCharacter(char c) {
        String oldLine = lineContent.toString();
        lineContent.append(c);
        support.firePropertyChange("line", oldLine, lineContent.toString());
    }

    public void removeCharacter() {
        // Comprobamos si hay caracteres para eliminar.
        if (lineContent.length() > 0) {
            // Guardamos el estado actual de la línea para poder notificar a los observadores sobre el cambio.
            String oldLine = lineContent.toString();

            // Eliminamos el último carácter.
            lineContent.deleteCharAt(lineContent.length() - 1);

            // Notificamos a los observadores sobre el cambio en la propiedad "line".
            support.firePropertyChange("line", oldLine, lineContent.toString());
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }



}
