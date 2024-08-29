package thePackmaster.cards.darkflamepactpack;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.powers.darkflamepactpack.FormlessMemoryPower;
import thePackmaster.util.Wiz;

public class FormlessMemory extends AbstractDarkflamePactCard{
  public static final String ID = SpireAnniversary5Mod.makeID("FormlessMemory");
  public FormlessMemory() {
    super(ID, 0, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    this.magicNumber = this.baseMagicNumber = 1;
    this.secondMagic = this.baseSecondMagic = 2;
  }

  @Override
  public void upp() {
    this.isInnate = true;
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    addToBot(new DrawCardAction(this.secondMagic));
    addToBot(new MakeTempCardInDiscardAction(new VoidCard(), 3));
    Wiz.applyToSelf(new FormlessMemoryPower(magicNumber));
  }
}
