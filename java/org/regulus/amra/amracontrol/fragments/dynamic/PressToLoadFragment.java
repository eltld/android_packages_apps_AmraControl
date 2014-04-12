/*
 * Copyright (C) 2014 Alexander "Evisceration" Martinz
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses
 */
package org.regulus.amra.amracontrol.fragments.dynamic;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.regulus.amra.amracontrol.R;
import org.regulus.amra.amracontrol.fragments.tools.PropModder;
import org.regulus.amra.amracontrol.fragments.tools.ToolsEditor;
import org.regulus.amra.amracontrol.fragments.tools.ToolsFreezer;
import org.regulus.amra.amracontrol.fragments.tools.VmFragment;
import org.regulus.amra.amracontrol.utils.constants.DeviceConstants;

public class PressToLoadFragment extends Fragment implements DeviceConstants {

    public static final String ARG_FRAGMENT = "arg_fragment";
    public static final String ARG_IMG      = "arg_img";

    public static final int FRAGMENT_VM         = 0;
    public static final int FRAGMENT_BUILD_PROP = 2;

    private Fragment fragment;
    private String   mText;

    private int mImgId = R.mipmap.ic_launcher;
    private int mFragmentId;

    public static PressToLoadFragment newInstance(int fragmentId, int imgId) {
        final PressToLoadFragment f = new PressToLoadFragment();

        final Bundle b = new Bundle();
        b.putInt(PressToLoadFragment.ARG_FRAGMENT, fragmentId);
        b.putInt(PressToLoadFragment.ARG_IMG, imgId);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mFragmentId = getArguments().getInt(ARG_FRAGMENT);
        mImgId = getArguments().getInt(ARG_IMG);

        switch (mFragmentId) {
            case 1:
                fragment = ToolsEditor.newInstance(1);
                mText = getString(R.string.fragment_press_to_load, "SysCtl Editor");
                break;
            case 3:
                fragment = ToolsFreezer.newInstance(0, "usr");
                mText = getString(R.string.fragment_press_to_load, "Freezer");
                break;
            case 4:
                fragment = ToolsFreezer.newInstance(1, "usr");
                mText = getString(R.string.fragment_press_to_load, "Unfreezer");
                break;
            default:
                mText = "Could not identify fragment to load";
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_ptl, viewGroup, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Fragment f;
        switch (mFragmentId) {
            case FRAGMENT_VM:
                f = new VmFragment();
                break;
            case FRAGMENT_BUILD_PROP:
                f = new PropModder();
                break;
            default:
                f = new ReplaceFragment();
                break;
        }
        replaceFragment(f, false);
    }

    public void replaceFragment(Fragment f, boolean animate) {
        final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (animate) {
            ft.setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                    R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        }
        ft.replace(R.id.container_ptl, f)
                .addToBackStack(null)
                .commit();
    }

    private class ReplaceFragment extends Fragment {

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                final Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_press_to_load, container, false);

            final TextView tvHelp = (TextView) view.findViewById(R.id.help_textview);
            tvHelp.setText(mText);

            final ImageView ivHelp = (ImageView) view.findViewById(R.id.help_imageview);
            ivHelp.setImageResource(mImgId);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    replaceFragment(fragment, true);
                }
            });

            return view;
        }
    }
}
