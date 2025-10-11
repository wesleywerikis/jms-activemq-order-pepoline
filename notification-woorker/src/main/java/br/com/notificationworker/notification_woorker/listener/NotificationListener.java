package br.com.notificationworker.notification_woorker.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import br.com.notificationworker.notification_woorker.config.JmsConfig;
import br.com.notificationworker.notification_woorker.model.BilledOrder;

@Component
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    @JmsListener(destination = JmsConfig.QUEUE_ORDERS_BILLED, containerFactory = "jmsListenerContainerFactory")
    public void onBilled(BilledOrder billed) {
        log.info("Notifying customer about order id={} (invoice={}, email={})", billed.getOrderId(),
                billed.getInvoiceNumber(), billed.getCustomerEmail());
    }

}
