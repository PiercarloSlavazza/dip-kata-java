package com.java_tutorials.dip_kata_java.impl;

import com.java_tutorials.dip_kata_java.DMSUser;
import com.java_tutorials.dip_kata_java.RecentDocuments;
import com.java_tutorials.dip_kata_java.user_analytics.UserAnalytics;
import com.java_tutorials.dip_kata_java.user_analytics.UserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.UserEventVisitor;
import com.java_tutorials.dip_kata_java.user_analytics.documents.Document;
import com.java_tutorials.dip_kata_java.user_analytics.documents.DocumentsStore;
import com.java_tutorials.dip_kata_java.user_analytics.events.DocumentDownloadedUserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.events.DocumentEditedUserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.events.UserLoginUserEvent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecentDocumentsFromUserAnalytics implements RecentDocuments {

    private final UserAnalytics userAnalytics;
    private final DocumentsStore documentsStore;

    public RecentDocumentsFromUserAnalytics(UserAnalytics userAnalytics, DocumentsStore documentsStore) {
	this.userAnalytics = userAnalytics;
	this.documentsStore = documentsStore;
    }

    private final UserEventVisitor<Boolean> isUserEventAboutDocumentsAccess = new UserEventVisitor<Boolean>() {

	@Override public Boolean visit(DocumentDownloadedUserEvent documentDownloadedUserEvent) {
	    return true;
	}

	@Override public Boolean visit(DocumentEditedUserEvent documentEditedUserEvent) {
	    return true;
	}

	@Override public Boolean visit(UserLoginUserEvent userLoginUserEvent) {
	    return false;
	}
    };

    private final UserEventVisitor<Optional<Document>> fetchDocumentFromUserEvent = new UserEventVisitor<Optional<Document>>() {
	@Override public Optional<Document> visit(DocumentDownloadedUserEvent documentDownloadedUserEvent) {
	    return documentsStore.fetchDocument(documentDownloadedUserEvent.getDocumentId());
	}

	@Override public Optional<Document> visit(DocumentEditedUserEvent documentEditedUserEvent) {
	    return documentsStore.fetchDocument(documentEditedUserEvent.getDocumentId());
	}

	@Override public Optional<Document> visit(UserLoginUserEvent userLoginUserEvent) {
	    throw new RuntimeException("cannot fetch a document from a login user event");
	}
    };

    private Optional<Document> fetchDocument(UserEvent userEvent) {
	return userEvent.accept(fetchDocumentFromUserEvent);
    }

    @Override public List<Document> fetchDocumentsAccessedByUserByDateDesc(DMSUser dmsUser, int maxDocuments) {
	return userAnalytics.listUserEventsByDateDesc().
			filter(userEvent -> userEvent.getDmsUser().equals(dmsUser)).
			filter(userEvent -> userEvent.accept(isUserEventAboutDocumentsAccess)).
			map(this::fetchDocument).
			filter(Optional::isPresent).
			map(Optional::get).
			limit(maxDocuments).
			collect(Collectors.toList());
    }

}
