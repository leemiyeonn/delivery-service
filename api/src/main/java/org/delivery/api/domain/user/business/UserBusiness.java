package org.delivery.api.domain.user.business;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.user.service.UserService;
import org.delivery.api.domain.user.converter.UserConverter;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.user.controller.model.UserResponse;
import org.delivery.api.domain.user.controller.model.UserLoginRequest;
import org.delivery.api.domain.user.controller.model.UserRegisterRequest;
import org.delivery.api.domain.token.business.TokenBusiness;
import org.delivery.api.domain.token.controller.model.TokenResponse;

@Business
@RequiredArgsConstructor
public class UserBusiness {

    private final UserService userService;
    private final UserConverter userConverter;
    private final TokenBusiness tokenBusiness;

    public UserResponse register(UserRegisterRequest request) {

        return Optional.ofNullable(request)
            .map(userConverter::toEntity)
            .map(userService::register)
            .map(userConverter::toResponse)
            .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "request null"));
    }

    public TokenResponse login(UserLoginRequest request) {
        var userEntity = userService.login(request.getEmail(), request.getPassword());
        return tokenBusiness.issueToken(userEntity);
    }

    public UserResponse me(User user) {
        var userEntity = userService.getUserWithThrow(user.getId());
        return userConverter.toResponse(userEntity);
    }
}
