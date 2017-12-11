package com.vladimir.zubencko;

import com.vladimir.zubencko.domain.User;
import com.vladimir.zubencko.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@Log4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/admin/**").access("hasRole('ADMIN')")
                .antMatchers("/home").permitAll()
                .and().formLogin().loginPage("/login").permitAll()
                .usernameParameter("login")
                .defaultSuccessUrl("/admin/home", false).and().logout().logoutUrl("/logout").logoutSuccessUrl("/home");
        http.sessionManagement().maximumSessions(100).sessionRegistry(sessionRegistry());
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            private final Md5PasswordEncoder md5 = new Md5PasswordEncoder();

            @Override
            public String encode(CharSequence cs) {
                return md5.encodePassword(cs.toString(), 1);
            }

            @Override
            @SuppressWarnings("PMD")
            public boolean matches(CharSequence cs, String string) {
                System.out.println("pass1 cs == " + md5.encodePassword(cs.toString(), 1));
                System.out.println("pass2 str == " + string);
                return (md5.encodePassword(cs.toString(), 1)).equals(string);
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService());
        return authProvider;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
                System.out.println("UserDETAILS");
                User user = null;
                try {
                    user = userService.getByLogin(login);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    log.error("User not found: can't find user: login" + login);
                    throw new UsernameNotFoundException("can't find user@login" + login);
                }
                if (user == null) {
                    log.error("User not found: can't find user: login = " + login);
                    throw new UsernameNotFoundException("can't find user@ulogin" + login);
                }
                return (UserDetails) user;
            }
        };
    }

    @Bean
    public AuthenticationDetailsSource authenticationDetailsSource() {
        return new WebAuthenticationDetailsSource() {
            public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
                return new WebAuthenticationDetails(context);
            }
        };

    }
}
