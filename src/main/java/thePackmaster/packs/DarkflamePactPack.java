package thePackmaster.packs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.cardmodifiers.darkflamepactpack.QuietusModifier;
import thePackmaster.cards.darkflamepactpack.*;
import thePackmaster.powers.darkflamepactpack.TheCalmBeforePower;
import thePackmaster.util.Wiz;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DarkflamePactPack extends AbstractCardPack {
  public static final String ID = SpireAnniversary5Mod.makeID("DarkflamePactPack");
  private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID);
  public static final String NAME = UI_STRINGS.TEXT[0];
  public static final String DESC = UI_STRINGS.TEXT[1];
  public static final String AUTHOR = UI_STRINGS.TEXT[2];

  public static final int OFFENSE = 2;
  public static final int DEFENSE = 2;
  public static final int SUPPORT = 3;
  public static final int FRONTLOAD = 4;
  public static final int SCALING = 3;

  public DarkflamePactPack() {
    super(ID, NAME, DESC, AUTHOR,
        new PackSummary(
            OFFENSE, DEFENSE, SUPPORT, FRONTLOAD, SCALING,
            PackSummary.Tags.Debuffs, PackSummary.Tags.Discard, PackSummary.Tags.Exhaust));
  }

  @Override
  public ArrayList<String> getCards() {
    return Stream.of(AnamnesisFlare.ID, DarkflameBarrier.ID, DarkRecollection.ID, Demonspark.ID, FormlessMemory.ID,
        FromAsh.ID, LostElegy.ID, SearingDelusion.ID, Requiescat.ID, TheCalmBefore.ID
    ).collect(Collectors.toCollection(ArrayList::new));
  }

  public static void triggerOnDiscardOrExhaust() {
    QuietusModifier.process();
    Optional.ofNullable(Wiz.adp().getPower(TheCalmBeforePower.POWER_ID))
        .ifPresent(p -> p.onExhaust(null));
  }
}
