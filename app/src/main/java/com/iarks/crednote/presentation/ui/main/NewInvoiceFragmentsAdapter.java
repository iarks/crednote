package com.iarks.crednote.presentation.ui.main;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.iarks.crednote.R;
import com.iarks.crednote.abstractions.GoodsListCarrier;
import com.iarks.crednote.abstractions.HsnDetailsCarrier;
import com.iarks.crednote.abstractions.InvoiceDetailsCarrier;
import com.iarks.crednote.abstractions.OrganizationDetailsCarrier;
import com.iarks.crednote.abstractions.TaxDetailsCarrier;
import com.iarks.crednote.models.GoodsAmountCarrier;
import com.iarks.crednote.models.Organisation;
import com.iarks.crednote.models.TaxAmountCarrier;
import com.iarks.crednote.presentation.fragments.DocumentPreviewFragment;
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
    public static final int[] TAB_TITLES = new int[] {
                    R.string.label_company,
                    R.string.party,
                    R.string.label_invoiceDetails,
                    R.string.GoodsDetails,
                    R.string.hsnDetails,
                    R.string.taxTotals,
                    R.string.preview
            };

    private final Fragment[] fragmentInstances;
    private final Context mContext;
    private final Organisation defaultOrg;

    public NewInvoiceFragmentsAdapter(FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity);
        mContext = context;
        fragmentInstances = new Fragment[TAB_TITLES.length];
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isCompanyAutofill = sharedPref.getBoolean("isCompanyAutofill", false);
        if(!isCompanyAutofill) {
            defaultOrg = null;
        }
        else
        {
            String comName = sharedPref.getString("orgName", "");
            String orgAddress = sharedPref.getString("orgAddress", "");
            String gstIn = sharedPref.getString("orgGstin", "");
            String orgState = sharedPref.getString("orgState", "");
            int orgStateCode = 0;
            try {
                orgStateCode = Integer.parseInt(sharedPref.getString("orgStateCode", "0"));
            }
            catch (Exception ex)
            {
                // ignore
            }
            defaultOrg = new Organisation(comName, new String[]{orgAddress}, gstIn, orgState, orgStateCode);
        }
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
                fr = new OrganizationAndPartyFragment(OrganizationAndPartyFragment.PartyType.ORGANISATION, mContext, defaultOrg);
                break;
            case 1:
                fr = new OrganizationAndPartyFragment(OrganizationAndPartyFragment.PartyType.PARTY, mContext);
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
                fragmentInstances[4] = fragmentInstances[4] == null ? new HsnListFragment() : fragmentInstances[4];
                fragmentInstances[3] = fragmentInstances[3] == null ? new GoodsListFragment() : fragmentInstances[3];
                fr = new TaxFragment((TaxAmountCarrier) fragmentInstances[4], (GoodsAmountCarrier) fragmentInstances[3]);
                break;
            case 6:
                fragmentInstances[0] = fragmentInstances[0] == null ? new OrganizationAndPartyFragment(OrganizationAndPartyFragment.PartyType.ORGANISATION, mContext, defaultOrg) : fragmentInstances[0];
                fragmentInstances[1] = fragmentInstances[1] == null ? new OrganizationAndPartyFragment(OrganizationAndPartyFragment.PartyType.PARTY, mContext) : fragmentInstances[1];
                fragmentInstances[2] = fragmentInstances[2] == null ? new InvoiceDetailsFragment(mContext) : fragmentInstances[2];
                fragmentInstances[3] = fragmentInstances[3] == null ? new GoodsListFragment() : fragmentInstances[3];
                fragmentInstances[4] = fragmentInstances[4] == null ? new HsnListFragment() : fragmentInstances[4];
                fragmentInstances[5] = fragmentInstances[5] == null ? new TaxFragment((TaxAmountCarrier) fragmentInstances[4], (GoodsAmountCarrier) fragmentInstances[3]) : fragmentInstances[5];
                fr = new DocumentPreviewFragment((OrganizationDetailsCarrier) fragmentInstances[0],
                        (OrganizationDetailsCarrier) fragmentInstances[1],
                        (InvoiceDetailsCarrier) fragmentInstances[2],
                        (GoodsListCarrier) fragmentInstances[3],
                        (HsnDetailsCarrier) fragmentInstances[4],
                        (TaxDetailsCarrier) fragmentInstances[5], mContext);
                break;
            default:
                if(fragmentInstances[0]!=null)
                {
                    return fragmentInstances[0];
                }
                else
                {
                    fr = new OrganizationAndPartyFragment(OrganizationAndPartyFragment.PartyType.ORGANISATION, mContext);
                    fragmentInstances[0] = fr;

                    return fr;
                }
        }
        fragmentInstances[position] = fr;
        return fr;
    }

    public Fragment getFragmentAt(int position)
    {
        if(position<0 || position>=fragmentInstances.length)
        {
            return fragmentInstances[0];
        }
        return fragmentInstances[position];
    }

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }
}