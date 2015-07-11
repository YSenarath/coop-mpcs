/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.pos;

import database.connector.DBConnection;
import database.handler.DBHandler;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.inventory.Product;
import model.pos.Invoice;
import util.definitions.AppConstants;

/**
 *
 * @author Shehan
 */
public class InvoiceController {
    public static Invoice getLastInvoiceId() throws SQLException{
         Connection connection = DBConnection.getConnectionToDB();
        String query = "SELECT bill_id FROM " + AppConstants.INVOICE + " ORDER BY bill_id DESC LIMIT 1";
        
        ResultSet resultSet = DBHandler.getData(connection, query);

        if (resultSet.next()) {
            
           return new Invoice(resultSet.getInt("bill_id"));
        }
        return null;
    }
    
}
