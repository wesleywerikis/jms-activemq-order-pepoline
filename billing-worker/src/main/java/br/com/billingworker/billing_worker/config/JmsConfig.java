package br.com.billingworker.billing_worker.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.billingworker.billing_worker.model.BilledOrder;
import br.com.billingworker.billing_worker.model.OrderPayload;

@Configuration
public class JmsConfig {

    public static final String QUEUE_ORDERS_NEW = "orders.new";
    public static final String QUEUE_ORDERS_BILLED = "orders.billed";

    @Bean
    public MappingJackson2MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("OrderPayload", OrderPayload.class);
        mappings.put("BilledOrder", BilledOrder.class);

        converter.setTypeIdMappings(mappings);
        converter.setObjectMapper(objectMapper);
        return converter;
    }

}
