package common.dao;

import common.dao.GenericHibernateDaoImpl;

import common.dao.UserDAO;
import common.dao.User;

public class UserDAOImpl extends GenericHibernateDaoImpl<User,Integer> implements 
UserDAO{
	
	@Override
	public void add() {
		// TODO Auto-generated method stub
		User newuser=new User();
		newuser.setName("abc");
		System.out.println("输出代码");
		this.save(newuser);
		//dao.save(newuser);
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

}
