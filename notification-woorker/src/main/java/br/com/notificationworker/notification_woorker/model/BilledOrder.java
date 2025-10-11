package br.com.notificationworker.notification_woorker.model;

import java.time.Instant;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BilledOrder {
    
    @NotBlank
    private String orderId;

    @NotBlank
    private String customerEmail;

    @NotBlank
    private String item;

    @Min(1)
    private int quantity;
    
    @NotBlank
    private String invoiceNumber;

    @NotNull
    private Instant billedAt;
}
