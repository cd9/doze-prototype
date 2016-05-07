package com.cdapplications.doze;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;

/**
 * Created by Colin on 2015-08-25.
 */
public class DozeHomeActivity extends SingleFragActivity {
    @Override
    protected Fragment createFragment(){
        return DozeHomeFragment.newInstance();
        }
}
