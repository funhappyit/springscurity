package org.cos.security1.config.oauth;

import org.cos.security1.config.auth.PrincipalDetails;
import org.cos.security1.model.User;
import org.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	//구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수 
	//함수종료시@AuthenticationPrincipal어노테이션이 만들어진다.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("userRequest:"+userRequest.getClientRegistration());//registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
		System.out.println("userRequest:"+userRequest.getAccessToken().getTokenValue());
		//구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리)-> AccessToken요청
		//userRequest정보->loadUser함수 호출 -> 구글로부터 회원프로필을 받아준다.
		System.out.println("userRequest:"+super.loadUser(userRequest).getAttributes());
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		System.out.println("userRequest:"+oauth2User.getAttributes());
		
		String provider = userRequest.getClientRegistration().getClientId();//GOOGLE
		String providerId = oauth2User.getAttribute("sub");
		String username = provider+"_"+providerId;
		String password = bCryptPasswordEncoder.encode("겟인데어");
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		if(userEntity == null) {
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}
		//회원가입을 강제로 진행해볼 예정
		return new PrincipalDetails(userEntity,oauth2User.getAttributes());
	}
	
}
