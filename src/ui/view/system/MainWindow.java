package ui.view.system;

import java.awt.Dimension;
import static java.awt.SystemColor.window;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.people.User;
import net.sf.jcarrierpigeon.Notification;
import net.sf.jcarrierpigeon.NotificationQueue;
import net.sf.jcarrierpigeon.WindowPosition;
import ui.view.credit.FinalCredit;
import ui.view.employee.ManageEmployees;
import ui.view.inventory.DiscountInterface;
import ui.view.inventory.ManageDepartment;
import ui.view.inventory.ManageProduct;
import ui.view.inventory.NotificationPanel;
import ui.view.ledger.DamageStockInterface;
import ui.view.ledger.GRNCancelInterface;
import ui.view.ledger.GRNInterface;
import ui.view.ledger.SupplierReturnNoteInterface;
import ui.view.report.GRNListingRequestInterfae;
import ui.view.report.GRNRequestInterface;
import ui.view.report.SRNListingRequestInterfae;
import ui.view.settings.settings;
import ui.view.supplier.SupplierInterface;
import ui.view.user.ManageUsers;

public class MainWindow extends javax.swing.JFrame {

    // <editor-fold defaultstate="collapsed" desc="Variables">
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MainWindow.class);
    private LogIn logIn;
    // </editor-fold>

    private ManageDepartment winManageDep;
    private ManageProduct winManagePro;
    private DiscountInterface winDiscount;
    private NotificationPanel winNotification;

    public MainWindow() {

        initComponents();
        initializeGUI();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //--------------------------------Internal Frames == Inventory-----------------------
        try {
            winManageDep = new ManageDepartment();
            winManagePro = new ManageProduct();
            winDiscount = new DiscountInterface();
            winNotification = new NotificationPanel();
        } catch (SQLException ex) {
            logger.error(ex);
        }

        mainDesktopPanel.add(winDiscount);
        mainDesktopPanel.add(winManageDep);
        mainDesktopPanel.add(winManagePro);
        mainDesktopPanel.add(winNotification);

        setInternalFrameLocation(winManageDep);
        setInternalFrameLocation(winManagePro);
        setInternalFrameLocation(winDiscount);
        setInternalFrameLocation(winNotification);
        //-----------------------------------------------------------------------
    }

    public void setLogInWindow(LogIn logIn) {
        this.logIn = logIn;
    }

    // <editor-fold defaultstate="collapsed" desc="Netbeans generated code">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainDesktopPanel = new javax.swing.JDesktopPane();
        welcomePanel = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuLogOff = new javax.swing.JMenuItem();
        mnuExit = new javax.swing.JMenuItem();
        mnuStock = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        mnuManageSupplier = new javax.swing.JMenuItem();
        setDiscountMenu = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        mnuCredit = new javax.swing.JMenu();
        mnuEmployees = new javax.swing.JMenuItem();
        mnuCoopCredit = new javax.swing.JMenuItem();
        mnuLedgers = new javax.swing.JMenu();
        mnuNewGRN = new javax.swing.JMenuItem();
        grnCancel = new javax.swing.JMenuItem();
        mnuNewSRN = new javax.swing.JMenuItem();
        mnuAddDamagedStock = new javax.swing.JMenuItem();
        mnuReports = new javax.swing.JMenu();
        mnuGRNRequest = new javax.swing.JMenuItem();
        mnuGrnList = new javax.swing.JMenuItem();
        mnuSrnListing = new javax.swing.JMenuItem();
        mnuUtilities = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        mnuSettings = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(1300, 825));
        setName("frmMain"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                MainWindow.this.windowClosed(evt);
            }
        });

        welcomePanel.setBackground(new java.awt.Color(153, 153, 153));

        lblWelcome.setFont(new java.awt.Font("Tahoma", 2, 36)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(255, 255, 255));
        lblWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWelcome.setText("Welcome to MEGA COOP CITY ");

        javax.swing.GroupLayout welcomePanelLayout = new javax.swing.GroupLayout(welcomePanel);
        welcomePanel.setLayout(welcomePanelLayout);
        welcomePanelLayout.setHorizontalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblWelcome, javax.swing.GroupLayout.DEFAULT_SIZE, 838, Short.MAX_VALUE)
        );
        welcomePanelLayout.setVerticalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, welcomePanelLayout.createSequentialGroup()
                .addContainerGap(450, Short.MAX_VALUE)
                .addComponent(lblWelcome)
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout mainDesktopPanelLayout = new javax.swing.GroupLayout(mainDesktopPanel);
        mainDesktopPanel.setLayout(mainDesktopPanelLayout);
        mainDesktopPanelLayout.setHorizontalGroup(
            mainDesktopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 838, Short.MAX_VALUE)
            .addGroup(mainDesktopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(welcomePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainDesktopPanelLayout.setVerticalGroup(
            mainDesktopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 528, Short.MAX_VALUE)
            .addGroup(mainDesktopPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(welcomePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainDesktopPanel.setLayer(welcomePanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jMenu1.setText("File");

        mnuLogOff.setText("Log Off");
        mnuLogOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLogOffActionPerformed(evt);
            }
        });
        jMenu1.add(mnuLogOff);

        mnuExit.setText("Exit");
        mnuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExitActionPerformed(evt);
            }
        });
        jMenu1.add(mnuExit);

        jMenuBar1.add(jMenu1);

        mnuStock.setText("Stock");

        jMenuItem1.setText("Departments");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        mnuStock.add(jMenuItem1);

        jMenuItem2.setText("Products");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        mnuStock.add(jMenuItem2);

        mnuManageSupplier.setText("Suppliers");
        mnuManageSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuManageSupplierActionPerformed(evt);
            }
        });
        mnuStock.add(mnuManageSupplier);

        setDiscountMenu.setText("Discount");
        setDiscountMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDiscountMenuActionPerformed(evt);
            }
        });
        mnuStock.add(setDiscountMenu);

        jMenuItem3.setText("Notification");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        mnuStock.add(jMenuItem3);

        jMenuBar1.add(mnuStock);

        mnuCredit.setText("Credit");
        mnuCredit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCreditActionPerformed(evt);
            }
        });

        mnuEmployees.setText("Employees");
        mnuEmployees.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuEmployeesActionPerformed(evt);
            }
        });
        mnuCredit.add(mnuEmployees);

        mnuCoopCredit.setText("Coop Credit");
        mnuCoopCredit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCoopCreditActionPerformed(evt);
            }
        });
        mnuCredit.add(mnuCoopCredit);

        jMenuBar1.add(mnuCredit);

        mnuLedgers.setText("Transactions");

        mnuNewGRN.setText("GoodRecieveNote");
        mnuNewGRN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNewGRNActionPerformed(evt);
            }
        });
        mnuLedgers.add(mnuNewGRN);

        grnCancel.setText("Good Recive Note Cancel");
        grnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grnCancelActionPerformed(evt);
            }
        });
        mnuLedgers.add(grnCancel);

        mnuNewSRN.setText("SupplierReturnNote");
        mnuNewSRN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNewSRNActionPerformed(evt);
            }
        });
        mnuLedgers.add(mnuNewSRN);

        mnuAddDamagedStock.setText("DamagedStock");
        mnuAddDamagedStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAddDamagedStockActionPerformed(evt);
            }
        });
        mnuLedgers.add(mnuAddDamagedStock);

        jMenuBar1.add(mnuLedgers);

        mnuReports.setText("Reports");

        mnuGRNRequest.setText("GRN Request");
        mnuGRNRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGRNRequestActionPerformed(evt);
            }
        });
        mnuReports.add(mnuGRNRequest);

        mnuGrnList.setText("GRN Listing");
        mnuGrnList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGrnListActionPerformed(evt);
            }
        });
        mnuReports.add(mnuGrnList);

        mnuSrnListing.setText("SRN Listing");
        mnuSrnListing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSrnListingActionPerformed(evt);
            }
        });
        mnuReports.add(mnuSrnListing);

        jMenuBar1.add(mnuReports);

        mnuUtilities.setText("Utilities");

        jMenuItem4.setText("User Management");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        mnuUtilities.add(jMenuItem4);

        mnuSettings.setText("Settings");
        mnuSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSettingsActionPerformed(evt);
            }
        });
        mnuUtilities.add(mnuSettings);

        jMenuBar1.add(mnuUtilities);

        jMenu6.setText("Help");
        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainDesktopPanel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainDesktopPanel, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
// </editor-fold> 

    private void mnuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExitActionPerformed
        System.exit(0);
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

    private void mnuLogOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLogOffActionPerformed
        this.dispose();
        logIn.setVisible(true);
    }//GEN-LAST:event_mnuLogOffActionPerformed

    private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed

    }//GEN-LAST:event_windowClosed

    private void setDiscountMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDiscountMenuActionPerformed
        winDiscount.setVisible(true);
    }//GEN-LAST:event_setDiscountMenuActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        winNotification.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void mnuNewGRNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNewGRNActionPerformed
        createNewGRN();
    }//GEN-LAST:event_mnuNewGRNActionPerformed

    private void grnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grnCancelActionPerformed
        createNewGRNCancel();
    }//GEN-LAST:event_grnCancelActionPerformed

    private void mnuNewSRNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNewSRNActionPerformed
        createNewSRN();
    }//GEN-LAST:event_mnuNewSRNActionPerformed

    private void mnuAddDamagedStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAddDamagedStockActionPerformed
        createNewDamagedStock();
    }//GEN-LAST:event_mnuAddDamagedStockActionPerformed

    private void mnuGRNRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGRNRequestActionPerformed
        createGRNPrint();
    }//GEN-LAST:event_mnuGRNRequestActionPerformed

    private void mnuGrnListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGrnListActionPerformed
        createGRNList();
    }//GEN-LAST:event_mnuGrnListActionPerformed

    private void mnuSrnListingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSrnListingActionPerformed
        createSRNList();
    }//GEN-LAST:event_mnuSrnListingActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        createUserManagement();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void mnuCreditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCreditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuCreditActionPerformed

    private void mnuCoopCreditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCoopCreditActionPerformed
        createFinalCredit();
    }//GEN-LAST:event_mnuCoopCreditActionPerformed

    private void mnuEmployeesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuEmployeesActionPerformed
        createEmployeeManagement();
    }//GEN-LAST:event_mnuEmployeesActionPerformed

    private void mnuSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSettingsActionPerformed
        createSttings();
    }//GEN-LAST:event_mnuSettingsActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        Properties props = new Properties();

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
    private javax.swing.JMenuItem grnCancel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JDesktopPane mainDesktopPanel;
    private javax.swing.JMenuItem mnuAddDamagedStock;
    private javax.swing.JMenuItem mnuCoopCredit;
    private javax.swing.JMenu mnuCredit;
    private javax.swing.JMenuItem mnuEmployees;
    private javax.swing.JMenuItem mnuExit;
    private javax.swing.JMenuItem mnuGRNRequest;
    private javax.swing.JMenuItem mnuGrnList;
    private javax.swing.JMenu mnuLedgers;
    private javax.swing.JMenuItem mnuLogOff;
    private javax.swing.JMenuItem mnuManageSupplier;
    private javax.swing.JMenuItem mnuNewGRN;
    private javax.swing.JMenuItem mnuNewSRN;
    private javax.swing.JMenu mnuReports;
    private javax.swing.JMenuItem mnuSettings;
    private javax.swing.JMenuItem mnuSrnListing;
    private javax.swing.JMenu mnuStock;
    private javax.swing.JMenu mnuUtilities;
    private javax.swing.JMenuItem setDiscountMenu;
    private javax.swing.JPanel welcomePanel;
    // End of variables declaration//GEN-END:variables

    private void createNewGRN() {

        GRNInterface grnInterface = new GRNInterface();

        mainDesktopPanel.add(grnInterface);

        setInternalFrameLocation(grnInterface);

        grnInterface.show();
    }

    private void createNewSupplier() {

        SupplierInterface supplierInterface = new SupplierInterface();

        mainDesktopPanel.add(supplierInterface);

        setInternalFrameLocation(supplierInterface);

        supplierInterface.show();
    }

    private void createNewSRN() {
        SupplierReturnNoteInterface srnInterface = new SupplierReturnNoteInterface();

        mainDesktopPanel.add(srnInterface);

        setInternalFrameLocation(srnInterface);

        srnInterface.show();
    }

    private void createNewDamagedStock() {
        DamageStockInterface i = new DamageStockInterface();

        mainDesktopPanel.add(i);

        setInternalFrameLocation(i);

        i.show();
    }

    public void initializeGUI() {
        logger.debug("initializeGUI invoked");
        if (logIn != null) {
            mnuUtilities.setEnabled(false);
            mnuReports.setEnabled(false);
            mnuLedgers.setEnabled(false);
            mnuStock.setEnabled(false);
            if (logIn.getLoggedUserType().equals(User.INVENTORY)) {
                logger.debug("Logged in as Inventory manager");
                mnuLedgers.setEnabled(true);
                mnuStock.setEnabled(true);
            } else if (logIn.getLoggedUserType().equals(User.MANAGER)) {
                logger.debug("Logged in as  Manager");
                mnuReports.setEnabled(true);
                mnuUtilities.setEnabled(true);
                mnuLedgers.setEnabled(true);
                mnuStock.setEnabled(true);
            } else {
                logger.debug("Not logged in");
            }
        } else {
            logger.debug("Not logged in");
        }
    }

    private void setInternalFrameLocation(JInternalFrame frame) {
        Dimension desktopSize = mainDesktopPanel.getSize();

        Dimension frameSize = frame.getSize();

        frame.setLocation((desktopSize.width - frameSize.width) / 2,
                (desktopSize.height - frameSize.height) / 2);
    }

    private void createNewGRNCancel() {
        GRNCancelInterface i = new GRNCancelInterface();

        mainDesktopPanel.add(i);

        setInternalFrameLocation(i);

        i.show();
    }

    private void createGRNPrint() {
        GRNRequestInterface i = new GRNRequestInterface();

        mainDesktopPanel.add(i);

        setInternalFrameLocation(i);

        i.show();
    }

    private void createGRNList() {
        GRNListingRequestInterfae i = new GRNListingRequestInterfae();

        mainDesktopPanel.add(i);

        setInternalFrameLocation(i);

        i.show();
    }

    private void createSRNList() {
        SRNListingRequestInterfae i = new SRNListingRequestInterfae();

        mainDesktopPanel.add(i);

        setInternalFrameLocation(i);

        i.show();
    }

    private void createUserManagement() {
        ManageUsers i = new ManageUsers();

        mainDesktopPanel.add(i);

        setInternalFrameLocation(i);

        i.show();
    }

    private void createEmployeeManagement() {
        ManageEmployees i = new ManageEmployees();

        mainDesktopPanel.add(i);

        setInternalFrameLocation(i);

        i.show();
    }

    private void createFinalCredit() {
        FinalCredit i = new FinalCredit();

        mainDesktopPanel.add(i);

        setInternalFrameLocation(i);

        i.show();
    }

    private void createSttings() {
        settings i = new settings();

        mainDesktopPanel.add(i);

        setInternalFrameLocation(i);

        i.show();
    }
}
