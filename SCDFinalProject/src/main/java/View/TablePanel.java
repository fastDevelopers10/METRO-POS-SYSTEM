package View;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class TablePanel extends JPanel {
    private JTable table;
    private JLabel title;
    private JTextField searchField;
    private String filePath;
    private DefaultTableModel model;
    private Object[][] originalData;

    public TablePanel(String titleText, Object[][] data, String[] columnNames, String filePath) {
        this.filePath = filePath;
        if (data == null || data.length == 0) {
            data = new Object[1][columnNames.length];
        }

        this.originalData = data;
        this.init(titleText, data, columnNames);
    }

    private void init(String titleText, Object[][] data, final String[] columnNames) {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 0;
        gbc.fill = 1;
        gbc.insets = new Insets(3, 0, 3, 0);
        this.title = new JLabel(titleText);
        this.title.setFont(new Font("Arial", 1, 20));
        this.title.setHorizontalAlignment(0);
        gbc.weightx = 0.5;
        gbc.weighty = 0.08;
        this.add(this.title, gbc);
        this.searchField = new JTextField();
        this.searchField.setBackground(new Color(143, 217, 251));
        this.searchField.setCaretColor(Color.WHITE);
        this.searchField.setForeground(Color.WHITE);
        this.searchField.setFont(new Font("Arial", 0, 15));
        this.searchField.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        this.searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                TablePanel.this.filterData();
            }
        });
        gbc.weighty = 0.09;
        this.add(this.searchField, gbc);
        this.model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return column >= columnNames.length - 3;
            }
        };

        for(int i = 0; i < data.length; ++i) {
            this.model.setValueAt("Update", i, columnNames.length - 3);
            this.model.setValueAt("Delete", i, columnNames.length - 2);
        }

        this.table = new JTable(this.model);
        this.table.setPreferredScrollableViewportSize(new Dimension(500, 150));
        this.table.setFillsViewportHeight(true);
        this.table.getTableHeader().setReorderingAllowed(false);
        this.setupButtonColumn("Update", this::handleUpdate, true);
        this.setupButtonColumn("Delete", this::handleDelete, false);
        JScrollPane scrollPane = new JScrollPane(this.table, 20, 30);
        gbc.weighty = 0.9;
        this.add(scrollPane, gbc);
        this.setVisible(true);
    }

    private void filterData() {
        String searchTerm = this.searchField.getText().toLowerCase();
        List<Object[]> filteredData = new ArrayList();
        Object[][] var3 = this.originalData;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Object[] row = var3[var5];
            Object[] var7 = row;
            int var8 = row.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                Object value = var7[var9];
                if (value != null && value.toString().toLowerCase().contains(searchTerm)) {
                    filteredData.add(row);
                    break;
                }
            }
        }

        this.updateTableModel(filteredData);
    }

    private void updateTableModel(List<Object[]> filteredData) {
        this.model.setRowCount(0);
        Iterator var2 = filteredData.iterator();

        while(var2.hasNext()) {
            Object[] rowData = (Object[])var2.next();
            this.model.addRow(rowData);
        }

    }

    private void handleUpdate(int row) {
        if (row >= 0) {
            DefaultTableModel model = (DefaultTableModel)this.table.getModel();
            Object[] updatedRowData = new Object[model.getColumnCount() - 3];

            for(int i = 0; i < updatedRowData.length; ++i) {
                updatedRowData[i] = JOptionPane.showInputDialog(this, "Update " + model.getColumnName(i), model.getValueAt(row, i));
                if (updatedRowData[i] == null) {
                    return;
                }

                model.setValueAt(updatedRowData[i], row, i);
            }

            if (this.updateRecordsInDataSource(model)) {
                JOptionPane.showMessageDialog(this, "Record updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update record in file.");
            }

        }
    }

    private boolean updateRecordsInDataSource(DefaultTableModel model) {
        List<String> records = new ArrayList();
        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();

        for(int i = 0; i < rowCount; ++i) {
            StringBuilder recordBuilder = new StringBuilder();

            for(int j = 0; j < columnCount - 2; ++j) {
                recordBuilder.append(model.getValueAt(i, j));
                if (j < columnCount - 3) {
                    recordBuilder.append(",");
                }
            }

            records.add(recordBuilder.toString());
        }

        return this.writeRecordsToFile(records);
    }

    private boolean writeRecordsToFile(List<String> records) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.filePath));

            boolean var9;
            try {
                Iterator var3 = records.iterator();

                while(true) {
                    if (!var3.hasNext()) {
                        var9 = true;
                        break;
                    }

                    String record = (String)var3.next();
                    writer.write(record);
                    writer.newLine();
                }
            } catch (Throwable var6) {
                try {
                    writer.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }

                throw var6;
            }

            writer.close();
            return var9;
        } catch (IOException var7) {
            IOException e = var7;
            JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage());
            return false;
        }
    }

    private void handleDelete(int row) {
        DefaultTableModel model = (DefaultTableModel)this.table.getModel();
        if (model.getRowCount() != 0 && row >= 0 && row < model.getRowCount()) {
            int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the record at row " + (row + 1) + "?", "Confirm Deletion", 0);
            if (confirmation == 0) {
                model.removeRow(row);
                if (this.deleteRecordFromDataSource(row)) {
                    JOptionPane.showMessageDialog(this, "Record deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete record from file.");
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "No records to delete or invalid selection.");
        }
    }

    private boolean deleteRecordFromDataSource(int row) {
        List<String> records = this.readAllRecordsFromFile();
        if (row >= 0 && row < records.size()) {
            records.remove(row);
            return this.writeRecordsToFile(records);
        } else {
            return false;
        }
    }

    private List<String> readAllRecordsFromFile() {
        List<String> records = new ArrayList();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.filePath));

            String line;
            try {
                while((line = reader.readLine()) != null) {
                    records.add(line);
                }
            } catch (Throwable var6) {
                try {
                    reader.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }

                throw var6;
            }

            reader.close();
        } catch (IOException var7) {
            IOException e = var7;
            JOptionPane.showMessageDialog(this, "Error reading from file: " + e.getMessage());
        }

        return records;
    }

    private void setupButtonColumn(String buttonText, Consumer<Integer> action, boolean isUpdateButton) {
        int column = this.model.getColumnCount() - (isUpdateButton ? 3 : 2);
        this.table.getColumnModel().getColumn(column).setCellRenderer(new ButtonRenderer(buttonText));
        this.table.getColumnModel().getColumn(column).setCellEditor(new ButtonEditor(new JCheckBox(), buttonText, action));
        this.table.getColumnModel().getColumn(column).setPreferredWidth(100);
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String label) {
            this.setText(label);
            this.setOpaque(true);
            this.setForeground(Color.WHITE);
            this.setBackground(new Color(70, 130, 180));
            this.setPreferredSize(new Dimension(80, 30));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            this.setText(this.getText());
            if (isSelected) {
                this.setBackground(Color.DARK_GRAY);
                this.setForeground(Color.WHITE);
            } else {
                this.setBackground(new Color(70, 130, 180));
                this.setForeground(Color.WHITE);
            }

            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean isClicked;
        private String label;
        private int currentRow;
        private Consumer<Integer> action;

        public ButtonEditor(JCheckBox checkBox, String label, final Consumer<Integer> action) {
            super(checkBox);
            this.label = label;
            this.action = action;
            this.button = new JButton();
            this.button.setOpaque(true);
            this.button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ButtonEditor.this.fireEditingStopped();
                    action.accept(ButtonEditor.this.currentRow);
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.currentRow = row;
            this.button.setText(this.label);
            return this.button;
        }

        public boolean stopCellEditing() {
            this.isClicked = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
