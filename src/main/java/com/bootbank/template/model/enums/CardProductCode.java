package com.bootbank.template.model.enums;

public enum CardProductCode {
    DIGICARD(CardType.DEBIT, "Bootbank DigiCard"),
    CLASSIC_DEBIT(CardType.DEBIT, "Bootbank Classic Debit"),
    PLATINUM_DEBIT(CardType.DEBIT, "Bootbank Platinum Debit"),
    VISA_INFINITE(CardType.DEBIT, "Bootbank Visa Infinite"),
    WORLD_ELITE(CardType.DEBIT, "Bootbank World Elite"),

    CLASSIC_CREDIT(CardType.CREDIT, "Bootbank Classic Credit"),
    GOLD_CREDIT(CardType.CREDIT, "Bootbank Gold Credit"),
    PREMIUM_CREDIT(CardType.CREDIT, "Bootbank Premium Credit"),
    PLATINUM_CREDIT(CardType.CREDIT, "Bootbank Platinum Credit");

    private final CardType cardType;
    private final String displayName;

    CardProductCode(CardType cardType, String displayName) {
        this.cardType = cardType;
        this.displayName = displayName;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getDisplayName() {
        return displayName;
    }
}
