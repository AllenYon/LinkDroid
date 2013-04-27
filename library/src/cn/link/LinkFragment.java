package cn.link;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class LinkFragment extends Fragment {
    private Activity mActivity;
    private AppContext mAppContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    public Activity getAct() {
        return mActivity;
    }

    public AppContext getAppContext() {

        if (mAppContext == null) {

            mAppContext = AppContext.getInstance(mActivity);

        }

        return mAppContext;
    }


}
