package org.delivery.api.domain.storemenu.converter;

import java.util.List;
import java.util.stream.Collectors;
import org.delivery.api.common.annotation.Converter;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuRegisterRequest;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuResponse;
import org.delivery.db.storemenu.StoreMenuEntity;

import java.util.Optional;

@Converter
public class StoreMenuConverter {
    public StoreMenuEntity toEntity(StoreMenuRegisterRequest request){

        return Optional.ofNullable(request)
                .map(it ->{

                    return StoreMenuEntity.builder()
                            .storeId(request.getStoreId())
                            .name(request.getName())
                            .amount(request.getAmount())
                            .thumbnailUrl(request.getThumbnailUrl())
                            .build()
                            ;

                })
                .orElseThrow(()-> new ApiException(ErrorCode.NULL_POINT));
    }


    public StoreMenuResponse toResponse(
            StoreMenuEntity storeMenuEntity
    ){
        return Optional.ofNullable(storeMenuEntity)
                .map(it ->{
                    return StoreMenuResponse.builder()
                            .id(storeMenuEntity.getId())
                            .name(storeMenuEntity.getName())
                            .storeId(storeMenuEntity.getStoreId())
                            .amount(storeMenuEntity.getAmount())
                            .status(storeMenuEntity.getStatus())
                            .thumbnailUrl(storeMenuEntity.getThumbnailUrl())
                            .likeCount(storeMenuEntity.getLikeCount())
                            .sequence(storeMenuEntity.getSequence())
                            .build()
                            ;
                })
                .orElseThrow(()-> new ApiException(ErrorCode.NULL_POINT));
    }

    // 리스트 처리
    public List<StoreMenuResponse> toResponseList(List<StoreMenuEntity> storeMenuEntities) {
        return storeMenuEntities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

}
