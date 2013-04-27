LinkDroid
=========

<h3>一个图片类应用框架，帮助开发者解决Bitmap相关的问题（如OOM，二级缓存，手势裁剪，显示等），快速开发应用。<br></h3>
主要包含以下几个模块：<br>
1.<a href="https://github.com/nostra13/Android-Universal-Image-loader" >Android-Universal-Image-Loader</a> 图片加载器 ，解决oom，二级缓存等<br>
  1.1. 将原有的并发请求统一在SimpleAsyncTask类中处理。（原因稍后解释）<br>
  1.2. 不再使用ImageLoader类来加载图片，改用 LKImageView extends ImageButton 加载。<br>
  1.3. 简化Configuration 和 DisplayImageOptions ，删除某些不必要的参数<br>
  1.4. 新增BitmapProgressListener 接口，处理显示进度问题<br>
  1.5. 新增点击效果<br>
  
2.<a href="https://github.com/loopj/android-async-http">Android-async-http</a> 异步HttpClient ，同时支持同步请求<br>
  2.1. 将原有的并发请求统一在 SimpleAsyncTask类中处理<br>
  2.2. 重写异步回调函数处理流程，和同步返回结果的包装<br>

3.CacheLoader 数据缓存层 使用内存/文件缓存方式加快应用加载速度，可使用后台刷新机制，刷新缓存并更新界面数据<br>
4.PhotoView  图片展示控件 未修改<br>
5.SimpleAsyncTask类,统一的并发处理线程池<br>
6.LasyAdapterView 对在AdapterView中加载大量图片的显示策略做细化，提升体验<br>

如果你有任何相关这个项目的问题的话，联系我：<br>
微博：@Link颜<br>
Email:wsyanligang@gmail.com<br>

<h2>License</h2>
<pre>
<code>
Copyright 2011-2013 Sergey Tarasevich

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
</code>
</pre>

  

