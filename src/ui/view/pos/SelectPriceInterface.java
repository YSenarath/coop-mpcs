package ui.view.pos;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import model.inventory.Batch;
import org.apache.log4j.Logger;

public class SelectPriceInterface extends javax.swing.JInternalFrame {

// <editor-fold defaultstate="collapsed" desc="Variables">
    private static final Logger logger = Logger.getLogger(SelectPriceInterface.class);
    private final InvoiceInternalInterface parent;

    private final int productCode;
    private final ArrayList<Batch> batches;

    private Map<Integer, Batch> batchMap;

    private boolean batchSelected = false;

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Constructor">
    public SelectPriceInterface(InvoiceInternalInterface parent, JDesktopPane desktopPane, int productCode, ArrayList<Batch> batches) {
        logger.debug("SelectPriceInterfa constructor invoked");
        initComponents();

        this.parent = parent;

        Dimension desktopSize = desktopPane.getSize();
        Dimension jInternalFrameSize = this.getSize();
        this.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        this.productCode = productCode;
        this.batches = batches;

        itemPriceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2) {
                    setSelectedBatch((int) itemPriceTable.getValueAt(row, 0));
                }
            }
        });

        initiateBatchView();
        requestFocus();
    }
    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Methods">

    private void initiateBatchView() {
        logger.debug("initiateBatchView invoked");
        lblItemCodeVal.setText(util.Utilities.formatId("P", 4, productCode));

        DefaultTableModel itemTableModel = (DefaultTableModel) itemPriceTable.getModel();
        itemTableModel.setRowCount(0);

        batchMap = new HashMap<>();
        for (Batch batch : batches) {
            batchMap.put(batch.getBatchId(), batch);
            Object[] ob = {
                batch.getBatchId(),
                batch.getExpDate(),
                batch.getQuantity(),
                batch.getUnitPrice()
            };
            itemTableModel.addRow(ob);
        }
    }

    private void setSelectedBatch(int batchId) {
        logger.debug("setSelectedBatch invoked");
        parent.setProductBatch(batchMap.get(batchId));
        batchSelected = true;
        parent.disableGlassPane(false);
        this.dispose();
    }

    private void escapeKeyHandler() {
        if (!batchSelected) {
            parent.invoiceClearProductinfo();
            parent.disableGlassPane(false);

        }
        this.dispose();
    }

    @Override
    public boolean isSelected() {
        return true;
    }

    // </editor-fold>
    //
    //
    //
// <editor-fold defaultstate="collapsed" desc="Netbeans generated Code">   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        containerPanel = new javax.swing.JPanel();
        lblItemCodeVal = new javax.swing.JLabel();
        lblItemCode = new javax.swing.JLabel();
        itemPriceContainerPanel = new javax.swing.JPanel();
        itemPriceSP = new javax.swing.JScrollPane();
        itemPriceTable = new javax.swing.JTable();

        setClosable(true);
        setTitle("Select Price");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShowLeftShadow(true);
        dropShadowBorder1.setShowTopShadow(true);
        containerPanel.setBorder(dropShadowBorder1);

        lblItemCodeVal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblItemCodeVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblItemCodeVal.setText("<Code>");
        lblItemCodeVal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblItemCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblItemCode.setText("Code");

        itemPriceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Batch", "Exp. Date", "Qty", "Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemPriceTable.getTableHeader().setReorderingAllowed(false);
        itemPriceSP.setViewportView(itemPriceTable);

        javax.swing.GroupLayout itemPriceContainerPanelLayout = new javax.swing.GroupLayout(itemPriceContainerPanel);
        itemPriceContainerPanel.setLayout(itemPriceContainerPanelLayout);
        itemPriceContainerPanelLayout.setHorizontalGroup(
            itemPriceContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(itemPriceSP, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        itemPriceContainerPanelLayout.setVerticalGroup(
            itemPriceContainerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(itemPriceSP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout containerPanelLayout = new javax.swing.GroupLayout(containerPanel);
        containerPanel.setLayout(containerPanelLayout);
        containerPanelLayout.setHorizontalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemPriceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(containerPanelLayout.createSequentialGroup()
                        .addComponent(lblItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblItemCodeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 87, Short.MAX_VALUE)))
                .addContainerGap())
        );
        containerPanelLayout.setVerticalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(containerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblItemCodeVal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemPriceContainerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        escapeKeyHandler();
    }//GEN-LAST:event_formInternalFrameClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel containerPanel;
    private javax.swing.JPanel itemPriceContainerPanel;
    private javax.swing.JScrollPane itemPriceSP;
    private javax.swing.JTable itemPriceTable;
    private javax.swing.JLabel lblItemCode;
    private javax.swing.JLabel lblItemCodeVal;
    // End of variables declaration//GEN-END:variables
 // </editor-fold>
}
