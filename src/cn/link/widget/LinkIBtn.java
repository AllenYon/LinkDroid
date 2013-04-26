package cn.link.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import cn.link.R;
import cn.link.core.SimpleAsyncTask;
import cn.link.core.UICallBack;
import cn.link.utils.Constants;

import java.io.*;
import java.net.URL;

/**
 * Function : 1. 按下有阴影效果 2. 可对图片进行裁剪 圆角 3.当设置 scaleType 为 matrix 时，实现 TopCrop效果
 *
 * @author Link
 */
public class LinkIBtn extends LruCacheIBtn {
    // 是否需要 对图片进行裁剪
    private boolean isCropCornerEnable;

    // 是否按钮按下效果可用
    private boolean isPressEffectEnable;

    private Path mClipPath;
    private RectF mClipRectF;

    private float[] radii;
    // private static final float Trans=255.f*(-5.5f);

    private static final float Trans = -25f;
    // 对长图 进行截断
    private static final float DEFAULT_MAX_MULTIPLE = 10f;

    private float mMaxMultiple;
    private boolean isCutoffed;
    private static final boolean DEBUG = true;

    /**
     * 按下这个按钮进行的颜色过滤
     */
    public final static float[] BT_SELECTED = new float[]{1, 0, 0, 0, Trans, 0, 1, 0, 0, Trans, 0, 0, 1, 0, Trans, 0,
            0, 0, 1, 0};

    /**
     * 按钮恢复原状的颜色过滤
     */
    public final static float[] BT_NOT_SELECTED = new float[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            1, 0};

    public LinkIBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        mClipPath = new Path();
        mClipRectF = new RectF(0, 0, 0, 0);

        // setScaleType(ScaleType.MATRIX);

        TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.HBIBtnStyle);
        mMaxMultiple = tArray.getFloat(R.styleable.HBIBtnStyle_maxMultipleHeight, DEFAULT_MAX_MULTIPLE);

        float lt = tArray.getDimension(R.styleable.HBIBtnStyle_lt, 0);
        float lb = tArray.getDimension(R.styleable.HBIBtnStyle_lb, 0);
        float rt = tArray.getDimension(R.styleable.HBIBtnStyle_rt, 0);
        float rb = tArray.getDimension(R.styleable.HBIBtnStyle_rb, 0);

        isPressEffectEnable = tArray.getBoolean(R.styleable.HBIBtnStyle_presseffectEnable, true);
        isCropCornerEnable = tArray.getBoolean(R.styleable.HBIBtnStyle_cropCornerEnable, false);

        float corner = tArray.getDimension(R.styleable.HBIBtnStyle_corner, -1);

        if (corner != -1) {
            setCropCorner(corner);
        } else {
            setCropCorner(lt, rt, lb, rb);
        }

        tArray.recycle();

    }

    /**
     * 设置裁剪圆角
     *
     * @param lt
     * @param rt
     * @param lb
     * @param rb
     */
    public void setCropCorner(float lt, float rt, float lb, float rb) {
        radii = new float[]{lt, lt, rt, rt, lb, lb, rb, rb};
        if (lt == 0 && lb == 0 && rt == 0 && rb == 0) {
            isCropCornerEnable = false;
        } else {
            isCropCornerEnable = true;
        }

    }

    /**
     * 设置裁剪圆角
     *
     * @param r
     */
    public void setCropCorner(float r) {
        setCropCorner(r, r, r, r);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isCropCornerEnable) {
            mClipRectF.right = getMeasuredWidth();
            mClipRectF.bottom = getMeasuredHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (!isPressEffectEnable) {
            // 如果按下效果无效 调用父类方法
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (getDrawable() != null) {
                    getDrawable().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (getDrawable() != null) {
                    getDrawable().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
                }

                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * @param width      原图宽
     * @param height     原图高
     * @param mItemWidth 目标宽
     */
    public void setLayoutParamsWidthAndHeight(int width, int height, int mItemWidth) {
        // TODO Auto-generated method stub
        // 高比宽 大于 max_multiple
        getLayoutParams().width = mItemWidth;
        if ((height * 1.0f / width) > mMaxMultiple) {
            getLayoutParams().height = (int) (mItemWidth * mMaxMultiple);
            isCutoffed = true;
        } else {
            getLayoutParams().height = (int) ((mItemWidth * height) / width * 1.0f);
            isCutoffed = false;
        }

    }

    public boolean isCutoffed() {
        return isCutoffed;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        // TODO Auto-generated method stub
        if (isCropCornerEnable) {
            mClipRectF.right = getMeasuredWidth();
            mClipRectF.bottom = getMeasuredHeight();
        }
        if (getScaleType() == ImageView.ScaleType.MATRIX) {
            /**
             * topCrop 方式 对应与 getScaleType() ScaleType .CenterCrop
             */
            Matrix matrix = new Matrix();
            float mImgWidth = (float) bm.getWidth();
            float xScale = (float) getLayoutParams().width / mImgWidth;
            matrix.postScale(xScale, xScale);
            matrix.postTranslate(0, 0);
            this.setImageMatrix(matrix);
        }
        super.setImageBitmap(bm);
    }

    public static void saveBitmap(final File targetFile, final String url, final UICallBack<Object> listener) {
        new SimpleAsyncTask<Void, Void, Bitmap>() {
            Exception mEx;

            @Override
            protected Bitmap doInBackground(Void... params) {
                // TODO Auto-generated method stub
                InputStream input = null;
                OutputStream fout = null;
                try {
                    input = new URL(url).openStream();
                    File image = null;
                    if (targetFile != null) {
                        image = targetFile;
                    } else {
                        image = new File(Constants.CACHE_PATH, String.valueOf(url.hashCode()));
                    }
                    if (!image.exists()) { // 不存在则保存
                        image.createNewFile();
                        fout = new BufferedOutputStream(new FileOutputStream(image));
                        byte[] buf1 = new byte[4098];
                        int len1 = 0;
                        while ((len1 = input.read(buf1)) != -1) {
                            fout.write(buf1, 0, len1);
                        }
                        fout.flush();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    mEx = e;
                } finally {
                    try {
                        if (fout != null) {
                            fout.close();
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        mEx = e;
                    }
                    try {
                        if (input != null) {
                            input.close();
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        mEx = e;
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (mEx != null) {
                    if (listener != null) {
                        listener.onFailed(mEx);
                    }
                    return;
                }
                if (listener != null) {
                    listener.onCompleted(new Object());
                }

            }

        }.execute();
    }

    public static String getBitmapFilePath(String url) {
        File image = new File(Constants.CACHE_PATH, String.valueOf(url.hashCode()));
        return image.getAbsolutePath();
    }


}
