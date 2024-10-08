package thePackmaster.cards.darkflamepactpack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.cardmodifiers.darkflamepactpack.QuietusModifier;
import thePackmaster.util.Wiz;

public class AnamnesisFlare extends AbstractDarkflamePactCard{
  public static final String ID = SpireAnniversary5Mod.makeID("AnamnesisFlare");
  public AnamnesisFlare() {
    super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
    this.damage = this.baseDamage = 16;
    this.magicNumber = this.baseMagicNumber = 1;
    this.isEthereal = true;
    this.shuffleBackIntoDrawPile = true;
    this.isMultiDamage = true;
    QuietusModifier.addTo(this, true);
  }

  @Override
  public void upp() {
    upgradeDamage(6);
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    Wiz.doAllDmg(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, false);
    Wiz.shuffleIn(new Dazed(), this.magicNumber);
  }
}
