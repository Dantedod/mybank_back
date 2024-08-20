package com.example.my_bank_backend.dto;

import java.util.Date;

public record InvoiceRequestDto(
    String invoiceDescription,
    Double amount,
    String cardName,
    Date invoiceDate,
    String invoiceStatus,
    Long id,
    Date dueDate,
    String email) {
}
