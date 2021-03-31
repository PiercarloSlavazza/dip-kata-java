package com.java_tutorials.dip_kata_java.user_analytics;

import java.util.stream.Stream;

public interface UserAnalytics {

    Stream<UserEvent> listUserEventsByDateDesc();

}
