package com.semihbkgr.springboottotp.config;

import com.semihbkgr.springboottotp.security.TotpAuthenticationFilter;
import com.semihbkgr.springboottotp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/signin")
                .loginProcessingUrl("/signin")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", RequestMethod.GET.name()))
                .logoutSuccessUrl("/signin?logout")
                .and()
                .authorizeRequests()
                .antMatchers("/signin")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterAfter(totpAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public TotpAuthenticationFilter totpAuthenticationFilter() {
        return new TotpAuthenticationFilter("/totp", "/", userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
