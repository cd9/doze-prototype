package com.cdapplications.doze;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragActivity extends AppCompatActivity {


    protected abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.single_frag_activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.single_frag_wrapper);

        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.single_frag_wrapper, fragment)
                    .commit();
        }
    }
}
