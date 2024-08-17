package thePackmaster.cards.darkflamepactpack;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.util.Wiz;

public class FromAsh extends AbstractDarkflamePactCard {
  public static final String ID = SpireAnniversary5Mod.makeID("FromAsh");
  public FromAsh() {
    super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
    this.block = this.baseBlock = 8;
    this.secondMagic = this.baseSecondMagic = 1;
    this.magicNumber = this.baseMagicNumber = 1;
  }

  @Override
  public void upp() {
    upgradeBlock(2);
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    Wiz.doBlk(block);
    addToBot(new DiscardAction(abstractPlayer, abstractPlayer, this.secondMagic, !this.upgraded));
    addToBot(new DrawCardAction(this.magicNumber));
  }
}
