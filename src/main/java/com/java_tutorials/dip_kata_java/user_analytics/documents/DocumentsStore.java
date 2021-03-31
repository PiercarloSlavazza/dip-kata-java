package com.java_tutorials.dip_kata_java.user_analytics.documents;

import com.java_tutorials.dip_kata_java.user_analytics.DocumentId;

import java.util.Optional;

public interface DocumentsStore {

    Optional<Document> fetchDocument(DocumentId documentId);

}
