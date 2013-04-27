package cn.link;

import android.app.Activity;
import android.content.Context;

public class LinkActivity extends Activity {
    private AppContext mAppContext;

    protected Activity getAct() {
        return this;
    }

    protected Context getThis() {
        return this;
    }

    public AppContext getAppContext() {

        if (mAppContext == null) {

            mAppContext = AppContext.getInstance(LinkActivity.this);

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
