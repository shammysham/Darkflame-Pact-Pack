package thePackmaster.cards.darkflamepactpack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.util.Wiz;

public class LostElegy extends AbstractDarkflamePactCard {
  public static final String ID = SpireAnniversary5Mod.makeID("LostElegy");
  public LostElegy() {
    super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
    this.damage = this.baseDamage = 9;
    this.magicNumber = this.baseMagicNumber = 1;
  }

  @Override
  public void upp() {
    upgradeDamage(3);
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    Wiz.doDmg(abstractMonster, damage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
    addToBot(new DrawPileToHandAction(magicNumber, CardType.STATUS));
  }
}
