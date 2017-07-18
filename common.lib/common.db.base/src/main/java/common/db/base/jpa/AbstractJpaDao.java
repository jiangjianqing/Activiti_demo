package common.db.base.jpa;


import java.util.List;

import javax.persistence.EntityManager;

import common.db.base.AbstractDao;
import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.page.PageObject;

public interface AbstractJpaDao<T> extends AbstractDao<T>{
	void setEntityManager(EntityManager em);
}
