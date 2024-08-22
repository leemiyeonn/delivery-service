package org.delivery.api.resolver;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.UserSession;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.user.service.UserService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class UserSessionResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        // 1. 메서드 파라미터에 @UserSession 어노테이션이 있는지 체크
        var annotation = parameter.hasParameterAnnotation(UserSession.class);

        // 2. 파라미터의 타입이 User 클래스인지 체크
        var parameterType = parameter.getParameterType().equals(User.class);

        // 어노테이션이 있고 파라미터 타입이 User 클래스이면 true 반환
        return (annotation && parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // supportsParameter 메서드 에서 true 를 반환하면 이 메서드가 실행됨

        // RequestContextHolder 에서 현재 요청의 속성 가져오기
        var requestContext = RequestContextHolder.getRequestAttributes();
        if (requestContext == null) {
            throw new IllegalStateException("Request context is not available");
        }

        var userId = requestContext.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        if (userId == null) {
            throw new IllegalStateException("User ID is not available in request context");
        }

        // userId를 이용해 UserService 에서 사용자 정보 가져 오기, userId는 Long 타입 으로 변환
        var userEntity = userService.getUserWithThrow(Long.parseLong(userId.toString()));

        // User 객체를 빌더 패턴으로 생성하여 반환
        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .status(userEntity.getStatus())
                .password(userEntity.getPassword())
                .address(userEntity.getAddress())
                .registeredAt(userEntity.getRegisteredAt())
                .unregisteredAt(userEntity.getUnregisteredAt())
                .lastLoginAt(userEntity.getLastLoginAt())
                .build();
    }
}
