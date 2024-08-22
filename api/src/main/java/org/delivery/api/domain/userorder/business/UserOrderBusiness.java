package org.delivery.api.domain.userorder.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.delivery.api.domain.userorder.converter.UserOrderConverter;
import org.delivery.api.domain.userorder.service.UserOrderService;
import org.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter;
import org.delivery.api.domain.userordermenu.service.UserOrderMenuService;
import org.delivery.db.userorder.UserOrderEntity;
import org.delivery.db.storemenu.StoreMenuEntity;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Business
public class UserOrderBusiness {

    private final UserOrderService userOrderService;
    private final UserOrderConverter userOrderConverter;

    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;

    private final UserOrderMenuConverter userOrderMenuConverter;
    private final UserOrderMenuService userOrderMenuService;

    private final StoreService storeService;
    private final StoreConverter storeConverter;

    // 사용자 주문 생성 메서드
    public UserOrderResponse userOrder(User user, UserOrderRequest body) {
        // 1. 요청된 메뉴 ID 목록으로 StoreMenu 엔티티 리스트 생성
        var storeMenuEntityList = body.getStoreMenuIdList()
                .stream()
                .map(storeMenuService::getStoreMenuWithThrow)
                .collect(Collectors.toList());

        // 2. 사용자와 메뉴 리스트로 UserOrder 엔티티 생성
        var userOrderEntity = userOrderConverter.toEntity(user, storeMenuEntityList);

        // 3. 주문 저장
        var newUserOrderEntity = userOrderService.order(userOrderEntity);

        // 4. UserOrderMenu 엔티티 리스트 생성
        var userOrderMenuEntityList = storeMenuEntityList.stream()
                .map(it -> userOrderMenuConverter.toEntity(newUserOrderEntity, it))
                .collect(Collectors.toList());

        // 5. UserOrderMenu 저장
        userOrderMenuEntityList.forEach(userOrderMenuService::order);

        // 6. 응답 생성 및 반환
        return userOrderConverter.toResponse(newUserOrderEntity);
    }

    // UserOrderDetailResponse 생성을 위한 헬퍼 메서드
    private UserOrderDetailResponse createUserOrderDetailResponse(UserOrderEntity userOrderEntity) {
        // 1. 주문에 해당하는 UserOrderMenu 엔티티 리스트 조회
        var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());

        // 2. UserOrderMenu에 해당하는 StoreMenu 엔티티 리스트 조회
        var storeMenuEntityList = userOrderMenuEntityList.stream()
                .map(userOrderMenuEntity -> storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId()))
                .collect(Collectors.toList());

        // 3. 주문한 첫 번째 메뉴의 스토어 ID 추출
        var storeId = storeMenuEntityList.stream()
                .findFirst()
                .map(StoreMenuEntity::getStoreId)
                .orElseThrow(() -> new RuntimeException("No store menu found for this order"));

        // 4. 스토어 정보 조회
        var storeEntity = storeService.getStoreWithThrow(storeId);

        // 5. UserOrderDetailResponse 생성 및 반환
        return UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderConverter.toResponse(userOrderEntity))
                .storeMenuResponseList(storeMenuConverter.toResponseList(storeMenuEntityList))
                .storeResponse(storeConverter.toResponse(storeEntity))
                .build();
    }

    // 현재 진행 중인 주문 목록 조회
    public List<UserOrderDetailResponse> current(User user) {
        return userOrderService.current(user.getId()).stream()
                .map(this::createUserOrderDetailResponse)
                .collect(Collectors.toList());
    }

    // 과거 주문 내역 조회
    public List<UserOrderDetailResponse> history(User user) {
        return userOrderService.history(user.getId()).stream()
                .map(this::createUserOrderDetailResponse)
                .collect(Collectors.toList());
    }

    // 특정 주문 상세 정보 조회
    public UserOrderDetailResponse read(User user, Long orderId) {
        var userOrderEntity = userOrderService.getUserOrderWithOutStatusWithThrow(orderId, user.getId());
        return createUserOrderDetailResponse(userOrderEntity);
    }
}