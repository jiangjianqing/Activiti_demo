package common.db.repository.jpa.identity;

import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.jpa.AbstractJpaDao;
import common.db.base.page.PageObject;
import common.db.model.identity.User;

public interface UserDAO extends AbstractJpaDao<User,Long> {
	User findByUserName(String userName) throws DaoException;
}

