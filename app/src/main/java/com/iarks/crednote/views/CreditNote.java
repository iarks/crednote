package com.iarks.crednote.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iarks.crednote.R;
import com.iarks.crednote.models.CredNote;
import com.iarks.crednote.models.InvoiceDetail;
import com.iarks.crednote.models.Organisation;

/**
 * TODO: document your custom view class.
 */
public class CreditNote extends RelativeLayout {

    private CredNote credNote;
    private TypedArray attribute;
    private TextView org;
    private TextView party;

    public CreditNote(Context context) {
        super(context);
        inflate(context, R.layout.crednote_template, this);
        org = findViewById(R.id.organisationName);
        party = findViewById(R.id.partyName);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Paint paint = new Paint();
        /*
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(org.getTextSize());
//        paint.setTextAlign(org.getPaint());

         */
        canvas.drawText(credNote.getOrganizationName(), org.getX(), org.getY(), org.getPaint());
    }

    public void setCredNode(CredNote credNote)
    {
        org.setText(credNote.getOrganizationName());
        this.credNote = credNote;
    }


}