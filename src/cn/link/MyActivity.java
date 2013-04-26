package cn.link;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);


    }

    class LinkAdapter extends PagerAdapter {
        Context mContext;

        public LinkAdapter(Context ctx) {
            this.mContext = ctx;
        }

        SparseArray<View> mViews = new SparseArray<View>();

        @Override
        public int getCount() {
            return 0;  //ToDo
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;  //ToDo
        }

        class Holder {
            Button mBtnAdd;
            Button mBtnRemove;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if (mViews.get(position) == null) {
                view = View.inflate(mContext, R.layout.fragment_cell, null);
                Holder holder = new Holder();
                holder.mBtnAdd= (Button) view.findViewById(R.id.btn_add);
                holder.mBtnRemove= (Button) view.findViewById(R.id.btn_remove);

                view.setTag(holder);

                mViews.append(position, view);
            } else {
                view = mViews.get(position);
            }

            Holder holder = (Holder) view.getTag();


            container.addView(view);
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
}
