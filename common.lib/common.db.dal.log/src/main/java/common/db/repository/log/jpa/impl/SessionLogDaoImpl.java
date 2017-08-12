package common.db.repository.log.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;

import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.jpa.AbstractJpaDaoImpl;
import common.db.model.log.SessionLog;
import common.db.repository.log.SessionLogDao;

public class SessionLogDaoImpl extends AbstractJpaDaoImpl<SessionLog,Long> implements SessionLogDao {

}
