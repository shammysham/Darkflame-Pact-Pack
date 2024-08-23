package thePackmaster.cardmodifiers.darkflamepactpack;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.random.Random;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.patches.darkflamepactpack.DarkflamePactPatches;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class QuietusModifier extends AbstractCardModifier {
  public static String ID = SpireAnniversary5Mod.makeID("QuietusModifier");
  private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
  private final boolean isInherent;
  private boolean isBeingPlayedByQuietusTrigger = false;
  public static final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
  private boolean originalAllowAutoplayWhenUnplayable = false;
  private boolean originalEthereateLast = false;

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
    return c == null ? Optional.empty() : CardModifierManager.getModifiers(c, QuietusModifier.ID).stream().map(cm -> (QuietusModifier) cm).findFirst();
  }

  @Override
  public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
    // reset trigger
    this.isBeingPlayedByQuietusTrigger = false;
  }

  @Override
  public void onInitialApplication(AbstractCard card) {
    originalAllowAutoplayWhenUnplayable = DarkflamePactPatches.Fields.allowAutoplayWhenUnplayable.get(card);
    originalEthereateLast = DarkflamePactPatches.Fields.ethereateLast.get(card);
    DarkflamePactPatches.Fields.allowAutoplayWhenUnplayable.set(card, true);
    DarkflamePactPatches.Fields.ethereateLast.set(card, true);
  }

  @Override
  public void onRemove(AbstractCard card) {
    DarkflamePactPatches.Fields.allowAutoplayWhenUnplayable.set(card, originalAllowAutoplayWhenUnplayable);
    DarkflamePactPatches.Fields.ethereateLast.set(card, originalEthereateLast);
  }

  public boolean isBeingPlayedByQuietusTrigger() {
    return isBeingPlayedByQuietusTrigger;
  }

  @Override
  public boolean shouldApply(AbstractCard card) {
    return !(CardModifierManager.hasModifier(card, ID)
        || card.type == AbstractCard.CardType.CURSE
        || card.type == AbstractCard.CardType.STATUS
        || (card.cost == -2 && !(card instanceof UnplayableQuietusCard))
    );
  }

  public static void process(AbstractCard card) {
    AbstractDungeon.actionManager.addToTop(new ProcessQuietusAction(card));
  }

  public interface UnplayableQuietusCard {
    default boolean canUse(AbstractCard c) {
      Optional<QuietusModifier> q = QuietusModifier.getFrom(c);
      return q.isPresent() && q.get().isBeingPlayedByQuietusTrigger;
    }
  }

  public static class ProcessQuietusAction extends AbstractGameAction {
    private final AbstractCard CARD;
    private final Random RANDOM = new Random();

    public ProcessQuietusAction(AbstractCard card) {
      this.CARD = card;
    }

    public void update() {
      List<AbstractCard> toRemove = new ArrayList<>();
      // we iterate limbo too, since retain cards get moved there
      Stream.of(
              AbstractDungeon.player.hand.group.stream(),
              AbstractDungeon.player.limbo.group.stream()
          )
          .flatMap(s -> s)
          .filter(c -> !c.equals(CARD)) // can't self-trigger, matching exact instance
          .filter(c -> {
            Optional<QuietusModifier> q = getFrom(c);
            return q.isPresent() && !q.get().isBeingPlayedByQuietusTrigger;
          })
          .forEach(c -> {
            // we will use this to allow the card to be played outside the player's turn, as well as allow
            // unplayables to be played
            getFrom(c).ifPresent(q -> q.isBeingPlayedByQuietusTrigger = true);

            // update card appearance
            c.target_y = Settings.HEIGHT / 2.0f + RANDOM.random(-100.0f, 300.0f);
            c.target_x = Settings.WIDTH / 2.0f + RANDOM.random(-Settings.WIDTH / 4.0f, Settings.WIDTH / 4.0f);
            c.targetAngle = 0;
            c.targetDrawScale = 0.8f;
            c.lighten(true);

            AbstractDungeon.actionManager.addToTop(new NewQueueCardAction(c, true, false, true));
            toRemove.add(c);
          });

      AbstractDungeon.player.limbo.group.removeAll(toRemove);
      AbstractDungeon.player.hand.group.removeAll(toRemove);
      group.group.addAll(toRemove);
      this.isDone = true;
    }
  }
}
