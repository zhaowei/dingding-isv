package dingdingisv.web.rest;

import com.codahale.metrics.annotation.Timed;
import dingdingisv.config.Constants;
import dingdingisv.domain.User;
import dingdingisv.repository.UserRepository;
import dingdingisv.security.jwt.JWTConfigurer;
import dingdingisv.security.jwt.TokenProvider;
import dingdingisv.service.IsvappService;
import dingdingisv.service.UserService;
import dingdingisv.web.rest.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
import java.net.URI;
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
    private IsvappService isvappService;

    @Inject
    private UserService userService;

    private final Logger log = LoggerFactory.getLogger(IsvappPermantCodeResource.class);

    @ApiOperation(value = "获取秘钥", notes = "用户认证并获取秘钥,后续接口调用都依赖此秘钥", position = 2)
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
            //return ResponseEntity.ok(new JWTToken(jwt));
            return ResponseEntity.ok(new OutputDTO<>(0, "success", jwt));
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(new OutputDTO(Constants.ERROR_INVIDE_ACCOUND, "无效账号"), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "注册账号", notes = "注册账号", position = 1)
    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<?> createUser(@RequestBody AccountDTO accountDTO, HttpServletRequest request) throws URISyntaxException {

        ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setLogin(accountDTO.getUsername());
        managedUserDTO.setPassword(accountDTO.getPassword());

        if (userRepository.findOneByLogin(managedUserDTO.getLogin()).isPresent()) {
            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_USER_NAME_EXIST, "该名字已被使用"));
        } else {
            managedUserDTO.setEmail(String.format("%s@dd-test.com", managedUserDTO.getLogin()));
            User newUser = userService.createUser(managedUserDTO);
            //注册 ISV 服务

            IsvappDTO isvappDTO = new IsvappDTO();
            isvappDTO.setUserName(managedUserDTO.getLogin());
            isvappDTO.setIsvKey(managedUserDTO.getLogin());
            isvappService.save(isvappDTO);
            log.debug("reg Isvapp : {}", isvappDTO);

            return ResponseEntity.ok(new OutputDTO<>(0, "success", accountDTO));
        }
    }

}
