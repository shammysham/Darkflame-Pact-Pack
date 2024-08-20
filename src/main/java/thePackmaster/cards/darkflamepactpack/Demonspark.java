package thePackmaster.cards.darkflamepactpack;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.actions.HandSelectAction;
import thePackmaster.powers.shamanpack.IgnitePower;
import thePackmaster.util.Wiz;

public class Demonspark extends AbstractDarkflamePactCard {
  public static final String ID = SpireAnniversary5Mod.makeID("Demonspark");
  public Demonspark() {
    super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
    this.damage = this.baseDamage = 7;
    this.magicNumber = this.baseMagicNumber = 1;
    this.secondMagic = this.baseSecondMagic = 1;
    this.exhaust = true;
  }

  @Override
  public void upp() {
    upgradeMagicNumber(1);
  }

  @Override
  public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    this.addToBot(new HandSelectAction(
        this.magicNumber, card -> true,
        cards -> cards.forEach(card -> addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand, true))),
        cards -> {
          if(!cards.isEmpty()) {
            Wiz.applyToEnemyTop(abstractMonster, new IgnitePower(abstractMonster, secondMagic * cards.size()));
          }
        },
        String.format(cardStrings.EXTENDED_DESCRIPTION[cardStrings.EXTENDED_DESCRIPTION.length > 1 && (this.magicNumber != 1) ? 1 : 0], this.magicNumber),
        true, true, true
    ));
    Wiz.doDmg(abstractMonster, damage, DamageInfo.DamageType.NORMAL);
  }
}
