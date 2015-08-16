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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.log4j.Logger;
import org.apache.velocity.texen.util.FileUtil;
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

        String fileName = "/model/report/pos/cashierSignOn.jasper";
        String outFileName = "reports/output/pos/cashierSignOn.pdf";
        FileUtil.mkdir("reports/output/pos/");
        HashMap hm = new HashMap();
        try {
            hm.put("counter_id", Integer.parseInt(Utilities.loadProperty("counter")));

            JasperPrint jasperPrint = JasperFillManager.fillReport(new ReportGenerator().getClass().getResourceAsStream(fileName), hm, DBConnection.getConnectionToDB());

            // Create a PDF exporter
            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outFileName));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);

            exporter.exportReport();
        } catch (JRException | SQLException ex) {
            logger.error("Error : " + ex.getMessage());
        }
    }
}
