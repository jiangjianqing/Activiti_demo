package common.db.repository.mybatis.identity;

import common.db.base.AbstractDao;
import common.db.base.jpa.AbstractJpaDao;
import common.db.model.identity.Role;

//20170808 使用通用Mapper，但尚未解决1对多问题,其只能用于单表
public interface RoleDao extends /*Mapper<Role>,*/AbstractDao<Role,Long>{
	int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}