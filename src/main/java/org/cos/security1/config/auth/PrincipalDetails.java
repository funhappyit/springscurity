package org.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/* 
 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
 로그인을 진행이 완료가 되면 시큐리티 session을 만들어줍니다.(Security ContextHolder)
 오브젝트 => Authentication 타입 객체 
 Authentication 안에 User정보가 있어야 됨.
 User오브젝트타입 => UserDetails 타입 객체 
 
 Security Session => Authentication => UserDetails(PrincipalDetails)
 
*/
public class PrincipalDetails implements UserDetails{

	private User user;//콤포지션 
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//해당 유저의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		// arrayList는 collection의 자식 
		Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
			
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {
		//우리 사이트!! 1년동안 회원이 로그인을 안하면!! 휴먼 계정으로 하게 됨
		//현재시간 - 로그인시간 -> 1년이 넘으면 false
		return true;
	}
	

}
