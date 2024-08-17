package thePackmaster.cardmodifiers.darkflamepactpack;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import thePackmaster.SpireAnniversary5Mod;

import java.util.Optional;

public class QuietusModifier extends AbstractCardModifier {
  public static String ID = SpireAnniversary5Mod.makeID("QuietusModifier");
  private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
  private final boolean isInherent;
  private boolean isBeingPlayedByQuietusTrigger = false;

  private QuietusModifier(boolean isInherent) {
    this.isInherent = isInherent;
  }

  @Override
  public AbstractCardModifier makeCopy() {
    return new QuietusModifier(isInherent);
  }

  @Override
  public String identifier(AbstractCard card) {
    return ID;
  }

  @Override
  public boolean isInherent(AbstractCard card) {
    return isInherent;
  }

  @Override
  public String modifyDescription(String rawDescription, AbstractCard card) {
    return (isInherent ? "" : uiStrings.TEXT[0]) + rawDescription;
  }

  public static void addTo(AbstractCard c, boolean isInherent) {
    CardModifierManager.addModifier(c, new QuietusModifier(isInherent));
  }

  public static Optional<QuietusModifier> getFrom(AbstractCard c) {
    return CardModifierManager.getModifiers(c, QuietusModifier.ID).stream().map(cm -> (QuietusModifier) cm).findFirst();
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    // reset trigger
    this.isBeingPlayedByQuietusTrigger = false;
  }

  @Override
  public boolean shouldApply(AbstractCard card) {
    return !(CardModifierManager.hasModifier(card, ID)
        || card.type == AbstractCard.CardType.CURSE
        || card.type == AbstractCard.CardType.STATUS
        || (card.cost == -2 && !(card instanceof UnplayableQuietusCard))
    );
  }

  public static void process() {
    AbstractDungeon.player.hand.group.stream()
        .filter(c -> {
          Optional<QuietusModifier> q = getFrom(c);
          return q.isPresent() && !q.get().isBeingPlayedByQuietusTrigger;
        })
        .forEach(c -> {
          // we will use this to allow the card to be played outside the player's turn, as well as allow
          // unplayables to be played
          getFrom(c).ifPresent(q -> q.isBeingPlayedByQuietusTrigger = true);
          AbstractDungeon.actionManager.addToTop(new NewQueueCardAction(c, true, false, true));
        });
  }

  public interface UnplayableQuietusCard {
    default boolean canUse(AbstractCard c) {
      Optional<QuietusModifier> q = QuietusModifier.getFrom(c);
      return q.isPresent() && q.get().isBeingPlayedByQuietusTrigger;
    }
  }
}
