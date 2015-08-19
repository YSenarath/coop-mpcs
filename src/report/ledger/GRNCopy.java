/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report.ledger;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
 * @author Aesky_Server
 */
public class GRNCopy {

    private static final Logger logger = Logger.getLogger(GRNCopy.class);

    public static void produceCopy(GoodRecieveNote grn) throws SQLException, JRException {
        System.out.println("Commencing report production...");
        URL url = GRNCopy.class.getResource("F16BDocumentCopyTemplate.jasper");
        String sourceFileName = url.getPath();
        String printFileName;

        ArrayList<Batch> dataList = grn.getBatches();

        Map parameters = grn.getParameters();

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

    public static void produceCopyTest(GoodRecieveNote grn) throws SQLException, JRException {
        String jasperFileName = "/report/ledger/F16BDocumentCopyTemplate.jasper";
        String outFileName = "reports/output/ledger/F61BReport";

        ArrayList<Batch> dataList = grn.getBatches();

        Map parameters = grn.getParameters();

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
