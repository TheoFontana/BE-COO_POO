import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class CellRenderer extends JLabel implements ListCellRenderer<Object> {
    public CellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Color background;
        Color foreground;
        setText(value.toString());
        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index) {
            background = new Color(236, 240, 241);
            foreground = new Color(44, 62, 80);
        } else if (isSelected) {
            background = new Color(236, 240, 241);
            foreground = new Color(44, 62, 80);
        } else {
            background = new Color(44, 62, 80);
            foreground = new Color(236, 240, 241);
        }
        setBackground(background);
        setForeground(foreground);
        return this;
    }
}
