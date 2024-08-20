package thePackmaster.cards.darkflamepactpack;

import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.powers.shamanpack.IgnitePower;
import thePackmaster.util.Wiz;

public class DarkflameBarrier extends AbstractDarkflamePactCard {
  public static final String ID = SpireAnniversary5Mod.makeID("DarkflameBarrier");
  public DarkflameBarrier() {
    super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
    this.block = this.baseBlock = 13;
    this.magicNumber = this.baseMagicNumber = 2;
    this.secondMagic = this.baseSecondMagic = 1;
  }

  @Override
  public void upp() {
    upgradeBlock(4);
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    Wiz.doBlk(this.block);
    Wiz.makeInHand(new Burn(), this.secondMagic);
    Wiz.forAllMonstersLiving(monster -> Wiz.applyToEnemy(monster, new IgnitePower(monster, this.magicNumber * ((int) Wiz.hand().group.stream().filter(c -> c.type == CardType.STATUS).count() + 1))));
  }
}
