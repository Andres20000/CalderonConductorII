package calderonconductor.tactoapps.com.calderonconductor.Adapter;

/**
 * Created by tacto on 2/10/17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import calderonconductor.tactoapps.com.calderonconductor.Tab1;
import calderonconductor.tactoapps.com.calderonconductor.Tab2;
import calderonconductor.tactoapps.com.calderonconductor.Tab3;
import calderonconductor.tactoapps.com.calderonconductor.Tab4;
import calderonconductor.tactoapps.com.calderonconductor.Tab5;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:

                return new Tab1();
            case 1:

                return new Tab2();
            case 2:

                return new Tab3();
            case 3:

                return new Tab4();
            case 4:

                return new Tab5();
        }

        return null;
    }

    @Override
    public int getCount() {

        return 5;
    }

}