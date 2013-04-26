LinkDroid
=========

一个图片类应用框架，帮助开发者解决Bitmap相关的问题（如OOM，二级缓存，手势裁剪，显示等），快速开发应用。<br>
主要包含以下几个模块：<br>
1.<a href="https://github.com/nostra13/Android-Universal-Image_loader" >Android-Universal-Image-Loader</a> 图片加载器 ，解决oom，二级缓存等<br>
  https://github.com/nostra13/Android-Universal-Image-Loader<br>
  1.1. 将原有的并发请求统一在SimpleAsyncTask类(AsyncTask的子类)中处理。（原因稍后解释）<br>
  1.2. 不再使用ImageLoader类来加载图片，改用 LKImageView extends ImageButton 加载。<br>
  1.3. 简化Configuration 和 DisplayImageOptions ，删除某些不必要的参数<br>
  1.4. 新增BitmapProgressListener 接口，处理显示进度问题<br>
  1.5. 新增点击效果<br>
  
2.Android-async-http 异步HttpClient ，同时支持同步请求<br>
  https://github.com/loopj/android-async-http?source=cr<br>
  2.1. 将原有的并发请求统一在 SimpleAsyncTask类中处理<br>
  2.2. 重写异步回调函数处理流程，和同步返回结果的包装<br>

3.CacheLoader 数据缓存层 使用内存/文件缓存方式加快应用加载速度，可使用后台刷新机制，刷新缓存并更新界面数据<br>
4.PhotoView 
  

