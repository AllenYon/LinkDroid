package cn.link.photoview;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import cn.link.widget.LinkIBtn;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-4-1
 * Time: AM11:15
 * To change this template use File | Settings | File Templates.
 */
public class PhotoView extends LinkIBtn implements IPhotoView {

    private final PhotoViewAttacher mAttacher;

    private ImageView.ScaleType mPendingScaleType;

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setScaleType(ImageView.ScaleType.MATRIX);
        mAttacher = new PhotoViewAttacher(this);

        if (null != mPendingScaleType) {
            setScaleType(mPendingScaleType);
            mPendingScaleType = null;
        }
    }

    @Override
    public boolean canZoom() {
        return mAttacher.canZoom();
    }

    @Override
    public RectF getDisplayRect() {
        return mAttacher.getDisplayRect();
    }

    @Override
    public float getMinScale() {
        return mAttacher.getMinScale();
    }

    @Override
    public float getMidScale() {
        return mAttacher.getMidScale();
    }

    @Override
    public float getMaxScale() {
        return mAttacher.getMaxScale();
    }

    @Override
    public float getScale() {
        return mAttacher.getScale();
    }

    @Override
    public ImageView.ScaleType getScaleType() {
        return mAttacher.getScaleType();
    }

    @Override
    public void setAllowParentInterceptOnEdge(boolean allow) {
        mAttacher.setAllowParentInterceptOnEdge(allow);
    }

    @Override
    public void setMinScale(float minScale) {
        mAttacher.setMinScale(minScale);
    }

    @Override
    public void setMidScale(float midScale) {
        mAttacher.setMidScale(midScale);
    }

    @Override
    public void setMaxScale(float maxScale) {
        mAttacher.setMaxScale(maxScale);
    }

    @Override
    // setImageBitmap calls through to this method
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }

    @Override
    public void setOnMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener listener) {
        mAttacher.setOnMatrixChangeListener(listener);
    }

    @Override
    public void setOnLongClickListener(View.OnLongClickListener l) {
        mAttacher.setOnLongClickListener(l);
    }

    @Override
    public void setOnPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener listener) {
        mAttacher.setOnPhotoTapListener(listener);
    }

    @Override
    public void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener listener) {
        mAttacher.setOnViewTapListener(listener);
    }

    @Override
    public void setScaleType(ImageView.ScaleType scaleType) {
        if (null != mAttacher) {
            mAttacher.setScaleType(scaleType);
        } else {
            mPendingScaleType = scaleType;
        }
    }

    @Override
    public void setZoomable(boolean zoomable) {
        mAttacher.setZoomable(zoomable);
    }

    @Override
    public void zoomTo(float scale, float focalX, float focalY) {
        mAttacher.zoomTo(scale, focalX, focalY);
    }

    @Override
    protected void onDetachedFromWindow() {
        mAttacher.cleanup();
        super.onDetachedFromWindow();
    }
}
