/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testSuite.dbTest;

/**
 *
 * @author Shehan
 */
public class IntegerMatchTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        //must fail
        System.out.println("0000 - " + match("0000"));
        System.out.println("00000 - " + match("00000"));

        System.out.println("");
        //Must pass
        System.out.println("0001 - " + match("0001"));
        System.out.println("0010 - " + match("0010"));
        System.out.println("0006 - " + match("0006"));
        System.out.println("10000 - " + match("1000"));
        System.out.println("00004 - " + match("00004"));
        System.out.println("20010 - " + match("20010"));

    }

    private static boolean match(String text) {
        try {
            if (text.isEmpty()) {
                return true;
            } else if (text.matches("^(([0-9][0-9][0-9][0-9][1-9])|([0-9][0-9][0-9][1-9]([0-9])?)|([0-9][0-9][1-9][0-9]{0,2})|([0-9][1-9][0-9]{0,3})|([1-9][0-9]{0,4}))$")) {
                return Integer.parseInt(text) >= 0;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
