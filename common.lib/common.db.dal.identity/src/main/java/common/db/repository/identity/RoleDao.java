package common.db.service.identity;

import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.jpa.sample.SimpleJpaDao;
import common.db.base.page.PageObject;
import common.db.model.identity.Role;

public interface RoleDao extends SimpleJpaDao<Role>{
	PageObject<Role> getList(int currPage) throws OutOfPageRangeException, DaoException;

	PageObject<Role> getList(int currPage, int pageSize) throws OutOfPageRangeException, DaoException;
}

