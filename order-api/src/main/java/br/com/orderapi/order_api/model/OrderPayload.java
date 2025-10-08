package br.com.orderapi.order_api.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPayload {

    @NotBlank
    private String orderId;

    @NotBlank
    private String customerEmail;

    @NotBlank
    private String item;

    @Min(1)
    private int quantity;

}
