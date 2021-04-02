package com.java_tutorials.dip_kata_java;

import java.io.IOException;
import java.io.Writer;

public class DocumentManagementPortalHome {

    public static final int MAX_RECENT_DOCUMENTS_IN_PORTLET = 10;

    private final RecentDocuments recentDocuments;

    public DocumentManagementPortalHome(RecentDocuments recentDocuments) {
        this.recentDocuments = recentDocuments;
    }

    public void renderMostRecentDocumentsPortlet(Writer portletWriter, DMSUser dmsUser) {
        try {
            portletWriter.write("<h1>Most recent documents for user \"" + dmsUser.getUserId() + "\"</h1>");
            portletWriter.write("<ul>");

            recentDocuments.fetchDocumentsAccessedByUserByDateDesc(dmsUser, MAX_RECENT_DOCUMENTS_IN_PORTLET).
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
