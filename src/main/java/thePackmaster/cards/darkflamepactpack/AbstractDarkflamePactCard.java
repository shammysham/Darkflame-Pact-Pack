package thePackmaster.cards.darkflamepactpack;

import thePackmaster.cards.AbstractPackmasterCard;

public abstract class AbstractDarkflamePactCard extends AbstractPackmasterCard {
  public AbstractDarkflamePactCard(String cardID, int cost, CardType type, CardRarity rarity, CardTarget target) {
    super(cardID, cost, type, rarity, target, "darkflamepact");
  }
}
