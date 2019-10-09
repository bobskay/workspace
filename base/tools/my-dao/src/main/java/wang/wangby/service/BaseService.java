package wang.wangby.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import wang.wangby.dao.BaseDao;
import wang.wangby.model.dao.BaseModel;
import wang.wangby.model.dao.Pagination;
import wang.wangby.utils.CollectionUtil;
import wang.wangby.utils.IdWorker;

public class BaseService {
	protected  Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IdWorker idWorker;

	public Long newId() {
		return idWorker.nextId();
	}

	public Pagination selectPage(BaseModel model,BaseDao baseDao, Integer offset, Integer limit) {
		if (offset == null) {
			offset = 0;
		}
		if (limit == null) {
			limit = Pagination.DEFAULT_SIZE;
		}
		long count = baseDao.getCount(model);
		if (offset == null || offset > count) {
			return new Pagination(count, new ArrayList(), offset, limit);
		}
		model.getExt().put("offset", offset);
		model.getExt().put("limit", limit);
		List list = baseDao.select(model);
		return new Pagination(count, list, offset, limit);
	}


}
