package com.bootbank.template.model.enums;

public enum CardType {
    DIGICARD("Bootbank DigiCard", CardCategory.DEBIT),
    CLASSIC_DEBIT("Bootbank Classic Debit", CardCategory.DEBIT),
    PLATINUM_DEBIT("Bootbank Platinum Debit", CardCategory.DEBIT),
    VISA_INFINITE("Bootbank Visa Infinite", CardCategory.DEBIT),
    WORLD_ELITE("Bootbank World Elite", CardCategory.DEBIT),


    CLASSIC_CREDIT("Bootbank Classic", CardCategory.CREDIT),
    GOLD_CREDIT("Bootbank Gold", CardCategory.CREDIT),
    PREMIUM_CREDIT("Bootbank Premium", CardCategory.CREDIT),
    PLATINUM_CREDIT("Bootbank Platinum", CardCategory.CREDIT);

    private final String displayName;
    private final CardCategory category;

    CardType(String displayName, CardCategory category) {
        this.displayName = displayName;
        this.category = category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public CardCategory getCategory() {
        return category;
    }


    public enum CardCategory {
        DEBIT,
        CREDIT
    }
}
