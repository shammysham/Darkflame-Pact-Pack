package thePackmaster.cards.darkflamepactpack;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.util.Wiz;

public class DarkRecollection extends AbstractDarkflamePactCard {
  public static final String ID = SpireAnniversary5Mod.makeID("DarkRecollection");
  public DarkRecollection() {
    super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
    this.magicNumber = this.baseMagicNumber = 3;
    this.secondMagic = this.baseSecondMagic = 2;
    this.isEthereal = true;
  }

  @Override
  public void upp() {
    upgradeMagicNumber(1);
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    Wiz.shuffleIn(new Dazed(), secondMagic);
    addToBot(new DrawCardAction(magicNumber));
  }
}
