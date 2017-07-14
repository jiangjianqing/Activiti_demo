package common.db.service.identity;

import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.jpa.sample.SimpleJpaDao;
import common.db.base.page.PageObject;
import common.db.model.identity.User;

public interface UserDAO extends SimpleJpaDao<User> {
	User findByName(String userName) throws DaoException;
	PageObject<User> getList(int currPage) throws OutOfPageRangeException, DaoException;

	PageObject<User> getList(int currPage, int pageSize) throws OutOfPageRangeException, DaoException;
}

