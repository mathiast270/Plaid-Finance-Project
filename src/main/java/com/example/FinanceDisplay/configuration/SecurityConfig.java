package com.example.FinanceDisplay.configuration;


import com.example.FinanceDisplay.Filter.CustomAuthenticationFIlter;
import com.example.FinanceDisplay.Filter.CustomAuthorizationFilter;
import com.example.FinanceDisplay.model.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.FinanceDisplay.FinanceDisplayApplication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import retrofit2.http.GET;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private final UserDetailsService userDetailsService;
    @Autowired
    private final BCryptPasswordEncoder encoder;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/v1/user/getall/**").hasAnyAuthority(Roles.ADMIN.getKey());
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/v1/user/createuser/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"/api/v1/user/updateusers/**").hasAnyAuthority(Roles.ADMIN.getKey());
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"/api/v1/user/updateusers/**").hasAnyAuthority(Roles.ADMIN.getKey());
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/v1/user/create_link_token/**").hasAnyAuthority(Roles.ADMIN.getKey());
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/v1/user/exchange_public_token/**").hasAnyAuthority(Roles.ADMIN.getKey());

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new CustomAuthenticationFIlter(authenticationManager(userDetailsService)));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider dao= new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService);
        dao.setPasswordEncoder(encoder);
        return new ProviderManager(dao);
    }

}
