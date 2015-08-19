/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.handler.inventory;

import controller.inventory.BatchController;
import controller.inventory.ProductController;
import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.inventory.Batch;
import model.inventory.Product;
import net.sf.jcarrierpigeon.Notification;
import net.sf.jcarrierpigeon.NotificationQueue;
import net.sf.jcarrierpigeon.WindowPosition;
import org.apache.log4j.Logger;
import ui.view.inventory.NotificationFrame;
import ui.view.inventory.NotificationPanel;
import util.Utilities;

/**
 *
 * @author Nadheesh
 */
public class NotificationPanelHandler {

    private NotificationPanel gui;
    private ArrayList<Batch> notifybatches;
    private ArrayList<Product> reoRderProducts;
    private int expiredbatches;
    private int notifyBatches;
    private final static Logger logger = Logger.getLogger(NotificationPanelHandler.class);
    private int outOfStockBatches;
    private int finishingProducts;
    private NotificationQueue queue;
    

    //timer service
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    //

    public NotificationPanelHandler(NotificationPanel gui) {
        this.gui = gui;
        queue = new NotificationQueue();

    }

    public void showAboutToFinish(boolean onlyOutOfStock) throws SQLException {

        loadCounts();
        setButtonNamesWithCounts();

        DefaultTableModel tbModel = (DefaultTableModel) (gui.getLowQuantityTable().getModel());

        tbModel.setRowCount(0);

        for (Product p : reoRderProducts) {

            if (onlyOutOfStock && p.getTotalQuantity() > 0) {
                continue;
            }
            tbModel.addRow(new String[]{
                p.getProductId(),
                p.getProductName(),
                p.getTotalQuantity() + "",
                p.getUnit(),
                p.getReorderLevel() + ""
            });
        }
        colorOutOfStockProducts(!onlyOutOfStock);

    }

    public synchronized void loadCounts() throws SQLException {

        reoRderProducts = ProductController.getAboutToFinishGoods();
        notifybatches = BatchController.getCloseToExpireBatches();

        expiredbatches = 0;
        notifyBatches = 0;
        outOfStockBatches = 0;
        finishingProducts = 0;

        for (Batch b : notifybatches) {
            if (b.getExpirationDate() != null && b.getExpirationDate().compareTo(Utilities.getToday()) <= 0) {
                expiredbatches++;
            }
            if (b.getNotificationDate() != null && b.getNotificationDate().compareTo(Utilities.getToday()) <= 0) {
                notifyBatches++;
            }
        }

        for (Product p : reoRderProducts) {
            if (p.getTotalQuantity() <= p.getReorderLevel()) {
                finishingProducts++;
            }
            if (p.getTotalQuantity() <= 0) {
                outOfStockBatches++;
            }
        }
    }

    public void showCloseToExpireBatches(boolean onlyExpired) throws SQLException {

        loadCounts();
        setButtonNamesWithCounts();

        DefaultTableModel tbModel = (DefaultTableModel) (gui.getExpireTable().getModel());

        tbModel.setRowCount(0);

        for (Batch b : notifybatches) {

            String expDate;
            String notifyDate;
            boolean c;
            if (b.getNotificationDate() != null) {
                if (onlyExpired && b.getExpirationDate() == null) {
                    continue;
                } else if (onlyExpired && b.getExpirationDate().compareTo(Utilities.getToday()) > 0) {
                    continue;
                } else if (b.getExpirationDate() != null) {
                    expDate = Utilities.getStringDate(b.getExpirationDate());
                } else {
                    expDate = "Not Set";
                }
                notifyDate = Utilities.getStringDate(b.getNotificationDate());

            } else {
                notifyDate = "Not Set";

                if (onlyExpired && b.getExpirationDate() != null && b.getExpirationDate().compareTo(Utilities.getToday()) <= 0) {
                    expDate = Utilities.getStringDate(b.getExpirationDate());
                } else {
                    continue;
                }
            }
            tbModel.addRow(new String[]{
                b.getProductId(),
                b.getProductName(),
                b.getBatchId(),
                b.getGrnNumber(),
                b.getSupplierName(),
                b.getRecievedQuantity() + "",
                expDate,
                notifyDate

            });
        }
        colorExpiredBatches(!onlyExpired);

    }

    private void colorExpiredBatches(boolean setColor) {

        gui.getExpireTable().setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (table.getRowCount() > 0) {
                    Date expDate = Utilities.getDateFromString(table.getModel().getValueAt(row, 6).toString());
                    if (expDate == null) {
                        setBackground(Color.GRAY);
                        setForeground(Color.WHITE);
                    } else if (setColor && expDate.compareTo(Utilities.getToday()) <= 0) {
                        setBackground(Color.RED);
                        setForeground(Color.WHITE);
                    } else {
                        setBackground(table.getBackground());
                        setForeground(table.getForeground());
                    }
                }
                return this;
            }
        });
    }

    private void colorOutOfStockProducts(boolean setColor) {

        gui.getLowQuantityTable().setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (table.getRowCount() > 0) {
                    try {
                        double qty = Double.parseDouble(table.getModel().getValueAt(row, 2).toString());
                        if (setColor && qty == 0) {
                            setBackground(Color.RED);
                            setForeground(Color.WHITE);
                        } else {
                            setBackground(table.getBackground());
                            setForeground(table.getForeground());
                        }
                    } catch (NumberFormatException ex) {
                        setBackground(table.getBackground());
                        setForeground(table.getForeground());
                    }

                }
                return this;
            }
        });
    }

    public void notifyUser() {
        
        
        final Runnable notifier = new Runnable() {
            Notification note;
            String aboutToExpire = "";
            String expired = "";
            String aboutToFinish = "";
            String finished = "";
            
            
            @Override
            public void run() {
                try {
                    loadCounts();
                } catch (SQLException ex) {
                    logger.error(ex);
                }
                
                if (expiredbatches > 0){
                    expired = "Expired Batches : " + expiredbatches;
                }
                if (notifyBatches > 0){
                    aboutToExpire = "Batches are about to expire : " + notifyBatches;
                }
                if (finishingProducts > 0){
                    aboutToFinish = "Low stock Products : " +finishingProducts;
                }
                if (outOfStockBatches > 0){
                    finished = "Out Of stock Products : " +outOfStockBatches;
                }
                
                NotificationFrame nf = new NotificationFrame(gui);
                nf.getNotificationTextField().setText(""
                        + aboutToExpire + "\n" 
                        + expired + "\n\n" 
                        + aboutToFinish + "\n"
                        + finished );
                
                note = new Notification(nf, WindowPosition.BOTTOMRIGHT, 25, 25, 10000);
                
                nf.setNote(note);
                queue.add(note);
            }
        };

        scheduler.scheduleAtFixedRate(notifier, 0, 5, TimeUnit.MINUTES);
    }

    public void setButtonNamesWithCounts() throws SQLException {

        gui.getAboutToExpireB().setText("About To Expire (" + notifyBatches + ")");
        gui.getExpiredBatchesB().setText("Expired Batches (" + expiredbatches + ")");
        gui.getAboutToFinishB().setText("About To Finish (" + finishingProducts + ")");
        gui.getOutOfStockB().setText("Out Of Stock (" + outOfStockBatches + ")");

    }
}
