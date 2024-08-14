package thePackmaster.packs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import thePackmaster.SpireAnniversary5Mod;

import java.util.ArrayList;
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
  public static final int SUPPORT = 2;
  public static final int FRONTLOAD = 2;
  public static final int SCALING = 2;

  public DarkflamePactPack() {
    super(ID, NAME, DESC, AUTHOR,
        new PackSummary(
            OFFENSE, DEFENSE, SUPPORT, FRONTLOAD, SCALING,
            PackSummary.Tags.Debuffs, PackSummary.Tags.Discard, PackSummary.Tags.Exhaust));
  }

  @Override
  public ArrayList<String> getCards() {
    return Stream.of("").collect(Collectors.toCollection(ArrayList::new));
  }
}
