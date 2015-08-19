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
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Aesky_Server
 */
public class GRNCopy {

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

}
