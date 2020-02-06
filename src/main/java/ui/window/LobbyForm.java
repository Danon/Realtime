package ui.window;

import network.LobbyEntry;
import ui.ILobbyObserver;
import ui.ILobbyOperator;
import ui.LobbyState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import static util.LookAndFeel.setLookAndFeel;

public class LobbyForm extends javax.swing.JFrame implements ILobbyObserver {
    private final ILobbyOperator lobby;
    private final List<LobbyState> lobbyStates = new ArrayList<>();
    private final DefaultListModel<String> chatModel = new DefaultListModel<>();

    private LobbyForm(ILobbyOperator lobbyOperator, Component alignment) {
        setLookAndFeel();
        initComponents();

        this.setLocationRelativeTo(alignment);
        this.lobbyStates.add(new LobbyState(lstTeamlist1));
        this.lobbyStates.add(new LobbyState(lstTeamlist2));
        this.lobbyStates.add(new LobbyState(lstTeamlist3));
        this.lobbyStates.add(new LobbyState(lstTeamlist4));

        this.lobby = lobbyOperator;
    }

    public LobbyForm(ILobbyOperator operator) {
        this(operator, null);
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        pnlTeamLookup1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstTeamlist1 = new javax.swing.JList<>();
        btnJoinTeam1 = new javax.swing.JButton();
        pnlChat = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtChatHistory = new javax.swing.JList<>(chatModel);
        txtChatText = new javax.swing.JTextField();
        pnlTeamLookup2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstTeamlist2 = new javax.swing.JList<>();
        btnJoinTeam2 = new javax.swing.JButton();
        pnlTeamLookup3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        lstTeamlist3 = new javax.swing.JList<>();
        btnJoinTeam3 = new javax.swing.JButton();
        pnlTeamLookup4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        lstTeamlist4 = new javax.swing.JList<>();
        btnJoinTeam4 = new javax.swing.JButton();
        btntReady = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Realtime | Chose your team");
        setName("frmLobby"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("<html>The game is about to beggin. Choose your teammates and side and setValues ready!!!</html>");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        pnlTeamLookup1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lstTeamlist1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lstTeamlist1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(lstTeamlist1);

        btnJoinTeam1.setText("Join \"Te Ahi\"");
        btnJoinTeam1.setName("joinTeam1"); // NOI18N
        btnJoinTeam1.addActionListener(this::btnJoinTeam1ActionPerformed);

        javax.swing.GroupLayout pnlTeamLookup1Layout = new javax.swing.GroupLayout(pnlTeamLookup1);
        pnlTeamLookup1.setLayout(pnlTeamLookup1Layout);
        pnlTeamLookup1Layout.setHorizontalGroup(
                pnlTeamLookup1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlTeamLookup1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlTeamLookup1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                                        .addComponent(btnJoinTeam1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        pnlTeamLookup1Layout.setVerticalGroup(
                pnlTeamLookup1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlTeamLookup1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnJoinTeam1)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlChat.setBorder(new javax.swing.border.MatteBorder(null));

        jScrollPane2.setViewportView(txtChatHistory);

        txtChatText.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtChatText.addKeyListener(new ChatTextKeyAdapter(txtChatText));

        javax.swing.GroupLayout pnlChatLayout = new javax.swing.GroupLayout(pnlChat);
        pnlChat.setLayout(pnlChatLayout);
        pnlChatLayout.setHorizontalGroup(
                pnlChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlChatLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2)
                                        .addComponent(txtChatText))
                                .addContainerGap())
        );
        pnlChatLayout.setVerticalGroup(
                pnlChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlChatLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtChatText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        pnlTeamLookup2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lstTeamlist2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lstTeamlist2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(lstTeamlist2);

        btnJoinTeam2.setText("Join \"Veseae\"");
        btnJoinTeam2.setName("joinTeam2"); // NOI18N
        btnJoinTeam2.addActionListener(this::btnJoinTeam2ActionPerformed);

        javax.swing.GroupLayout pnlTeamLookup2Layout = new javax.swing.GroupLayout(pnlTeamLookup2);
        pnlTeamLookup2.setLayout(pnlTeamLookup2Layout);
        pnlTeamLookup2Layout.setHorizontalGroup(
                pnlTeamLookup2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlTeamLookup2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlTeamLookup2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                                        .addComponent(btnJoinTeam2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        pnlTeamLookup2Layout.setVerticalGroup(
                pnlTeamLookup2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlTeamLookup2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnJoinTeam2)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlTeamLookup3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lstTeamlist3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lstTeamlist3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(lstTeamlist3);

        btnJoinTeam3.setText("Join \"Ddaear\"");
        btnJoinTeam3.setName("joinTeam3"); // NOI18N
        btnJoinTeam3.addActionListener(this::btnJoinTeam3ActionPerformed);

        javax.swing.GroupLayout pnlTeamLookup3Layout = new javax.swing.GroupLayout(pnlTeamLookup3);
        pnlTeamLookup3.setLayout(pnlTeamLookup3Layout);
        pnlTeamLookup3Layout.setHorizontalGroup(
                pnlTeamLookup3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlTeamLookup3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlTeamLookup3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                                        .addComponent(btnJoinTeam3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        pnlTeamLookup3Layout.setVerticalGroup(
                pnlTeamLookup3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlTeamLookup3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnJoinTeam3)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlTeamLookup4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lstTeamlist4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lstTeamlist4.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane5.setViewportView(lstTeamlist4);

        btnJoinTeam4.setText("Join \"Aria\"");
        btnJoinTeam4.setName("joinTeam4"); // NOI18N
        btnJoinTeam4.addActionListener(this::btnJoinTeam4ActionPerformed);

        javax.swing.GroupLayout pnlTeamLookup4Layout = new javax.swing.GroupLayout(pnlTeamLookup4);
        pnlTeamLookup4.setLayout(pnlTeamLookup4Layout);
        pnlTeamLookup4Layout.setHorizontalGroup(
                pnlTeamLookup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlTeamLookup4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlTeamLookup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                                        .addComponent(btnJoinTeam4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        pnlTeamLookup4Layout.setVerticalGroup(
                pnlTeamLookup4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlTeamLookup4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnJoinTeam4)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btntReady.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btntReady.setText("Ready");
        btntReady.addActionListener(this::btntReadyActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(pnlTeamLookup1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(pnlTeamLookup2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(pnlTeamLookup3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(pnlTeamLookup4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(btntReady, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pnlChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pnlChat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(pnlTeamLookup1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(pnlTeamLookup2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(pnlTeamLookup3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(pnlTeamLookup4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 140, Short.MAX_VALUE)
                                                .addComponent(btntReady)))
                                .addContainerGap())
        );

        pack();
    }

    @Override
    public void teamChanged(int userId, int previousTeamId, int currentTeamId, boolean readyForGame) {
        SwingUtilities.invokeLater(() -> {
            if (previousTeamId != LobbyEntry.ROOMLESS) {
                lobbyStates.get(previousTeamId - 1).removeUser(userId);
            }
            if (currentTeamId != LobbyEntry.ROOMLESS) {
                lobbyStates.get(currentTeamId - 1).addUser(userId, currentTeamId, readyForGame);
            }
        });
    }

    @Override
    public void teamSet(LobbyEntry[] teammates) {
        SwingUtilities.invokeLater(() -> {
            List<List<LobbyEntry>> teams = new ArrayList<List<LobbyEntry>>() {{
                add(new ArrayList<>());
                add(new ArrayList<>());
                add(new ArrayList<>());
                add(new ArrayList<>());
                add(new ArrayList<>());
            }};

            for (LobbyEntry state : teammates) {
                int id = state.getChosenTeamId();
                if (id != LobbyEntry.ROOMLESS) {
                    teams.get(id).add(state);
                }
            }

            for (int i = 1; i < teams.size(); i++) {
                lobbyStates.get(i - 1).setLobbyEntries(teams.get(i));
            }
        });
    }

    @Override
    public void receiveTextMessage(int userId, String text) {
        SwingUtilities.invokeLater(() -> chatModel.addElement(userId + ": " + text));
    }

    private void btntReadyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntReadyActionPerformed
        lobby.setReady(btntReady.isSelected());
    }//GEN-LAST:event_btntReadyActionPerformed

    private void btnJoinTeam1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinTeam1ActionPerformed
        lobby.joinTeam(1);
    }//GEN-LAST:event_btnJoinTeam1ActionPerformed

    private void btnJoinTeam2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinTeam2ActionPerformed
        lobby.joinTeam(2);
    }//GEN-LAST:event_btnJoinTeam2ActionPerformed

    private void btnJoinTeam3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinTeam3ActionPerformed
        lobby.joinTeam(3);
    }//GEN-LAST:event_btnJoinTeam3ActionPerformed

    private void btnJoinTeam4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinTeam4ActionPerformed
        lobby.joinTeam(4);
    }//GEN-LAST:event_btnJoinTeam4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnJoinTeam1;
    private javax.swing.JButton btnJoinTeam2;
    private javax.swing.JButton btnJoinTeam3;
    private javax.swing.JButton btnJoinTeam4;
    private javax.swing.JToggleButton btntReady;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JList<LobbyEntry> lstTeamlist1;
    private javax.swing.JList<LobbyEntry> lstTeamlist2;
    private javax.swing.JList<LobbyEntry> lstTeamlist3;
    private javax.swing.JList<LobbyEntry> lstTeamlist4;
    private javax.swing.JPanel pnlChat;
    private javax.swing.JPanel pnlTeamLookup1;
    private javax.swing.JPanel pnlTeamLookup2;
    private javax.swing.JPanel pnlTeamLookup3;
    private javax.swing.JPanel pnlTeamLookup4;
    private javax.swing.JList<String> txtChatHistory;
    private javax.swing.JTextField txtChatText;
    // End of variables declaration//GEN-END:variables

    private class ChatTextKeyAdapter implements KeyListener {
        private JTextField field;

        ChatTextKeyAdapter(JTextField txtChatText) {
            this.field = txtChatText;
        }

        @Override
        public void keyTyped(KeyEvent event) {
            if (event.getKeyChar() == KeyEvent.VK_ENTER) {
                String text = field.getText().trim();
                if (text.length() > 0) {
                    lobby.sendTextMessage(text);
                }
                field.setText("");
            }

            if (event.getKeyChar() == KeyEvent.VK_ESCAPE) {
                field.setText("");
            }
        }

        @Override
        public void keyPressed(KeyEvent event) {
        }

        @Override
        public void keyReleased(KeyEvent event) {
        }
    }
}
