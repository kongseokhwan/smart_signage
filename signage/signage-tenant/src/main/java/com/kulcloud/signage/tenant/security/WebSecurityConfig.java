package com.kulcloud.signage.tenant.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.kulcloud.signage.commons.security.CustomRequestCache;
import com.kulcloud.signage.commons.security.SecurityUtils;
import com.kulcloud.signage.commons.utils.CommonConstants;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;
	
	@Value("${com.kulcloud.security.enable:true}")
	private boolean enable;
	
	@Value("${com.kulcloud.security.ui:true}")
	private boolean ui;
	
	@Autowired
	public WebSecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return SecurityUtils.pwdEncoder;
	}
	
	/**
	 * 로그인 처리를 위한 UserDetailsService와 PasswordEncoder 등록
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(userDetailsService).passwordEncoder(SecurityUtils.pwdEncoder);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		if(enable) {
			// Register our CustomRequestCache, that saves unauthorized access attempts, so
			// the user is redirected after login.
			http.requestCache().requestCache(new CustomRequestCache())

			// Restrict access to our application.
			.and().authorizeRequests()

			// Allow all flow internal requests.
			.requestMatchers(request -> isPermitAllRequest(request), SecurityUtils::isVaadinInternalRequest).permitAll()

			// Allow all requests by logged in users.
//			.anyRequest().hasAnyAuthority(userService.getAllRoleIds())

			// Configure the login page.
			.and().formLogin().loginPage(CommonConstants.LOGIN_URL).permitAll().loginProcessingUrl(CommonConstants.LOGIN_PROCESSING_URL)
			.failureUrl(CommonConstants.LOGIN_FAILURE_URL)

			// Register the success handler that redirects users to the page they last tried
			// to access
			.successHandler(new SavedRequestAwareAuthenticationSuccessHandler())

			// Configure logout
			.and().logout().logoutSuccessUrl(CommonConstants.LOGOUT_SUCCESS_URL);
		} else {
			http.authorizeRequests().antMatchers("/**").anonymous();
		}
	}
	
	boolean isPermitAllRequest(HttpServletRequest request) {
		return StringUtils.containsAny(request.getPathInfo(), "/cast", "/guacamole", "/tunnel");
	} 
}
