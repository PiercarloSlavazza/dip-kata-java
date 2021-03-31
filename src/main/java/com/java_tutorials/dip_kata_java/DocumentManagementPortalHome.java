package com.java_tutorials.dip_kata_java;

import com.java_tutorials.dip_kata_java.user_analytics.UserAnalytics;
import com.java_tutorials.dip_kata_java.user_analytics.UserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.UserEventVisitor;
import com.java_tutorials.dip_kata_java.user_analytics.documents.Document;
import com.java_tutorials.dip_kata_java.user_analytics.documents.DocumentsStore;
import com.java_tutorials.dip_kata_java.user_analytics.events.DocumentDownloadedUserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.events.DocumentEditedUserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.events.UserLoginUserEvent;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DocumentManagementPortalHome {

    public static final int MAX_RECENT_DOCUMENTS_IN_PORTLET = 10;

    private final UserAnalytics userAnalytics;
    private final DocumentsStore documentsStore;

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

    public DocumentManagementPortalHome(UserAnalytics userAnalytics, DocumentsStore documentsStore) {
        this.userAnalytics = userAnalytics;
        this.documentsStore = documentsStore;
    }

    private Optional<Document> fetchDocument(UserEvent userEvent) {
        return userEvent.accept(fetchDocumentFromUserEvent);
    }

    private List<Document> getMostRecentDocuments(DMSUser dmsUser) {
        return userAnalytics.listUserEventsByDateDesc().
                        filter(userEvent -> userEvent.getDmsUser().equals(dmsUser)).
                        filter(userEvent -> userEvent.accept(isUserEventAboutDocumentsAccess)).
                        map(this::fetchDocument).
                        filter(Optional::isPresent).
                        map(Optional::get).
                        limit(MAX_RECENT_DOCUMENTS_IN_PORTLET).
                        collect(Collectors.toList());
    }

    public void renderMostRecentDocumentsPortlet(Writer portletWriter, DMSUser dmsUser) {
        try {
            portletWriter.write("<h1>Most recent documents for user \"" + dmsUser.getUserId() + "\"</h1>");
            portletWriter.write("<ul>");

            getMostRecentDocuments(dmsUser).
                            forEach(document -> {
                                try {
                                    portletWriter.write("<li>");
                                    portletWriter.write("<i>");
                                    portletWriter.write(document.getTitle());
                                    portletWriter.write("</i>");
                                    portletWriter.write("</li>");
                                } catch (IOException e) {
                                    throw new RuntimeException("cannot render most recent documents portlet|user|" + dmsUser.getUserId(), e);
                                }
                            });

            portletWriter.write("</ul>");
        } catch (IOException e) {
            throw new RuntimeException("cannot render most recent documents portlet|user|" + dmsUser.getUserId(), e);
        }
    }

}
