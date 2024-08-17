package thePackmaster.powers.darkflamepactpack;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thePackmaster.powers.AbstractPackmasterPower;
import thePackmaster.powers.shamanpack.IgnitePower;
import thePackmaster.util.Wiz;

import static thePackmaster.SpireAnniversary5Mod.makeID;

public class TheCalmBeforePower extends AbstractPackmasterPower {
  public static final String POWER_ID = makeID("TheCalmBeforePower");
  public static final String NAME = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).NAME;
  public static final String[] DESCRIPTIONS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).DESCRIPTIONS;
  public TheCalmBeforePower(int amount) {
    super(POWER_ID, NAME, PowerType.BUFF, false, AbstractDungeon.player, amount);
  }

  @Override
  public void onExhaust(AbstractCard card) {
    Wiz.forAllMonstersLiving(monster -> Wiz.applyToEnemy(monster, new IgnitePower(monster, this.amount)));
  }
}
