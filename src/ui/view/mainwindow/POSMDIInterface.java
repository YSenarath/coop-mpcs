package ui.view.mainwindow;


import javax.swing.JFrame;

public class POSMDIInterface extends javax.swing.JFrame {

// <editor-fold defaultstate="collapsed" desc="Constructor">
    public POSMDIInterface(boolean isDebugMode) {
      //  logger.debug("POSMDIInterface constructor invoked");

        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    }

    // </editor-fold>
    
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
        desktopContainerPanel = new javax.swing.JPanel();
        desktopPane = new javax.swing.JDesktopPane();
        welcomePanel = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        controlPanel = new javax.swing.JPanel();
        btnCashierLog = new javax.swing.JButton();
        btnManagerLog = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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

        btnNewSale.setText("New Sale");
        btnNewSale.setToolTipText("");
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
        btnHoldSale.setEnabled(false);
        btnHoldSale.setPreferredSize(new java.awt.Dimension(73, 30));
        btnHoldSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoldSaleActionPerformed(evt);
            }
        });
        billTaskPane.getContentPane().add(btnHoldSale);

        btnRestoreSale.setText("Restore Sale [F10 ]");
        btnRestoreSale.setEnabled(false);
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
                .addComponent(logoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder3 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder3.setShowLeftShadow(true);
        dropShadowBorder3.setShowTopShadow(true);
        cardContainerPanel.setBorder(dropShadowBorder3);
        cardContainerPanel.setLayout(new java.awt.CardLayout());

        desktopContainerPanel.setMinimumSize(new java.awt.Dimension(1050, 750));
        desktopContainerPanel.setPreferredSize(new java.awt.Dimension(1050, 750));

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder4 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder4.setShowLeftShadow(true);
        dropShadowBorder4.setShowTopShadow(true);
        desktopPane.setBorder(dropShadowBorder4);

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
                .addContainerGap(343, Short.MAX_VALUE)
                .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        welcomePanelLayout.setVerticalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(welcomePanelLayout.createSequentialGroup()
                .addGroup(welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, welcomePanelLayout.createSequentialGroup()
                        .addContainerGap(417, Short.MAX_VALUE)
                        .addComponent(lblWelcome))
                    .addGroup(welcomePanelLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)))
                .addContainerGap())
        );

        desktopPane.add(welcomePanel);
        welcomePanel.setBounds(80, 90, 0, 0);

        javax.swing.GroupLayout desktopContainerPanelLayout = new javax.swing.GroupLayout(desktopContainerPanel);
        desktopContainerPanel.setLayout(desktopContainerPanelLayout);
        desktopContainerPanelLayout.setHorizontalGroup(
            desktopContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE)
        );
        desktopContainerPanelLayout.setVerticalGroup(
            desktopContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
        );

        cardContainerPanel.add(desktopContainerPanel, "desktopCard");

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder5 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder5.setShowLeftShadow(true);
        dropShadowBorder5.setShowTopShadow(true);
        controlPanel.setBorder(dropShadowBorder5);

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
                    .addComponent(controlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cardContainerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 955, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cardContainerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(counterInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jxTaskPaneContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewSaleActionPerformed
        // TODO add your handling code here:
   
    }//GEN-LAST:event_btnNewSaleActionPerformed

    private void btnCheckStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckStockActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnCheckStockActionPerformed

    private void btnHoldSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoldSaleActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnHoldSaleActionPerformed

    private void billTaskPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_billTaskPaneMouseClicked
        // TODO add your handling code here:
       
    }//GEN-LAST:event_billTaskPaneMouseClicked

    private void btnCashierLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashierLogActionPerformed
       
    }//GEN-LAST:event_btnCashierLogActionPerformed

    private void btnManagerLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManagerLogActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_btnManagerLogActionPerformed

    private void btnRestoreSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestoreSaleActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnRestoreSaleActionPerformed

    private void otherTaskPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_otherTaskPaneMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_otherTaskPaneMouseClicked

    private void btnCashWithdrawalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashWithdrawalActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnCashWithdrawalActionPerformed

    private void btnBillCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillCopyActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnBillCopyActionPerformed

    private void btnBillRefundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillRefundActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnBillRefundActionPerformed

    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new POSMDIInterface(true).setVisible(true);
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
