package dingdingisv.config;


  import javax.inject.Inject;

  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.http.HttpMethod;
  import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
  import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
  import org.springframework.security.config.annotation.web.builders.HttpSecurity;
  import org.springframework.security.config.annotation.web.builders.WebSecurity;
  import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
  import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
  import org.springframework.security.config.http.SessionCreationPolicy;
  import org.springframework.security.core.userdetails.UserDetailsService;
  import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
  import org.springframework.security.crypto.password.PasswordEncoder;
  import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

  import dingdingisv.security.AuthoritiesConstants;
  import dingdingisv.security.jwt.JWTConfigurer;
  import dingdingisv.security.jwt.TokenProvider;

  @Configuration
  @EnableWebSecurity
  @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
  public class MicroserviceSecurityConfiguration extends WebSecurityConfigurerAdapter {

      @Inject
      private TokenProvider tokenProvider;

      @Inject
      private UserDetailsService userDetailsService;

      @Bean
      public PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
      }

      @Inject
      public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
          auth
              .userDetailsService(userDetailsService)
              .passwordEncoder(passwordEncoder());
      }

      @Override
      public void configure(WebSecurity web) throws Exception {
          web.ignoring()
              .antMatchers(HttpMethod.OPTIONS, "/**")
              .antMatchers("/app/**/*.{js,html}")
              .antMatchers("/bower_components/**")
              .antMatchers("/i18n/**")
              .antMatchers("/content/**")
              .antMatchers("/swagger-ui/index.html")
              .antMatchers("/test/**")
              .antMatchers("/h2-console/**");
      }

      @Override
      protected void configure(HttpSecurity http) throws Exception {
          http
              .csrf()
              .disable()
              .headers()
              .frameOptions()
              .disable()
          .and()
              .sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
              .authorizeRequests()
              .antMatchers("/dingding/authenticate").permitAll()
              .antMatchers("/dingding/reg").permitAll()
              .antMatchers("/api/**").authenticated()
              .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
              .antMatchers("/configuration/ui").permitAll()
          .and()
              .apply(securityConfigurerAdapter());

      }

      private JWTConfigurer securityConfigurerAdapter() {
          return new JWTConfigurer(tokenProvider);
      }

      @Bean
      public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
          return new SecurityEvaluationContextExtension();
      }
  }


