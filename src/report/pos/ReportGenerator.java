/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report.pos;

import database.connector.DBConnection;
import java.sql.SQLException;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.log4j.Logger;
import util.Utilities;

/**
 *
 * @author Shehan
 */
public class ReportGenerator {

    private ReportGenerator() {

    }
    private static final Logger logger = Logger.getLogger(ReportGenerator.class);

    public static void generateSignOnSlip() {
        logger.debug("generateSignOnSlip invoked ");

        String fileName = "reports/template/pos/cashierSignOn.jasper";
        String outFileName = "reports/output/pos/cashierSignOn.pdf";
        HashMap hm = new HashMap();
        try {
            hm.put("counter_id", Integer.parseInt(Utilities.loadProperty("counter")));
            // Fill the report using an empty data source
            JasperPrint print = JasperFillManager.fillReport(fileName, hm, DBConnection.getConnectionToDB());

            // Create a PDF exporter
            JRExporter exporter = new JRPdfExporter();

            // Configure the exporter (set output file name and print object)
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

            // Export the PDF file
            exporter.exportReport();

        } catch (JRException | SQLException ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }
}
