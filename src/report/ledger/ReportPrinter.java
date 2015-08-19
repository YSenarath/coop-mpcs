package report.ledger;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Logger;
import org.apache.velocity.texen.util.FileUtil;
import report.pos.ReportGenerator;

public class ReportPrinter {

    private static final Logger logger = Logger.getLogger(ReportPrinter.class);

    public static void showReport(JasperPrint jasperPrint) throws Exception {
        logger.debug("showReport invoked ");

        if (jasperPrint != null) {
            JasperViewer.viewReport(jasperPrint, false);
        }

    }

    public static void saveAsPDF(JasperPrint jasperPrint, String outFileName) throws Exception {
        logger.debug("saveAsPDF invoked ");

        if (jasperPrint != null && outFileName != null && !outFileName.isEmpty()) {
            FileUtil.mkdir("reports/output/pos/");

            java.util.Date date = new java.util.Date(System.currentTimeMillis());

            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
            String dateString = formatter.format(date);

            // Create a PDF exporter
            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outFileName + "_" + dateString + ".pdf"));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);

            exporter.exportReport();
            logger.info("PDF file creadted");
        }
    }

}
