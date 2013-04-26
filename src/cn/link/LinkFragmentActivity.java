package cn.link;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

public class LinkFragmentActivity extends FragmentActivity {
    private AppContext mAppContext;

    protected Context getThis() {
        return this;
    }


    public AppContext getAppContext() {
        if (mAppContext == null) {
            mAppContext = AppContext.getInstance(LinkFragmentActivity.this);
        }
        return mAppContext;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
