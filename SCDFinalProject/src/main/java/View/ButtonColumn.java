package View;


import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
    private JTable table;
    private JButton button;
    private String text;
    private int column;
    private ActionListener actionListener;

    public ButtonColumn(JTable table, ActionListener actionListener, int column) {
        this.table = table;
        this.column = column;
        this.actionListener = actionListener;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(this);
        table.getColumnModel().getColumn(column).setCellRenderer(this);
        table.getColumnModel().getColumn(column).setCellEditor(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
        actionListener.actionPerformed(e);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(UIManager.getColor("Button.background"));
        }

        text = (value == null) ? "" : value.toString();
        button.setText(text);
        return button;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        text = (value == null) ? "" : value.toString();
        button.setText(text);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return text;
    }
}
