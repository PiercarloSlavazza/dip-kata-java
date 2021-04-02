package com.java_tutorials.dip_kata_java;

import com.java_tutorials.dip_kata_java.user_analytics.documents.Document;

import java.util.List;

public interface RecentDocuments {

    List<Document> fetchDocumentsAccessedByUserByDateDesc(DMSUser dmsUser, int maxDocuments);

}
