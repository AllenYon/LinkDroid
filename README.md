LinkDroid
=========

一个图片类应用框架，帮助开发者解决Bitmap相关的问题（如OOM，二级缓存，手势裁剪，显示等），快速开发应用。<br>
主要包含以下几个模块：<br>
1.Android-Universal-Image-Loader 图片加载器 ，解决oom，二级缓存等<br>
  https://github.com/nostra13/Android-Universal-Image-Loader<br>
  1.1. 将原有的并发请求统一在SimpleAsyncTask类(AsyncTask的子类)中处理。（原因稍后解释）<br>
  1.2. 不再使用ImageLoader类来加载图片，改用 LKImageView extends ImageButton 加载。
  1.3. 简化Configuration 和 DisplayImageOptions ，删除某些不必要的参数
  1.4. 新增BitmapProgressListener 接口，处理显示进度问题
  1.5. 新增点击效果
  
2.Android-async-http 异步HttpClient ，同时支持同步请求
  https://github.com/loopj/android-async-http?source=cr
  2.1. 将原有的并发请求统一在 SimpleAsyncTask类中处理
  2.2. 重写异步回调函数处理流程，和同步返回结果的包装
  

