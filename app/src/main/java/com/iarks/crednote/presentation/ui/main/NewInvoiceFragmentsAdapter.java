package com.iarks.crednote.presentation.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.iarks.crednote.R;
import com.iarks.crednote.presentation.fragments.GoodsListFragment;
import com.iarks.crednote.presentation.fragments.HsnListFragment;
import com.iarks.crednote.presentation.fragments.InvoiceDetailsFragment;
import com.iarks.crednote.presentation.fragments.OrganizationAndPartyFragment;
import com.iarks.crednote.presentation.fragments.TaxFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class NewInvoiceFragmentsAdapter extends FragmentStateAdapter {
    @StringRes
    public static final int[] TAB_TITLES = new int[]{R.string.label_company, R.string.party, R.string.label_invoiceDetails, R.string.GoodsDetails, R.string.hsnDetails, R.string.taxTotals};
    private Fragment[] fragmentInstances;
    private final Context mContext;

    public NewInvoiceFragmentsAdapter(FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity);
        mContext = context;
        fragmentInstances = new Fragment[TAB_TITLES.length];
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(fragmentInstances[position]!=null)
        {
            return fragmentInstances[position];
        }
        Fragment fr=null;
        switch (position)
        {
            case 0:
                fr = new OrganizationAndPartyFragment(OrganizationAndPartyFragment.PartyType.Organisation, mContext);
                break;
            case 1:
                fr = new OrganizationAndPartyFragment(OrganizationAndPartyFragment.PartyType.Party, mContext);
                break;
            case 2:
                fr = new InvoiceDetailsFragment(mContext);
                break;
            case 3:
                fr = new GoodsListFragment();
                break;
            case 4:
                fr = new HsnListFragment();
                break;
            case 5:
                fr = new TaxFragment();
                break;
            default:
                if(fragmentInstances[0]!=null)
                {
                    return fragmentInstances[0];
                }
                else
                {
                    fr = new OrganizationAndPartyFragment(OrganizationAndPartyFragment.PartyType.Organisation, mContext);
                    fragmentInstances[0] = fr;

                    return fr;
                }
        }
        fragmentInstances[position] = fr;
        return fr;
    }

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }
}