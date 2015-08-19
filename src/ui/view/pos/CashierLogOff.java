package ui.view.pos;

import controller.pos.CashWithdrawalController;
import controller.pos.CounterController;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.text.PlainDocument;
import model.pos.CashWithdrawal;
import model.pos.Counter;
import model.pos.CounterLogin;
import org.apache.log4j.Logger;
import util.CurrencyFilter;
import util.Utilities;

class CashierLogOff extends javax.swing.JDialog {

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(CashierLogOff.class);
    private final CounterLogin counterLogin;
    private final POSMDIInterface parent;

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructoer">
    public CashierLogOff(POSMDIInterface parent, boolean modal) {
        super(parent, modal);
        logger.debug("CashierLogOff constructor invoked");

        initComponents();
        this.parent = parent;
        this.counterLogin = parent.getCounterLogin();
        ((PlainDocument) txtActualAmount.getDocument()).setDocumentFilter(new CurrencyFilter());

        setLocationRelativeTo(null);
        showDetails();
    }
    // </editor-fold>   
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Methods">

    private void txtInitialAmountKeyHandler(java.awt.event.KeyEvent evt) {
        if (txtActualAmount.getText().equals("")) {
            txtActualAmount.requestFocus();
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Utilities.doubleFormatComponentText(txtActualAmount);
            btnConfirm.requestFocus();
        }
    }

    private void showDetails() {
        logger.debug("showDetails invoked");

        try {
            Counter counter = CounterController.getCounter(Integer.parseInt(Utilities.loadProperty("counter")));
            ArrayList<CashWithdrawal> cashWithdrawals = CashWithdrawalController.getCashWithdrawals(counterLogin.getShiftId());
            double totalWithdrawalsAmount = 0;
            for (int itr = 0; itr < cashWithdrawals.size(); itr++) {
                totalWithdrawalsAmount += cashWithdrawals.get(itr).getAmount();
            }

            counterLogin.setCashWithdrawals(totalWithdrawalsAmount);
            counterLogin.setLogOffExpected(counter.getCurrentAmount() - totalWithdrawalsAmount);

            txtTotalSales.setText(String.format("%.2f", counter.getCurrentAmount()));
            txtCashWithdrawals.setText(String.format("%.2f", totalWithdrawalsAmount));
            txtExpectedAmount.setText(String.format("%.2f", counter.getCurrentAmount() - totalWithdrawalsAmount));
            txtActualAmount.setText(String.format("%.2f", counter.getCurrentAmount() - totalWithdrawalsAmount));

            txtActualAmount.requestFocus();
        } catch (SQLException ex) {
            logger.error("SQL error : " + ex.getMessage(), ex);
        }

    }

    private void exit() {
        logger.debug("exit invoked");

        if (txtActualAmount.getText().trim().isEmpty()) {
            Utilities.showMsgBox("Please enter valid amount", "Warning", JOptionPane.INFORMATION_MESSAGE);
            txtActualAmount.requestFocus();
        }

        double actualAmountInCounter = Double.parseDouble(txtActualAmount.getText());

        counterLogin.setLogOffActual(actualAmountInCounter);
        parent.setCashierLogOff(true);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnConfirm = new javax.swing.JButton();
        txtTotalSales = new javax.swing.JTextField();
        txtCashWithdrawals = new javax.swing.JTextField();
        txtExpectedAmount = new javax.swing.JTextField();
        txtActualAmount = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("End of Shift Details");

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        jPanel1.setBorder(dropShadowBorder1);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Shift End Details");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Total Sales (Rs.)");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Cash Withdrawals (Rs.)");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Expected Cash in counter (Rs.)");
        jLabel4.setToolTipText("");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Actual cash amount in counter (Rs.)");
        jLabel5.setToolTipText("");

        btnConfirm.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnConfirm.setText("Confirm");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });

        txtTotalSales.setEditable(false);
        txtTotalSales.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtTotalSales.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtCashWithdrawals.setEditable(false);
        txtCashWithdrawals.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtCashWithdrawals.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtExpectedAmount.setEditable(false);
        txtExpectedAmount.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtExpectedAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtActualAmount.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtActualAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtActualAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtActualAmountKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnConfirm))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotalSales, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addComponent(txtCashWithdrawals)
                            .addComponent(txtExpectedAmount)
                            .addComponent(txtActualAmount))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTotalSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCashWithdrawals, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtExpectedAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtActualAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnConfirm)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        // TODO add your handling code here:
        exit();
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void txtActualAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtActualAmountKeyReleased
        // TODO add your handling code here:
        txtInitialAmountKeyHandler(evt);
    }//GEN-LAST:event_txtActualAmountKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtActualAmount;
    private javax.swing.JTextField txtCashWithdrawals;
    private javax.swing.JTextField txtExpectedAmount;
    private javax.swing.JTextField txtTotalSales;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
