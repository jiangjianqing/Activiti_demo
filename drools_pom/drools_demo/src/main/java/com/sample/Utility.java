package com.sample;

import org.drools.core.spi.KnowledgeHelper;

/**
 * 实用工具类，知道哪些规则正在被触发或发射。
 *通过这种方法，可以检查所有的规则都在Drools项目得到触发
 *特别注意，KnowledgeHelper可以用来在代码与rule之间进行interact operation
 * @author ztxs
 *
 */
public class Utility {
	
	/**
	 * 第一种方法帮助打印规则一起，可以通过为String通过DRL文件中的一些额外的信息触发。
	 * @param drools
	 * @param message
	 */
	public static void help(final KnowledgeHelper drools, final String message) {
		System.out.println("Utility.receive:"+message);
		System.out.println("rule triggered: " + drools.getRule().getName());
	}

	/**
	 * 第二条规则助手打印特定的规则是否被触发。
	 * @param drools
	 */
	public static void helper(final KnowledgeHelper drools) {
		System.out.println("rule triggered: " + drools.getRule().getName());
	}
}
