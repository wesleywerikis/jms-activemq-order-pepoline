package br.com.billingworker.billing_worker.listener;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import br.com.billingworker.billing_worker.config.JmsConfig;
import br.com.billingworker.billing_worker.model.BilledOrder;
import br.com.billingworker.billing_worker.model.OrderPayload;

@Component
public class BillingListener {

    private static final Logger log = LoggerFactory.getLogger(BillingListener.class);
    private final JmsTemplate jmsTemplate;

    public BillingListener(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = JmsConfig.QUEUE_ORDERS_NEW, containerFactory = "jmsListenerContainerFactory")
    public void onNewOrder(OrderPayload order) {
        log.info("Billing received order id={} item={} quantity={}", order.getOrderId(), order.getItem(),
                order.getQuantity());

        BilledOrder billed = new BilledOrder();
        billed.setOrderId(order.getOrderId());
        billed.setCustomerEmail(order.getCustomerEmail());
        billed.setItem(order.getItem());
        billed.setQuantity(order.getQuantity());
        billed.setInvoiceNumber("NF-" + UUID.randomUUID());
        billed.setBilledAt(Instant.now());

        jmsTemplate.convertAndSend(JmsConfig.QUEUE_ORDERS_BILLED, billed);
        log.info("Published billed order to {} -> id={} invoice={}", JmsConfig.QUEUE_ORDERS_BILLED, billed.getOrderId(),
                billed.getInvoiceNumber());

    }

}
