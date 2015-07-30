package ui.view.pos;

import controller.pos.CashWithdrawalController;
import controller.pos.CounterController;
import controller.pos.SettingsController;
import controller.pos.TransactionController;
import controller.pos.UserController;
import database.connector.DatabaseInterface;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.text.PlainDocument;
import model.pos.CashWithdrawal;
import model.pos.Counter;
import model.pos.CounterLogin;
import model.pos.Setting;
import org.apache.log4j.Logger;
import util.DoubleFilter;
import util.Utilities;
import static util.Utilities.doubleFormatComponentText;

public class CashWithdrawalDialog extends javax.swing.JDialog {

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(CashWithdrawalDialog.class);
    private final CounterLogin counterLogin;
    private CashWithdrawal cashWithdrawal;

    // </editor-fold>
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    public CashWithdrawalDialog(POSMDIInterface parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.counterLogin = parent.getCounterLogin();

        setLocationRelativeTo(null);
        performKeyBinding();

        showInfo();
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Key Bindings "> 
    private void performKeyBinding() {

        InputMap inputMap = cashWithdrawalPanel.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = cashWithdrawalPanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "doEscapeAction");
        actionMap.put("doEscapeAction", new keyBindingAction("Escape"));

    }

    private class keyBindingAction extends AbstractAction {

        private final String cmd;

        public keyBindingAction(String cmd) {
            this.cmd = cmd;
        }

        @Override
        public void actionPerformed(ActionEvent tf) {
            if (cmd.equalsIgnoreCase("Escape")) {
                logger.debug("CashWithdrawal Dialog - Escape Pressed ");
                exitDialog();
            }
        }
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Helper method">
    private void showInfo() {
        logger.debug("showInfo invoked");

        try {

            int lastCashWithdrawalId = CashWithdrawalController.getLastWithdrawalId();
            if (lastCashWithdrawalId > 0) {
                cashWithdrawal = new CashWithdrawal(lastCashWithdrawalId + 1);
                lblCashWithrawalNoVal.setText(Utilities.convertKeyToString(lastCashWithdrawalId + 1, DatabaseInterface.CASH_WITHDRAWAL));
            } else {
                cashWithdrawal = new CashWithdrawal(1);
                lblCashWithrawalNoVal.setText(Utilities.convertKeyToString(1, DatabaseInterface.CASH_WITHDRAWAL));
            }

            String counterId = Utilities.loadProperty("counter");
            lblCounter.setText(counterId);

            String userName = counterLogin.getUserName();
            lblUser.setText(userName);
            cashWithdrawal.setShiftId(counterLogin.getShiftId());

            lblShiftVal.setText(Utilities.convertKeyToString(counterLogin.getShiftId(), DatabaseInterface.COUNTER_LOGIN));
            lblSignOnDate.setText(counterLogin.getLogInDate());
            lblSignOnTime.setText(Utilities.convert24hTo12h(counterLogin.getLogInTime()));
            lblInitialAmount.setText(String.format("%.2f", counterLogin.getInitialAmount()));

            lblCWDate.setText(Utilities.getStringDate(Utilities.getCurrentDate()));
            lblCWTime.setText(Utilities.getCurrentTime(false));

            cashWithdrawal.setDate(Utilities.getStringDate(Utilities.getCurrentDate()));
            cashWithdrawal.setTime(Utilities.getCurrentTime(true));

            Counter counter = CounterController.getCounter(Integer.parseInt(Utilities.loadProperty("counter")));

            ArrayList<CashWithdrawal> cashWithdrawals = CashWithdrawalController.getCashWithdrawals(counterLogin.getShiftId());
            double totalWithdrawalsAmount = 0;
            for (int itr = 0; itr < cashWithdrawals.size(); itr++) {
                totalWithdrawalsAmount += cashWithdrawals.get(itr).getAmount();
            }

            double currentWithdrawalbleAmount = counter.getCurrentAmount() - totalWithdrawalsAmount;
            lblCurrentAmountVal.setText(String.format("%.2f", currentWithdrawalbleAmount));

            Setting mainCashierSetting = SettingsController.getSetting("main_cashier_name");
            if (mainCashierSetting != null) {
                lblChiefCashierID.setText(mainCashierSetting.getValue());
            } else {
                logger.error("main_cashier_name setting not found");
                btnOk.setEnabled(false);
            }

            ((PlainDocument) txtWithdrawalAmount.getDocument()).setDocumentFilter(new DoubleFilter());

        } catch (SQLException ex) {
            logger.error("SQL error : " + ex.getMessage());
            btnOk.setEnabled(false);
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
            btnOk.setEnabled(false);
        }
    }

    //Exit
    private void exitDialog() {
        this.dispose();
    }

    //Handle txtWithdrawalAmount key press
    private void txtWithdrawalAmountKeyPressHandler(java.awt.event.KeyEvent evt) {
        logger.debug("txtWithdrawalAmountKeyPressHandler invoked");

        if (txtWithdrawalAmount.getText().equals("")) {
            txtWithdrawalAmount.requestFocus();
            return;
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCashierPassword.requestFocus();
        }
    }

    //Handle txtCashierPassword key press
    private void txtCashierPasswordKeyPressHandler(java.awt.event.KeyEvent evt) {
        logger.debug("txtCashierPasswordKeyPressHandler invoked");

        if (new String(txtCashierPassword.getPassword()).equals("")) {
            txtCashierPassword.requestFocus();
            return;
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtChiefCashierPassword.requestFocus();
        }
    }

    //Handle txtChiefCashierPassword key press
    private void txtChiefCashierPasswordKeyPressHandler(java.awt.event.KeyEvent evt) {
        logger.debug("txtChiefCashierPasswordKeyPressHandler invoked");

        if (new String(txtChiefCashierPassword.getPassword()).equals("")) {
            txtChiefCashierPassword.requestFocus();
            return;
        }

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            withdrawCash();
        }
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Cash withdrawal">
    //Withdraw cash from counter
    private void withdrawCash() {
        logger.debug("withdrawCash invoked");

        if (txtWithdrawalAmount.getText().isEmpty()) {
            Utilities.showMsgBox("Please enter the withdrawal amount", "Error ", JOptionPane.ERROR_MESSAGE);
            txtWithdrawalAmount.requestFocus();
            txtCashierPassword.setText("");
            txtChiefCashierPassword.setText("");
            return;
        }
        if (new String(txtCashierPassword.getPassword()).isEmpty()) {
            Utilities.showMsgBox("Please enter user password", "Authorization faliure", JOptionPane.ERROR_MESSAGE);
            txtCashierPassword.requestFocus();
            txtCashierPassword.setText("");
            txtChiefCashierPassword.setText("");
            return;
        }
        if (new String(txtChiefCashierPassword.getPassword()).isEmpty()) {
            Utilities.showMsgBox("Please enter master password", "Authorization faliure", JOptionPane.ERROR_MESSAGE);
            txtChiefCashierPassword.requestFocus();
            txtCashierPassword.setText("");
            txtChiefCashierPassword.setText("");
            return;
        }

        double withdrawalAmount = Double.parseDouble(txtWithdrawalAmount.getText());
        double maxWithdrawableAmount = Double.parseDouble(lblCurrentAmountVal.getText());
        if (0 < withdrawalAmount && withdrawalAmount <= maxWithdrawableAmount) {
            String userPassword = new String(txtCashierPassword.getPassword());
            try {
                if (UserController.isUserAuthenticated(counterLogin.getUserName(), userPassword)) {
                    String masterPassword = new String(txtChiefCashierPassword.getPassword());
                    if (masterPassword.equals(SettingsController.getSetting("main_cashier_password").getValue())) {
                        cashWithdrawal.setAmount(withdrawalAmount);
                        if (cashWithdrawal.isValidWithdrawal()) {
                            boolean result = TransactionController.performCashWithdrawalTransaction(cashWithdrawal);
                            if (result) {
                                Utilities.showMsgBox("Cash Withdrawal successfull", "Cash Withdrawal compleate", JOptionPane.INFORMATION_MESSAGE);
                                this.dispose();
                            } else {
                                Utilities.showMsgBox("Cash Withdrawal not successfull", "Cash Withdrawal faliure", JOptionPane.INFORMATION_MESSAGE);
                                logger.warn("Cash Withdrawal not successfull");
                                txtWithdrawalAmount.setText("");
                            }
                        } else {
                            Utilities.showMsgBox("CashWithdrawal not valid", "Withdrawal faliure", JOptionPane.ERROR_MESSAGE);
                            logger.warn("CashWithdrawal not valid");
                            txtWithdrawalAmount.setText("");
                        }
                    } else {
                        Utilities.showMsgBox("Incorrect master password", "Authorization faliure", JOptionPane.ERROR_MESSAGE);
                        logger.warn("Incorrect master password");
                        txtChiefCashierPassword.requestFocus();
                    }
                } else {
                    Utilities.showMsgBox("Incorrect user password", "Authorization faliure", JOptionPane.ERROR_MESSAGE);
                    logger.warn("Incorrect user password");
                    txtCashierPassword.requestFocus();
                }
            } catch (SQLException ex) {
                logger.error("SQL error : " + ex.getMessage());
            }
        } else {
            logger.warn("withdrawalAmount not in range");
            Utilities.showMsgBox("withdrawal amount not in range", "Invalid amount", JOptionPane.ERROR_MESSAGE);
            txtWithdrawalAmount.requestFocus();
        }
        txtCashierPassword.setText("");
        txtChiefCashierPassword.setText("");
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Netbeans generated Code">    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cashWithdrawalPanel = new javax.swing.JPanel();
        cashWithdrawalInfoPanel = new javax.swing.JPanel();
        lblCashWithrawalNoDisplay = new javax.swing.JLabel();
        lblCashWithrawalNoVal = new javax.swing.JLabel();
        lblCounterDisplay = new javax.swing.JLabel();
        lblCounter = new javax.swing.JLabel();
        infoPanel = new javax.swing.JPanel();
        lblWithdrawAmountDisplay = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        lblCashierPassDisplay = new javax.swing.JLabel();
        lblDateDisplay = new javax.swing.JLabel();
        lblSignOnDate = new javax.swing.JLabel();
        lblTimeDisplay = new javax.swing.JLabel();
        lblSignOnTime = new javax.swing.JLabel();
        lblSignOn = new javax.swing.JLabel();
        lblCW = new javax.swing.JLabel();
        lblCWDate = new javax.swing.JLabel();
        lblCWTime = new javax.swing.JLabel();
        lblCashierIDDisplay = new javax.swing.JLabel();
        txtCashierPassword = new javax.swing.JPasswordField();
        lblInitialAmountDisplay = new javax.swing.JLabel();
        lblInitialAmount = new javax.swing.JLabel();
        lblChiefCashierIDDisplay = new javax.swing.JLabel();
        lblChiefCashierID = new javax.swing.JLabel();
        lblChiefCashierPasswordDisplay = new javax.swing.JLabel();
        txtChiefCashierPassword = new javax.swing.JPasswordField();
        btnOk = new javax.swing.JButton();
        lblCurrentAmountDisplay = new javax.swing.JLabel();
        lblCurrentAmountVal = new javax.swing.JLabel();
        lblShiftDisplay = new javax.swing.JLabel();
        lblShiftVal = new javax.swing.JLabel();
        txtWithdrawalAmount = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cash Withdrawal");
        setName("cashWithdrawalDialog"); // NOI18N
        setResizable(false);

        cashWithdrawalPanel.setPreferredSize(new java.awt.Dimension(475, 600));

        lblCashWithrawalNoDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCashWithrawalNoDisplay.setText("Cash Withdrawal No");

        lblCashWithrawalNoVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCashWithrawalNoVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCashWithrawalNoVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCounterDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCounterDisplay.setText("Counter");
        lblCounterDisplay.setToolTipText("");

        lblCounter.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCounter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCounter.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout cashWithdrawalInfoPanelLayout = new javax.swing.GroupLayout(cashWithdrawalInfoPanel);
        cashWithdrawalInfoPanel.setLayout(cashWithdrawalInfoPanelLayout);
        cashWithdrawalInfoPanelLayout.setHorizontalGroup(
            cashWithdrawalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashWithdrawalInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCashWithrawalNoDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCashWithrawalNoVal, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(lblCounterDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        cashWithdrawalInfoPanelLayout.setVerticalGroup(
            cashWithdrawalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashWithdrawalInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cashWithdrawalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cashWithdrawalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCounterDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cashWithdrawalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblCashWithrawalNoVal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCashWithrawalNoDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        infoPanel.setBorder(dropShadowBorder1);

        lblWithdrawAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblWithdrawAmountDisplay.setText("Withdraw Amount (Rs.)");

        lblUser.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUser.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblCashierPassDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCashierPassDisplay.setText("User Password");

        lblDateDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDateDisplay.setText("Date");

        lblSignOnDate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblSignOnDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSignOnDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblTimeDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTimeDisplay.setText("Time");

        lblSignOnTime.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblSignOnTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSignOnTime.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblSignOn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblSignOn.setText("Sign On");

        lblCW.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCW.setText("Cash Withdrawal");

        lblCWDate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCWDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCWDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblCWTime.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCWTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCWTime.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblCashierIDDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCashierIDDisplay.setText("User ID");

        txtCashierPassword.setBackground(new java.awt.Color(255, 255, 0));
        txtCashierPassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCashierPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCashierPasswordKeyReleased(evt);
            }
        });

        lblInitialAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblInitialAmountDisplay.setText("Initial Amount (Rs.) ");

        lblInitialAmount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblInitialAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInitialAmount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblChiefCashierIDDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblChiefCashierIDDisplay.setText("Chief Cashier  ID");

        lblChiefCashierID.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblChiefCashierID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChiefCashierID.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblChiefCashierPasswordDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblChiefCashierPasswordDisplay.setText("Cashier Password");

        txtChiefCashierPassword.setBackground(new java.awt.Color(255, 255, 0));
        txtChiefCashierPassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtChiefCashierPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtChiefCashierPasswordKeyReleased(evt);
            }
        });

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        lblCurrentAmountDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCurrentAmountDisplay.setText("Current Amount (Rs.) ");

        lblCurrentAmountVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCurrentAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCurrentAmountVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblShiftDisplay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblShiftDisplay.setText("Shift");
        lblShiftDisplay.setToolTipText("");

        lblShiftVal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblShiftVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblShiftVal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtWithdrawalAmount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtWithdrawalAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtWithdrawalAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtWithdrawalAmountFocusLost(evt);
            }
        });
        txtWithdrawalAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtWithdrawalAmountKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSignOn)
                        .addGap(106, 106, 106)
                        .addComponent(lblCW)
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(infoPanelLayout.createSequentialGroup()
                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTimeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblDateDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCashierIDDisplay)
                                    .addComponent(lblInitialAmountDisplay))
                                .addGap(34, 34, 34)
                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(infoPanelLayout.createSequentialGroup()
                                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblSignOnDate, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                            .addComponent(lblSignOnTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, Short.MAX_VALUE)
                                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblCWTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblCWDate, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)))
                                    .addGroup(infoPanelLayout.createSequentialGroup()
                                        .addComponent(lblInitialAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(infoPanelLayout.createSequentialGroup()
                                        .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                                        .addComponent(lblShiftDisplay)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblShiftVal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(infoPanelLayout.createSequentialGroup()
                                .addComponent(lblChiefCashierPasswordDisplay)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtChiefCashierPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(infoPanelLayout.createSequentialGroup()
                                .addComponent(lblChiefCashierIDDisplay)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblChiefCashierID, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoPanelLayout.createSequentialGroup()
                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCashierPassDisplay)
                                    .addComponent(lblWithdrawAmountDisplay))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtCashierPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                    .addComponent(txtWithdrawalAmount))))))
                .addContainerGap())
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCurrentAmountDisplay)
                .addGap(18, 18, 18)
                .addComponent(lblCurrentAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblShiftDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblShiftVal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCashierIDDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSignOn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCW, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDateDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSignOnDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCWDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTimeDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSignOnTime, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCWTime, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblInitialAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInitialAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCurrentAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCurrentAmountVal, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWithdrawAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtWithdrawalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCashierPassDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCashierPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblChiefCashierIDDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblChiefCashierID, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblChiefCashierPasswordDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtChiefCashierPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 31, Short.MAX_VALUE)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout cashWithdrawalPanelLayout = new javax.swing.GroupLayout(cashWithdrawalPanel);
        cashWithdrawalPanel.setLayout(cashWithdrawalPanelLayout);
        cashWithdrawalPanelLayout.setHorizontalGroup(
            cashWithdrawalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cashWithdrawalInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(infoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        cashWithdrawalPanelLayout.setVerticalGroup(
            cashWithdrawalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashWithdrawalPanelLayout.createSequentialGroup()
                .addComponent(cashWithdrawalInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cashWithdrawalPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cashWithdrawalPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        withdrawCash();
    }//GEN-LAST:event_btnOkActionPerformed

    private void txtWithdrawalAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtWithdrawalAmountFocusLost
        // TODO add your handling code here:
        doubleFormatComponentText(txtWithdrawalAmount);
    }//GEN-LAST:event_txtWithdrawalAmountFocusLost

    private void txtWithdrawalAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWithdrawalAmountKeyReleased
        // TODO add your handling code here:
        txtWithdrawalAmountKeyPressHandler(evt);
    }//GEN-LAST:event_txtWithdrawalAmountKeyReleased

    private void txtCashierPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCashierPasswordKeyReleased
        // TODO add your handling code here:
        txtCashierPasswordKeyPressHandler(evt);
    }//GEN-LAST:event_txtCashierPasswordKeyReleased

    private void txtChiefCashierPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtChiefCashierPasswordKeyReleased
        // TODO add your handling code here:
        txtChiefCashierPasswordKeyPressHandler(evt);
    }//GEN-LAST:event_txtChiefCashierPasswordKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JPanel cashWithdrawalInfoPanel;
    private javax.swing.JPanel cashWithdrawalPanel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JLabel lblCW;
    private javax.swing.JLabel lblCWDate;
    private javax.swing.JLabel lblCWTime;
    private javax.swing.JLabel lblCashWithrawalNoDisplay;
    private javax.swing.JLabel lblCashWithrawalNoVal;
    private javax.swing.JLabel lblCashierIDDisplay;
    private javax.swing.JLabel lblCashierPassDisplay;
    private javax.swing.JLabel lblChiefCashierID;
    private javax.swing.JLabel lblChiefCashierIDDisplay;
    private javax.swing.JLabel lblChiefCashierPasswordDisplay;
    private javax.swing.JLabel lblCounter;
    private javax.swing.JLabel lblCounterDisplay;
    private javax.swing.JLabel lblCurrentAmountDisplay;
    private javax.swing.JLabel lblCurrentAmountVal;
    private javax.swing.JLabel lblDateDisplay;
    private javax.swing.JLabel lblInitialAmount;
    private javax.swing.JLabel lblInitialAmountDisplay;
    private javax.swing.JLabel lblShiftDisplay;
    private javax.swing.JLabel lblShiftVal;
    private javax.swing.JLabel lblSignOn;
    private javax.swing.JLabel lblSignOnDate;
    private javax.swing.JLabel lblSignOnTime;
    private javax.swing.JLabel lblTimeDisplay;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblWithdrawAmountDisplay;
    private javax.swing.JPasswordField txtCashierPassword;
    private javax.swing.JPasswordField txtChiefCashierPassword;
    private javax.swing.JTextField txtWithdrawalAmount;
    // End of variables declaration//GEN-END:variables
// </editor-fold>
}
