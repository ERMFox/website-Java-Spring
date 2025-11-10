package ch.ermfox.website.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

@Configuration
public class SecurityConfig {

    @Value("${admin.password}")
    private String adminPassword;

    private static final IpAddressMatcher LAN_10   = new IpAddressMatcher("10.0.0.0/8");
    private static final IpAddressMatcher LAN_172  = new IpAddressMatcher("172.16.0.0/12");
    private static final IpAddressMatcher LAN_192  = new IpAddressMatcher("192.168.0.0/16");
    private static final IpAddressMatcher LOOPBACK = new IpAddressMatcher("127.0.0.1/32");
    private static final IpAddressMatcher LOOPBACK_V6 = new IpAddressMatcher("::1/128");

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").access(
                                (Supplier<Authentication> authentication, RequestAuthorizationContext ctx) -> {

                                    String ip = ctx.getRequest().getRemoteAddr();
                                    boolean inLan =
                                            LAN_10.matches(ip) ||
                                                    LAN_172.matches(ip) ||
                                                    LAN_192.matches(ip) ||
                                                    LOOPBACK.matches(ip) ||
                                                    LOOPBACK_V6.matches(ip);

                                    if (!inLan) {
                                        return new AuthorizationDecision(false);
                                    }

                                    var authObj = (Authentication) authentication.get();

                                    boolean hasAdmin = authObj.getAuthorities().stream()
                                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                                    return new AuthorizationDecision(hasAdmin);
                                }
                        )

                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .defaultSuccessUrl("/admin", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                );

        return http.build();
    }

    @Bean
    public UserDetailsService users(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password(encoder.encode(adminPassword))
                        .roles("ADMIN")
                        .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
