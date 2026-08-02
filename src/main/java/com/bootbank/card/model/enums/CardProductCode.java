package com.bootbank.card.model.enums;

import lombok.Getter;

@Getter
public enum CardProductCode {
    DIGICARD(CardType.DEBIT),
    CLASSIC_DEBIT(CardType.DEBIT),
    PLATINUM_DEBIT(CardType.DEBIT),
    VISA_INFINITE(CardType.DEBIT),
    WORLD_ELITE(CardType.DEBIT),

    CLASSIC_CREDIT(CardType.CREDIT),
    GOLD_CREDIT(CardType.CREDIT),
    PREMIUM_CREDIT(CardType.CREDIT),
    PLATINUM_CREDIT(CardType.CREDIT);

    private final CardType cardType;

    CardProductCode(CardType cardType) {
        this.cardType = cardType;
    }

}
