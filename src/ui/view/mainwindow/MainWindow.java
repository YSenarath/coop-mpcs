/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.mainwindow;

import java.awt.Dimension;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.people.User;

import ui.view.inventory.ManageDepartment;
import ui.view.inventory.ManageProduct;
import ui.view.ledger.DamageStockInterface;
import ui.view.ledger.GRNInterface;
import ui.view.ledger.SupplierReturnNoteInterface;
import ui.view.supplier.SupplierInterface;

/**
 *
 * @author Nadheesh
 */
public class MainWindow extends javax.swing.JFrame {

    // <editor-fold defaultstate="collapsed" desc="Variables">
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MainWindow.class);
    private LogIn logIn;
    // </editor-fold>

    private ManageDepartment winManageDep;
    private ManageProduct winManagePro;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();

        initializeGUI();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //--------------------------------Internal Frames-----------------------
        try {
            winManageDep = new ManageDepartment();
            winManagePro = new ManageProduct();
        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        jDesktopPane1.add(winManageDep);
        jDesktopPane1.add(winManagePro);

        //-----------------------------------------------------------------------
    }

    public void setLogInWindow(LogIn logIn) {
        this.logIn = logIn;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        welcomePanel = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        mnuManageSupplier = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mnuNewGRN = new javax.swing.JMenuItem();
        mnuNewSRN = new javax.swing.JMenuItem();
        mnuAddDamagedStock = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(1300, 825));
        setName("frmMain"); // NOI18N

        welcomePanel.setBackground(new java.awt.Color(153, 153, 153));

        lblWelcome.setFont(new java.awt.Font("Tahoma", 2, 36)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(255, 255, 255));
        lblWelcome.setText("Welcome to MEGA COOP CITY POS");

        lblLogo.setBackground(new java.awt.Color(153, 153, 153));
        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout welcomePanelLayout = new javax.swing.GroupLayout(welcomePanel);
        welcomePanel.setLayout(welcomePanelLayout);
        welcomePanelLayout.setHorizontalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(welcomePanelLayout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                .addContainerGap())
        );
        welcomePanelLayout.setVerticalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(welcomePanelLayout.createSequentialGroup()
                .addGroup(welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, welcomePanelLayout.createSequentialGroup()
                        .addContainerGap(286, Short.MAX_VALUE)
                        .addComponent(lblWelcome))
                    .addGroup(welcomePanelLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 838, Short.MAX_VALUE)
            .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(welcomePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 399, Short.MAX_VALUE)
            .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(welcomePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDesktopPane1.setLayer(welcomePanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jMenu1.setText("File");

        mnuExit.setText("Exit");
        mnuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExitActionPerformed(evt);
            }
        });
        jMenu1.add(mnuExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Stock");

        jMenuItem1.setText("Manage Departments");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem2.setText("Manage Products");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        mnuManageSupplier.setText("Manage Suppliers");
        mnuManageSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuManageSupplierActionPerformed(evt);
            }
        });
        jMenu2.add(mnuManageSupplier);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Transactions");

        mnuNewGRN.setText("GoodRecieveNote");
        mnuNewGRN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNewGRNActionPerformed(evt);
            }
        });
        jMenu3.add(mnuNewGRN);

        mnuNewSRN.setText("SupplierReturnNote");
        mnuNewSRN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNewSRNActionPerformed(evt);
            }
        });
        jMenu3.add(mnuNewSRN);

        mnuAddDamagedStock.setText("DamagedStock");
        mnuAddDamagedStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAddDamagedStockActionPerformed(evt);
            }
        });
        jMenu3.add(mnuAddDamagedStock);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Reports");
        jMenuBar1.add(jMenu4);

        jMenu5.setText("Utilities");
        jMenuBar1.add(jMenu5);

        jMenu6.setText("Help");
        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_mnuExitActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        winManageDep.getDepIdCombo().setSelectedIndex(0);
        winManageDep.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        winManagePro.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void mnuManageSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManageSupplierActionPerformed
        createNewSupplier();
    }//GEN-LAST:event_mnuManageSupplierActionPerformed

    private void mnuNewGRNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNewGRNActionPerformed
        createNewGRN();
    }//GEN-LAST:event_mnuNewGRNActionPerformed

    private void mnuNewSRNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNewSRNActionPerformed
        createNewSRN();
    }//GEN-LAST:event_mnuNewSRNActionPerformed

    private void mnuAddDamagedStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAddDamagedStockActionPerformed
        createNewDamagedStock();
    }//GEN-LAST:event_mnuAddDamagedStockActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        Properties props = new Properties();

        props.put("logoString", "CO-OP");


        props.put("logoString", "");
        

        com.jtattoo.plaf.acryl.AcrylLookAndFeel.setCurrentTheme(props);

        try {
            com.jtattoo.plaf.graphite.GraphiteLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
            //UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
            // UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ManageDepartment.class.getName()).log(Level.SEVERE, null, ex);
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JMenuItem mnuAddDamagedStock;
    private javax.swing.JMenuItem mnuExit;
    private javax.swing.JMenuItem mnuManageSupplier;
    private javax.swing.JMenuItem mnuNewGRN;
    private javax.swing.JMenuItem mnuNewSRN;
    private javax.swing.JPanel welcomePanel;
    // End of variables declaration//GEN-END:variables

    private void createNewGRN() {

        GRNInterface grnInterface = new GRNInterface();

        setInternalFrameLocation(grnInterface);

        grnInterface.show();
    }

    private void createNewSupplier() {

        SupplierInterface supplierInterface = new SupplierInterface();

        setInternalFrameLocation(supplierInterface);

        supplierInterface.show();
    }

    private void createNewSRN() {
        SupplierReturnNoteInterface srnInterface = new SupplierReturnNoteInterface();

        setInternalFrameLocation(srnInterface);

        srnInterface.show();
    }

    private void createNewDamagedStock() {
        DamageStockInterface i = new DamageStockInterface();

        jDesktopPane1.add(i);

        setInternalFrameLocation(i);

        i.show();
    }

    private void initializeGUI() {
        logger.debug("initializeGUI invoked");
        if (logIn != null) {
            if (logIn.getLoggedUserType().equals(User.INVENTORY)) {
                logger.debug("Logged in as Inventory cleark");
            } else if (logIn.getLoggedUserType().equals(User.MANAGER)) {
                logger.debug("Logged in as  Manager");
            } else {
                logger.debug("Not logged in");
            }
        } else {
            logger.debug("Not logged in");
        }
    }

    private void setInternalFrameLocation(JInternalFrame frame) {
        Dimension desktopSize = jDesktopPane1.getSize();
        Dimension frameSize = frame.getSize();

        frame.setLocation((desktopSize.width - frameSize.width) / 2,
                (desktopSize.height - frameSize.height) / 2);

    }
}
