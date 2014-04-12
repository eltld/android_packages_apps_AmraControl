/*
 *  Copyright (C) 2013-2014 Alexander "Evisceration" Martinz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.regulus.amra.amracontrol.fragments.performance;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.regulus.amra.amracontrol.R;
import org.regulus.amra.amracontrol.activities.MainActivity;
import org.regulus.amra.amracontrol.fragments.parents.AttachFragment;
import org.regulus.amra.amracontrol.widgets.JfViewPager;
import org.regulus.amra.amracontrol.widgets.adapters.ScreenSlidePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PerformanceFragment extends AttachFragment {

    public static final int ID = 2;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View rootView = layoutInflater.inflate(R.layout.fragment_viewpager, viewGroup, false);

        List<Fragment> mFragments = getFragments();
        List<String> mTitles = getTitles();

        JfViewPager mViewPager = (JfViewPager) rootView.findViewById(R.id.pager);

        ScreenSlidePagerAdapter mTabsAdapter = new ScreenSlidePagerAdapter(
                getChildFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mTabsAdapter);

        PagerTabStrip mPagerTabStrip = (PagerTabStrip) rootView.findViewById(R.id.pagerTabStrip);
        mPagerTabStrip.setDrawFullUnderline(false);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        if (activity instanceof MainActivity) {
            super.onAttach(activity, PerformanceFragment.ID);
        } else {
            super.onAttach(activity);
        }
    }

    private List<Fragment> getFragments() {
        List<Fragment> tmpList = new ArrayList<Fragment>();
        tmpList.add(new PerformanceInformationFragment());
        tmpList.add(new PerformanceCpuSettings());
        if (PerformanceExtrasFragment.isSupported(getActivity())) {
            tmpList.add(new PerformanceExtrasFragment());
        }
        return tmpList;
    }

    private List<String> getTitles() {
        List<String> tmpList = new ArrayList<String>();
        tmpList.add(getString(R.string.information));
        tmpList.add(getString(R.string.cpusettings));
        if (PerformanceExtrasFragment.isSupported(getActivity())) {
            tmpList.add(getString(R.string.general));
        }
        return tmpList;
    }
}
