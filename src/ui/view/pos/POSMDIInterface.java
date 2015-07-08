/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.pos;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.pos.Employee;
import org.apache.log4j.Logger;
import ui.handler.pos.LogOffHandler;
import util.Utilities;

/**
 *
 * @author Shehan
 */
public class POSMDIInterface extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(POSMDIInterface.class);

    private boolean isCashierLogedIn;

    private String userName;

    private LogOffHandler logoffhandler;

    private JInternalFrame invoiceInterface;
    private JInternalFrame billRefundInterface;
    private JInternalFrame billCopyInterface;
    private JInternalFrame cashWithdrawInterface;
    private JInternalFrame checkStockInterface;

    /**
     * Creates new form POSMDIInterface
     */
    public POSMDIInterface() {
        initComponents();
        initializeGUI();
        enableDebugMode();
    }

    public POSMDIInterface(String userName) {
        initComponents();
        initializeGUI();
        setUser(userName);

    }

    private void enableDebugMode() {
        setTitle("MEGA COOP CITY POS : DEBUG MODE");
        isCashierLogedIn = true;
        btnCashierLog.setText("Debug Mode");
        btnCashierLog.setEnabled(false);
    }

    private void initializeGUI() {

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
        billTaskPane.setCollapsed(false);

        otherTaskPane.setCollapsed(true);

        lblCounter.setText(Utilities.loadProperty("counter"));

        logoffhandler = new LogOffHandler(this);
    }

    private void setUser(String userName) {
        this.userName = userName;
        lblCashier.setText(this.userName);
        this.isCashierLogedIn = true;
    }

    //user log in ui changes
    private void setLogOffControls() {
        logger.debug("setLogControles invoked");
        if (!isCashierLogedIn) {
            btnCashierLog.setText("Cashier logged off");
            btnCashierLog.setEnabled(false);

            lblCashier.setText("<Cashier name>");
            lblCounter.setText("<counter>");

            CardLayout card = (CardLayout) cardContainerPanel.getLayout();
            card.show(cardContainerPanel, "welcomeCard");

            billTaskPane.setCollapsed(true);
            otherTaskPane.setCollapsed(true);
            billTaskPane.setEnabled(false);
            otherTaskPane.setEnabled(false);
        }
    }

    //Show the main bill screen
    private void bill_showInvoicePanel(java.awt.event.MouseEvent evt) {
        logger.debug("bill_showInvoicePanel invoked");
        if (!isCashierLogedIn) {
            logger.error("Cashier not logged in");
            billTaskPane.setCollapsed(true);
            evt.consume();
        } else {
            //billTaskPane.setCollapsed(false);
        }
    }

    //Show the other task panel
    private void other_showTaskPane(java.awt.event.MouseEvent evt) {
        logger.debug("other_showTaskPane invoked");
        if (!isCashierLogedIn) {
            logger.error("Cashier not logged in");
            otherTaskPane.setCollapsed(true);
            evt.consume();
        } else {
            // otherTaskPane.setCollapsed(false);
        }
    }

    //Search a item
    private void bill_searchItem() {
        //NOT IMPLEMENTED - send a search string to the search UI

    }

    //New sale
    private void bill_newSale() {
        logger.debug("bill_newSale invoked");
        CardLayout card = (CardLayout) cardContainerPanel.getLayout();
        card.show(cardContainerPanel, "desktopCard");
        if (invoiceInterface == null) {
            invoiceInterface = new InvoiceInterface(desktopPane);
        } else {
            desktopPane.remove(invoiceInterface);
        }
        desktopPane.add(invoiceInterface);
        invoiceInterface.setVisible(true);
    }

    //Hold sale
    private void bill_holdSale() {
        logger.warn("bill_holdSale not implemented");
    }

    //Restore sale
    private void bill_restoreSale() {
        logger.warn("bill_restoreSale not implemented");
    }

    //Show bill copy screen
    private void billCopy_ShowPanel() {
        logger.debug("billCopy_ShowPanel invoked");
        CardLayout card = (CardLayout) cardContainerPanel.getLayout();
        card.show(cardContainerPanel, "desktopCard");
        if (billRefundInterface == null) {
            billRefundInterface = new BillRefundInterface();
        } else {
            desktopPane.remove(billRefundInterface);
        }
        desktopPane.add(billRefundInterface);
        billRefundInterface.setVisible(true);
    }

    //Show refund screen
    private void refund_showRefundPanel() {
        logger.debug("refund_showRefundPanel invoked");

        CardLayout card = (CardLayout) cardContainerPanel.getLayout();
        card.show(cardContainerPanel, "desktopCard");
        if (billCopyInterface == null) {
            billCopyInterface = new BillCopyInterface();
        } else {
            desktopPane.remove(billCopyInterface);
        }
        desktopPane.add(billCopyInterface);
        billCopyInterface.setVisible(true);
    }

    //Show the cash withdrawal UI
    private void cashWithdrawal_showUI() {
        logger.debug("cashWithdrawal_showUI invoked");

        CardLayout card = (CardLayout) cardContainerPanel.getLayout();
        card.show(cardContainerPanel, "desktopCard");
        if (cashWithdrawInterface == null) {
            cashWithdrawInterface = new CashWithdrawInterface(desktopPane);
        } else {
            desktopPane.remove(cashWithdrawInterface);
        }
        desktopPane.add(cashWithdrawInterface);
        cashWithdrawInterface.setVisible(true);
    }

    //Show the check stock UI
    private void chechStock() {
        logger.debug("chechStock invoked");

        CardLayout card = (CardLayout) cardContainerPanel.getLayout();
        card.show(cardContainerPanel, "desktopCard");
        if (checkStockInterface == null) {
            checkStockInterface = new CheckStockInterface(desktopPane);
        } else {
            desktopPane.remove(checkStockInterface);
        }
        desktopPane.add(checkStockInterface);
        checkStockInterface.setVisible(true);
    }

    //Mange cashier login
    private void cashier_logOff() {
        // TODO add your handling code here:

        /*
         Important :
         1.NOT IMPLEMENTED - Show intial amount of drawayer  UI at log on
         2.NOT IMPLEMENTED - Show log of UI to print the summery for cashier - 
         */
        logger.warn("NOT IMPLEMENTED - Show log of UI to print the summery for cashier");
        logger.warn("Check for unfinished business");

        if (isCashierLogedIn) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to log off ?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                logger.info("Cashier logged off");
                try {
                    logoffhandler.logOffUser(userName);
                    logger.debug("User : " + userName + " logged off");
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
        logger.warn("manager_logIn not implemented");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
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
        counterInfoPanel = new javax.swing.JPanel();
        lblCashierDisplay = new javax.swing.JLabel();
        lblCashier = new javax.swing.JLabel();
        lblCounterDisplay = new javax.swing.JLabel();
        lblCounter = new javax.swing.JLabel();
        lblDateDisplay = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblTimeDisplay = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        cardContainerPanel = new javax.swing.JPanel();
        welcomePanel = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        desktopContainerPanel = new javax.swing.JPanel();
        desktopPane = new javax.swing.JDesktopPane();
        controlPanel = new javax.swing.JPanel();
        btnCashierLog = new javax.swing.JButton();
        btnManagerLog = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MEGA COOP CITY POS");

        jxTaskPaneContainer.setBackground(new java.awt.Color(204, 204, 204));
        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        jxTaskPaneContainer.setBorder(dropShadowBorder1);
        jxTaskPaneContainer.setEnabled(false);
        org.jdesktop.swingx.VerticalLayout verticalLayout3 = new org.jdesktop.swingx.VerticalLayout();
        verticalLayout3.setGap(15);
        jxTaskPaneContainer.setLayout(verticalLayout3);

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

        jxTaskPaneContainer.add(billTaskPane);

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

        jxTaskPaneContainer.add(otherTaskPane);

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
        lblCashier.setText("<Cashier name>");
        lblCashier.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lblCounterDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCounterDisplay.setText("Counter");
        lblCounterDisplay.setToolTipText("");

        lblCounter.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCounter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCounter.setText("<counter>");
        lblCounter.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lblDateDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDateDisplay.setText("Date");

        lblDate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDate.setText("<date>");
        lblDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lblTimeDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTimeDisplay.setText("Time");

        lblTime.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTime.setText("<time>");
        lblTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

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
                    .addGroup(counterInfoPanelLayout.createSequentialGroup()
                        .addComponent(lblCounterDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        counterInfoPanelLayout.setVerticalGroup(
            counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(counterInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCashier, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCashierDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(counterInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCounterDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 21, Short.MAX_VALUE)
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
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pos/coop_logo.png"))); // NOI18N

        javax.swing.GroupLayout welcomePanelLayout = new javax.swing.GroupLayout(welcomePanel);
        welcomePanel.setLayout(welcomePanelLayout);
        welcomePanelLayout.setHorizontalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(welcomePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, welcomePanelLayout.createSequentialGroup()
                        .addGap(0, 445, Short.MAX_VALUE)
                        .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        welcomePanelLayout.setVerticalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, welcomePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblWelcome)
                .addContainerGap())
        );

        cardContainerPanel.add(welcomePanel, "welcomeCard");

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder3 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder3.setShowLeftShadow(true);
        dropShadowBorder3.setShowTopShadow(true);
        desktopPane.setBorder(dropShadowBorder3);

        javax.swing.GroupLayout desktopContainerPanelLayout = new javax.swing.GroupLayout(desktopContainerPanel);
        desktopContainerPanel.setLayout(desktopContainerPanelLayout);
        desktopContainerPanelLayout.setHorizontalGroup(
            desktopContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1051, Short.MAX_VALUE)
        );
        desktopContainerPanelLayout.setVerticalGroup(
            desktopContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
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
                        .addComponent(jxTaskPaneContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1051, Short.MAX_VALUE)
                    .addComponent(controlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(counterInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jxTaskPaneContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cardContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewSaleActionPerformed
        // TODO add your handling code here:
        bill_newSale();
    }//GEN-LAST:event_btnNewSaleActionPerformed

    private void btnCheckStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckStockActionPerformed
        // TODO add your handling code here:
        chechStock();
    }//GEN-LAST:event_btnCheckStockActionPerformed

    private void btnHoldSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoldSaleActionPerformed
        // TODO add your handling code here:
        bill_holdSale();
    }//GEN-LAST:event_btnHoldSaleActionPerformed

    private void btnRestoreSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestoreSaleActionPerformed
        // TODO add your handling code here:
        bill_restoreSale();
    }//GEN-LAST:event_btnRestoreSaleActionPerformed

    private void billTaskPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_billTaskPaneMouseClicked
        // TODO add your handling code here:
        bill_showInvoicePanel(evt);
    }//GEN-LAST:event_billTaskPaneMouseClicked

    private void btnBillRefundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillRefundActionPerformed
        // TODO add your handling code here:
        refund_showRefundPanel();
    }//GEN-LAST:event_btnBillRefundActionPerformed

    private void btnBillCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillCopyActionPerformed
        // TODO add your handling code here:
        billCopy_ShowPanel();
    }//GEN-LAST:event_btnBillCopyActionPerformed

    private void btnCashWithdrawalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashWithdrawalActionPerformed
        // TODO add your handling code here:
        cashWithdrawal_showUI();
    }//GEN-LAST:event_btnCashWithdrawalActionPerformed

    private void otherTaskPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_otherTaskPaneMouseClicked
        // TODO add your handling code here:
        other_showTaskPane(evt);
    }//GEN-LAST:event_otherTaskPaneMouseClicked

    private void btnCashierLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashierLogActionPerformed
        cashier_logOff();
    }//GEN-LAST:event_btnCashierLogActionPerformed

    private void btnManagerLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManagerLogActionPerformed
        // TODO add your handling code here:
        manager();
    }//GEN-LAST:event_btnManagerLogActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        setupUI();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new POSMDIInterface().setVisible(true);
            }
        });
    }

    private static void setupUI() {

        Properties props = new Properties();

        //RGB colours
        String buttonClolor = "200 200 200";
        String controlClolor = "200 200 200";

        String menuColor = "222 222 222";
        String menuBackgroundColor = "224 224 224";

        String selectionBackgroundColor = "240 240 240";
        String selectionForegroundColor = "67 148 103";

        String rollOverClolor = "114 114 114";

        String frameColor = "171 171 171";
        String windowTitleColor = "10 10 10";

        //Customize Theme
        props.put("logoString", "");

        props.put("linuxStyleScrollBar", "on");
        props.put("centerWindowTitle", "on");
        props.put("textAntiAliasing", "on");
        props.put("textAntiAliasingMode", "default");
        props.put("toolbarDecorated", "off");
        props.put("windowDecoration", "on");
        props.put("dynamicLayout", "on");
        props.put("darkTexture", "off");

        props.put("buttonColor", buttonClolor);//button colours
        props.put("buttonColorLight", buttonClolor);
        props.put("buttonColorDark", buttonClolor);

        props.put("controlColor", controlClolor);//Control colours
        props.put("controlColorLight", controlClolor);
        props.put("controlColorDark", controlClolor);

        props.put("menuColorLight", menuColor);//menu colours
        props.put("menuColorDark", menuColor);
        props.put("menuBackgroundColor", menuBackgroundColor);

        props.put("selectionBackgroundColor", selectionBackgroundColor);//hilighted text
        props.put("selectionForegroundColor", selectionForegroundColor);

        props.put("rolloverColor", rollOverClolor); //on hovering
        props.put("rolloverColorLight", rollOverClolor);
        props.put("rolloverColorDark", rollOverClolor);

        props.put("frameColor", frameColor);
        props.put("windowTitleColorLight", windowTitleColor);//Windows boarder colours
        props.put("windowTitleColorDark", windowTitleColor);
        props.put("disabledForegroundColor", windowTitleColor);

        try {
            com.jtattoo.plaf.graphite.GraphiteLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ey) {
                System.exit(3);
            }
            System.exit(3);
        }
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
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTimeDisplay;
    private javax.swing.JLabel lblWelcome;
    private org.jdesktop.swingx.JXTaskPane otherTaskPane;
    private javax.swing.JPanel welcomePanel;
    // End of variables declaration//GEN-END:variables

}