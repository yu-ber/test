package cn.youyu.logistics.shiro;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import cn.youyu.logistics.pojo.User;
import cn.youyu.logistics.service.PermissionService;
import cn.youyu.logistics.service.UserService;

public class CustomRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;
	
	@Autowired
	private PermissionService permissionService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("授权++");
		// 获取主身份
		User user = (User) principals.getPrimaryPrincipal();
		System.out.println("user:" + user);
		String permissionIds = user.getPermissionIds();
		System.out.println("permissionIds:" + permissionIds);
		if(StringUtils.isNotBlank(permissionIds)) {
			
			String[] split = permissionIds.split(",");
			//将字符串类型的数组装换成Long类型
			List<Long> permissionList = new ArrayList<>();
			for (String permission : split) {
				permissionList.add(Long.valueOf(permission));
			}
			//根据每一个权限ID值获取对应的权限
			List<String> permissionExpressions = permissionService.selectPermissionByIds(permissionList);
			System.out.println("permissionExpressions:"+permissionExpressions);
			
			SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
			authorizationInfo.addStringPermissions(permissionExpressions);
			return authorizationInfo;
		}
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("认证++");
		String username = (String) token.getPrincipal();
		User user = userService.selectUserByUsername(username);
		System.out.println(user);
		if (user == null) {
			return null;
		}

		String password = user.getPassword();
		// SimpleAuthenticationInfo authenticationInfo = new
		// SimpleAuthenticationInfo(user, password, this.getName());
		String salt = user.getSalt();

		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, password,
				ByteSource.Util.bytes(salt), this.getName());
		return authenticationInfo;
	}

}
