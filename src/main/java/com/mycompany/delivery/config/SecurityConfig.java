package com.mycompany.delivery.config;

import com.mycompany.delivery.entity.Role;
import com.mycompany.delivery.entity.Route;
import com.mycompany.delivery.entity.User;
import com.mycompany.delivery.service.RoleService;
import com.mycompany.delivery.service.RouteService;
import com.mycompany.delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final RoleService roleService;
    private final RouteService routeService;

    @Autowired
    public SecurityConfig(UserService userService, RoleService roleService, RouteService routeService) {
        this.userService = userService;
        this.roleService = roleService;
        this.routeService = routeService;
    }

    @PostConstruct
    private void init() {
        roleService.saveRole(Role.builder().name("ROLE_ADMIN").build());
        roleService.saveRole(Role.builder().name("ROLE_USER").build());

        userService.saveUser(User.builder()
                .username("admin")
                .email("admin@gmail.com")
                .password(new BCryptPasswordEncoder().encode("admin"))
                .balance(0)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .authorities(Collections.singleton(roleService.loadRoleByName("ROLE_ADMIN")))
                .build());

        userService.saveUser(User.builder()
                .username("user1")
                .email("user@user")
                .password(new BCryptPasswordEncoder().encode("user1"))
                .balance(0)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .authorities(Collections.singleton(roleService.loadRoleByName("ROLE_USER")))
                .build());

        routeService.saveRoute(Route.builder().departurePoint("Київ").arrivalPoint("Рівне").length(327).build());
        routeService.saveRoute(Route.builder().departurePoint("Київ").arrivalPoint("Львів").length(541).build());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/cabinet/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/registration").not().fullyAuthenticated()
                .antMatchers("/", "/img/**").permitAll()
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll()
                    .logoutSuccessUrl("/")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }
}
