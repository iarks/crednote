package com.iarks.crednote.abstractions;

import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;

import com.iarks.crednote.models.CredNote;

public interface PdfInvoiceRenderer {
    PdfDocument generateCredNote(CredNote credNote);
}
