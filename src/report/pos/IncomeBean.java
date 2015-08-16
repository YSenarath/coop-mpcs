/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report.pos;

/**
 *
 * @author Shehan
 */
public class IncomeBean {

    private final String desc;
    private final double expected;
    private final double signOff;
    private final double varience;

    public IncomeBean(String desc, double expected, double signOff, double varience) {
        this.desc = desc;
        this.expected = expected;
        this.signOff = signOff;
        this.varience = varience;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @return the expected
     */
    public double getExpected() {
        return expected;
    }

    /**
     * @return the signOff
     */
    public double getSignOff() {
        return signOff;
    }

    /**
     * @return the varience
     */
    public double getVarience() {
        return varience;
    }

}
