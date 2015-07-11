/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.view.pos;

/**
 *
 * @author Shehan
 */
public class ProductComboItem {

    private final int itemCode;
    private final String value;

    public ProductComboItem(int itemCode, String value) {
        this.itemCode = itemCode;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public int getItemCode() {
        return itemCode;
    }

    public String getValue() {
        return value;
    }
}
