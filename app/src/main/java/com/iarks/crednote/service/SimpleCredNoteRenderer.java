package com.iarks.crednote.service;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Pair;

import com.iarks.crednote.abstractions.PdfInvoiceRenderer;
import com.iarks.crednote.models.CredNote;
import com.iarks.crednote.models.Good;
import com.iarks.crednote.models.HsnDetails;
import com.iarks.crednote.models.InvoiceDetail;
import com.iarks.crednote.models.Organisation;

import java.util.Map;


public class SimpleCredNoteRenderer implements PdfInvoiceRenderer {

    private class CellRenderInformation
    {
        public int left;
        public int right;
        public String renderText;

        public CellRenderInformation(int left, int right, String renderText)
        {
            this.left = left;
            this.right = right;
            this.renderText = renderText;
        }
    }

    private final int documentHeight;
    private final int documentWidth;
    private final int spaceFromTop;
    private final int spaceFromBottom;
    private final int spaceFromLeft;
    private final int spaceFromRight;

    private final int documentDivision;
    private final int documentRatio;

    private final float topSectionRatioedHeight;
    private final float centreLine;

    private final float effectiveHeight;
    private final float effectiveWidth;

    private final Rect mainContainer;

    int topContainerBottom;
    int bottomContainerTop;

    private final int companyPadding;

    private static String[] invoiceFields = new String[] {
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

    private final CellRenderInformation[][] goodsSectionHeaderAndFooterLayout;
    private final CellRenderInformation[] taxSectionHeaderInformation;
    private final CellRenderInformation[] taxSectionHeaderSubSection;

    public SimpleCredNoteRenderer()
    {
        documentHeight = (int)(11.70 * 72);
        documentWidth =  (int)(8.27 * 72);

        spaceFromTop = 50;
        spaceFromBottom = 20;
        spaceFromLeft = 20;
        spaceFromRight = 20;

        companyPadding = 10;

        mainContainer = new Rect(spaceFromLeft, spaceFromTop, documentWidth -spaceFromRight, documentHeight -spaceFromBottom);
        effectiveHeight = Math.abs(mainContainer.bottom  - mainContainer.top);
        effectiveWidth = Math.abs(mainContainer.top - mainContainer.bottom);

        documentDivision = 24;
        documentRatio = (int)effectiveHeight/documentDivision;

        marginPainter = new Paint();
        marginPainter.setColor(Color.BLACK);
        marginPainter.setStrokeWidth(0.6f);
        marginPainter.setStyle(Paint.Style.STROKE);

        topSectionRatioedHeight = documentRatio * 5;

        centreLine = documentWidth /2;

        float goodsSectionRatioedHeight = mainContainer.width()/20;

        taxSectionHeaderInformation = new CellRenderInformation[]{
                new CellRenderInformation(mainContainer.left, (int)(goodsSectionRatioedHeight*4), "HSN/ SAC"),
                new CellRenderInformation(mainContainer.left, (int)(goodsSectionRatioedHeight*4), "Taxable Amount"),
                new CellRenderInformation(mainContainer.left, (int)(goodsSectionRatioedHeight*4.5), "Central Tax"),
                new CellRenderInformation(mainContainer.left, (int)(goodsSectionRatioedHeight*4.5), "State Tax"),
                new CellRenderInformation(mainContainer.left, (int)(goodsSectionRatioedHeight*4), "Total Tax Amount"),
        };

        taxSectionHeaderSubSection = new CellRenderInformation[]{
                new CellRenderInformation(mainContainer.left, (int)(goodsSectionRatioedHeight*4), ""),
                new CellRenderInformation(mainContainer.left, (int)(goodsSectionRatioedHeight*4), ""),
                new CellRenderInformation(mainContainer.left, (int)(goodsSectionRatioedHeight*2.25), "Rate"),
                new CellRenderInformation(mainContainer.left, (int)(goodsSectionRatioedHeight*2.26), "Amount"),
                new CellRenderInformation(mainContainer.left, (int)(goodsSectionRatioedHeight*2.25), "Rate"),
                new CellRenderInformation(mainContainer.left, (int)(goodsSectionRatioedHeight*2.26), "Amount"),
                new CellRenderInformation(mainContainer.left, (int)(goodsSectionRatioedHeight*4), ""),
        };

        goodsSectionHeaderAndFooterLayout = new CellRenderInformation[][]{
                {
                        new CellRenderInformation(mainContainer.left, (int) (goodsSectionRatioedHeight * 1), "Sl.No"),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 10.2), "Description of Goods"),

                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), "hsn"),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), "Quantity"),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.7), "Rate"),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), "per"),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), "Disc%"),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 10), "Amount")
                },
                {
                        new CellRenderInformation(mainContainer.left, (int) (goodsSectionRatioedHeight * 1), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 10.2), "CGST"),

                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.7), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 10), "")
                },
                {
                        new CellRenderInformation(mainContainer.left, (int) (goodsSectionRatioedHeight * 1), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 10.2), "SGST"),

                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.7), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 10), "")
                },
                {
                        new CellRenderInformation(mainContainer.left, (int) (goodsSectionRatioedHeight * 1), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 10.2), "Round off"),

                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.7), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 10), "")
                },
                {
                        new CellRenderInformation(mainContainer.left, (int) (goodsSectionRatioedHeight * 1), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 10.2), "Total"),

                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.7), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 1.5), ""),
                        new CellRenderInformation(-1, (int) (goodsSectionRatioedHeight * 10), "")
                }
        };
    }



    @Override
    public PdfDocument generateCredNote(CredNote credNote) {
        PdfDocument result = new PdfDocument();
        Paint titlePaint = new Paint();
        PdfDocument.PageInfo pageMetadata = new PdfDocument.PageInfo.Builder(documentWidth, documentHeight, 1).create();
        PdfDocument.Page invoicePage = result.startPage(pageMetadata);
        Canvas cv = invoicePage.getCanvas();

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(20);
        titlePaint.setColor(Color.BLACK);
        cv.drawText("Credit Note", (int) documentWidth /2, 35, titlePaint);

        drawTopContainer(cv, credNote);
        drawBottomContainer(cv, credNote);
        drawMiddleContainer(cv, credNote);
        result.finishPage(invoicePage);
        return result;
    }

    private void drawMiddleContainer(Canvas cv, CredNote credNote) {
        int sectionTop = topContainerBottom;
        int rowHeight = (int)(documentRatio*0.7);

        TextPaint companyPainer = new TextPaint();
        companyPainer.setTextAlign(Paint.Align.CENTER);
        companyPainer.setTextSize(9);
        companyPainer.setColor(Color.BLACK);

        int prev = mainContainer.left;
        Rect rect = new Rect(mainContainer.left, sectionTop, -1, sectionTop+rowHeight);
        for (CellRenderInformation headerSection: goodsSectionHeaderAndFooterLayout[0])
        {
            rect.left = prev;
            rect.right = ((prev + headerSection.right) > mainContainer.right) ? mainContainer.right : prev + headerSection.right;
            cv.drawRect(rect, marginPainter);
            StaticLayout sl = StaticLayout.Builder.obtain(headerSection.renderText, 0, headerSection.renderText.length(), companyPainer, rect.width()).build();
            cv.save();
            cv.translate(rect.centerX(), rect.top+5);
            sl.draw(cv);
            cv.restore();
            prev = rect.right;
        }

        prev = mainContainer.left;
        rect.top = rect.bottom;
        rect.bottom = rect.top+rowHeight;
        companyPainer.setTextAlign(Paint.Align.LEFT);
        for(int i=0;i<credNote.getNumberOfGoods();i++)
        {
            Good good = credNote.getGood(i);
            for(int j = 0; j< goodsSectionHeaderAndFooterLayout[0].length; j++)
            {
                rect.left = prev;
                rect.right = ((prev + goodsSectionHeaderAndFooterLayout[0][j].right) > mainContainer.right) ? mainContainer.right : prev + goodsSectionHeaderAndFooterLayout[0][j].right;
                //cv.drawRect(rect, marginPainter);
                StaticLayout sl;
                if(j== goodsSectionHeaderAndFooterLayout[0].length-1)
                    sl = StaticLayout.Builder.obtain(String.valueOf(good.getAmount()), 0, String.valueOf(good.getAmount()).length(), companyPainer, rect.width()).build();
                else
                    sl = StaticLayout.Builder.obtain(good.getDataAt(j), 0, good.getDataAt(j).length(), companyPainer, rect.width()).build();
                cv.save();
                cv.drawLine(rect.left, rect.top, rect.left, rect.top+rowHeight, marginPainter);
                cv.translate(rect.left+5, rect.top);
                sl.draw(cv);
                cv.restore();
                prev = rect.right;
            }
            cv.drawLine(rect.right, rect.top, rect.right, rect.top+rowHeight, marginPainter);
            rect.top = rect.top+rowHeight;
            rect.bottom = rect.top+rowHeight;
            prev= mainContainer.left;
        }
        cv.drawLine(mainContainer.left, rect.top, mainContainer.right, rect.top, marginPainter);

        TextPaint rightPainter = new TextPaint();
        rightPainter.setTextAlign(Paint.Align.RIGHT);
        rightPainter.setTextSize(9);
        rightPainter.setColor(Color.BLACK);

        companyPainer.setTextAlign(Paint.Align.LEFT);

        renderCgst(cv, credNote, rowHeight, companyPainer, rect, rightPainter);

        renderSgst(cv, credNote, rowHeight, companyPainer, rect, rightPainter);

        renderRoundoff(cv, credNote, rowHeight, companyPainer, rect, rightPainter);

        String inWords = renderTotal(cv, credNote, rowHeight, companyPainer, rect, rightPainter);

        RenderInWords(cv, rowHeight, companyPainer, rect, inWords);

        taxHeaderInformation(cv, rowHeight, rect, rightPainter);

        prev = mainContainer.left;
        rect.top = rect.bottom;
        rect.bottom = rect.top+rowHeight;
        for(int i = 0; i< taxSectionHeaderSubSection.length; i++)
        {
            rect.left = prev;
            rect.right = ((prev + taxSectionHeaderSubSection[i].right) > mainContainer.right) ? mainContainer.right : prev + taxSectionHeaderSubSection[i].right;
            cv.drawRect(rect, marginPainter);
            StaticLayout sl;
            sl = StaticLayout.Builder.obtain(taxSectionHeaderSubSection[i].renderText, 0, taxSectionHeaderSubSection[i].renderText.length(), rightPainter, rect.width()).build();
            cv.save();
            cv.translate(rect.right-5, rect.top+5);
            sl.draw(cv);
            cv.restore();
            prev = rect.right;
        }


        for(Map.Entry<String,HsnDetails> mapElement: credNote.uniqueHsnAmounts.entrySet())
        {
            prev = mainContainer.left;
            rect.top = rect.bottom;
            rect.bottom = rect.top+rowHeight;
            String key = mapElement.getKey();
            HsnDetails value = mapElement.getValue();
            for(int i = 0; i< taxSectionHeaderSubSection.length; i++)
            {
                rect.left = prev;
                rect.right = ((prev + taxSectionHeaderSubSection[i].right) > mainContainer.right) ? mainContainer.right : prev + taxSectionHeaderSubSection[i].right;
                cv.drawRect(rect, marginPainter);
                StaticLayout sl=null;
                System.out.println(i);
                switch (i)
                {
                    case 0:
                        sl = StaticLayout.Builder.obtain(key, 0, key.length(), rightPainter, rect.width()).build();
                        break;
                    case 1:
                        sl = StaticLayout.Builder.obtain(String.valueOf(value.getTaxableAmount()), 0, String.valueOf(value.getTaxableAmount()).length(), rightPainter, rect.width()).build();
                        break;
                    case 2:
                        sl = StaticLayout.Builder.obtain(String.valueOf(value.getCentralTaxRate()), 0, String.valueOf(value.getCentralTaxRate()).length(), rightPainter, rect.width()).build();
                        break;
                    case 3:
                        sl = StaticLayout.Builder.obtain(String.valueOf(value.getCentralTaxAmount()), 0, String.valueOf(value.getCentralTaxAmount()).length(), rightPainter, rect.width()).build();
                        break;
                    case 4:
                        sl = StaticLayout.Builder.obtain(String.valueOf(value.getStateTaxRate()), 0, String.valueOf(value.getStateTaxRate()).length(), rightPainter, rect.width()).build();
                        break;
                    case 5:
                        sl = StaticLayout.Builder.obtain(String.valueOf(value.getStateTaxAmount()), 0, String.valueOf(value.getStateTaxAmount()).length(), rightPainter, rect.width()).build();
                        break;
                    case 6:
                        sl = StaticLayout.Builder.obtain(String.valueOf(value.getTotalTaxAmount()), 0, String.valueOf(value.getTotalTaxAmount()).length(), rightPainter, rect.width()).build();
                        break;
                }
                cv.save();
                cv.translate(rect.right-5, rect.top+5);
                sl.draw(cv);
                cv.restore();
                prev = rect.right;
            }
        }

        prev = mainContainer.left;
        rect.top = rect.bottom;
        rect.bottom = rect.top+rowHeight;
        for(int i = 0; i< taxSectionHeaderSubSection.length; i++)
        {
            rect.left = prev;
            rect.right = ((prev + taxSectionHeaderSubSection[i].right) > mainContainer.right) ? mainContainer.right : prev + taxSectionHeaderSubSection[i].right;
            cv.drawRect(rect, marginPainter);
            StaticLayout sl=null;
            System.out.println(i);
            switch (i)
            {
                case 0:
                    sl = StaticLayout.Builder.obtain("Total", 0, "Total".length(), rightPainter, rect.width()).build();
                    break;
                case 1:
                    sl = StaticLayout.Builder.obtain(String.valueOf(credNote.getTotal()), 0, String.valueOf(credNote.getTotal()).length(), rightPainter, rect.width()).build();
                    break;
                case 2:
                case 4:
                    sl = StaticLayout.Builder.obtain("", 0, 0, rightPainter, rect.width()).build();
                    break;
                case 3:
                    sl = StaticLayout.Builder.obtain(String.valueOf(credNote.getTotalCentralTaxValue()), 0, String.valueOf(credNote.getTotalCentralTaxValue()).length(), rightPainter, rect.width()).build();
                    break;
                case 5:
                    sl = StaticLayout.Builder.obtain(String.valueOf(credNote.getTotalStateTaxValue()), 0, String.valueOf(credNote.getTotalStateTaxValue()).length(), rightPainter, rect.width()).build();
                    break;
                case 6:
                    sl = StaticLayout.Builder.obtain(String.valueOf(credNote.getTotalTaxValue()), 0, String.valueOf(credNote.getTotalTaxValue()).length(), rightPainter, rect.width()).build();
                    break;
            }
            cv.save();
            cv.translate(rect.right-5, rect.top+5);
            sl.draw(cv);
            cv.restore();
            prev = rect.right;
        }

    }

    private void taxHeaderInformation(Canvas cv, int rowHeight, Rect rect, TextPaint rightPainter) {
        int prev;
        prev = mainContainer.left;
        rect.top = rect.bottom;
        rect.bottom = rect.top + rowHeight;
        for (int i = 0; i < taxSectionHeaderInformation.length; i++) {
            rect.left = prev;
            rect.right = ((prev + taxSectionHeaderInformation[i].right) > mainContainer.right) ? mainContainer.right : prev + taxSectionHeaderInformation[i].right;
            cv.drawRect(rect, marginPainter);
            StaticLayout sl;
            sl = StaticLayout.Builder.obtain(taxSectionHeaderInformation[i].renderText, 0, taxSectionHeaderInformation[i].renderText.length(), rightPainter, rect.width()).build();
            cv.save();
            cv.translate(rect.right - 5, rect.top + 5);
            sl.draw(cv);
            cv.restore();
            prev = rect.right;
        }
    }

    private void renderCgst(Canvas cv, CredNote credNote, int rowHeight, TextPaint companyPainer, Rect rect, TextPaint rightPainter) {
        int prev;
        prev = mainContainer.left;
        rect.top = rect.top;
        rect.bottom = rect.top+rowHeight;
        for(int i = 0; i< goodsSectionHeaderAndFooterLayout[1].length; i++)
        {
            rect.left = prev;
            rect.right = ((prev + goodsSectionHeaderAndFooterLayout[1][i].right) > mainContainer.right) ? mainContainer.right : prev + goodsSectionHeaderAndFooterLayout[1][i].right;
            cv.drawRect(rect, marginPainter);
            StaticLayout sl;
            if(i== goodsSectionHeaderAndFooterLayout[1].length-1) {
                sl = StaticLayout.Builder.obtain(String.valueOf(credNote.getCgst()), 0, String.valueOf(credNote.getCgst()).length(), companyPainer, rect.width()).build();
            }
            else
            {
                sl = StaticLayout.Builder.obtain(goodsSectionHeaderAndFooterLayout[1][i].renderText, 0, goodsSectionHeaderAndFooterLayout[1][i].renderText.length(), rightPainter, rect.width()).build();
            }
            cv.save();
            cv.translate(rect.right-5, rect.top+5);
            sl.draw(cv);
            cv.restore();
            prev = rect.right;
        }
    }

    private void renderSgst(Canvas cv, CredNote credNote, int rowHeight, TextPaint companyPainer, Rect rect, TextPaint rightPainter) {
        int prev;
        prev = mainContainer.left;
        rect.top = rect.bottom;
        rect.bottom = rect.top+rowHeight;
        for(int i = 0; i< goodsSectionHeaderAndFooterLayout[2].length; i++)
        {
            rect.left = prev;
            rect.right = ((prev + goodsSectionHeaderAndFooterLayout[2][i].right) > mainContainer.right) ? mainContainer.right : prev + goodsSectionHeaderAndFooterLayout[2][i].right;
            cv.drawRect(rect, marginPainter);
            StaticLayout sl;
            if(i== goodsSectionHeaderAndFooterLayout[2].length-1) {
                sl = StaticLayout.Builder.obtain(String.valueOf(credNote.getSgst()), 0, String.valueOf(credNote.getCgst()).length(), companyPainer, rect.width()).build();
            }
            else
            {
                sl = StaticLayout.Builder.obtain(goodsSectionHeaderAndFooterLayout[2][i].renderText, 0, goodsSectionHeaderAndFooterLayout[2][i].renderText.length(), rightPainter, rect.width()).build();
            }
            cv.save();
            cv.translate(rect.right-5, rect.top+5);
            sl.draw(cv);
            cv.restore();
            prev = rect.right;
        }
    }

    private void renderRoundoff(Canvas cv, CredNote credNote, int rowHeight, TextPaint companyPainer, Rect rect, TextPaint rightPainter) {
        int prev;
        prev = mainContainer.left;
        rect.top = rect.bottom;
        rect.bottom = rect.top+rowHeight;
        for(int i = 0; i< goodsSectionHeaderAndFooterLayout[3].length; i++)
        {
            rect.left = prev;
            rect.right = ((prev + goodsSectionHeaderAndFooterLayout[3][i].right) > mainContainer.right) ? mainContainer.right : prev + goodsSectionHeaderAndFooterLayout[3][i].right;
            cv.drawRect(rect, marginPainter);
            StaticLayout sl;
            if(i== goodsSectionHeaderAndFooterLayout[3].length-1) {
                sl = StaticLayout.Builder.obtain(String.valueOf(credNote.getRoundOff()), 0, String.valueOf(credNote.getRoundOff()).length(), companyPainer, rect.width()).build();
            }
            else
            {
                sl = StaticLayout.Builder.obtain(goodsSectionHeaderAndFooterLayout[3][i].renderText, 0, goodsSectionHeaderAndFooterLayout[3][i].renderText.length(), rightPainter, rect.width()).build();
            }
            cv.save();
            cv.translate(rect.right-5, rect.top+5);
            sl.draw(cv);
            cv.restore();
            prev = rect.right;
        }
    }

    private String renderTotal(Canvas cv, CredNote credNote, int rowHeight, TextPaint companyPainer, Rect rect, TextPaint rightPainter) {
        int prev;
        prev = mainContainer.left;
        rect.top = rect.bottom;
        rect.bottom = rect.top+ rowHeight;
        String inWords="";
        for(int i = 0; i< goodsSectionHeaderAndFooterLayout[4].length; i++)
        {
            rect.left = prev;
            rect.right = ((prev + goodsSectionHeaderAndFooterLayout[4][i].right) > mainContainer.right) ? mainContainer.right : prev + goodsSectionHeaderAndFooterLayout[4][i].right;
            cv.drawRect(rect, marginPainter);
            StaticLayout sl;
            if(i== goodsSectionHeaderAndFooterLayout[4].length-1) {
                Pair<Float, String> total = credNote.getTotal();
                inWords = total.second;
                sl = StaticLayout.Builder.obtain(String.valueOf(total.first), 0, String.valueOf(total.first).length(), companyPainer, rect.width()).build();
            }
            else
            {
                sl = StaticLayout.Builder.obtain(goodsSectionHeaderAndFooterLayout[4][i].renderText, 0, goodsSectionHeaderAndFooterLayout[4][i].renderText.length(), rightPainter, rect.width()).build();
            }
            cv.save();
            cv.translate(rect.right-5, rect.top+5);
            sl.draw(cv);
            cv.restore();
            prev = rect.right;
        }
        return inWords;
    }

    private void RenderInWords(Canvas cv, int rowHeight, TextPaint companyPainer, Rect rect, String inWords) {
        rect.top = rect.bottom;
        rect.bottom = (int)(rect.top+1.5* rowHeight);
        rect.left = mainContainer.left;
        rect.right = mainContainer.right;
        cv.drawRect(rect, marginPainter);
        String s = "Amount chargeable in words:";
        companyPainer.setTextAlign(Paint.Align.LEFT);
        StaticLayout sl = StaticLayout.Builder.obtain(s, 0, s.length(), companyPainer, rect.width()).build();
        cv.save();
        cv.translate(rect.left+5, rect.top+5);
        sl.draw(cv);
        cv.restore();
        companyPainer.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        StaticLayout s2 = StaticLayout.Builder.obtain(inWords, 0, inWords.length(), companyPainer, rect.width()).build();
        cv.save();
        cv.translate(rect.left+5, rect.top+sl.getHeight()+5);
        s2.draw(cv);
        cv.restore();
    }

    private void drawTopContainer(Canvas cv, CredNote credNote) {

        Rect companyRect = new Rect(spaceFromLeft, spaceFromTop, (int) centreLine, (int) (topSectionRatioedHeight /2)+spaceFromTop);
        Rect partyRect = new Rect(spaceFromLeft, companyRect.bottom, (int) centreLine, (int) (topSectionRatioedHeight)+spaceFromTop);
        cv.drawRect(companyRect, marginPainter);
        cv.drawRect(partyRect, marginPainter);

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
        for(int i = 0; i< invoiceFields.length; i++)
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
            drawInfo(invoiceFields[i], credNote.invoiceDetails, i, rect.right-rect.left).draw(cv);
            cv.restore();
        }

        Rect transportRect = new Rect((int)centreLine, prevOffset,  half2CentralLine+(documentWidth /4)-spaceFromRight,  partyRect.bottom);
        cv.drawRect(transportRect, marginPainter);
        cv.save();
        cv.translate(transportRect.left+5, transportRect.top);
        getTransportDetails(credNote.invoiceDetails, transportRect.right-transportRect.left).draw(cv);
        cv.restore();

        topContainerBottom = partyRect.bottom;
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

    private void drawBottomContainer(Canvas cv, CredNote credNote)
    {
        TextPaint companyPainer = new TextPaint();
        companyPainer.setTextAlign(Paint.Align.LEFT);
        companyPainer.setTextSize(9);
        companyPainer.setColor(Color.BLACK);

        int expectedHeight = 5;

        int sectionStart = documentRatio*expectedHeight + documentRatio*(documentDivision-expectedHeight);

        // region render left side
        Rect bottomSection = new Rect(mainContainer.left, sectionStart, (int)centreLine, Math.min(sectionStart + documentRatio*expectedHeight, mainContainer.bottom));
        cv.drawRect(bottomSection, marginPainter);
        // endregion

        // region render declaration
        String declarationStatement = "Declaration:\nWe declare that this invoice shows the actual price of the goods described and that all particulars are true and correct";
        StaticLayout sl = StaticLayout.Builder
                .obtain(declarationStatement, 0, declarationStatement.length(), companyPainer, bottomSection.width()-5)
                .build();

        cv.save();
        cv.translate(bottomSection.left+5, bottomSection.top+5);
        sl.draw(cv);
        cv.restore();
        // endregion

        // region render right rect
        bottomSection.left = (int)centreLine;
        bottomSection.right= mainContainer.right;
        cv.drawRect(bottomSection, marginPainter);
        // endregion

        // region render right side text
        String companyStatement = "For "+credNote.organisation.getName();
        companyPainer.setTextAlign(Paint.Align.CENTER);
        sl = StaticLayout.Builder
                .obtain(companyStatement, 0, companyStatement.length(), companyPainer, bottomSection.width())
                .build();
        cv.save();
        cv.translate(bottomSection.left + (bottomSection.right - bottomSection.left)/2, bottomSection.top+5);
        sl.draw(cv);
        cv.restore();

        String authorisedSignatory = "Authorised Signatory";
        sl = StaticLayout.Builder
            .obtain(authorisedSignatory, 0, authorisedSignatory.length(), companyPainer, bottomSection.width())
            .build();

        cv.save();
        cv.translate(bottomSection.left + (bottomSection.right - bottomSection.left)/2, bottomSection.bottom - sl.getHeight() - 5);
        sl.draw(cv);
        cv.restore();
        bottomContainerTop = bottomSection.top;
    }
}
