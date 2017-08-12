package common.db.repository.identity;

import common.db.base.AbstractDao;

import common.db.model.identity.User;

public interface UserDAO extends AbstractDao<User,Long> {
	User findByUserName(String userName);
}

