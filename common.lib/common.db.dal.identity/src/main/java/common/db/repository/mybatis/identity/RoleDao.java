package common.db.repository.mybatis.identity;

import common.db.model.identity.Role;
import tk.mybatis.mapper.common.Mapper;

//20170808 使用通用Mapper，但尚未解决1对多问题
public interface RoleDao extends Mapper<Role>{
	int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}