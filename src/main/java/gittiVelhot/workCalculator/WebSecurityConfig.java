package gittiVelhot.workCalculator;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import gittiVelhot.workCalculator.web.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

	@Autowired
	private UserDetailService userDetailsService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests
				.requestMatchers(antMatcher("/css/**")).permitAll() 
				.requestMatchers(antMatcher("/signup")).permitAll()
				.requestMatchers(antMatcher("/saveuser")).permitAll()
				.anyRequest().authenticated())
			.formLogin((form) -> form
				.loginPage("/login")
				.defaultSuccessUrl("https://work-calculator-back-fe87ca711a8e.herokuapp.com/home", true)
				.permitAll())
			.logout((logout) -> logout
				.permitAll())
			.csrf(csrf -> csrf.disable());

		return http.build();
	}
}
