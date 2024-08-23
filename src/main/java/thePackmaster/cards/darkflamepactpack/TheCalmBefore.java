package thePackmaster.cards.darkflamepactpack;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.cardmodifiers.darkflamepactpack.QuietusModifier;
import thePackmaster.powers.darkflamepactpack.TheCalmBeforePower;
import thePackmaster.util.Wiz;

public class TheCalmBefore extends AbstractDarkflamePactCard implements QuietusModifier.UnplayableQuietusCard {
  public static final String ID = SpireAnniversary5Mod.makeID("TheCalmBefore");
  public TheCalmBefore() {
    super(ID, -2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    this.magicNumber = this.baseMagicNumber = 2;
    QuietusModifier.addTo(this, true);
  }

  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    return canUse(this);
  }

  @Override
  public void upp() {
    upgradeMagicNumber(1);
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    Wiz.applyToSelf(new TheCalmBeforePower(this.baseMagicNumber));
  }
}
