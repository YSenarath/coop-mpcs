/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.inventory;

import java.io.Serializable;

/**
 *
 * @author Nadheesh
 */
public class NotificationInfo implements Serializable {

    private int timeInterval;
    private int intervalType;
    private int timeShowing;
    private int position;
    private int displayType;

    public NotificationInfo(int timeInterval, int intervalType, int timeShowing, int position, int displayType) {
        this.timeInterval = timeInterval;
        this.intervalType = intervalType;
        this.timeShowing = timeShowing;
        this.position = position;
        this.displayType = displayType;
    }

    public NotificationInfo() {
        this.timeInterval = 1;
        this.intervalType = 5;
        this.timeShowing = 10000;
        this.position = 2;
        this.displayType = 0;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public int getIntervalType() {
        return intervalType;
    }

    public int getTimeShowing() {
        return timeShowing;
    }

    public int getPosition() {
        return position;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public void setIntervalType(int intervalType) {
        this.intervalType = intervalType;
    }

    public void setTimeShowing(int timeShowing) {
        this.timeShowing = timeShowing;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
