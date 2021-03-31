package com.java_tutorials.dip_kata_java;

import java.util.Objects;

public class DMSUser {

    private final String userId;

    public DMSUser(String userId) {
	this.userId = userId;
    }

    public String getUserId() {
	return userId;
    }

    @Override public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || getClass() != o.getClass())
	    return false;
	DMSUser dmsUser = (DMSUser) o;
	return Objects.equals(userId, dmsUser.userId);
    }

    @Override public int hashCode() {
	return Objects.hash(userId);
    }

    @Override public String toString() {
	return "DMSUser{" +
			"userId='" + userId + '\'' +
			'}';
    }
}
