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
import model.inventory.NotificationInfo;
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
    private NotificationInfo settings;

    //timer service
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    //

    public NotificationPanelHandler(NotificationPanel gui) {
        this.gui = gui;
        queue = new NotificationQueue();
        settings = new NotificationInfo();

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

                if (expiredbatches > 0) {
                    expired = "Expired Batches : " + expiredbatches;
                }
                if (notifyBatches > 0) {
                    aboutToExpire = "Batches are about to expire : " + notifyBatches;
                }
                if (finishingProducts > 0) {
                    aboutToFinish = "Low stock Products : " + finishingProducts;
                }
                if (outOfStockBatches > 0) {
                    finished = "Out Of stock Products : " + outOfStockBatches;
                }

                NotificationFrame nf = new NotificationFrame(gui);
                nf.getNotificationTextField().setText(""
                        + aboutToExpire + "\n"
                        + expired + "\n\n"
                        + aboutToFinish + "\n"
                        + finished);

                WindowPosition wp;
                switch (settings.getPosition()) {
                    case 0:
                        wp = WindowPosition.TOPLEFT;
                        break;
                    case 1:
                        wp = WindowPosition.TOPRIGHT;
                        break;
                    case 2:
                        wp = WindowPosition.BOTTOMRIGHT;
                        break;
                    case 3:
                        wp = WindowPosition.BOTTOMLEFT;
                        break;
                    default:
                        wp = WindowPosition.BOTTOMRIGHT;
                        break;
                }
                int n = 60;
                
                switch (settings.getDisplayType()) {
                    case 0:
                        n = 60;
                        break;
                    case 1:
                        n = 3600;
                        break;

                    default:
                        n = 60;
                        break;
                }
                
                
                logger.info("DisplayType " + settings.getDisplayType());
                logger.info("TimeShowing " + settings.getTimeShowing());
                
                logger.info("Position " + settings.getPosition());
                logger.info("IntervalType " + settings.getIntervalType());
                
                 
                logger.info("TimeInterval " + settings.getTimeInterval());
                
                note = new Notification(nf, wp, 20, 20, settings.getTimeShowing()*n);
                nf.setNote(note);
                queue.add(note);
            }
        };

        TimeUnit tu;

        switch (settings.getIntervalType()) {
            case 0:
                tu = TimeUnit.MINUTES;
                break;
            case 1:
                tu = TimeUnit.HOURS;
                break;
            case 2:
                tu = TimeUnit.DAYS;
                break;
            default:
                tu = TimeUnit.MINUTES;
                break;
        }
        if (settings.getTimeInterval()
                <= 0) {
            settings.setTimeInterval(1);
        }

        scheduler.scheduleAtFixedRate(notifier,
                0, settings.getTimeInterval(), tu);
    }

    public void setButtonNamesWithCounts() throws SQLException {

        gui.getAboutToExpireB().setText("About To Expire (" + notifyBatches + ")");
        gui.getExpiredBatchesB().setText("Expired Batches (" + expiredbatches + ")");
        gui.getAboutToFinishB().setText("About To Finish (" + finishingProducts + ")");
        gui.getOutOfStockB().setText("Out Of Stock (" + outOfStockBatches + ")");

    }

    private void storeSettings(NotificationInfo ni) {
        try {
            Utilities.saveProperty("timeInterval", +ni.getTimeInterval() + "");
            Utilities.saveProperty("intervalType", ni.getIntervalType() + "");
            Utilities.saveProperty("timeShowing", ni.getTimeShowing() + "");
            Utilities.saveProperty("position", ni.getPosition() + "");
            Utilities.saveProperty("displayType", ni.getDisplayType() + "");
            settings = ni;
        } catch (Exception ex) {
            Utilities.ShowErrorMsg(gui, "Error! Settings saving faild");
        }
    }

    private NotificationInfo retrieveSettings() {

        try {
            int timeInterval = Integer.parseInt(Utilities.loadProperty("timeInterval"));
            logger.debug("Step 1 passed");
            int intervalType = Integer.parseInt(Utilities.loadProperty("intervalType"));
            logger.debug("Step 2 passed");
            int timeShowing = Integer.parseInt(Utilities.loadProperty("timeShowing"));
            logger.debug("Step 3 passed");
            int position = Integer.parseInt(Utilities.loadProperty("position"));
            logger.debug("Step 4 passed");
            int displayType = Integer.parseInt(Utilities.loadProperty("displayType"));
            logger.debug("Step 5 passed");
            return new NotificationInfo(timeInterval, intervalType, timeShowing, position, displayType);

        } catch (NumberFormatException ex) {
            Utilities.ShowErrorMsg(gui, "Error! Settings loading faild");
            return new NotificationInfo();
        }

    }

    public void loadSettings() {
        settings = retrieveSettings();
        gui.getInterval().setValue(settings.getTimeInterval());
        logger.debug("Loaded display Time : " + settings.getTimeInterval());

        switch (settings.getIntervalType()) {
            case 0:
                gui.getMinR().setSelected(true);
                break;
            case 1:
                gui.getHoursR().setSelected(true);
                break;
            case 2:
                gui.getDaysR().setSelected(true);
                break;
            default:
                gui.getMinR().setSelected(true);
                break;
        }
        switch (settings.getPosition()) {
            case 0:
                gui.getTopLeft().setSelected(true);
                break;
            case 1:
                gui.getTopRight().setSelected(true);
                break;
            case 2:
                gui.getBottomRight().setSelected(true);
                break;
            case 3:
                gui.getBottomLeft().setSelected(true);
                break;
            default:
                gui.getBottomRight().setSelected(true);
                break;
        }
        switch (settings.getDisplayType()) {
            case 0:
                gui.getSecR1().setSelected(true);
                gui.getDisplayTime().setValue((settings.getTimeShowing() * 60) / 1000);
                break;
            case 1:
                gui.getMinR1().setSelected(true);
                gui.getDisplayTime().setValue((settings.getTimeShowing() * 3600) / 1000);
                break;

            default:
                gui.getSecR1().setSelected(true);
                gui.getDisplayTime().setValue((settings.getTimeShowing() * 60) / 1000);
                break;
        }
    }

    public void saveSettings() {
        NotificationInfo ni = new NotificationInfo();

        if (gui.getMinR1().isSelected()) {
            ni.setTimeShowing((gui.getDisplayTime().getValue() * 1000) / 3600);
            ni.setDisplayType(1);
        } else if (gui.getSecR1().isSelected()) {
            ni.setTimeShowing((gui.getDisplayTime().getValue() * 1000) / 60);
            ni.setDisplayType(0);
        }

        if (gui.getMinR().isSelected()) {
            ni.setIntervalType(0);
        } else if (gui.getHoursR().isSelected()) {
            ni.setIntervalType(1);
        } else if (gui.getDaysR().isSelected()) {
            ni.setIntervalType(2);
        }

        if (gui.getTopLeft().isSelected()) {
            ni.setPosition(0);
        } else if (gui.getTopRight().isSelected()) {
            ni.setPosition(1);
        } else if (gui.getBottomLeft().isSelected()) {
            ni.setPosition(3);
        } else if (gui.getBottomRight().isSelected()) {
            ni.setPosition(2);
        }

        ni.setTimeInterval(gui.getInterval().getValue());

        storeSettings(ni);
    }

    public NotificationInfo getSettings() {
        return settings;
    }

}
