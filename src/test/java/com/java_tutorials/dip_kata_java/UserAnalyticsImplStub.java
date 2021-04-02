package com.java_tutorials.dip_kata_java;

import com.java_tutorials.dip_kata_java.user_analytics.UserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.documents.DocumentsStore;
import com.java_tutorials.dip_kata_java.user_analytics.impl.UserAnalyticsImpl;

import java.util.Set;

public class UserAnalyticsImplStub extends UserAnalyticsImpl {
    protected UserAnalyticsImplStub(DocumentsStore documentsStore) {
	super(documentsStore);
    }

    @Override protected void setUserEventsSortedByDateDesc(Set<UserEvent> userEvents) {
	super.setUserEventsSortedByDateDesc(userEvents);
    }
}
