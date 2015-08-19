/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report.ledger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import model.ledger.SupplierReturnNote;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Aesky_Server
 */
public class SRNListing {

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

}
