package org.asu.chilll.power.config;

import org.asu.chilll.power.enums.UserType;
import org.asu.chilll.power.service.security.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private AppUserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception{
		//super.configure(web);
		web.ignoring()
			.antMatchers("/*.js", "/*.css", "/assets/images/login/**", "/assets/images/logos/**", "/fontawesome-webfont.*");
	}
	
	@Value("${power.security.enable}")
	private Boolean securityEnable;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		if(securityEnable != null && securityEnable) {
			http.httpBasic()
			.and()
			.cors()
			.and()
			.authorizeRequests()
			.antMatchers("/api/login", "/login").permitAll()
			.antMatchers("/admin/*").hasAnyAuthority(UserType.ADMIN.toString())
			.anyRequest().fullyAuthenticated()
			.and()
			.logout().permitAll()
			.logoutRequestMatcher(new AntPathRequestMatcher("/api/logout", "POST"))
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.and()
			.csrf().disable();
		}else {
			http
			.csrf().disable()
			.authorizeRequests().antMatchers("/**").permitAll();
		}
	}
}