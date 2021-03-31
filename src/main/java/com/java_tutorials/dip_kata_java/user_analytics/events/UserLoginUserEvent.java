package com.java_tutorials.dip_kata_java.user_analytics.events;

import com.java_tutorials.dip_kata_java.DMSUser;
import com.java_tutorials.dip_kata_java.user_analytics.UserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.UserEventVisitor;

import java.util.Date;

public class UserLoginUserEvent extends UserEvent {

    public UserLoginUserEvent(DMSUser dmsUser, Date date) {
	super(dmsUser, date);
    }

    @Override public <T> T accept(UserEventVisitor<T> userEventVisitor) {
	return userEventVisitor.visit(this);
    }
}
