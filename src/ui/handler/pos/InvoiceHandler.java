/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.handler.pos;

import controller.inventory.BatchController;
import controller.inventory.ProductController;
import controller.pos.InvoiceController;
import java.sql.SQLException;
import java.util.ArrayList;
import model.inventory.Product;
import model.pos.Invoice;

/**
 *
 * @author Shehan
 */
public class InvoiceHandler {

    public static int getLastInvoicelId() throws SQLException {
        Invoice invoice = InvoiceController.getLastInvoiceId();
        if (invoice != null) {
            return invoice.getInvoiceNo();
        }
        return 0;
    }

    public static ArrayList<Product> getAllProducts() throws SQLException {
        return ProductController.getAllProducts();
    }

    public static ArrayList<Product> getAllSellebleProducts() throws SQLException {
        ArrayList<Product> products = ProductController.getAllAvailableProducts();
        ArrayList<Product> availableProducts = new ArrayList();

        for (Product product : products) {
            product.setBatches(BatchController.getAllAvailableBatches(product.getProductId()));
            if (product.getBatches().size() > 0) {
                availableProducts.add(product);
            }
        }
        return availableProducts;
    }

}
