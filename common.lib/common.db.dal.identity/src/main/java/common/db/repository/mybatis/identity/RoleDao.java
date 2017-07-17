package common.db.repository.mybatis.identity;

import common.db.model.identity.Role;

public interface RoleDao {
    int insert(Role record);

    int insertSelective(Role record);
}