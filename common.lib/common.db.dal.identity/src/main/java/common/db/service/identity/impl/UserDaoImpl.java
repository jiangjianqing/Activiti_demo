package common.db.service.identity.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import common.db.base.exception.DaoException;
import common.db.base.exception.NoFieldChangedException;
import common.db.base.exception.OutOfPageRangeException;
import common.db.base.jpa.BaseDaoImpl;
import common.db.base.jpa.EntityBeanUtil;
import common.db.base.jpa.JpaUtil;
import common.db.base.jpa.PaginationJpaDao;
import common.db.base.jpa.sample.SimpleJpaDaoImpl;
import common.db.base.page.PageObject;
import common.db.model.identity.Role;
import common.db.model.identity.User;
import common.db.service.identity.UserDAO;

public class UserDaoImpl extends SimpleJpaDaoImpl<User> implements UserDAO {

	protected class GenericBaseDaoImpl extends BaseDaoImpl<User> {
	};

	public UserDaoImpl(){
		baseDao=new GenericBaseDaoImpl();
	}

	public User findByName(String userName) throws DaoException{
		;
		//String jpql=String.format("select o from %s o where o.username=?",User.class.getSimpleName());
		//List<User> userList=paginationDao.queryForList(jpql,new Object[]{userName});
		Map<String,Object> sMap = new HashMap<String, Object>();  
		sMap.put("name", userName);  
		List<User> userList=paginationDao.queryForList(baseDao.createNamedQuery("User.findByName"),sMap);
		User ret=null;
		if (userList.size()!=1){
			if(userList.size()>1){
				throw new DaoException("CustomException","username存在重复");

			}
		}else{
			ret=userList.get(0);
		}
		return ret;
	}

	public PageObject<User> getList(int currPage) throws OutOfPageRangeException, DaoException {
		return paginationDao.queryForPaginationList(currPage, User.class);
	}

	public PageObject<User> getList(int currPage, int pageSize) throws OutOfPageRangeException, DaoException {
		return paginationDao.queryForPaginationList(currPage, pageSize, User.class);
	}

	private <T> boolean isArrayEqual(List<T> list,List<T> list2){
		return isArrayEqual(list.toArray(),list2.toArray());
	}

	private boolean isArrayEqual(Object[] objArray1,Object[] objArray2){
		if(objArray1==null && objArray2!=null)
			return false;
		if(objArray2==null && objArray1!=null)
			return false;
		if(objArray1.length!=objArray2.length)
			return false;

		boolean ret=true;
		for(Object obj:objArray1){
			for(Object obj2:objArray2){
				if (!obj.equals(obj2)){
					ret=false;
					break;
				}
			}
			if(ret==false)
				break;
		}
		return ret;
	}

	@Override
	public User update(User user) throws DaoException, NoFieldChangedException {
		User ret=baseDao.findByKey(user.getId());
		if(ret==null){
			return null;
		}
		List<String> diffFields=ret.getDifferentFields(user);
		diffFields.remove("password");
		diffFields.remove("username");
		boolean testResult=isArrayEqual(ret.getRoles().toArray(),user.getRoles().toArray());
		System.out.println("isArrayEqual="+testResult);
		if(diffFields.size()>0 || !testResult){
			if(diffFields.size()>0){
				String[] array=new String[diffFields.size()];
				diffFields.toArray(array);
				ret.copyAttributeValue(user, array);
			}			

			if(!testResult){
				if(user.getRoles().size()>0){

					Map<String, Object> params = new HashMap<String, Object>();
					params.put("ids", user.getRoles());
					String jqhl =JpaUtil.buildSelect(Role.class, null, "o");
					jqhl+= "where id in(:ids)";
					List<Role> roles=this.paginationDao.queryForList(jqhl, params);

					ret.setRoles(roles);
				}else{
					ret.getRoles().clear();
				}
			}

			baseDao.merge(ret);
		}else{
			throw new NoFieldChangedException("User",String.format("id=%s ,no field changed",user.getId()));
		}
		return ret;
	}

}

