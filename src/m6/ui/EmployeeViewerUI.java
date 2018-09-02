package m6.ui;

import m6.UserLoginInfo;
import m6.table_model.AccountTableModel;
import m6.table_model.EmployeeTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EmployeeViewerUI extends UserBaseUI {

    private UserLoginInfo userLoginInfo;

    private JTable table;
    private JScrollPane scrollPane;

    public EmployeeViewerUI(UserLoginInfo userLoginInfo) {
        super(userLoginInfo);
        this.userLoginInfo = userLoginInfo;

        setPageTitle("View Employees");
        backButton.setVisible(true);
        backButton.addActionListener(this);

        initUI();
    }

    private void initUI() {
        table = new JTable(new EmployeeTableModel(this));
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));

        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 75, 765, 375);

        mainPanel.add(scrollPane);
    }

    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object src = e.getSource();

        if (src == backButton) {
            new EmployeeUI(userLoginInfo).setVisible(true);
            setVisible(false);
            dispose();
        }
    }

}
