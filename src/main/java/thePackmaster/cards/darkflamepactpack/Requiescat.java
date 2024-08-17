package thePackmaster.cards.darkflamepactpack;

import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.cardmodifiers.darkflamepactpack.QuietusModifier;
import thePackmaster.util.Wiz;

public class Requiescat extends AbstractDarkflamePactCard {
  public static final String ID = SpireAnniversary5Mod.makeID("Requiescat");
  public Requiescat() {
    super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
    this.magicNumber = this.baseMagicNumber = 1;
    this.secondMagic = this.baseSecondMagic = 1;
    QuietusModifier.addTo(this, true);
  }

  @Override
  public void upp() {
    upgradeMagicNumber(1);
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    Wiz.applyToSelf(new StrengthPower(abstractPlayer, magicNumber));
    Wiz.applyToSelf(new DexterityPower(abstractPlayer, secondMagic));
    Wiz.shuffleIn(new Dazed(), secondMagic);
  }
}
