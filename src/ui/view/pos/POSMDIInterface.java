package ui.view.pos;

import controller.pos.CounterLoginController;
import controller.pos.UserController;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import model.pos.CounterLogin;
import org.apache.log4j.Logger;
import util.Utilities;
import static util.Utilities.setupUI;

public class POSMDIInterface extends javax.swing.JFrame {

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(POSMDIInterface.class);

    //Enable pos only if cashier is logged on
    private boolean isCashierLogedIn;

    //Flag to to allow only one of the 3 ui of pos to run at once
    private boolean isMainActivityRunning;

    private boolean isInvoiceRunning;
    private boolean isBillCopyRunning;
    private boolean isbillRefundRunning;

    //Name of the logged on cashier
    private String userName;

    //The three main POS UI's
    private InvoiceInternalInterface invoiceInterface;
    private BillRefundInternalInterface billRefundInterface;
    private BillCopyInternalInterface billCopyInterface;
    private CheckStockInterface checkStockInterface;

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    public POSMDIInterface() {
        logger.debug("POSMDIInterface constructor invoked");

        initComponents();
        initializeGUI();
        enableDebugMode();
    }

    public POSMDIInterface(String userName) {
        logger.debug("POSMDIInterface(String userName) constructor invoked");

        initComponents();
        initializeGUI();
        showLoginDetails(userName);

    }
    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Methods">

    private void enableDebugMode() {
        logger.debug("enableDebugMode invoked");

        setTitle("MEGA COOP CITY POS : DEBUG MODE");
        isCashierLogedIn = true;
        this.userName = "msw";
        lblCashier.setText("DEBUG : msw");
        btnCashierLog.setText("Debug Mode");
        btnCashierLog.setEnabled(false);
        // bill_newSale();
        //chechStock();

    }

    private void initializeGUI() {
        logger.debug("initializeGUI invoked");

        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/images/pos/pos_icon.png")).getImage());
        ActionListener timerListener = (ActionEvent e) -> {
            Date currentDate = new Date();

            String strDate = new SimpleDateFormat("EEE, d MMM yyyy").format(currentDate);
            String strTime = new SimpleDateFormat("h:mm a").format(currentDate);

            lblDate.setText(strDate);

            lblTime.setText(strTime);
        };
        Timer timer = new Timer(1000, timerListener);
        timer.setInitialDelay(0);
        timer.start();

        //billTaskPane.setCollapsed(true);
        //billTaskPane.setCollapsed(false);
        //otherTaskPane.setCollapsed(true);
        lblCounter.setText(Utilities.loadProperty("counter"));

        try {
            CounterLogin lastCounterLogin = CounterLoginController.getLastCounterLogin(Integer.parseInt(Utilities.loadProperty("counter")));
            if (lastCounterLogin == null || lastCounterLogin.isShiftEnded()) {
                Utilities.showMsgBox("No current active shift", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(3);
            } else {
                lblShift.setText(Utilities.formatId("SF", 5, lastCounterLogin.getShiftId()));
            }
        } catch (SQLException ex) {
            logger.error("SQL Error : " + ex.getMessage());
        }

        lblTaskLogo.setVisible(false);

        isMainActivityRunning = false;
        isInvoiceRunning = false;
        isBillCopyRunning = false;
        isbillRefundRunning = false;
    }

    public String getUserName() {
        logger.debug("getUserName invoked");

        if (!userName.isEmpty()) {
            return userName;
        } else {
            return "msw";
        }
    }

    public void setIsMainActivityRunning(boolean value) {
        this.isMainActivityRunning = value;
    }

    private boolean isMainActivityRunning() {
        return this.isMainActivityRunning;
    }

    public void setIsInvoiceRunning(boolean value) {
        this.isInvoiceRunning = value;
    }

    private boolean isInvoiceRunning() {
        return this.isInvoiceRunning;
    }

    public void setIsBillCopyRunning(boolean value) {
        this.isBillCopyRunning = value;
    }

    private boolean isBillCopyRunning() {
        return this.isBillCopyRunning;
    }

    public void setIsBillRefundRunning(boolean value) {
        this.isbillRefundRunning = value;
    }

    private boolean isBillRefundRunning() {
        return this.isbillRefundRunning;
    }

    private void showDesktopPane(boolean val) {
        logger.debug("showDesktopPane invoked");

        if (val) {
            CardLayout card = (CardLayout) cardContainerPanel.getLayout();
            card.show(cardContainerPanel, "desktopCard");
            lblTaskLogo.setVisible(true);
        } else {
            CardLayout card = (CardLayout) cardContainerPanel.getLayout();
            card.show(cardContainerPanel, "welcomeCard");
            lblTaskLogo.setVisible(false);
        }
    }

    private void showLoginDetails(String userName) {
        logger.debug("showLoginDetails invoked");

        this.userName = userName;
        lblCashier.setText(this.getUserName());
        this.isCashierLogedIn = true;

    }

    //user log in ui changes
    private void setLogOffControls() {
        logger.debug("setLogControles invoked");

        if (!isCashierLogedIn) {
            btnCashierLog.setText("Cashier logged off");
            btnCashierLog.setEnabled(false);

            lblCashier.setText("");
            //lblCounter.setText("");
            lblShift.setText("");

            showDesktopPane(false);

            billTaskPane.setCollapsed(true);
            otherTaskPane.setCollapsed(true);

            billTaskPane.setEnabled(false);
            otherTaskPane.setEnabled(false);

        }
    }

    //Show the main bill screen
    private void showInvoicePane(java.awt.event.MouseEvent evt) {
        logger.debug("showInvoicePane invoked");

        if (!isCashierLogedIn) {
            logger.error("Cashier not logged in");
            billTaskPane.setCollapsed(true);
            evt.consume();
        } else {
            //billTaskPane.setCollapsed(false);
        }
    }

    //Show the other task panel
    private void showOtherPane(java.awt.event.MouseEvent evt) {
        logger.debug("showOtherPane invoked");

        if (!isCashierLogedIn) {
            logger.error("Cashier not logged in");
            otherTaskPane.setCollapsed(true);
            evt.consume();
        } else {
            // otherTaskPane.setCollapsed(false);
        }
    }

    //New sale
    private void showNewInvoiceUI() {
        logger.debug("showNewInvoiceUI invoked");

        if (isMainActivityRunning()) {
            logger.error("A main activity is already running");
            return;
        }
        showDesktopPane(true);
        if (invoiceInterface != null) {
            desktopPane.remove(invoiceInterface);
        }
        invoiceInterface = new InvoiceInternalInterface(this, desktopPane);

        setIsMainActivityRunning(true);
        setIsInvoiceRunning(true);

        desktopPane.add(invoiceInterface);
        invoiceInterface.setVisible(true);
        try {
            invoiceInterface.setMaximum(true);
        } catch (PropertyVetoException ex) {
            logger.error("PropertyVetoException : " + ex.getMessage());
        }
    }

    //Hold sale
    private void holdSale() {
        logger.warn("holdSale not implemented");
    }

    //Restore sale
    private void restoreSale() {
        logger.warn("restoreSale not implemented");
    }

    //Show bill copy screen
    private void showBillCopyUI() {
        logger.debug("showBillCopyUI invoked");

        if (isMainActivityRunning()) {
            logger.error("A main activity is already running");
            return;
        }

        showDesktopPane(true);
        if (billCopyInterface != null) {
            desktopPane.remove(billCopyInterface);

        }
        billCopyInterface = new BillCopyInternalInterface(this, desktopPane);
        setIsMainActivityRunning(true);
        setIsBillCopyRunning(true);

        desktopPane.add(billCopyInterface);
        billCopyInterface.setVisible(true);

    }

    //Show refund screen
    private void showRefundUI() {
        logger.debug("showRefundUI invoked");

        if (isMainActivityRunning()) {
            logger.error("A main activity is already running");
            return;
        }
        if (billRefundInterface != null) {
            desktopPane.remove(billRefundInterface);
        }
        billRefundInterface = new BillRefundInternalInterface(this, desktopPane);

        setIsMainActivityRunning(true);
        setIsBillRefundRunning(true);

        desktopPane.add(billRefundInterface);
        billRefundInterface.setVisible(true);
        showDesktopPane(true);

    }

    //Show the cash withdrawal UI
    private void showCashWithdrawalShowUI() {
        logger.debug("showCashWithdrawalShowUI invoked");

        try {
            CounterLogin lastCounterLogin = CounterLoginController.getLastCounterLogin(Integer.parseInt(Utilities.loadProperty("counter")));
            if (lastCounterLogin == null || lastCounterLogin.isShiftEnded()) {
                Utilities.showMsgBox("No current active shift", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                new CashWithdrawalDialog(this, true).setVisible(true);
            }

        } catch (SQLException ex) {
            logger.error("SQL error : " + ex.getMessage());
        }

    }

    //Show the check stock UI
    private void showCheckStockUI() {
        logger.debug("showCheckStockUI invoked");

        showDesktopPane(true);
        if (checkStockInterface != null) {
            desktopPane.remove(checkStockInterface);
        }
        checkStockInterface = new CheckStockInterface(this, invoiceInterface, billRefundInterface, billCopyInterface, desktopPane);
        desktopPane.add(checkStockInterface, new Integer(50));//On top of all others

        checkStockInterface.setVisible(true);
    }

    //Mange cashier login
    private void cashierLogOff() {
        logger.debug("cashierLogOff invoked");
        // TODO add your handling code here:

        /*
         Important :
         1.NOT IMPLEMENTED - Show log of UI to print the summery for cashier - 
         */
        logger.warn("NOT IMPLEMENTED - Show log of UI to print the summery for cashier");
        logger.warn("Check for unfinished business");

        if (isCashierLogedIn) {
            if (isInvoiceRunning() || isBillRefundRunning() || isBillCopyRunning()) {
                Utilities.showMsgBox("Please exit all the POS interfaces.", "Cannot Log off", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to log off ?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                try {
                    UserController.setUserLoginState(userName, false);

                    //set the last shift from this machine to eneded
                    CounterLogin lastCounterLogin = CounterLoginController.getLastCounterLogin(Integer.parseInt(Utilities.loadProperty("counter")));
                    if (lastCounterLogin == null || lastCounterLogin.isShiftEnded()) {
                        Utilities.showMsgBox("No current active shift", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        boolean result = CounterLoginController.endShift(lastCounterLogin.getShiftId(), lastCounterLogin.getCounterId());
                        logger.info("Shift ended : " + result);
                    }

                    logger.info("User : " + getUserName() + " logged off");
                } catch (SQLException ex) {
                    logger.error("User log off error : " + ex.getMessage());

                }
                isCashierLogedIn = false;
                logger.info("isCashierLogedIn :" + isCashierLogedIn);
                setLogOffControls();
            }

        }
    }

    //Manager features
    private void manager() {
        logger.warn("manager not implemented");
        if (isCashierLogedIn) {
            logger.warn("User still logged in ");
        }
        this.dispose();
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Netbeans generated Code">    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jxTaskPaneContainer = new org.jdesktop.swingx.JXTaskPaneContainer();
        billTaskPane = new org.jdesktop.swingx.JXTaskPane();
        btnNewSale = new javax.swing.JButton();
        btnCheckStock = new javax.swing.JButton();
        btnHoldSale = new javax.swing.JButton();
        btnRestoreSale = new javax.swing.JButton();
        otherTaskPane = new org.jdesktop.swingx.JXTaskPane();
        btnBillRefund = new javax.swing.JButton();
        btnBillCopy = new javax.swing.JButton();
        btnCashWithdrawal = new javax.swing.JButton();
        logoPanel = new javax.swing.JPanel();
        lblTaskLogo = new javax.swing.JLabel();
        counterInfoPanel = new javax.swing.JPanel();
        lblCashierDisplay = new javax.swing.JLabel();
        lblCashier = new javax.swing.JLabel();
        lblCounterDisplay = new javax.swing.JLabel();
        lblCounter = new javax.swing.JLabel();
        lblDateDisplay = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblTimeDisplay = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        lblShiftDisplay = new javax.swing.JLabel();
        lblShift = new javax.swing.JLabel();
        cardContainerPanel = new javax.swing.JPanel();
        welcomePanel = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        desktopContainerPanel = new javax.swing.JPanel();
        desktopPane = new javax.swing.JDesktopPane();
        controlPanel = new javax.swing.JPanel();
        btnCashierLog = new javax.swing.JButton();
        btnManagerLog = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("MEGA COOP CITY POS");
        setMinimumSize(new java.awt.Dimension(1300, 825));

        jxTaskPaneContainer.setBackground(new java.awt.Color(204, 204, 204));
        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        jxTaskPaneContainer.setBorder(dropShadowBorder1);
        jxTaskPaneContainer.setEnabled(false);

        billTaskPane.setBackground(new java.awt.Color(102, 102, 102));
        billTaskPane.setForeground(new java.awt.Color(153, 153, 153));
        billTaskPane.setSpecial(true);
        billTaskPane.setTitle("Bill");
        billTaskPane.setEnabled(false);
        billTaskPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                billTaskPaneMouseClicked(evt);
            }
        });

        btnNewSale.setText("New Sale [ F7 ]");
        btnNewSale.setPreferredSize(new java.awt.Dimension(73, 30));
        btnNewSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewSaleActionPerformed(evt);
            }
        });
        billTaskPane.getContentPane().add(btnNewSale);

        btnCheckStock.setText("Check Stock [ F2 ]");
        btnCheckStock.setPreferredSize(new java.awt.Dimension(73, 30));
        btnCheckStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckStockActionPerformed(evt);
            }
        });
        billTaskPane.getContentPane().add(btnCheckStock);

        btnHoldSale.setText("Hold Sale [ F9 ]");
        btnHoldSale.setPreferredSize(new java.awt.Dimension(73, 30));
        btnHoldSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoldSaleActionPerformed(evt);
            }
        });
        billTaskPane.getContentPane().add(btnHoldSale);

        btnRestoreSale.setText("Restore Sale [F10 ]");
        btnRestoreSale.setPreferredSize(new java.awt.Dimension(73, 30));
        btnRestoreSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestoreSaleActionPerformed(evt);
            }
        });
        billTaskPane.getContentPane().add(btnRestoreSale);

        otherTaskPane.setSpecial(true);
        otherTaskPane.setTitle("Other");
        otherTaskPane.setEnabled(false);
        otherTaskPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                otherTaskPaneMouseClicked(evt);
            }
        });

        btnBillRefund.setText("Refund");
        btnBillRefund.setPreferredSize(new java.awt.Dimension(0, 30));
        btnBillRefund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillRefundActionPerformed(evt);
            }
        });
        otherTaskPane.getContentPane().add(btnBillRefund);

        btnBillCopy.setText("Bill Copy");
        btnBillCopy.setPreferredSize(new java.awt.Dimension(0, 30));
        btnBillCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillCopyActionPerformed(evt);
            }
        });
        otherTaskPane.getContentPane().add(btnBillCopy);

        btnCashWithdrawal.setText("Cash Withdrawal");
        btnCashWithdrawal.setPreferredSize(new java.awt.Dimension(0, 30));
        btnCashWithdrawal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCashWithdrawalActionPerformed(evt);
            }
        });
        otherTaskPane.getContentPane().add(btnCashWithdrawal);

        logoPanel.setBackground(new java.awt.Color(204, 204, 204));

        lblTaskLogo.setBackground(new java.awt.Color(204, 204, 204));
        lblTaskLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTaskLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pos/coop_200.png"))); // NOI18N

        javax.swing.GroupLayout logoPanelLayout = new javax.swing.GroupLayout(logoPanel);
        logoPanel.setLayout(logoPanelLayout);
        logoPanelLayout.setHorizontalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTaskLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        logoPanelLayout.setVerticalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTaskLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jxTaskPaneContainerLayout = new javax.swing.GroupLayout(jxTaskPaneContainer);
        jxTaskPaneContainer.setLayout(jxTaskPaneContainerLayout);
        jxTaskPaneContainerLayout.setHorizontalGroup(
            jxTaskPaneContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jxTaskPaneContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jxTaskPaneContainerLayout.createSequentialGroup()
                .addGroup(jxTaskPaneContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(billTaskPane, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(otherTaskPane, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jxTaskPaneContainerLayout.setVerticalGroup(
            jxTaskPaneContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jxTaskPaneContainerLayout.createSequentialGroup()
                .addComponent(billTaskPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(otherTaskPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder2 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder2.setShadowSize(10);
        dropShadowBorder2.setShowLeftShadow(true);
        dropShadowBorder2.setShowTopShadow(true);
        counterInfoPanel.setBorder(dropShadowBorder2);
        counterInfoPanel.setPreferredSize(new java.awt.Dimension(224, 250));

        lblCashierDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCashierDisplay.setText("Cashier");

        lblCashier.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCashier.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCashier.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lblCounterDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCounterDisplay.setText("Counter");
        lblCounterDisplay.setToolTipText("");

        lblCounter.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCounter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCounter.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lblDateDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDateDisplay.setText("Date");

        lblDate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lblTimeDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTimeDisplay.setText("Time");

        lblTime.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lblShiftDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblShiftDisplay.setText("Shift");

        lblShift.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblShift.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShift.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout counterInfoPanelLayout = new javax.swing.GroupLayout(counterInfoPanel);
        counterInfoPanel.setLayout(counterInfoPanelLayout);
        counterInfoPanelLayout.setHorizontalGroup(
            counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(counterInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(counterInfoPanelLayout.createSequentialGroup()
                        .addComponent(lblDateDisplay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(counterInfoPanelLayout.createSequentialGroup()
                        .addComponent(lblCashierDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addComponent(lblCashier, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(counterInfoPanelLayout.createSequentialGroup()
                        .addComponent(lblTimeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, counterInfoPanelLayout.createSequentialGroup()
                        .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblShiftDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCounterDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblCounter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblShift, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))))
                .addContainerGap())
        );
        counterInfoPanelLayout.setVerticalGroup(
            counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(counterInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCashier, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCashierDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblShift, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblShiftDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCounterDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDateDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTimeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        cardContainerPanel.setLayout(new java.awt.CardLayout());

        welcomePanel.setBackground(new java.awt.Color(153, 153, 153));

        lblWelcome.setFont(new java.awt.Font("Tahoma", 2, 36)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(255, 255, 255));
        lblWelcome.setText("Welcome to MEGA COOP CITY POS");

        lblLogo.setBackground(new java.awt.Color(153, 153, 153));
        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pos/coop_300.png"))); // NOI18N

        javax.swing.GroupLayout welcomePanelLayout = new javax.swing.GroupLayout(welcomePanel);
        welcomePanel.setLayout(welcomePanelLayout);
        welcomePanelLayout.setHorizontalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(welcomePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, welcomePanelLayout.createSequentialGroup()
                        .addGap(0, 444, Short.MAX_VALUE)
                        .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        welcomePanelLayout.setVerticalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, welcomePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblWelcome)
                .addContainerGap())
        );

        cardContainerPanel.add(welcomePanel, "welcomeCard");

        desktopContainerPanel.setMinimumSize(new java.awt.Dimension(1050, 750));
        desktopContainerPanel.setPreferredSize(new java.awt.Dimension(1050, 750));

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder3 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder3.setShowLeftShadow(true);
        dropShadowBorder3.setShowTopShadow(true);
        desktopPane.setBorder(dropShadowBorder3);

        javax.swing.GroupLayout desktopContainerPanelLayout = new javax.swing.GroupLayout(desktopContainerPanel);
        desktopContainerPanel.setLayout(desktopContainerPanelLayout);
        desktopContainerPanelLayout.setHorizontalGroup(
            desktopContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1050, Short.MAX_VALUE)
        );
        desktopContainerPanelLayout.setVerticalGroup(
            desktopContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
        );

        cardContainerPanel.add(desktopContainerPanel, "desktopCard");

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder4 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder4.setShowLeftShadow(true);
        dropShadowBorder4.setShowTopShadow(true);
        controlPanel.setBorder(dropShadowBorder4);

        btnCashierLog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCashierLog.setText("Cashier Log Off");
        btnCashierLog.setPreferredSize(new java.awt.Dimension(71, 35));
        btnCashierLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCashierLogActionPerformed(evt);
            }
        });

        btnManagerLog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnManagerLog.setText("Manager report");
        btnManagerLog.setPreferredSize(new java.awt.Dimension(71, 35));
        btnManagerLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManagerLogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCashierLog, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnManagerLog, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCashierLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnManagerLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(counterInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jxTaskPaneContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(controlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(counterInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jxTaskPaneContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewSaleActionPerformed
        // TODO add your handling code here:
        showNewInvoiceUI();
    }//GEN-LAST:event_btnNewSaleActionPerformed

    private void btnCheckStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckStockActionPerformed
        // TODO add your handling code here:
        showCheckStockUI();
    }//GEN-LAST:event_btnCheckStockActionPerformed

    private void btnHoldSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoldSaleActionPerformed
        // TODO add your handling code here:
        holdSale();
    }//GEN-LAST:event_btnHoldSaleActionPerformed

    private void btnRestoreSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestoreSaleActionPerformed
        // TODO add your handling code here:
        restoreSale();
    }//GEN-LAST:event_btnRestoreSaleActionPerformed

    private void billTaskPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_billTaskPaneMouseClicked
        // TODO add your handling code here:
        showInvoicePane(evt);
    }//GEN-LAST:event_billTaskPaneMouseClicked

    private void btnBillRefundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillRefundActionPerformed
        // TODO add your handling code here:
        showRefundUI();
    }//GEN-LAST:event_btnBillRefundActionPerformed

    private void btnBillCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillCopyActionPerformed
        // TODO add your handling code here:
        showBillCopyUI();
    }//GEN-LAST:event_btnBillCopyActionPerformed

    private void btnCashWithdrawalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashWithdrawalActionPerformed
        // TODO add your handling code here:
        showCashWithdrawalShowUI();
    }//GEN-LAST:event_btnCashWithdrawalActionPerformed

    private void otherTaskPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_otherTaskPaneMouseClicked
        // TODO add your handling code here:
        showOtherPane(evt);
    }//GEN-LAST:event_otherTaskPaneMouseClicked

    private void btnCashierLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashierLogActionPerformed
        cashierLogOff();
    }//GEN-LAST:event_btnCashierLogActionPerformed

    private void btnManagerLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManagerLogActionPerformed
        // TODO add your handling code here:
        manager();
    }//GEN-LAST:event_btnManagerLogActionPerformed

    public static void main(String args[]) {
        setupUI();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new POSMDIInterface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXTaskPane billTaskPane;
    private javax.swing.JButton btnBillCopy;
    private javax.swing.JButton btnBillRefund;
    private javax.swing.JButton btnCashWithdrawal;
    private javax.swing.JButton btnCashierLog;
    private javax.swing.JButton btnCheckStock;
    private javax.swing.JButton btnHoldSale;
    private javax.swing.JButton btnManagerLog;
    private javax.swing.JButton btnNewSale;
    private javax.swing.JButton btnRestoreSale;
    private javax.swing.JPanel cardContainerPanel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel counterInfoPanel;
    private javax.swing.JPanel desktopContainerPanel;
    private javax.swing.JDesktopPane desktopPane;
    private org.jdesktop.swingx.JXTaskPaneContainer jxTaskPaneContainer;
    private javax.swing.JLabel lblCashier;
    private javax.swing.JLabel lblCashierDisplay;
    private javax.swing.JLabel lblCounter;
    private javax.swing.JLabel lblCounterDisplay;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDateDisplay;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblShift;
    private javax.swing.JLabel lblShiftDisplay;
    private javax.swing.JLabel lblTaskLogo;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTimeDisplay;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel logoPanel;
    private org.jdesktop.swingx.JXTaskPane otherTaskPane;
    private javax.swing.JPanel welcomePanel;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
}
