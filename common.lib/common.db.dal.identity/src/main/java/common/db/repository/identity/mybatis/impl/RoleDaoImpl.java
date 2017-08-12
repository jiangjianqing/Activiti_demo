package common.db.repository.identity.mybatis.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import com.github.pagehelper.PageInfo;

import common.db.model.identity.Role;
import common.db.repository.identity.RoleDao;

//20170808 使用通用Mapper，但尚未解决1对多问题,其只能用于单表
public class RoleDaoImpl implements RoleDao {

	@Resource
	private SqlSessionFactory sessionFactory;
	
	//@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public SqlSessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SqlSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public int insert(Role record) {
		//return sqlSessionTemplate.insert("insert", record);
		try(SqlSession session=sessionFactory.openSession()){
			//session.getMapper获取的是一个代理对象
			RoleDao roleDao=session.getMapper(RoleDao.class);

			return roleDao.insert(record);
			}
	}

	public int insertSelective(Role record) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int updateByPrimaryKeySelective(Role record) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int updateByPrimaryKey(Role record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Role> getList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageInfo<Role> getPageList(int currPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageInfo<Role> getPageList(int currPage, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Role selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
