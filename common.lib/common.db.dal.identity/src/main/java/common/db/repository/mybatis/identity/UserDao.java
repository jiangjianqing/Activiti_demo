package common.db.repository.mybatis.identity;

import common.db.model.identity.User;

public interface UserDao {
    int insert(User record);

    int insertSelective(User record);
}