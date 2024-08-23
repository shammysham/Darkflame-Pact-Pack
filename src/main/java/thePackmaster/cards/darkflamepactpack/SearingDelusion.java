package thePackmaster.cards.darkflamepactpack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.util.Wiz;

public class SearingDelusion extends AbstractDarkflamePactCard{
  public static final String ID = SpireAnniversary5Mod.makeID("SearingDelusion");
  public SearingDelusion() {
    super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
    this.damage = this.baseDamage = 12;
    this.magicNumber = this.baseMagicNumber = 1;
    this.secondMagic = this.baseSecondMagic = 1;
    this.isEthereal = true;
  }

  @Override
  public void upp() {
    upgradeDamage(3);
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    Wiz.doDmg(abstractMonster, damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE);
    Wiz.applyToEnemy(abstractMonster, new WeakPower(abstractMonster, magicNumber, false));
    Wiz.shuffleIn(new SearingDelusion(), secondMagic);
  }
}
