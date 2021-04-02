package com.java_tutorials.dip_kata_java;

import com.java_tutorials.dip_kata_java.impl.RecentDocumentsFromUserAnalytics;
import com.java_tutorials.dip_kata_java.user_analytics.DocumentId;
import com.java_tutorials.dip_kata_java.user_analytics.UserAnalytics;
import com.java_tutorials.dip_kata_java.user_analytics.UserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.documents.Document;
import com.java_tutorials.dip_kata_java.user_analytics.documents.DocumentsStore;
import com.java_tutorials.dip_kata_java.user_analytics.events.DocumentDownloadedUserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.events.DocumentEditedUserEvent;
import com.java_tutorials.dip_kata_java.user_analytics.events.UserLoginUserEvent;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import static com.java_tutorials.dip_kata_java.utils.DateBuilder.date;
import static java.util.TimeZone.getTimeZone;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DocumentManagementPortalHomeTest {

    private DocumentManagementPortalHome portalHome;
    private UserAnalytics userAnalytics;
    private DocumentsStore documentsStore;
    private RecentDocumentsFromUserAnalytics recentDocuments;

    @Before
    public void setup() {
        documentsStore = mock(DocumentsStore.class);
        userAnalytics = mock(UserAnalytics.class);
        recentDocuments = new RecentDocumentsFromUserAnalytics(userAnalytics, documentsStore);
        portalHome = new DocumentManagementPortalHome(recentDocuments);
    }

    private Date addHours(Date date, int hours) {
        return new Date(date.getTime() + hours * 3600000L);
    }

    @SuppressWarnings("SameParameterValue") private StringBuilder htmlOfDocumentsPortlet(final String user, String firstTitle, String secondTitle) {
        StringBuilder expectedPortletHtml = new StringBuilder();
        expectedPortletHtml.append("<h1>Most recent documents for user \"").append(user).append("\"</h1>");
        expectedPortletHtml.append("<ul>");
        expectedPortletHtml.append("<li>");
        expectedPortletHtml.append("<i>");
        expectedPortletHtml.append(firstTitle);
        expectedPortletHtml.append("</i>");
        expectedPortletHtml.append("</li>");
        expectedPortletHtml.append("<li>");
        expectedPortletHtml.append("<i>");
        expectedPortletHtml.append(secondTitle);
        expectedPortletHtml.append("</i>");
        expectedPortletHtml.append("</li>");
        expectedPortletHtml.append("</ul>");
        return expectedPortletHtml;
    }

    @Test
    public void renderMostRecentDocumentsPortlet() throws IOException {
        DMSUser john = new DMSUser("john");
        DMSUser mary = new DMSUser("mary");

        Document document1 = new Document(new DocumentId("d1"), "Lorem Ipsum");
        Document document2 = new Document(new DocumentId("d2"), "Dolor sit amet");

        Date startDate = date(getTimeZone("UTC")).day(1976, Calendar.MARCH, 22).asDate();

        UserLoginUserEvent johnLoggedIn = new UserLoginUserEvent(john, startDate);
        DocumentEditedUserEvent document1EditedByJohn = new DocumentEditedUserEvent(john, addHours(startDate, 1), document1.getDocumentId());
        DocumentEditedUserEvent document2EditedByMary = new DocumentEditedUserEvent(mary, addHours(startDate, 2), document2.getDocumentId());
        DocumentDownloadedUserEvent document2DownloadedByJohn = new DocumentDownloadedUserEvent(john, addHours(startDate, 3), document2.getDocumentId());

        Stream<UserEvent> userEvents = Stream.of(johnLoggedIn, document1EditedByJohn, document2EditedByMary, document2DownloadedByJohn, johnLoggedIn);
        when(userAnalytics.listUserEventsByDateDesc()).thenReturn(userEvents.sorted(Comparator.comparing(UserEvent::getDate).reversed()));
        when(documentsStore.fetchDocument(document1.getDocumentId())).thenReturn(Optional.of(document1));
        when(documentsStore.fetchDocument(document2.getDocumentId())).thenReturn(Optional.of(document2));

        String expectedPortletHtml = htmlOfDocumentsPortlet("john", "Dolor sit amet", "Lorem Ipsum").toString();

        try (StringWriter portletHtmlStringWriter = new StringWriter()) {
            portalHome.renderMostRecentDocumentsPortlet(portletHtmlStringWriter, john);
            assertEquals(expectedPortletHtml, portletHtmlStringWriter.toString());
        }
    }
}
