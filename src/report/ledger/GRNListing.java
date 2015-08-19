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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.log4j.Logger;

/**
 *
 * @author Yasas
 */
public class GRNListing {

    private static final Logger logger = Logger.getLogger(GRNListing.class);
    
    public static void produceListing(ArrayList<GoodRecieveNote> glist, String s, Date from, Date to) throws JRException {
        System.out.println("Commencing report production...");
        URL url = GRNCopy.class.getResource("GRNListing.jasper");
        String sourceFileName = url.getPath();
        String printFileName;

        ArrayList<GoodRecieveNote> dataList = glist;

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

    public static void produceCopyTest(ArrayList<GoodRecieveNote> glist, String s, Date from, Date to) throws SQLException, JRException {
        String jasperFileName = "/report/ledger/GRNListing.jasper";
        String outFileName = "reports/output/ledger/GRNListing";

        ArrayList<GoodRecieveNote> dataList = glist;

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
