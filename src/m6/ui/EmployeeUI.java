package m6.ui;

import m6.ConnectionManager;
import m6.UserLoginInfo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.*;

/**
 * Home page for employees. Based on whether the employee is a manager or not, it'll automatically
 * show the relevant actions which he can perform.
 */
public class EmployeeUI extends UserBaseUI {

    private UserLoginInfo userLoginInfo;

    private JButton viewAccountBtn, editAccountBtn, addAccountBtn,
            viewCustomerBtn, editCustomerBtn, addCustomerBtn,
            viewEmployeeBtn, editEmployeeBtn, addEmployeeBtn,
            myInfoBtn;

    public EmployeeUI(UserLoginInfo userLoginInfo) {
        super(userLoginInfo);
        this.userLoginInfo = userLoginInfo;

        setPageTitle("Employee | Home");

        initUI();
        bind();
        readFromDb();
    }

    private void initUI() {
        final int x = 190;
        // Accounts
        viewAccountBtn = new JButton("View Accounts");
        viewAccountBtn.setBounds(x, 150, 120, 30);

        addAccountBtn = new JButton("Add Account");
        addAccountBtn.setBounds(x + 140, 150, 120, 30);

        editAccountBtn = new JButton("Edit Account");
        editAccountBtn.setBounds(x + 2 * 140, 150, 120, 30);

        // Customers
        viewCustomerBtn = new JButton("View Customers");
        viewCustomerBtn.setBounds(x, 200, 120, 30);

        addCustomerBtn = new JButton("Add Customers");
        addCustomerBtn.setBounds(x + 140, 200, 120, 30);

        editCustomerBtn = new JButton("Edit Customer");
        editCustomerBtn.setBounds(x + 2 * 140, 200, 120, 30);

        // Employees
        viewEmployeeBtn = new JButton("View Employees");
        viewEmployeeBtn.setBounds(x, 250, 120, 30);
        viewEmployeeBtn.setVisible(false);

        addEmployeeBtn = new JButton("Add Employee");
        addEmployeeBtn.setBounds(x + 140, 250, 120, 30);
        addEmployeeBtn.setVisible(false);

        editEmployeeBtn = new JButton("Edit Employee");
        editEmployeeBtn.setBounds(x + 2 * 140, 250, 120, 30);
        editEmployeeBtn.setVisible(false);

        myInfoBtn = new JButton("My Information");
        myInfoBtn.setBounds(x, 300, 400, 30);

        mainPanel.add(viewAccountBtn);
        mainPanel.add(addAccountBtn);
        mainPanel.add(editAccountBtn);

        mainPanel.add(viewCustomerBtn);
        mainPanel.add(addCustomerBtn);
        mainPanel.add(editCustomerBtn);

        mainPanel.add(viewEmployeeBtn);
        mainPanel.add(addEmployeeBtn);
        mainPanel.add(editEmployeeBtn);

        mainPanel.add(myInfoBtn);
    }

    private void bind() {
        viewAccountBtn.addActionListener(this);
        addAccountBtn.addActionListener(this);
        editAccountBtn.addActionListener(this);

        viewCustomerBtn.addActionListener(this);
        addCustomerBtn.addActionListener(this);
        editCustomerBtn.addActionListener(this);

        viewEmployeeBtn.addActionListener(this);
        addEmployeeBtn.addActionListener(this);
        editEmployeeBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object src = e.getSource();

        if (src == viewAccountBtn) {
            new AccountsViewerUI(userLoginInfo).setVisible(true);
            setVisible(false);
            dispose();
        } else if (src == viewCustomerBtn) {
            new CustomerViewerUI(userLoginInfo).setVisible(true);
            setVisible(false);
            dispose();
        } else if (src == viewEmployeeBtn) {
            new EmployeeViewerUI(userLoginInfo).setVisible(true);
            setVisible(false);
            dispose();
        } else if (src == editAccountBtn) {
            String accountNumber = JOptionPane.showInputDialog(this,"Account Number: ");

            if (verifyAccountNumber(accountNumber)) {
                new AccountEditorUI(userLoginInfo, accountNumber).setVisible(true);
                setVisible(false);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Account not found or account not specified.");
            }
        } else if (src == addAccountBtn) {
            new AccountEditorUI(userLoginInfo, null).setVisible(true);
            setVisible(false);
            dispose();
        } else if (src == addEmployeeBtn) {
            new EmployeeEditorUI(userLoginInfo, null).setVisible(true);
            setVisible(false);
            dispose();
        } else if (src == editEmployeeBtn) {
            String username = JOptionPane.showInputDialog(this,"Employee username: ");

            if (verifyEmployeeUsername(username)) {
                new EmployeeEditorUI(userLoginInfo, username).setVisible(true);
                setVisible(false);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Username not found or username not specified.");
            }
        } else if (src == addCustomerBtn) {
            new CustomerEditorUI(userLoginInfo, null).setVisible(true);
            setVisible(false);
            dispose();
        } else if (src == editCustomerBtn) {
            String username = JOptionPane.showInputDialog(this,"Customer username: ");

            if (verifyCustomerUsername(username)) {
                new CustomerEditorUI(userLoginInfo, username).setVisible(true);
                setVisible(false);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Username not found or username not specified.");
            }
        }
    }

    private boolean verifyAccountNumber(String accountNumber) {
        Connection conn = ConnectionManager.getInstance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT accountNumber FROM account WHERE accountNumber=?"
            );
            ps.setString(1, accountNumber);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rs.close();
                ps.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean verifyEmployeeUsername(String username) {
        Connection conn = ConnectionManager.getInstance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT username FROM employee WHERE username=?"
            );
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rs.close();
                ps.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean verifyCustomerUsername(String username) {
        Connection conn = ConnectionManager.getInstance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT username FROM customer WHERE username=?"
            );
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rs.close();
                ps.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Enables all manager related functions
     */
    private void enableManagerFunctions() {
        setPageTitle("Manager | Home");
        viewEmployeeBtn.setVisible(true);
        editEmployeeBtn.setVisible(true);
        addEmployeeBtn.setVisible(true);
    }

    private void readFromDb() {
        try {
            Connection conn = ConnectionManager.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM employee WHERE username=?"
            );
            ps.setString(1, userLoginInfo.username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                if (role.equals("manager")) {
                    // enable all manager functions, if the current user is a manager
                    enableManagerFunctions();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error! Failed to fetch data.");
        }
    }

}
