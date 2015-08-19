/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report.ledger;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import model.inventory.Batch;
import model.ledger.GoodRecieveNote;
import model.ledger.SupplierReturnNote;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.log4j.Logger;

/**
 *
 * @author Aesky_Server
 */
public class SRNListing {

    private static final Logger logger = Logger.getLogger(SRNListing.class);

    public static void produceListing(ArrayList<SupplierReturnNote> list, String s, Date from, Date to) throws JRException {
        System.out.println("Commencing report production...");
        URL url = GRNCopy.class.getResource("SRNListing.jasper");
        String sourceFileName = url.getPath();
        String printFileName;

        ArrayList<SupplierReturnNote> dataList = list;

        Map parameters = new HashMap();

        Date reportDate = util.Utilities.getCurrentDate();

        parameters.put("supplier", s);
        parameters.put("date", reportDate);
        parameters.put("fromDate", from);
        parameters.put("toDate", to);

        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        printFileName = JasperFillManager.fillReportToFile(
                sourceFileName,
                parameters,
                beanColDataSource);

        if (printFileName != null) {
            JasperPrintManager.printReport(
                    printFileName, true);
        }
    }

    public static void produceCopyTest(ArrayList<SupplierReturnNote> list, String s, Date from, Date to) throws SQLException, JRException {
        String jasperFileName = "/report/ledger/SRNListing.jasper";
        String outFileName = "reports/output/ledger/SRNListing";

        ArrayList<SupplierReturnNote> dataList = list;

        Map parameters = new HashMap();

        Date reportDate = util.Utilities.getCurrentDate();

        parameters.put("supplier", s);
        parameters.put("date", reportDate);
        parameters.put("fromDate", from);
        parameters.put("toDate", to);

        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);

        JasperPrint jasperPrint = JasperFillManager.fillReport(GRNCopy.class.getResourceAsStream(jasperFileName), parameters, beanColDataSource);

        try {
            ReportPrinter.showReport(jasperPrint);
            ReportPrinter.saveAsPDF(jasperPrint, outFileName);
        } catch (Exception ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }

}
