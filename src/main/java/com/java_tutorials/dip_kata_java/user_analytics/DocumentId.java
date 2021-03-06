package com.java_tutorials.dip_kata_java.user_analytics;

import java.util.Objects;

public class DocumentId {

    private final String id;

    public DocumentId(String id) {
	this.id = id;
    }

    public String getId() {
	return id;
    }

    @Override public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || getClass() != o.getClass())
	    return false;
	DocumentId that = (DocumentId) o;
	return Objects.equals(id, that.id);
    }

    @Override public int hashCode() {
	return Objects.hash(id);
    }

    @Override public String toString() {
	return "DocumentId{" +
			"id='" + id + '\'' +
			'}';
    }
}
