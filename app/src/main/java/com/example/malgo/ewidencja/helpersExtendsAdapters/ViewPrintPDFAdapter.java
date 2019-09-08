package com.example.malgo.ewidencja.helpersExtendsAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.view.View;

import java.io.FileOutputStream;
import java.io.IOException;

public class ViewPrintPDFAdapter extends PrintDocumentAdapter
{
    Context context;
    private int pageHeight;
    private int pageWidth;
    private View myView;
    private String receiptName;
    public PrintedPdfDocument myPdfDocument;
    public int totalpages = 1;

    public ViewPrintPDFAdapter(Context context, View view, String receipt_name)
    {
        this.context = context;
        myView = view;
        receiptName = receipt_name;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes,
                         PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback,
                         Bundle metadata) {

        myPdfDocument = new PrintedPdfDocument(context, newAttributes);

        pageHeight = newAttributes.getMediaSize().getHeightMils()/1000 * 72;
        pageWidth = newAttributes.getMediaSize().getWidthMils()/1000 * 72;

        if (cancellationSignal.isCanceled() ) {
            callback.onLayoutCancelled();
            return;
        }

        if (totalpages > 0) {
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder(receiptName + ".pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(totalpages);

            PrintDocumentInfo info = builder.build();
            callback.onLayoutFinished(info, true);
        } else {
            callback.onLayoutFailed("Page count is zero.");
        }
    }


    @Override
    public void onWrite(final PageRange[] pageRanges,
                        final ParcelFileDescriptor destination,
                        final CancellationSignal cancellationSignal,
                        final WriteResultCallback callback) {

        // Start the page
        PdfDocument.Page page = myPdfDocument.startPage(0);
        // Create a bitmap and put it a canvas for the view to draw to. Make it the size of the view
        Bitmap bitmap = Bitmap.createBitmap(myView.getWidth(), myView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        myView.draw(canvas);
        // create a Rect with the view's dimensions.
        Rect src = new Rect(0, 0, myView.getWidth(), myView.getHeight());
        // get the page canvas and measure it.
        Canvas pageCanvas = page.getCanvas();
        float pageWidth = pageCanvas.getWidth();
        float pageHeight = pageCanvas.getHeight();
        // how can we fit the Rect src onto this page while maintaining aspect ratio?
        float scale = Math.min(pageWidth/src.width(), pageHeight/src.height());
        float left = pageWidth / 2 - src.width() * scale / 2;
        float top = pageHeight / 2 - src.height() * scale / 2;
        float right = pageWidth / 2 + src.width() * scale / 2;
        float bottom = pageHeight / 2 + src.height() * scale / 2;
        RectF dst = new RectF(left, top, right, bottom);

        pageCanvas.drawBitmap(bitmap, src, dst, null);
        myPdfDocument.finishPage(page);

        try {
            myPdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            myPdfDocument.close();
            myPdfDocument = null;
        }
        callback.onWriteFinished(new PageRange[]{new PageRange(0, 0)});
    }
}
