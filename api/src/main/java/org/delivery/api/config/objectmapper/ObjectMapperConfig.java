package org.delivery.api.config.objectmapper;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Java 8에서 도입된 새로운 날짜/시간 API (예: LocalDate, LocalDateTime) 지원을 위한 모듈
        objectMapper.registerModule(new Jdk8Module());
        // Java 8 날짜/시간 타입 (java.time 패키지)의 직렬화/역직렬화를 위한 모듈
        objectMapper.registerModule(new JavaTimeModule());

        // JSON에 알 수 없는 속성이 있을 때 에러를 발생시키지 않고 무시하도록 설정
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // getter나 setter가 없는 빈 객체를 직렬화할 때 에러를 발생시키지 않도록 설정
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 날짜를 타임스탬프 형식(숫자)이 아닌 ISO-8601 형식의 문자열로 직렬화하도록 설정
        objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        // JSON 속성 이름을 스네이크 케이스(snake_case)로 변환하는 전략 설정
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());

        return objectMapper;
    }
}
