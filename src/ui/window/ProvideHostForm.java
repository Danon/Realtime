package ui.window;

import ui.DiscoveredHostsListModel;
import ui.IHostObserver;
import ui.IHostOperator;
import ui.MessageBox;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class ProvideHostForm extends javax.swing.JFrame implements IHostObserver {
    private final IHostOperator hostOperator;
    private final DiscoveredHostsListModel discoveredHostsListModel = new DiscoveredHostsListModel();

    private ProvideHostForm(IHostOperator operator, Component alignment) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProvideHostForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        initComponents();

        setLocationRelativeTo(alignment);
        hostOperator = operator;
        lstAvialbleHosts.setModel(discoveredHostsListModel);
    }

    public ProvideHostForm(IHostOperator operator) {
        this(operator, null);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblSelectServer = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblIPorHostname = new javax.swing.JLabel();
        txtIpAddressOrHostname = new javax.swing.JTextField();
        btnConnectIP = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        lblDiscoverHost = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstAvialbleHosts = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Connect to server");
        setName("frmProvideHost"); // NOI18N
        setResizable(false);

        lblSelectServer.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblSelectServer.setForeground(new java.awt.Color(102, 102, 102));
        lblSelectServer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSelectServer.setText("Server Address:");
        lblSelectServer.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblSelectServer.setMaximumSize(new java.awt.Dimension(200, 29));
        lblSelectServer.setMinimumSize(new java.awt.Dimension(200, 29));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblIPorHostname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblIPorHostname.setLabelFor(txtIpAddressOrHostname);
        lblIPorHostname.setText("<html>Please type server's IP Address (ie. 172 12.31.234) or hostname (www.gameserv.com):</html>");

        txtIpAddressOrHostname.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtIpAddressOrHostname.setText("localhost");

        btnConnectIP.setText("Connect");
        btnConnectIP.addActionListener(this::btnConnectIPActionPerformed);

        lblDiscoverHost.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblDiscoverHost.setText("<html>Search your LAN network looking for avialble servers:</html>");

        btnRefresh.setText("Search");
        btnRefresh.addActionListener(this::btnRefreshActionPerformed);

        lstAvialbleHosts.addListSelectionListener(this::lstAvialbleHostsValueChanged);
        jScrollPane1.setViewportView(lstAvialbleHosts);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(lblSelectServer, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addComponent(txtIpAddressOrHostname, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(btnConnectIP, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addComponent(lblIPorHostname, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblDiscoverHost, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(29, 29, 29)
                                                .addComponent(lblSelectServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addComponent(lblIPorHostname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtIpAddressOrHostname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnConnectIP))
                                                .addGap(18, 18, 18)
                                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lblDiscoverHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnRefresh))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void provideHostList(List<InetAddress> discoveredHosts) {
        discoveredHostsListModel.setDiscoveredHosts(discoveredHosts);
    }

    @Override
    public void connected() {
        setVisible(false);
        dispose();
    }

    private void btnConnectIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectIPActionPerformed
        String text = txtIpAddressOrHostname.getText();
        try {
            InetAddress address = InetAddress.getByName(text);
            hostOperator.connectToHost(address);
        } catch (UnknownHostException e) {
            MessageBox.showFormat("Unknown host or address \"%s\"", text);
        }
    }//GEN-LAST:event_btnConnectIPActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        btnRefresh.setEnabled(false);
        new Thread(hostOperator::discoverHosts).start();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void lstAvialbleHostsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstAvialbleHostsValueChanged
        if (lstAvialbleHosts.getSelectedIndex() > -1) {
            InetAddress selected = lstAvialbleHosts.getSelectedValue();
            txtIpAddressOrHostname.setText(selected.getCanonicalHostName());
        }
    }//GEN-LAST:event_lstAvialbleHostsValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnectIP;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblDiscoverHost;
    private javax.swing.JLabel lblIPorHostname;
    private javax.swing.JLabel lblSelectServer;
    private javax.swing.JList<InetAddress> lstAvialbleHosts;
    private javax.swing.JTextField txtIpAddressOrHostname;
    // End of variables declaration//GEN-END:variables
}
