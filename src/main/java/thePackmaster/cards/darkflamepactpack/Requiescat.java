package thePackmaster.cards.darkflamepactpack;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
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
    this.magicNumber = this.baseMagicNumber = 2;
    this.secondMagic = this.baseSecondMagic = 1;
    QuietusModifier.addTo(this, true);
  }

  @Override
  public void upp() {}

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    Wiz.applyToSelf(new StrengthPower(abstractPlayer, magicNumber));
    Wiz.applyToSelf(new DexterityPower(abstractPlayer, secondMagic));
    if(upgraded) {
      Wiz.atb(new MakeTempCardInDiscardAction(new Dazed(), this.secondMagic));
    }
    else {
      Wiz.shuffleIn(new Dazed(), this.secondMagic);
    }
  }
}
