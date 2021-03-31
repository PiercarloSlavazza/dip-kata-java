package com.java_tutorials.dip_kata_java.user_analytics.events;

import com.java_tutorials.dip_kata_java.DMSUser;
import com.java_tutorials.dip_kata_java.user_analytics.DocumentId;
import com.java_tutorials.dip_kata_java.user_analytics.UserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.UserEventVisitor;

import java.util.Date;
import java.util.Objects;

public class DocumentDownloadedUserEvent extends UserEvent {

    private final DocumentId documentId;

    public DocumentDownloadedUserEvent(DMSUser dmsUser, Date date, DocumentId documentId) {
	super(dmsUser, date);
	this.documentId = documentId;
    }

    public DocumentId getDocumentId() {
	return documentId;
    }

    @Override public <T> T accept(UserEventVisitor<T> userEventVisitor) {
	return userEventVisitor.visit(this);
    }

    @Override public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || getClass() != o.getClass())
	    return false;
	DocumentDownloadedUserEvent that = (DocumentDownloadedUserEvent) o;
	return Objects.equals(documentId, that.documentId);
    }

    @Override public int hashCode() {
	return Objects.hash(documentId);
    }
}
