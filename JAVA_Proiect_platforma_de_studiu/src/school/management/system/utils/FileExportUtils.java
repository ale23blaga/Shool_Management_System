package school.management.system.utils;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileWriter;
import java.io.IOException;

public class FileExportUtils {

    //Exportarea tabelelor sub fisierc cvs
    public static void exportTableToCSV(JTable table, String filePath) {
        try (FileWriter fw = new FileWriter(filePath)) {
            TableModel model = table.getModel();
            int columnCount = model.getColumnCount();

            // Write header
            for (int i = 0; i < columnCount; i++) {
                fw.write(model.getColumnName(i));
                if (i < columnCount - 1) fw.write(",");
            }
            fw.write("\n");

            // Write rows
            int rowCount = model.getRowCount();
            for (int r = 0; r < rowCount; r++) {
                for (int c = 0; c < columnCount; c++) {
                    Object value = model.getValueAt(r, c);
                    fw.write(value == null ? "" : value.toString());
                    if (c < columnCount - 1) fw.write(",");
                }
                fw.write("\n");
            }
            fw.flush();
            JOptionPane.showMessageDialog(null, "Export reușit: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Eroare la export: " + e.getMessage());
        }
    }


}
