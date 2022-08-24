package com.iarks.crednote.presentation;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.iarks.crednote.R;
import com.iarks.crednote.databinding.ActivityNewInvoiceBinding;
import com.iarks.crednote.presentation.ui.main.NewInvoiceFragmentsAdapter;

public class NewInvoiceActivity extends AppCompatActivity {

    private ActivityNewInvoiceBinding binding;
    private ViewPager2 viewPager;

    private TabLayout tabLayout;

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

        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}