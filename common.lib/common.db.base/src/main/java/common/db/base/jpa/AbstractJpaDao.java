package common.db.base.jpa;


import java.util.List;

import javax.persistence.EntityManager;

import common.db.base.AbstractDao;
import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;

public interface AbstractJpaDao<T,K> extends AbstractDao<T,K>{
	void setEntityManager(EntityManager em);
}
