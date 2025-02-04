package electricity.billing;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class DepositDetail extends JFrame implements ActionListener {

    private Choice meternumber, cmonth;
    private JTable table;
    private JButton search, print;

    public DepositDetail() {
        super("Deposit Details");
        initializeUI();
        loadMeterNumbers();
        loadBillData();
    }

    private void initializeUI() {
        setSize(700, 700);
        setLocation(400, 100);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblmeternumber = new JLabel("Search By Meter Number");
        lblmeternumber.setBounds(20, 20, 150, 20);
        add(lblmeternumber);

        meternumber = new Choice();
        meternumber.setBounds(180, 20, 150, 20);
        add(meternumber);

        JLabel lblmonth = new JLabel("Search By Month");
        lblmonth.setBounds(400, 20, 100, 20);
        add(lblmonth);

        cmonth = new Choice();
        cmonth.setBounds(520, 20, 150, 20);
        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        for (String month : months) {
            cmonth.add(month);
        }
        add(cmonth);

        table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(0, 100, 700, 600);
        add(sp);

        search = new JButton("Search");
        search.setBounds(20, 70, 80, 20);
        search.addActionListener(this);
        add(search);

        print = new JButton("Print");
        print.setBounds(120, 70, 80, 20);
        print.addActionListener(this);
        add(print);

        setVisible(true);
    }

    private void loadMeterNumbers() {
        try (Conn c = new Conn();
             ResultSet rs = c.s.executeQuery("SELECT meter_no FROM customer")) {
            while (rs.next()) {
                meternumber.add(rs.getString("meter_no"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading meter numbers: " + e.getMessage());
        }
    }

    private void loadBillData() {
        try (Conn c = new Conn();
             ResultSet rs = c.s.executeQuery("SELECT * FROM bill")) {
            populateTable(rs);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading bill data: " + e.getMessage());
        }
    }

    private void populateTable(ResultSet rs) throws SQLException {
        // Create a table model
        DefaultTableModel model = new DefaultTableModel();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Add column names to the table model
        for (int i = 1; i <= columnCount; i++) {
            model.addColumn(metaData.getColumnName(i));
        }

        // Add rows to the table model
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }

        // Set the model to the table
        table.setModel(model);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == search) {
            searchBills();
        } else if (ae.getSource() == print) {
            printTable();
        }
    }

    private void searchBills() {
        String query = "SELECT * FROM bill WHERE meter_no = '" + meternumber.getSelectedItem() +
                "' AND month = '" + cmonth.getSelectedItem() + "'";
        try (Conn c = new Conn();
             ResultSet rs = c.s.executeQuery(query)) {
            populateTable(rs);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching search results: " + e.getMessage());
        }
    }

    private void printTable() {
        try {
            table.print();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error printing table: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new DepositDetail();
    }
}
