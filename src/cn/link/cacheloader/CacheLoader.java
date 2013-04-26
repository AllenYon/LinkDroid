package cn.link.cacheloader;

import cn.link.AppContext;
import cn.link.core.SimpleAsyncTask;

/**
 * @author link
 */
public class CacheLoader {
    public static final int NULL = 0;
    public static final int FILE = 1;
    public static final int NETWORK = 2;
    public static final int MEMORY = 3;
    public static final int START = 10;
    public static final int FIRST = 11;
    public static final int SECOUND = 12;

    private AppContext mAppContext;

    public CacheLoader(AppContext app) {
        mAppContext = app;
    }

    public <Result> void execute(final ICacheLoader mLoader,
                                 final CacheLoadCallback<Result> callBack) {
        new LoadTask<Result>(mLoader, callBack, false, false).execute();
    }

    public <Result> void executeTwice(final ICacheLoader mLoader,
                                      final CacheLoadCallback<Result> callBack) {
        new LoadTask<Result>(mLoader, callBack, false, true).execute();
    }

    private int getSrcType(String fileName) {
        boolean fileExist = mAppContext.fileExist(fileName);
        int srcType;
        if (fileExist) {
            srcType = FILE;
        } else {
            srcType = NETWORK;
        }
        return srcType;
    }

    public class LoadTask<Result> extends SimpleAsyncTask<Void, Void, Result> {
        ICacheLoader mLoader;
        UIResult<Result> mAResult;
        CacheLoadCallback<Result> mCallBack;
        boolean isNetwork;
        boolean runSecond;

        public LoadTask(ICacheLoader mLoader, CacheLoadCallback<Result> callBack, boolean cache, boolean runSecond) {
            this.mAResult = new UIResult<Result>();
            this.mLoader = mLoader;
            this.mCallBack = callBack;
            this.isNetwork = cache;
            this.runSecond = runSecond;

        }

        public LoadTask(UIResult<Result> aresult, ICacheLoader mLoader, CacheLoadCallback<Result> callBack, boolean cache,
                        boolean runSecond) {
            this.mAResult = aresult;
            this.mLoader = mLoader;
            this.mCallBack = callBack;
            this.isNetwork = cache;
            this.runSecond = runSecond;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            int stype = getSrcType(mLoader.getFileName(mAppContext));
            mAResult.setSourceType(stype);
            mAResult.nextTimeType();
            mCallBack.onPre(mAResult);
            mAResult.toString();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Result doInBackground(Void... params) {
            Result result = null;

            try {
                if (isNetwork) {
                    // run network;
                    result = (Result) mLoader.getCurrent(mAppContext);
                    mAResult.setSourceType(NETWORK);
                } else {
                    // memory --> file -- net work;
                    result = (Result) mLoader.getCacheFromMemory(mAppContext);
                    mAResult.setSourceType(MEMORY);
                    if (result == null) {
                        // file
                        result = (Result) mLoader.getCacheFromDisk(mAppContext);
                        // result = (Result)
                        mAResult.setSourceType(FILE);
                        if (result == null) {
                            // From Network
                            result = (Result) mLoader.getCurrent(mAppContext);
                            mAResult.setSourceType(NETWORK);
                            runSecond = false;
                        }
                    }
                }
                int addDataCount = mLoader.getAddedDataCount();
                mAResult.setAddedDataCount(addDataCount);
                //询问是否写入磁盘
                if (mLoader.writeToDisk()) {
                    mLoader.onWriteToDisk(mAppContext, result, mAResult.getSourceType());
                }
                //询问是否添加内存缓存
                if (mLoader.setToMemory()) {
                    mLoader.onSetToMemory(mAppContext, result, mAResult.getSourceType());
                }
                mAResult.setObject(result);
            } catch (Throwable e1) {
                mAResult.setEx(e1);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Result result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mCallBack.onPost(mAResult, mAResult.getObject());
            if (runSecond) {
                // 需要 第二次下载 并且 回调函数不可空 （即指定为需要二次下载）
                runSecond = false;
                new NetworkTask<Result>(mAResult, mLoader, mCallBack).execute();
            }
        }
    }

    public class NetworkTask<Result> extends SimpleAsyncTask<Void, Void, Result> {
        ICacheLoader mLoader;
        UIResult<Result> mAResult;
        CacheLoadCallback<Result> mCallBack;

        public NetworkTask(UIResult<Result> aresult, ICacheLoader mLoader, CacheLoadCallback<Result> callBack) {
            this.mAResult = aresult;
            this.mLoader = mLoader;
            this.mCallBack = callBack;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mAResult.setSourceType(NETWORK);
            mAResult.nextTimeType();
            mCallBack.onPre(mAResult);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Result doInBackground(Void... params) {
            Result result = null;
            try {
                // run network;
                result = (Result) mLoader.getCurrent(mAppContext);
                mAResult.setSourceType(NETWORK);
                //询问是否写入磁盘
                if (mLoader.writeToDisk()) {
                    mLoader.onWriteToDisk(mAppContext, result, NETWORK);
                }
                //询问是否添加内存缓存
                if (mLoader.setToMemory()) {
                    mLoader.onSetToMemory(mAppContext, result, NETWORK);
                }
                int addDataCount = mLoader.getAddedDataCount();
                mAResult.setAddedDataCount(addDataCount);
                mAResult.setObject(result);
            } catch (Throwable e1) {
                mAResult.setEx(e1);

            }
            return result;
        }

        @Override
        protected void onPostExecute(Result result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mCallBack.onPost(mAResult, mAResult.getObject());
            mAResult.toString();
        }
    }

    // -------------------------------------------------------------------------------------------//
    public <Result> void getCurrent(final ICacheLoader mLoader, final CacheLoadCallback<Result> callBack) {
        new ListTask<Result>(mLoader, callBack).execute(0);
    }

    public <Result> void getNewer(final ICacheLoader mLoader, final CacheLoadCallback<Result> callBack) {
        new ListTask<Result>(mLoader, callBack).execute(1);
    }

    public <Result> void getOlder(final ICacheLoader mLoader, final CacheLoadCallback<Result> callBack) {
        new ListTask<Result>(mLoader, callBack).execute(2);
    }

    private class ListTask<Result> extends SimpleAsyncTask<Integer, Void, Result> {
        ICacheLoader mLoader;
        UIResult<Result> mAResult;
        CacheLoadCallback<Result> mCallBack;

        public ListTask(ICacheLoader mLoader, CacheLoadCallback<Result> callBack) {
            this.mAResult = new UIResult<Result>();
            this.mLoader = mLoader;
            this.mCallBack = callBack;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAResult.setSourceType(NETWORK);
            mCallBack.onPre(mAResult);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Result doInBackground(Integer... params) {
            int type = params[0];
            Result result = null;
            try {
                switch (type) {
                    case 0:
                        result = (Result) mLoader.getCurrent(mAppContext);
                        break;
                    case 1:
                        result = (Result) mLoader.getNewer(mAppContext);
                        break;
                    case 2:
                        result = (Result) mLoader.getOlder(mAppContext);
                        break;
                    default:
                        result = (Result) mLoader.getCurrent(mAppContext);
                        break;
                }
                //询问是否写入磁盘
                if (mLoader.writeToDisk()) {
                    mLoader.onWriteToDisk(mAppContext, result, NETWORK);
                }
                //询问是否添加内存缓存
                if (mLoader.setToMemory()) {
                    mLoader.onSetToMemory(mAppContext, result, NETWORK);
                }
                int addedDataCount = mLoader.getAddedDataCount();
                mAResult.setAddedDataCount(addedDataCount);
                mAResult.setObject(result);
            } catch (Throwable e) {
                e.printStackTrace();
                mAResult.setEx(e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Result result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mCallBack.onPost(mAResult, mAResult.getObject());
        }
    }


}
