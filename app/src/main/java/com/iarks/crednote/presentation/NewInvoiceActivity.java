package com.iarks.crednote.presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.iarks.crednote.Exceptions.FormNotSavedException;
import com.iarks.crednote.R;
import com.iarks.crednote.databinding.ActivityNewInvoiceBinding;
import com.iarks.crednote.models.Good;
import com.iarks.crednote.models.HsnDetails;
import com.iarks.crednote.models.InvoiceDetail;
import com.iarks.crednote.models.Organisation;
import com.iarks.crednote.presentation.fragments.GoodsListFragment;
import com.iarks.crednote.presentation.fragments.HsnListFragment;
import com.iarks.crednote.presentation.fragments.InvoiceDetailsFragment;
import com.iarks.crednote.presentation.fragments.OrganizationAndPartyFragment;
import com.iarks.crednote.presentation.ui.main.NewInvoiceFragmentsAdapter;

import java.util.List;

public class NewInvoiceActivity extends AppCompatActivity {

    private ActivityNewInvoiceBinding binding;
    private ViewPager2 viewPager;

    private TabLayout tabLayout;

    private Context context;

    public NewInvoiceActivity()
    {
        context = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityNewInvoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NewInvoiceFragmentsAdapter newInvoiceFragmentsAdapter =
                new NewInvoiceFragmentsAdapter(this, this);
        viewPager = binding.viewPager;
        viewPager.setAdapter(newInvoiceFragmentsAdapter);
        tabLayout = binding.tabLayout;
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(NewInvoiceFragmentsAdapter.TAB_TITLES[position])
        ).attach();

        tabLayout = findViewById(R.id.tabLayout);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.BACK_CONFIRM);
        builder.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.NO,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }
}