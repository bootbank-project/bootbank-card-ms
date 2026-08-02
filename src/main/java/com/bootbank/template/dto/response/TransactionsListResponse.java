package com.bootbank.template.dto.response;

import java.util.List;

public record TransactionsListResponse(
        List<TransactionResponse> items,
        long total
) {
}
