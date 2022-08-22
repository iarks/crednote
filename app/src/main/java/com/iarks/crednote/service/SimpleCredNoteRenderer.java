package com.iarks.crednote.service;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.iarks.crednote.abstractions.PdfInvoiceRenderer;
import com.iarks.crednote.models.CredNote;
import com.iarks.crednote.models.InvoiceDetail;
import com.iarks.crednote.models.Organisation;

import java.util.HashMap;

public class SimpleCredNoteRenderer implements PdfInvoiceRenderer {

    private final int documentHeight;
    private final int documentWidth;
    private final int spaceFromTop;
    private final int spaceFromBottom;
    private final int spaceFromLeft;
    private final int spaceFromRight;

    private final int documentEffectiveHeightRatio;

    private float topSectionRatioLine;
    private float centreLine;

    private float effectiveHeight;
    private float effectiveWidth;

    private float bottomSectionRatio;

    private Rect mainCell;

    int topContainerEnd;
    int bottomContainerStart;
    int startOfGoods;

    int companyPadding = 10;

    private HashMap<String, Rect> sections = new HashMap<>();

    private String[] section = new String[] {
            "Invoice No: ",
            "Dated: ",
            "Delivery Note: ",
            "Mode Of Payment: ",
            "Supplier's Ref: ",
            "Other Ref: ",
            "Buyer's Order No.: ",
            "Dated: ",
            "Dispatch Document Number: ",
            "Delivery Note Date: ",
            "Dispatch Through: ",
            "Destination: "
    };

    private final Paint marginPainter;

    public SimpleCredNoteRenderer()
    {
        documentHeight = (int)11.70 * 72;
        documentWidth =  (int)8.27 * 72;

        spaceFromTop = 50;
        spaceFromBottom = 20;
        spaceFromLeft = 20;
        spaceFromRight = 20;

        documentEffectiveHeightRatio = 1/10;

        marginPainter = new Paint();
        marginPainter.setColor(Color.BLACK);
        marginPainter.setColor(Color.BLACK);
        marginPainter.setStrokeWidth(1);
        marginPainter.setStyle(Paint.Style.STROKE);

        topSectionRatioLine = effectiveHeight/4;

        centreLine = documentWidth /2;

        bottomSectionRatio = effectiveHeight/10;

        mainCell = new Rect(spaceFromLeft, spaceFromTop, documentWidth -spaceFromRight, documentHeight -spaceFromBottom);
        effectiveHeight = Math.abs(mainCell.bottom  - mainCell.top);
        effectiveWidth = Math.abs(mainCell.top - mainCell.bottom);
    }



    @Override
    public PdfDocument generateCredNote(CredNote credNote) {
        PdfDocument result = new PdfDocument();
        Paint titlePaint = new Paint();
        PdfDocument.PageInfo pageMetadata = new PdfDocument.PageInfo.Builder(documentWidth, documentHeight, 1).create();
        PdfDocument.Page invoicePage = result.startPage(pageMetadata);
        Canvas cv = invoicePage.getCanvas();

        drawTopContainer(cv, credNote);
        drawBottomContainer(cv);
        drawMiddleSection(cv, 10);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(20);
        titlePaint.setColor(Color.RED);
        cv.drawText("Credit Note", (int) documentWidth /2, 35, titlePaint);

        result.finishPage(invoicePage);
        return result;
    }

    private void drawMiddleSection(Canvas cv, int numberOfGoods) {

        Paint marginPaint = new Paint();
        marginPaint.setColor(Color.BLACK);
        marginPaint.setStrokeWidth(1);
        marginPaint.setStyle(Paint.Style.STROKE);

        int sectionTop = topContainerEnd;
        int sectionBottom = bottomContainerStart - (bottomContainerStart-topContainerEnd)/7;

        int divideSection3 = ((mainCell.right - mainCell.left)/3)*2;

        int eachSectionWidth = (mainCell.right - divideSection3)/5;

        int divideSection1 = ((mainCell.right - mainCell.left)/12);


        Rect[] header = new Rect[]{
                new Rect(mainCell.left, sectionTop, divideSection1, sectionTop+30),
                new Rect(divideSection1, sectionTop, divideSection3, sectionTop+30),
                new Rect(divideSection3, sectionTop, divideSection3 + eachSectionWidth*1, sectionTop+30),
                new Rect(divideSection3 + eachSectionWidth*1, sectionTop, divideSection3 + eachSectionWidth*2, sectionTop+30),
                new Rect(divideSection3 + eachSectionWidth*2, sectionTop, divideSection3 + eachSectionWidth*3, sectionTop+30),
                new Rect(divideSection3 + eachSectionWidth*3, sectionTop, divideSection3 + eachSectionWidth*4, sectionTop+30),
                new Rect(divideSection3 + eachSectionWidth*4, sectionTop, mainCell.right, sectionTop+30)
        };

        for (Rect headerSection:header)
        {
            cv.drawRect(headerSection, marginPaint);
        }

        int offset = sectionTop+40;

        for(int i=0;i<=numberOfGoods;i++)
        {
            for (Rect headerSection:header)
            {
                headerSection.top = offset;
                headerSection.bottom = headerSection.top+30;
                cv.drawRect(headerSection, marginPaint);
            }
            offset+=30;
        }
    }

    private void drawTopContainer(Canvas cv, CredNote credNote) {

        Rect companyRect = new Rect(spaceFromLeft, spaceFromTop, (int) centreLine, (int) (topSectionRatioLine/2)+spaceFromTop);
        Rect partyRect = new Rect(spaceFromLeft, companyRect.bottom, (int) centreLine, (int) (topSectionRatioLine)+spaceFromTop);
        cv.drawRect(companyRect, marginPainter);
        cv.drawRect(partyRect, marginPainter);

        TextPaint textMaint = new TextPaint();
        textMaint.setColor(Color.BLACK);
        textMaint.setTextSize(10);
        textMaint.setTextAlign(Paint.Align.LEFT);

        StaticLayout companyDetails = getPartyDetailsLayout(credNote.organisation, companyRect.right - companyRect.left);
        StaticLayout partyDetails = getPartyDetailsLayout(credNote.party, partyRect.right - partyRect.left);

        cv.save();
        cv.translate(companyRect.left+companyPadding, companyRect.top+companyPadding);
        companyDetails.draw(cv);
        cv.restore();
        cv.save();
        cv.translate(partyRect.left+companyPadding, partyRect.top+companyPadding);
        partyDetails.draw(cv);
        cv.restore();

        int half2CentralLine = (documentWidth /4)*3;
        int heightOfEachSection = (partyRect.bottom - spaceFromTop)/7;
        int prevOffset = spaceFromTop;
        int centreOffSet = 0;
        for(int i=0;i<section.length;i++)
        {
            Rect rect = new Rect();
            rect.top = prevOffset;
            rect.bottom = prevOffset + heightOfEachSection;
            if((i+1)%2==1)
            {
                rect.left = (int)centreLine;
                rect.right = half2CentralLine;
            }
            else
            {
                rect.left = half2CentralLine;
                rect.right = half2CentralLine+(documentWidth /4)-spaceFromRight;
                prevOffset = rect.bottom;
            }
            cv.drawRect(rect, marginPainter);
            cv.save();
            cv.translate(rect.left+5, rect.top);
            drawInfo(section[i], credNote.invoiceDetails, i, rect.right-rect.left).draw(cv);
            cv.restore();
        }

        Rect transportRect = new Rect((int)centreLine, prevOffset,  half2CentralLine+(documentWidth /4)-spaceFromRight,  partyRect.bottom);
        cv.drawRect(transportRect, marginPainter);
        cv.save();
        cv.translate(transportRect.left+5, transportRect.top);
        getTransportDetails(credNote.invoiceDetails, transportRect.right-transportRect.left).draw(cv);
        cv.restore();

        topContainerEnd = partyRect.bottom;
    }

    private StaticLayout getTransportDetails(InvoiceDetail invoiceDetails, int maxContainerWidth) {
        TextPaint companyPainer = new TextPaint();
        companyPainer.setTextAlign(Paint.Align.LEFT);
        companyPainer.setTextSize(9);
        companyPainer.setColor(Color.BLACK);
        String textToRender = "Terms Of Delivery: "+ invoiceDetails.getTermsOfDelivery();
        StaticLayout result = StaticLayout.Builder.obtain(textToRender, 0, textToRender.length(), companyPainer, maxContainerWidth).build();
        return result;
    }

    private StaticLayout drawInfo(String info, InvoiceDetail invoiceDetails, int id, int maxContainerWidth) {
        TextPaint companyPainer = new TextPaint();
        companyPainer.setTextAlign(Paint.Align.LEFT);
        companyPainer.setTextSize(9);
        companyPainer.setColor(Color.BLACK);
        String detail = info+System.lineSeparator()+invoiceDetails.getById(id);
        StaticLayout result = StaticLayout.Builder.obtain(detail, 0, detail.length(),companyPainer, maxContainerWidth).build();
        return result;
    }

    private StaticLayout getPartyDetailsLayout(Organisation organisation, int maxContainerWidth) {
        TextPaint companyPainer = new TextPaint();
        companyPainer.setTextAlign(Paint.Align.LEFT);
        companyPainer.setTextSize(9);
        companyPainer.setColor(Color.BLACK);

        StringBuilder sb = new StringBuilder(organisation.getName());
        sb.append(System.lineSeparator())
                .append(organisation.getAddress())
                .append(System.lineSeparator())
                .append("GSTIN:"+ organisation.getGSTIn())
                .append(System.lineSeparator())
                .append("State:"+ organisation.getState()).append(" ").append("State Code:"+ organisation.getStateCode());


        StaticLayout staticLayout = StaticLayout.Builder
                .obtain(sb.toString(), 0, sb.length(), companyPainer, maxContainerWidth)
                .setIncludePad(true)
                .build();

        return staticLayout;
    }

    private void drawBottomContainer(Canvas cv)
    {
        Paint marginPaint = new Paint();
        marginPaint.setColor(Color.BLACK);
        marginPaint.setStrokeWidth(1);
        marginPaint.setStyle(Paint.Style.STROKE);

        Rect bottomSection = new Rect();
        bottomSection.left = spaceFromLeft;
        bottomSection.bottom = mainCell.bottom;
        bottomSection.right=(int)centreLine;
        bottomSection.top = (int)(bottomSectionRatio*9)+spaceFromTop;

        cv.drawRect(bottomSection, marginPaint);

        Rect stampSection = new Rect();
        stampSection.left = (int)centreLine;
        stampSection.bottom = mainCell.bottom;
        stampSection.right=mainCell.right;
        stampSection.top = (int)(bottomSectionRatio*9)+spaceFromTop;

        cv.drawRect(stampSection, marginPaint);

        bottomContainerStart = bottomSection.top;
    }

}
