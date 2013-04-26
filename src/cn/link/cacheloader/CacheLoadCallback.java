package cn.link.cacheloader;


public interface CacheLoadCallback<Result> {

	public void onPre(UIResult<Result> result);

	public void onPost(UIResult<Result> result, Result object);
}