package common.dao;

import common.dao.User;

import common.dao.GenericHibernateDao;

public interface UserDAO extends GenericHibernateDao <User,Integer> {
	public void add();
	public void delete();
}
