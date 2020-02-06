package ui.window;

import ui.IHostOperator;
import ui.MessageBox;
import util.LookAndFeel;

import java.util.Arrays;

import static java.awt.Font.PLAIN;
import static util.LookAndFeel.setLookAndFeel;

public class ServerLoginForm extends javax.swing.JFrame {
    private IHostOperator operator;

    private ServerLoginForm(IHostOperator operator, java.awt.Component alignment) {
        setLookAndFeel();
        initComponents();
        this.setLocationRelativeTo(alignment);
        this.operator = operator;
    }

    public ServerLoginForm(IHostOperator operator) {
        this(operator, null);
    }

    // <editor-fold defaultstate="collapsed" desc="initComponents() ">
    private void initComponents() {

        lblSelectServer = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        lblUsernameAndPassword = new javax.swing.JLabel();
        lblRegister = new javax.swing.JLabel();
        lblResetPassword = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Realtime | Login to server");
        setName("frmLogin"); // NOI18N
        setResizable(false);

        lblSelectServer.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblSelectServer.setForeground(new java.awt.Color(102, 102, 102));
        lblSelectServer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSelectServer.setText("Login to server:");
        lblSelectServer.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblSelectServer.setMaximumSize(new java.awt.Dimension(200, 29));
        lblSelectServer.setMinimumSize(new java.awt.Dimension(200, 29));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        txtUsername.setFont(new java.awt.Font("Tahoma", PLAIN, 14)); // NOI18N
        txtUsername.setText("Test");

        txtPassword.setFont(new java.awt.Font("Tahoma", PLAIN, 14)); // NOI18N
        txtPassword.setText("test");

        btnLogin.setText("Login");
        btnLogin.addActionListener(this::btnLoginActionPerformed);

        lblUsernameAndPassword.setFont(new java.awt.Font("Tahoma", PLAIN, 12)); // NOI18N
        lblUsernameAndPassword.setLabelFor(txtUsername);
        lblUsernameAndPassword.setText("<html>Please type your username and password to an existing account:</html>");

        lblRegister.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblRegister.setForeground(new java.awt.Color(51, 51, 51));
        lblRegister.setText("<html><u>I have no account</u></html>");
        lblRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegisterMouseClicked(evt);
            }
        });

        lblResetPassword.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblResetPassword.setForeground(new java.awt.Color(51, 51, 51));
        lblResetPassword.setText("<html><u>Reset password</u></html>");
        lblResetPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblResetPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblResetPasswordMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblSelectServer, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblRegister, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblResetPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(28, 28, 28))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(0, 79, Short.MAX_VALUE))
                                                        .addComponent(lblUsernameAndPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                                .addContainerGap())))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(29, 29, 29)
                                                                .addComponent(lblSelectServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(lblUsernameAndPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(35, 35, 35)
                                                                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnLogin))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(51, 51, 51)
                                                                .addComponent(lblRegister, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(lblResetPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }
    //</editor-fold>

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        String username = txtUsername.getText();
        char[] password = txtPassword.getPassword();

        if (username.length() < 3 || username.length() > 20) {
            MessageBox.show("Username must be within the range of 3-20 characters.");
            return;
        }

        if (password.length < 3 || password.length > 64) {
            MessageBox.show("Password must be within the range of 3-64 characters.");
            return;
        }

        operator.loginToHost(username, Arrays.toString(password));
        this.setVisible(false);
    }//GEN-LAST:event_btnLoginActionPerformed

    private void lblRegisterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegisterMouseClicked
        new ServerRegisterForm(operator).setVisible(true);
    }//GEN-LAST:event_lblRegisterMouseClicked

    private void lblResetPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblResetPasswordMouseClicked
        MessageBox.show("This server doesn't support password reseting.");
    }//GEN-LAST:event_lblResetPasswordMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblRegister;
    private javax.swing.JLabel lblResetPassword;
    private javax.swing.JLabel lblSelectServer;
    private javax.swing.JLabel lblUsernameAndPassword;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
