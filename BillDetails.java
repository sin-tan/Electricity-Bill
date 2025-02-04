package electricity.billing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BillDetails extends JFrame {

    BillDetails(String meter) {
        setSize(700, 650);
        setLocation(400, 150);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();

        try (Conn c = new Conn()) { // Use try-with-resources
            if (c == null) {
                throw new SQLException("Database connection not initialized!");
            }

            String query = "SELECT * FROM bill WHERE meter_no = ?";
            PreparedStatement pst = c.prepareStatement(query);
            pst.setString(1, meter);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Add columns
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(rsmd.getColumnName(i));
            }

            // Add rows
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            table.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unexpected Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(0, 0, 700, 650);
        add(sp);

        setVisible(true);
    }

    public static void main(String[] args) {
        new BillDetails("12345"); // Replace with an actual meter number for testing
    }
}
