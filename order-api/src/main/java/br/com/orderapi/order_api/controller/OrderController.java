package br.com.orderapi.order_api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.orderapi.order_api.config.JmsConfig;
import br.com.orderapi.order_api.model.OrderPayload;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final JmsTemplate jmsTemplate;

    public OrderController(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody OrderPayload payload) {
        jmsTemplate.convertAndSend(JmsConfig.QUEUE_ORDERS_NEW, payload);
        log.info("Published new order to {} -> id={}", JmsConfig.QUEUE_ORDERS_NEW, payload.getOrderId());
        return ResponseEntity.accepted().body(
                "Order accepted and queued (id=" + payload.getOrderId() + ")");
    }

}
