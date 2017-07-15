package common.db.service.identity.impl;

import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.jpa.BaseDaoImpl;
import common.db.base.jpa.sample.SimpleJpaDaoImpl;
import common.db.base.page.PageObject;
import common.db.model.identity.Role;
import common.db.service.identity.RoleDao;

public class RoleDaoImpl extends SimpleJpaDaoImpl<Role> implements RoleDao {

		protected class GenericBaseDaoImpl extends BaseDaoImpl<Role> {
		};

		public RoleDaoImpl(){
			baseDao=new GenericBaseDaoImpl();
		}

		public PageObject<Role> getList(int currPage) throws OutOfPageRangeException, DaoException {
			return paginationDao.queryForPaginationList(currPage, Role.class);
		}

		public PageObject<Role> getList(int currPage, int pageSize) throws OutOfPageRangeException, DaoException {
			return paginationDao.queryForPaginationList(currPage, pageSize, Role.class);
		}
}

