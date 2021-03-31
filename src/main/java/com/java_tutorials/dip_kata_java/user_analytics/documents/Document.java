package com.java_tutorials.dip_kata_java.user_analytics.documents;

import com.java_tutorials.dip_kata_java.user_analytics.DocumentId;

import java.util.Objects;

public class Document {

    private final DocumentId documentId;
    private final String title;

    public Document(DocumentId documentId, String title) {
	this.documentId = documentId;
	this.title = title;
    }

    public DocumentId getDocumentId() {
	return documentId;
    }

    public String getTitle() {
	return title;
    }

    @Override public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || getClass() != o.getClass())
	    return false;
	Document document = (Document) o;
	return Objects.equals(documentId, document.documentId) && Objects.equals(title, document.title);
    }

    @Override public int hashCode() {
	return Objects.hash(documentId, title);
    }

    @Override public String toString() {
	return "Document{" +
			"documentId=" + documentId +
			", title='" + title + '\'' +
			'}';
    }
}
