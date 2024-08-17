package thePackmaster.powers.darkflamepactpack;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thePackmaster.powers.AbstractPackmasterPower;

import java.util.Collections;

import static thePackmaster.SpireAnniversary5Mod.makeID;

public class FormlessMemoryPower extends AbstractPackmasterPower {
  public static final String POWER_ID = makeID("FormlessMemoryPower");
  public static final String NAME = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).NAME;
  public static final String[] DESCRIPTIONS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).DESCRIPTIONS;
  public FormlessMemoryPower(int amount) {
    super(POWER_ID, NAME, PowerType.BUFF, false, AbstractDungeon.player, amount);
  }

  public void atStartOfTurn() {
    this.addToBot(new GainEnergyAction(this.amount));
    this.flash();
  }

  @Override
  public void updateDescription() {
    description = DESCRIPTIONS[0] + String.join(" ", Collections.nCopies(this.amount, "[E]")) + DESCRIPTIONS[1];
  }
}
