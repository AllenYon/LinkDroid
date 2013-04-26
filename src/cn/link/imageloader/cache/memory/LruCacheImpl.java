package cn.link.imageloader.cache.memory;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-4-26
 * Time: PM2:08
 * To change this template use File | Settings | File Templates.
 */
public class LruCacheImpl implements MemoryCacheAware<String, Bitmap> {
    private final LinkedHashMap<String, Bitmap> map;

    private final int maxSize;
    /**
     * Size of this cache in bytes
     */
    private int size;

    /**
     * @param maxSize Maximum sum of the sizes of the Bitmaps in this cache
     */
    public LruCacheImpl(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<String, Bitmap>(0, 0.75f, true);
    }

    /**
     * Returns the Bitmap for {@code key} if it exists in the cache. If a Bitmap was returned, it is moved to the head
     * of the queue. This returns null if a Bitmap is not cached.
     */
    @Override
    public final Bitmap get(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        synchronized (this) {
            return map.get(key);
        }
    }

    /**
     * Caches {@code Bitmap} for {@code key}. The Bitmap is moved to the head of the queue.
     */
    @Override
    public final boolean put(String key, Bitmap value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        synchronized (this) {
            size += sizeOf(key, value);
            Bitmap previous = map.put(key, value);
            if (previous != null) {
                size -= sizeOf(key, previous);
            }
        }

        trimToSize(maxSize);
        return true;
    }

    /**
     * Remove the eldest entries until the total of remaining entries is at or below the requested size.
     *
     * @param maxSize the maximum size of the cache before returning. May be -1 to evict even 0-sized elements.
     */
    private void trimToSize(int maxSize) {
        while (true) {
            String key;
            Bitmap value;
            synchronized (this) {
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
                }

                if (size <= maxSize || map.isEmpty()) {
                    break;
                }

                Map.Entry<String, Bitmap> toEvict = map.entrySet().iterator().next();
                if (toEvict == null) {
                    break;
                }
                key = toEvict.getKey();
                value = toEvict.getValue();
                map.remove(key);
                size -= sizeOf(key, value);
            }
        }
    }

    /**
     * Removes the entry for {@code key} if it exists.
     */
    @Override
    public final void remove(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        synchronized (this) {
            Bitmap previous = map.remove(key);
            if (previous != null) {
                size -= sizeOf(key, previous);
            }
        }
    }

    @Override
    public Collection<String> keys() {
        return new HashSet<String>(map.keySet());
    }

    @Override
    public void clear() {
        trimToSize(-1); // -1 will evict 0-sized elements
    }

    /**
     * Returns the size {@code Bitmap} in bytes.
     * <p/>
     * An entry's size must not change while it is in the cache.
     */
    private int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public synchronized final String toString() {
        return String.format("LruCache[maxSize=%d]", maxSize);
    }

//    public static LruCache<String, Bitmap> mBitmapCache =
//            new LruCache<String, Bitmap>(MAX_MEM_CACHE_SIZE) {
//                protected int sizeOf(String key, Bitmap value) {
//                    int intValue = value.getRowBytes() * value.getHeight();
//                    return intValue;
//                }
//
//                @Override
//                protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
//                    // TODO Auto-generated method stub
//                    super.entryRemoved(evicted, key, oldValue, newValue);
//                    if (evicted && oldValue != null && !oldValue.isRecycled()) {
//                        // HBLog.i("1 btimap entryRemoved " + oldValue.getRowBytes());
//                        oldValue = null;
//                    }
//
//                }
//            };

//    public LruCacheImpl(int maxSize) {
//        super(maxSize);
//    }
//
//    @Override
//    protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {
//        if (evicted && oldValue != null && !((Bitmap) oldValue).isRecycled()) {
//            // HBLog.i("1 btimap entryRemoved " + oldValue.getRowBytes());
//            oldValue = null;
//        }
//    }
//
//    @Override
//    protected int sizeOf(K key, V value) {
//        int intValue = ((Bitmap) value).getRowBytes() * ((Bitmap) value).getHeight();
//        return intValue;
//    }
//
//    @Override
//    public boolean putM(K key, V value) {
//        put(key, value);
//        return true;
//    }
//
//    @Override
//    public V getM(K key) {
//        return get(key);
//    }
//
//    @Override
//    public void removeM(K key) {
//        //ToDo
//    }
//
//    @Override
//    public Collection<K> keysM() {
//        return null;  //ToDo
//    }
//
//    @Override
//    public void clearM() {
//        //ToDo
//    }


//    @Override
//    public boolean put(K key, V value) {
//        mBitmapCache.put((String) key, (Bitmap) value);
//        return true;  //ToDo
//    }
//
//    @Override
//    public V get(K key) {
//        return (Bitmap) (mBitmapCache.get((String) key));  //ToDo
//    }
//
//    @Override
//    public void remove(K key) {
//        //ToDo
//    }
//
//    @Override
//    public Collection<K> keys() {
//        return null;  //ToDo
//    }
//
//    @Override
//    public void clear() {
//        //ToDo
//    }

}
