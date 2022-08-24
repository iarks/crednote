package com.iarks.crednote.service;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Pair;

import com.iarks.crednote.R;
import com.iarks.crednote.abstractions.PdfInvoiceRenderer;
import com.iarks.crednote.models.CredNote;
import com.iarks.crednote.models.Good;
import com.iarks.crednote.models.GoodLine;
import com.iarks.crednote.models.GoodLineItem;
import com.iarks.crednote.models.HsnDetails;
import com.iarks.crednote.models.InvoiceDetail;
import com.iarks.crednote.models.Organisation;

import java.util.Map;


public class SimpleCredNoteRenderer implements PdfInvoiceRenderer {

    private class CellRenderInformation
    {
        public int right;
        public String renderText;

        public CellRenderInformation(int right, String renderText)
        {
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
    private final int generalPadding;
    private final float goodsHeightRatio;

    private final int documentHeightDivision;
    private final int documentRatio;

    private final float topSectionHeight;
    private final float centreLine;

    private final float effectiveHeight;
    private final float effectiveWidth;
    private final int textSize;
    private final float bottomContainerRatio;

    private final Rect mainContainer;

    int goodsSectionDivisions;

    private final int companyPadding;

    private static final Typeface defaultTypeFace = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
    private static final Typeface boldTypeFace = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

    private Context context;

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
    private final TextPaint textPainter;

    private final CellRenderInformation[] goodsSectionHeaders;
    private final CellRenderInformation[] taxSectionHeaders;
    private final CellRenderInformation[] taxSubSectionHeaders;
    private final boolean includeHsn;

    public SimpleCredNoteRenderer(Context context, boolean includeHsn)
    {
        this.includeHsn = includeHsn;
        this.context = context;
        this.documentHeight = (int)(11.70 * 72);
        this.documentWidth =  (int)(8.27 * 72);

        this.spaceFromTop = 50;
        this.spaceFromBottom = 20;
        this.spaceFromLeft = 20;
        this.spaceFromRight = 20;
        this.generalPadding = 5;
        this.companyPadding = 10;

        this.goodsHeightRatio = 0.65f;
        this.textSize = 9;

        this.mainContainer = new Rect(spaceFromLeft, spaceFromTop, documentWidth -spaceFromRight, documentHeight -spaceFromBottom);
        this.effectiveHeight = Math.abs(mainContainer.bottom  - mainContainer.top);
        this.effectiveWidth = Math.abs(mainContainer.right - mainContainer.left);

        this.documentHeightDivision = 24;
        this.documentRatio = (int)effectiveHeight/ documentHeightDivision;
        this.topSectionHeight = documentRatio * 6;
        this.goodsSectionDivisions = 20;
        float goodsSectionWidthRatio = mainContainer.width()/goodsSectionDivisions;

        marginPainter = new Paint();
        marginPainter.setColor(Color.BLACK);
        marginPainter.setStrokeWidth(0.7f);
        marginPainter.setStyle(Paint.Style.STROKE);

        textPainter = new TextPaint();
        marginPainter.setColor(Color.BLACK);
        textPainter.setTextSize(textSize);

        this.centreLine = documentWidth >> 1;

        this.taxSectionHeaders = new CellRenderInformation[] {
                new CellRenderInformation((int)(goodsSectionWidthRatio*4), context.getString(R.string.HSN_SAC)),
                new CellRenderInformation((int)(goodsSectionWidthRatio*4), context.getString(R.string.label_taxable_amount)),
                new CellRenderInformation((int)(goodsSectionWidthRatio*4.5), context.getString(R.string.central_tax)),
                new CellRenderInformation((int)(goodsSectionWidthRatio*4.5), context.getString(R.string.state_tax)),
                new CellRenderInformation((int)(goodsSectionWidthRatio*4), context.getString(R.string.label_total_tax_amount)),
        };

        this.taxSubSectionHeaders = new CellRenderInformation[] {
                new CellRenderInformation((int)(goodsSectionWidthRatio*4), ""),
                new CellRenderInformation((int)(goodsSectionWidthRatio*4), ""),
                new CellRenderInformation((int)(goodsSectionWidthRatio*2.25), context.getString(R.string.Rate)),
                new CellRenderInformation((int)(goodsSectionWidthRatio*2.26), context.getString(R.string.label_amount)),
                new CellRenderInformation((int)(goodsSectionWidthRatio*2.25), context.getString(R.string.Rate)),
                new CellRenderInformation((int)(goodsSectionWidthRatio*2.26), context.getString(R.string.label_amount)),
                new CellRenderInformation((int)(goodsSectionWidthRatio*4), ""),
        };

        this.goodsSectionHeaders = new CellRenderInformation[]{

                        new CellRenderInformation((int) (goodsSectionWidthRatio * 1), context.getString(R.string.serial_number)),
                        new CellRenderInformation((int) (goodsSectionWidthRatio * 10.2), context.getString(R.string.label_description_of_goods)),

                        new CellRenderInformation((int) (goodsSectionWidthRatio * 1.5), context.getString(R.string.label_hsn_short)),
                        new CellRenderInformation((int) (goodsSectionWidthRatio * 1.5), context.getString(R.string.label_quantity)),
                        new CellRenderInformation((int) (goodsSectionWidthRatio * 1.7), context.getString(R.string.Rate)),
                        new CellRenderInformation((int) (goodsSectionWidthRatio * 1.2), context.getString(R.string.unit)),
                        new CellRenderInformation((int) (goodsSectionWidthRatio * 1.5), context.getString(R.string.label_disc_short)),
                        new CellRenderInformation((int) mainContainer.right, context.getString(R.string.label_amount))

        };
        bottomContainerRatio = 2.5f;
    }


    @Override
    public PdfDocument generateCredNote(CredNote credNote) {
        PdfDocument result = new PdfDocument();
        PdfDocument.PageInfo pageMetadata = new PdfDocument.PageInfo.Builder(documentWidth, documentHeight, 1).create();
        PdfDocument.Page invoicePage = result.startPage(pageMetadata);
        Canvas cv = invoicePage.getCanvas();

        textPainter.setTextAlign(Paint.Align.CENTER);
        textPainter.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPainter.setTextSize(20);
        cv.drawText(context.getString(R.string.CREDIT_NOTE), (int) documentWidth /2, 35, textPainter);
        reset(textPainter);

        cv.drawRect(mainContainer, marginPainter);
        drawTopContainer(cv, credNote);
        drawMiddleContainer(cv, credNote);
        drawBottomContainer(cv, credNote);

        String text = context.getString(R.string.this_is_a_computer_generated_invoice);
        textPainter.setTextAlign(Paint.Align.CENTER);
        StaticLayout sl = StaticLayout.Builder.obtain(text, 0, text.length(), textPainter, documentWidth).build();
        cv.save();
        cv.translate(centreLine, mainContainer.bottom);
        sl.draw(cv);
        cv.restore();
        result.finishPage(invoicePage);

        return result;
    }

    void reset(TextPaint textPainter)
    {
        textPainter.setTextSize(this.textSize);
        textPainter.setTextAlign(Paint.Align.LEFT);
        textPainter.setTypeface(defaultTypeFace);
    }

    private void drawMiddleContainer(Canvas cv, CredNote credNote) {
        float sectionTop = topSectionHeight + spaceFromTop;
        float rowHeight = (documentRatio*goodsHeightRatio);

        float left;
        float right;
        textPainter.setTextAlign(Paint.Align.CENTER);
        float prev = mainContainer.left;
        textPainter.setTypeface(boldTypeFace);
        for (int i=0;i<goodsSectionHeaders.length;i++)
        {
            left = prev;
            right = ((left + goodsSectionHeaders[i].right) > mainContainer.right) ? mainContainer.right : left + goodsSectionHeaders[i].right;

            if(i!=goodsSectionHeaders.length-1)
                cv.drawLine(right, sectionTop, right, sectionTop+rowHeight, marginPainter);

            StaticLayout sl = StaticLayout.Builder.obtain(goodsSectionHeaders[i].renderText, 0, goodsSectionHeaders[i].renderText.length(), textPainter, (int)(right-left)).build();
            cv.save();
            cv.translate(left + (right - left)/2 , sectionTop+generalPadding);
            sl.draw(cv);
            cv.restore();
            prev = right;
        }

        reset(textPainter);
        cv.drawLine(mainContainer.left, sectionTop+rowHeight, mainContainer.right, sectionTop+rowHeight, marginPainter);
        float rowHeightDoc = 0.5f * documentRatio;

        prev = mainContainer.left;
        sectionTop = sectionTop + rowHeight;
        for(int i=0;i<credNote.getNumberOfGoods();i++)
        {
            Good good = credNote.getGood(i);
            for(int j = 0; j< goodsSectionHeaders.length; j++)
            {
                left = prev;
                right = ((prev + goodsSectionHeaders[j].right) > mainContainer.right) ? mainContainer.right : prev + goodsSectionHeaders[j].right;
                StaticLayout sl = getGoodsInfo(i, j, good, right-left);
                cv.save();
                if(j!=goodsSectionHeaders.length-1)
                    cv.drawLine(right, sectionTop, right, sectionTop+rowHeightDoc, marginPainter);
                cv.translate(left+generalPadding, sectionTop);
                //cv.translate(left+generalPadding, sectionTop);
                sl.draw(cv);
                cv.restore();
                prev = right;
            }

            sectionTop = sectionTop+rowHeightDoc;
            prev= mainContainer.left;
        }
        //marginPainter.setColor(Color.RED);
        cv.drawLine(mainContainer.left, sectionTop, mainContainer.right, sectionTop, marginPainter);
        //marginPainter.setColor(Color.BLACK);


        prev = mainContainer.left;
        for(int i=1;i<=5;i++)
        {
            GoodLineItem goodLineItem = getLineItemAt(i, credNote);
            for(int j = 0; j< goodsSectionHeaders.length; j++)
            {
                left = prev;
                right = ((prev + goodsSectionHeaders[j].right) > mainContainer.right) ? mainContainer.right : prev + goodsSectionHeaders[j].right;

                StaticLayout sl = getGoodsInfo(i, j, goodLineItem, right-left);
                if(j!=goodsSectionHeaders.length-1)
                    cv.drawLine(right, sectionTop, right, sectionTop+rowHeight, marginPainter);
                if(j==goodsSectionHeaders.length-1)
                {
                    reset(textPainter);
                }
                if(j==1)
                {
                    textPainter.setTextAlign(Paint.Align.RIGHT);
                    cv.drawText(goodLineItem.getPrintableDescription(), right - generalPadding, sectionTop+rowHeight-rowHeight/3, textPainter);
                    reset(textPainter);
                }
                else if (j==0)
                {
                    // do nothing
                }
                else
                {
                    cv.save();
                    cv.translate(left+generalPadding, sectionTop+generalPadding);
                    sl.draw(cv);
                    cv.restore();
                }
                prev = right;
            }
            sectionTop = sectionTop+rowHeight;
            prev= mainContainer.left;
            cv.drawLine(mainContainer.left, sectionTop, mainContainer.right, sectionTop, marginPainter);
        }

        reset(textPainter);

        left = mainContainer.left;
        right = mainContainer.right;

        String s = context.getString(R.string.AMOUNT_CHARGEABLE_IN_WORDS);
        StaticLayout sl = StaticLayout.Builder.obtain(s, 0, s.length(), textPainter, mainContainer.width()/4).build();
        cv.save();
        cv.translate(left+generalPadding, sectionTop+generalPadding);
        sl.draw(cv);
        cv.restore();
        cv.drawLine(mainContainer.left, sectionTop+rowHeight, mainContainer.right, sectionTop + rowHeight, marginPainter);

        StaticLayout inwords = StaticLayout.Builder.obtain(credNote.getGrossTotalInWords(),
                0,
                credNote.getGrossTotalInWords().length(),
                textPainter,
                mainContainer.right - mainContainer.left
                ).build();

        textPainter.setTypeface(boldTypeFace);
        cv.save();
        cv.translate(sl.getWidth() + mainContainer.left + generalPadding + generalPadding, sectionTop+generalPadding);
        inwords.draw(cv);
        cv.restore();
        reset(textPainter);
        textPainter.setTypeface(boldTypeFace);
        textPainter.setTextAlign(Paint.Align.CENTER);

        if(includeHsn) {
            prev = mainContainer.left;
            sectionTop = sectionTop + rowHeight;
            for (int i = 0; i < taxSectionHeaders.length; i++) {
                left = prev;
                right = ((prev + taxSectionHeaders[i].right) > mainContainer.right) ? mainContainer.right : prev + taxSectionHeaders[i].right;
                if (i != taxSectionHeaders.length - 1)
                    cv.drawLine(right, sectionTop, right, sectionTop + rowHeight, marginPainter);

                sl = StaticLayout.Builder.obtain(taxSectionHeaders[i].renderText, 0, taxSectionHeaders[i].renderText.length(), textPainter, (int) (right - left)).build();
                cv.save();
                cv.translate(left + (right - left) / 2, sectionTop + generalPadding);
                sl.draw(cv);
                cv.restore();
                prev = right;
            }


            reset(textPainter);
            textPainter.setTypeface(boldTypeFace);
            prev = mainContainer.left;
            sectionTop = sectionTop + rowHeight;
            cv.drawLine(mainContainer.left, sectionTop, mainContainer.right, sectionTop, marginPainter);
            for (int i = 0; i < taxSubSectionHeaders.length; i++) {
                left = prev;
                right = ((prev + taxSubSectionHeaders[i].right) > mainContainer.right) ? mainContainer.right : prev + taxSubSectionHeaders[i].right;

                if (i != taxSubSectionHeaders.length - 1)
                    cv.drawLine(right, sectionTop, right, sectionTop + rowHeight, marginPainter);

                sl = StaticLayout.Builder.obtain(taxSubSectionHeaders[i].renderText, 0, taxSubSectionHeaders[i].renderText.length(), textPainter, (int) (right - left)).build();
                cv.save();
                cv.translate(left + generalPadding, sectionTop + generalPadding);
                sl.draw(cv);
                cv.restore();
                prev = right;
            }
            sectionTop = sectionTop + rowHeight;
            cv.drawLine(mainContainer.left, sectionTop, mainContainer.right, sectionTop, marginPainter);


            reset(textPainter);
            prev = mainContainer.left;
            for (int i = 0; i < credNote.getNumberOfHsn(); i++) {
                HsnDetails hsnDetails = credNote.getHsn(i);
                for (int j = 0; j < taxSubSectionHeaders.length; j++) {
                    left = prev;
                    right = ((prev + taxSubSectionHeaders[j].right) > mainContainer.right) ? mainContainer.right : prev + taxSubSectionHeaders[j].right;
                    if (j != taxSubSectionHeaders.length)
                        cv.drawLine(right, sectionTop, right, sectionTop + rowHeightDoc, marginPainter);

                    sl = null;
                    System.out.println(i);
                    switch (j) {
                        case 0:
                            sl = StaticLayout.Builder.obtain(hsnDetails.getHsnCode(), 0, hsnDetails.getHsnCode().length(), textPainter, (int) (right - left)).build();
                            break;
                        case 1:
                            sl = StaticLayout.Builder.obtain(String.valueOf(hsnDetails.getTaxableAmount()), 0, String.valueOf(hsnDetails.getTaxableAmount()).length(), textPainter, (int) (right - left)).build();
                            break;
                        case 2:
                            String rate = String.valueOf(hsnDetails.getCentralTaxRate());
                            sl = StaticLayout.Builder.obtain(rate, 0, rate.length(), textPainter, (int) (right - left)).build();
                            break;
                        case 3:
                            String amt = String.valueOf(hsnDetails.getCentralTaxAmount());
                            sl = StaticLayout.Builder.obtain(amt, 0, amt.length(), textPainter, (int) (right - left)).build();
                            break;
                        case 4:
                            String sRate = String.valueOf(hsnDetails.getStateTaxRate());
                            sl = StaticLayout.Builder.obtain(sRate, 0, sRate.length(), textPainter, (int) (right - left)).build();
                            break;
                        case 5:
                            String sAmt = String.valueOf(hsnDetails.getStateTaxAmount());
                            sl = StaticLayout.Builder.obtain(sAmt, 0, sAmt.length(), textPainter, (int) (right - left)).build();
                            break;
                        case 6:
                            String totalTax = String.valueOf(hsnDetails.getTotalTaxAmount());
                            sl = StaticLayout.Builder.obtain(totalTax, 0, totalTax.length(), textPainter, (int) (right - left)).build();
                            break;
                    }
                    cv.save();
                    cv.translate(left + generalPadding, sectionTop);
                    sl.draw(cv);
                    cv.restore();
                    prev = right;
                }
                sectionTop = sectionTop + rowHeightDoc;
                prev = mainContainer.left;
            }
            cv.drawLine(mainContainer.left, sectionTop, mainContainer.right, sectionTop, marginPainter);

            prev = mainContainer.left;
            for (int i = 0; i < taxSubSectionHeaders.length; i++) {
                left = prev;
                right = ((prev + taxSubSectionHeaders[i].right) > mainContainer.right) ? mainContainer.right : prev + taxSubSectionHeaders[i].right;

                if (i != taxSubSectionHeaders.length - 1) {
                    cv.drawLine(right, sectionTop, right, sectionTop + rowHeight, marginPainter);
                }
                String getTextToPrint = "";
                switch (i) {
                    case 0:
                        getTextToPrint = "Total \u20B9";
                        break;
                    case 1:
                        getTextToPrint = credNote.getTaxLineItem(CredNote.TOTAL_TAX).getPrintableTaxableAmount();
                        break;
                    case 3:
                        getTextToPrint = credNote.getTaxLineItem(CredNote.TOTAL_TAX).getPrintableCentralTaxAmount();
                        break;
                    case 5:
                        getTextToPrint = credNote.getTaxLineItem(CredNote.TOTAL_TAX).getPrintableStateTaxAmount();
                        break;
                    case 6:
                        getTextToPrint = credNote.getTaxLineItem(CredNote.TOTAL_TAX).getPrintableTotalTaxAmount();
                        break;
                    default:
                        getTextToPrint = "";
                }
                sl = StaticLayout.Builder.obtain(getTextToPrint, 0, getTextToPrint.length(), textPainter, (int) (right - left)).build();
                cv.save();
                cv.translate(left + generalPadding, sectionTop + generalPadding);
                sl.draw(cv);
                cv.restore();
                prev = right;
            }
            cv.drawLine(mainContainer.left, sectionTop + rowHeight, mainContainer.right, sectionTop + rowHeight, marginPainter);
        }
    }

    private GoodLineItem getLineItemAt(int position, CredNote credNote) {
        String key = "";
        switch (position)
        {
            case 1:
                key = CredNote.TOTAL_GOODS_AMOUNT;
                break;
            case 2:
                key = CredNote.CGST;
                break;
            case 3:
                key = CredNote.SGST;
                break;
            case 4:
                key = CredNote.ROUND_OFF;
                break;
            case 5:
                key = CredNote.GROSS_TOTAL;
        }
        return credNote.getGoodLineItem(key);
    }

    private StaticLayout getGoodsInfo(int goodNumber, int position, GoodLine goodLineItem, float width) {

        String dataToPrint = "";
        position = position+1;
        switch (position)
        {
            case 1:
                dataToPrint = String.valueOf(goodNumber + 1);
                break;
            case 2:
                dataToPrint = goodLineItem.getPrintableDescription();
                break;
            case 3:
                dataToPrint = goodLineItem.getPrintableHsn();
                break;
            case 4:
                dataToPrint = goodLineItem.getPrintableQuantity();
                break;
            case 5:
                dataToPrint = goodLineItem.getPrintableRate();
                break;
            case 6:
                dataToPrint = goodLineItem.getPrintableUnit();
                break;
            case 7:
                dataToPrint = goodLineItem.getPrintableDiscountRate();
                break;
            case 8:
                dataToPrint = goodLineItem.getPrintableAmount();
                break;
            default:
                dataToPrint = "";
        }

        return StaticLayout
                .Builder
                .obtain(dataToPrint, 0, dataToPrint.length(), textPainter, (int)width)
                .build();
    }

    private void drawTopContainer(Canvas cv, CredNote credNote) {

        cv.drawLine(centreLine, spaceFromTop, centreLine, topSectionHeight+spaceFromTop, marginPainter);
        cv.drawLine(spaceFromLeft, topSectionHeight/2 + spaceFromTop, centreLine, topSectionHeight/2 + spaceFromTop, marginPainter);
        cv.drawLine(spaceFromLeft, topSectionHeight + spaceFromTop, centreLine, topSectionHeight + spaceFromTop, marginPainter);

        StaticLayout companyDetails = getPartyDetailsLayout(credNote.organisation, (int)(centreLine - spaceFromLeft));
        StaticLayout partyDetails = getPartyDetailsLayout(credNote.party, (int)(centreLine - spaceFromLeft));

        cv.save();
        cv.translate(mainContainer.left+generalPadding, mainContainer.top+generalPadding);
        companyDetails.draw(cv);
        cv.restore();
        cv.save();
        cv.translate(mainContainer.left+generalPadding, topSectionHeight/2 + spaceFromTop+generalPadding);
        partyDetails.draw(cv);
        cv.restore();

        float half2CentralLine =  centreLine + (mainContainer.right - centreLine)/2 ;
        float heightOfEachSection = (topSectionHeight + spaceFromTop - spaceFromTop)/7;
        float top = spaceFromTop;
        float bottom = top + heightOfEachSection;
        float left = centreLine;
        float right;
        StaticLayout sl;
        for(int i = 0; i < invoiceFields.length; i++)
        {
            if((i+1)%2==1)
            {
                left = centreLine;
                right = half2CentralLine;
                cv.drawLine(right, top, right, bottom, marginPainter);
            }
            else
            {
                left = half2CentralLine;
                right = mainContainer.right;
            }
            cv.save();
            cv.translate(left+generalPadding, top);
            drawInfo(invoiceFields[i], credNote.invoiceDetails, i, (int) (right-left)).draw(cv);
            cv.restore();
            if((i+1)%2==0)
            {
                cv.drawLine(centreLine, bottom, mainContainer.right, bottom, marginPainter);
                top = bottom;
                bottom = top+heightOfEachSection;
            }
        }

        cv.drawLine(centreLine, topSectionHeight + spaceFromTop, mainContainer.right, topSectionHeight+spaceFromTop, marginPainter);
        cv.save();
        cv.translate(centreLine+generalPadding, top);
        getTransportDetails(credNote.invoiceDetails, mainContainer.right - mainContainer.left).draw(cv);
        cv.restore();
    }

    private StaticLayout getTransportDetails(InvoiceDetail invoiceDetails, int maxContainerWidth) {
        String textToRender = context.getString(R.string.terms_Of_Delivery)+" "+ invoiceDetails.getTermsOfDelivery();
        StaticLayout result = StaticLayout.Builder.obtain(textToRender, 0, textToRender.length(), textPainter, maxContainerWidth).build();
        return result;
    }

    private StaticLayout drawInfo(String info, InvoiceDetail invoiceDetails, int id, int maxContainerWidth) {
        String detail = info+System.lineSeparator()+invoiceDetails.getById(id);
        StaticLayout result = StaticLayout.Builder.obtain(detail, 0, detail.length(), textPainter, maxContainerWidth)
                .setIncludePad(true)
                .setLineSpacing(1.5f,1.0f)
                .build();
        return result;
    }

    private StaticLayout getPartyDetailsLayout(Organisation organisation, int maxContainerWidth) {
        StringBuilder sb = new StringBuilder(organisation.getName());
        sb.append(System.lineSeparator())
                .append(organisation.getAddress())
                .append(System.lineSeparator())
                .append(context.getString(R.string.GSTIN)+": "+ organisation.getGSTIn())
                .append(System.lineSeparator())
                .append(context.getString(R.string.label_state)+": "+ organisation.getState())
                .append(", ").append(context.getString(R.string.state_code)+": "+ organisation.getStateCode());


        StaticLayout staticLayout = StaticLayout.Builder
                .obtain(sb.toString(), 0, sb.length(), textPainter, maxContainerWidth)
                .setIncludePad(true)
                .setLineSpacing(1, 1.1f)
                .build();

        return staticLayout;
    }

    private void drawBottomContainer(Canvas cv, CredNote credNote)
    {
        reset(textPainter);
        float sectionStart = mainContainer.bottom - documentRatio*bottomContainerRatio;

        cv.drawLine(mainContainer.left, sectionStart, mainContainer.right, sectionStart, marginPainter);

        String declarationStatement = context.getString(R.string.DECLERATION);
        StaticLayout sl = StaticLayout.Builder
                .obtain(declarationStatement, 0, declarationStatement.length(), textPainter, (int) (centreLine-mainContainer.left))
                .build();

        cv.save();
        cv.translate(mainContainer.left+generalPadding, sectionStart+generalPadding);
        sl.draw(cv);
        cv.restore();

        String companyStatement = context.getString(R.string.FOR)+" "+credNote.organisation.getName();
        textPainter.setTextAlign(Paint.Align.CENTER);
        sl = StaticLayout.Builder
                .obtain(companyStatement, 0, companyStatement.length(), textPainter, (int) (mainContainer.right-centreLine))
                .build();
        cv.save();
        cv.translate((centreLine+ (mainContainer.right - centreLine)/2)+generalPadding, sectionStart+generalPadding);
        sl.draw(cv);
        cv.restore();

        String authorisedSignatory = context.getString(R.string.Authorised_Signatory);
        sl = StaticLayout.Builder
            .obtain(authorisedSignatory, 0, authorisedSignatory.length(), textPainter, (int) (mainContainer.right-centreLine))
            .build();

        cv.drawLine(centreLine, sectionStart, centreLine, mainContainer.bottom, marginPainter);

        cv.save();
        cv.translate((centreLine+ (mainContainer.right - centreLine)/2)+generalPadding, mainContainer.bottom - sl.getHeight() - generalPadding);
        sl.draw(cv);
        cv.restore();
    }
}
