package cn.link;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;


/**
 *  底部进入动画Activity
 * @author Link
 */
public class LeftInHBActivity extends LinkActivity {

	public static void animShowForResult(Context ctx, Intent intent, int requestCode) {
		Activity act = (Activity) ctx;
		act.startActivityForResult(intent, requestCode);
		act.overridePendingTransition(R.anim.anim_window_in, R.anim.anim_window_out);
	}

	public static void animShow(Context ctx, Intent intent) {
		Activity act = (Activity) ctx;
		act.startActivity(intent);
		act.overridePendingTransition(R.anim.anim_window_in, R.anim.anim_window_out);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (isFinishing()) {
			overridePendingTransition(R.anim.anim_window_close_in, R.anim.anim_window_close_out);
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.anim_window_close_in, R.anim.anim_window_close_out);
	}

	protected OnClickListener mFinishListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
}
