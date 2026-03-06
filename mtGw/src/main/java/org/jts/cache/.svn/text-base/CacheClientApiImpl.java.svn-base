package org.jts.cache;

import org.jts.cache.local.PbCache;
import org.jts.cache.resource.impl.NewCodeCacheLoader;

import java.util.List;
import java.util.function.Supplier;


/**
* 缓存使用实现
* @version V2.0
*/
public class CacheClientApiImpl implements CacheClientApi {

	private final Supplier<PbCache> pbCacheSupplier;

	public CacheClientApiImpl(Supplier<PbCache> pbCacheSupplier) {
        this.pbCacheSupplier = pbCacheSupplier;
	}

	public List<String> getAllNewCodeList(){
		return cache().get(NewCodeCacheLoader.RESOURCE_NAME, NewCodeCacheLoader.KEY,List.class);
	}

	private PbCache cache(){
		return pbCacheSupplier.get();
	}
}
