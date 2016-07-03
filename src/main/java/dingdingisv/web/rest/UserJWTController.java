package dingdingisv.web.rest;

import com.codahale.metrics.annotation.Timed;
import dingdingisv.domain.User;
import dingdingisv.repository.UserRepository;
import dingdingisv.security.jwt.JWTConfigurer;
import dingdingisv.security.jwt.TokenProvider;
import dingdingisv.service.UserService;
import dingdingisv.web.rest.dto.LoginDTO;
import dingdingisv.web.rest.dto.ManagedUserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/dingding")
@Api(value = "user-jwt", description = "用户登录", position = 1)
public class UserJWTController {

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private UserService userService;

    @ApiOperation(value = "用户登录", notes = "用户认证并获取秘钥,后续接口调用都依赖此秘钥", position = 2)
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginDTO.isRememberMe() == null) ? false : loginDTO.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return ResponseEntity.ok(new JWTToken(jwt));
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "注册账号", notes = "注册账号", position = 1)
    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<?> createUser(@RequestBody ManagedUserDTO managedUserDTO, HttpServletRequest request) throws URISyntaxException {
        if (userRepository.findOneByLogin(managedUserDTO.getLogin()).isPresent()) {
            return new ResponseEntity<Object>("该名字已被使用.", HttpStatus.BAD_REQUEST);
        } else {
            managedUserDTO.setEmail(String.format("%s@dd-test.com", managedUserDTO.getLogin()));
            User newUser = userService.createUser(managedUserDTO);
            return new ResponseEntity<Object>(newUser, HttpStatus.OK);
        }
    }
}
