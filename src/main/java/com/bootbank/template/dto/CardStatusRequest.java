package com.bootbank.template.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CardStatusRequest {

    @NotBlank(message = "Status boş ola bilməz")
    // Yalnız ACTIVE və ya BLOCKED qəbul edilməsi üçün validasiya (Biznes Qaydası 2)
    @Pattern(regexp = "^(ACTIVE|BLOCKED)$", message = "Status yalnız 'ACTIVE' və ya 'BLOCKED' ola bilər")
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
