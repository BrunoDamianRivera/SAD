import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class Console implements PropertyChangeListener {
    private Line lineModel;

    public Console(Line lineModel) {
        this.lineModel = lineModel;
        lineModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Se ha detectado un cambio en la propiedad 'line'.
        if ("line".equals(evt.getPropertyName())) {
            clearCurrentLineInConsole();
            System.out.print(evt.getNewValue());
            moveCursorToPosition(lineModel.getCursorPosition());
        }
        // Se ha detectado un cambio en la propiedad 'cursorPosition'.
        else if ("cursorPosition".equals(evt.getPropertyName())) {
            moveCursorToPosition((int) evt.getNewValue());
        }
    }

    private String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }

    private void clearCurrentLineInConsole() {
        // Limpia la línea actual usando espacios y vuelve al inicio de la línea.
        System.out.print("\r" + repeat(" ", 80) + "\r");
    }

    private void moveCursorToPosition(int position) {
        // Usa secuencias de escape ANSI para mover el cursor a la posición.
        System.out.print("\033[" + (position + 1) + "G");
    }
}
