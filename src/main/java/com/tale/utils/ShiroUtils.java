package com.tale.utils;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class ShiroUtils {
	private static Subject subject;
	/**
	 *  初始化shiro数据库配置
	 */
	public static void shiroUtils(){
		/*另一种自定义realm的方式：
		设置Realm
		DruidDataSource ds = new DruidDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/shiro");
		ds.setUsername("root");
		ds.setPassword("");

		JdbcRealm jdbcRealm = new JdbcRealm();
		jdbcRealm.setDataSource(ds);
		jdbcRealm.setPermissionsLookupEnabled(true);
		securityManager.setRealms(Arrays.asList((Realm) jdbcRealm));*/

		//1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<org.apache.shiro.mgt.SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:shiro-jdbc-realm.ini");

        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = (org.apache.shiro.mgt.SecurityManager) factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        
        //3.得到Subject
        subject = SecurityUtils.getSubject();
        
	}
	    /**
	     * 验证身份并保持登录状态
	     * @param username
	     * @param password
	     * @return
	     */
	    public static boolean login(String username,String password){
	    	
	    	//创建用户名/密码身份验证Token（即用户身份/凭证）
	        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
	        try {
	            //登录，即身份验证
	            subject.login(token);
	        } catch (AuthenticationException e) {
	            //身份验证失败
	        	return false;
	        }
	        //用户已经登录
			return true;
	    }
	    /**
	     * 验证角色权限
	     * @param role
	     * @return
	     */
	    public static boolean hasRole(String role){
	    	//判断用户是否拥有该角色权限 
		    return  subject.hasRole(role);
	    }
	    /**
	     * 退出登录状态
	     */
	    public static void logout(){
	    	//6、退出
	        subject.logout();
	    }
}
