package com.java_tutorials.dip_kata_java.user_analytics.impl;

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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserAnalyticsImpl implements UserAnalytics, RecentDocuments {

    protected List<UserEvent> userEventsSortedByDateDesc;
    private final DocumentsStore documentsStore;

    protected UserAnalyticsImpl(DocumentsStore documentsStore) {
	this.documentsStore = documentsStore;
    }

    protected void setUserEventsSortedByDateDesc(Set<UserEvent> userEvents) {
	this.userEventsSortedByDateDesc = userEvents.stream().sorted(Comparator.comparing(UserEvent::getDate).reversed()).collect(Collectors.toList());
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

    @Override public Stream<UserEvent> listUserEventsByDateDesc() {
	return userEventsSortedByDateDesc.stream();
    }

    private Optional<Document> fetchDocument(UserEvent userEvent) {
	return userEvent.accept(fetchDocumentFromUserEvent);
    }

    @Override public List<Document> fetchDocumentsAccessedByUserByDateDesc(DMSUser dmsUser, int maxDocuments) {
	return userEventsSortedByDateDesc.
			stream().
			filter(userEvent -> userEvent.getDmsUser().equals(dmsUser)).
			filter(userEvent -> userEvent.accept(isUserEventAboutDocumentsAccess)).
			map(this::fetchDocument).
			filter(Optional::isPresent).
			map(Optional::get).
			limit(maxDocuments).
			collect(Collectors.toList());
    }
}
