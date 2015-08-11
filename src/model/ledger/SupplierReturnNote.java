package model.ledger;

import java.util.ArrayList;
import java.util.Date;
import model.ledger.item.SRNItem;
import model.supplier.Supplier;

public class SupplierReturnNote {

    private String f19Number;
    private String grnNumber;
    private Date date;
    private Supplier supplier;
    private String location;

    ArrayList<SRNItem> items;

    public SupplierReturnNote(String f19Number, String grnNumber, Date date, Supplier supplier, String location) {
        this.f19Number = f19Number;
        this.grnNumber = grnNumber;
        this.date = date;
        this.supplier = supplier;
        this.location = location;
        this.items = new ArrayList<>();
    }

    public SupplierReturnNote(String f19Number, String grnNumber, Date date, Supplier supplier, String location, ArrayList<SRNItem> items) {
        this.f19Number = f19Number;
        this.grnNumber = grnNumber;
        this.date = date;
        this.supplier = supplier;
        this.location = location;
        this.items = items;
    }

    public String getF19Number() {
        return f19Number;
    }

    public void setF19Number(String f19Number) {
        this.f19Number = f19Number;
    }

    public String getGrnNumber() {
        return grnNumber;
    }

    public void setGrnNumber(String grnNumber) {
        this.grnNumber = grnNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void addItem(SRNItem itm) {
        items.add(itm);
    }

    public ArrayList<SRNItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<SRNItem> items) {
        this.items = items;
    }
}
