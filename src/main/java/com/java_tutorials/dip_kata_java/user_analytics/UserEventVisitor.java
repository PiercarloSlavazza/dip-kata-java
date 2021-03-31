package com.java_tutorials.dip_kata_java.user_analytics;

import com.java_tutorials.dip_kata_java.user_analytics.events.DocumentDownloadedUserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.events.DocumentEditedUserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.events.UserLoginUserEvent;

public interface UserEventVisitor<T> {

    T visit(DocumentDownloadedUserEvent documentDownloadedUserEvent);
    T visit(DocumentEditedUserEvent documentEditedUserEvent);
    T visit(UserLoginUserEvent userLoginUserEvent);
}
