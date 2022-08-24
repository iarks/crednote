package com.iarks.crednote.presentation.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.iarks.crednote.Exceptions.FormNotSavedException;
import com.iarks.crednote.R;
import com.iarks.crednote.abstractions.GoodsListCarrier;
import com.iarks.crednote.abstractions.HsnDetailsCarrier;
import com.iarks.crednote.abstractions.InvoiceDetailsCarrier;
import com.iarks.crednote.abstractions.OrganizationDetailsCarrier;
import com.iarks.crednote.abstractions.TaxDetailsCarrier;
import com.iarks.crednote.models.CredNote;
import com.iarks.crednote.models.Good;
import com.iarks.crednote.models.GoodLineItem;
import com.iarks.crednote.models.HsnDetails;
import com.iarks.crednote.models.TaxDetails;
import com.iarks.crednote.models.TaxLineItem;
import com.iarks.crednote.service.SimpleCredNoteRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DocumentPreviewFragment extends Fragment {

    private final GoodsListCarrier goodsListCarrier;
    private final HsnDetailsCarrier hsnDetailsCarrier;
    private final InvoiceDetailsCarrier invoiceDetailsCarrier;
    private final OrganizationDetailsCarrier organizationDetailsCarrier;
    private final OrganizationDetailsCarrier partyDetailCarrier;
    private final TaxDetailsCarrier taxDetailsCarrier;
    private final Context context;

    public DocumentPreviewFragment(OrganizationDetailsCarrier organizationDetailsCarrier, OrganizationDetailsCarrier partyDetailsCarrier, InvoiceDetailsCarrier invoiceDetailsCarrier, GoodsListCarrier goodsListCarrier, HsnDetailsCarrier hsnDetailsCarrier, TaxDetailsCarrier taxDetailsCarrier, Context context) {
        this.organizationDetailsCarrier = organizationDetailsCarrier;
        this.partyDetailCarrier = partyDetailsCarrier;
        this.invoiceDetailsCarrier = invoiceDetailsCarrier;
        this.goodsListCarrier = goodsListCarrier;
        this.hsnDetailsCarrier = hsnDetailsCarrier;
        this.taxDetailsCarrier = taxDetailsCarrier;
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_document_preview, container, false);
        Button preview = v.findViewById(R.id.preview);
        ImageView surface = v.findViewById(R.id.surface);


        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    List<Good> lockedGoods = goodsListCarrier.getGoodDetails();
                    List<HsnDetails> lockedHsnDetails = hsnDetailsCarrier.GetHsnDetails();
                    TaxDetails taxDetails = taxDetailsCarrier.getTaxDetails();

                    CredNote credNote = new CredNote(organizationDetailsCarrier.getOrganisationDetails(),
                            partyDetailCarrier.getOrganisationDetails(),
                            invoiceDetailsCarrier.getInvoiceDetail(), taxDetails.getTotalInWords());
                    for (Good good : lockedGoods) {
                        credNote.addGoods(good);
                    }

                    for (HsnDetails hsnDetails : lockedHsnDetails) {
                        credNote.addHsn(hsnDetails);
                    }

                    List<GoodLineItem> goodLineItems = taxDetails.getGoodsLineItem();
                    List<TaxLineItem> taxLineItems = taxDetails.getTaxTotals();

                    for(GoodLineItem goodLineItem:goodLineItems)
                    {
                        credNote.setGoodLineItem(goodLineItem.KEY, goodLineItem);
                    }

                    for(TaxLineItem taxLineItem:taxLineItems)
                    {
                        credNote.setTaxLineItem(taxLineItem.KEY, taxLineItem);
                    }
                    boolean isHsnIncluded = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("sync", true);
                    SimpleCredNoteRenderer cr = new SimpleCredNoteRenderer(context, isHsnIncluded);
                    PdfDocument document = cr.generateCredNote(credNote);
                    File file = new File(Environment.getExternalStorageDirectory(), "temp.pdf");

                    FileOutputStream fileStream;
                    try {
                        fileStream = new FileOutputStream(file);
                        document.writeTo(new FileOutputStream(file));

                        Toast.makeText(context, "PDF generated successfully.", Toast.LENGTH_SHORT).show();
                        document.close();

                        MimeTypeMap myMime = MimeTypeMap.getSingleton();
                        Intent newIntent = new Intent(Intent.ACTION_VIEW);
                        String mimeType = myMime.getMimeTypeFromExtension("pdf");
                        newIntent.setDataAndType(Uri.fromFile(file),mimeType);
                        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            context.startActivity(newIntent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Could not generate PDF.", Toast.LENGTH_SHORT).show();
                    }


                } catch (FormNotSavedException e) {
                    Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
                catch (Exception ex) {
                    Snackbar.make(view, ex.getMessage(), Snackbar.LENGTH_LONG).show();
                }
                finally
                {
                }
            }
        });
        return v;
    }

}