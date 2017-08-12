package common.db.repository.log.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;

import common.db.base.exception.DaoException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.jpa.AbstractJpaDaoImpl;
import common.db.model.log.ModuleLog;
import common.db.repository.log.ModuleLogDao;

public class ModuleLogDaoImpl extends AbstractJpaDaoImpl<ModuleLog,Long> implements ModuleLogDao {

}
