package com.java_tutorials.dip_kata_java.user_analytics;

import com.java_tutorials.dip_kata_java.DMSUser;

import java.util.Date;

public abstract class UserEvent {

    private final DMSUser dmsUser;
    private final Date date;

    protected UserEvent(DMSUser dmsUser, Date date) {
	this.dmsUser = dmsUser;
	this.date = date;
    }

    public DMSUser getDmsUser() {
	return dmsUser;
    }

    public Date getDate() {
	return date;
    }

    public abstract <T> T accept(UserEventVisitor<T> userEventVisitor);
}
