package thePackmaster.patches.darkflamepactpack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.actions.common.MonsterStartTurnAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.EnemyTurnEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import thePackmaster.cardmodifiers.darkflamepactpack.QuietusModifier;
import thePackmaster.packs.DarkflamePactPack;

import java.util.Optional;

@SuppressWarnings("unused")
public class DarkflamePactPatches {

  @SpirePatch(
      clz = AbstractCard.class,
      method = SpirePatch.CLASS
  )
  public static class Fields {
    public static SpireField<Boolean> allowAutoplayWhenUnplayable = new SpireField<>(() -> false);
  }

  @SpirePatch(
      clz = GameActionManager.class,
      method = "incrementDiscard"
  )
  public static class TriggerOnDiscardPatch {
    @SpireInsertPatch(rloc = 3)
    public static void doQuietusCheck() {
      DarkflamePactPack.triggerOnDiscardOrExhaust();
    }
  }

  @SpirePatch(
      clz = AbstractCard.class,
      method = "hasEnoughEnergy"
  )
  public static class PlayOutOfTurnPatch {
    @SpirePrefixPatch
    public static SpireReturn<Boolean> bypassCheckForQuietus(AbstractCard __instance) {
      if(!AbstractDungeon.actionManager.turnHasEnded) {
        return SpireReturn.Continue();
      }
      Optional<QuietusModifier> q = QuietusModifier.getFrom(__instance);
      if(q.isPresent() && q.get().isBeingPlayedByQuietusTrigger()) {
        return SpireReturn.Return(true);
      }
      return SpireReturn.Continue();
    }
  }

  @SpirePatch(
      clz = UseCardAction.class,
      method = "update"
  )
  public static class UseCardActionInsertPatch {
    @SpireInsertPatch(
        locator = Locator.class
    )
    public static void playEnemyEndTurnEffectIfNoQuietusCardsRemain(UseCardAction __instance, AbstractCard ___targetCard) {
      QuietusModifier.group.removeCard(___targetCard);

      if(!AbstractDungeon.actionManager.turnHasEnded) {
        return;
      }

      for (CardQueueItem c : AbstractDungeon.actionManager.cardQueue) {
        if (QuietusModifier.getFrom(c.card).isPresent() && c.card != ___targetCard) {
          return;
        }
      }
      AbstractDungeon.topLevelEffects.add(new EnemyTurnEffect());
    }

    private static class Locator extends SpireInsertLocator {
      @Override
      public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
        return LineFinder.findInOrder(ctMethodToPatch, new Matcher.FieldAccessMatcher(AbstractCard.class, "freeToPlayOnce"));
      }
    }
  }

  @SpirePatch(
      clz = EndTurnAction.class,
      method = "update"
  )
  public static class StopEnemyTurnEffect {

    @SpireInstrumentPatch
    public static ExprEditor exprEditor() {
      return new ExprEditor(){
        public void edit(FieldAccess fieldAccess) throws CannotCompileException {
          if (fieldAccess.getClassName().equals(EndTurnAction.class.getName())
              && fieldAccess.getFieldName().equals("skipMonsterTurn")) {
            fieldAccess.replace(String.format("$_ = $proceed($$) || %s.skipEnemyTurnEffect();", StopEnemyTurnEffect.class.getName()));
          }
        }
      };
    }

    public static boolean skipEndEnemyTurnEffect() {
      for(CardQueueItem cqi : AbstractDungeon.actionManager.cardQueue) {
        if(QuietusModifier.getFrom(cqi.card).isPresent()) {
          return false;
        }
      }
      return true;
    }
  }

  @SpirePatch(
      clz = MonsterStartTurnAction.class,
      method = SpirePatch.CONSTRUCTOR
  )
  public static class DontDelay {
    @SpirePostfixPatch
    public static void removeEndOfTurnWaits(MonsterStartTurnAction __instance) {
      for (CardQueueItem cqi : AbstractDungeon.actionManager.cardQueue) {
        if (!QuietusModifier.getFrom(cqi.card).isPresent()) {
          continue;
        }

        AbstractDungeon.actionManager.actions.removeIf(action -> action instanceof WaitAction);
        return;
      }
    }
  }

  @SpirePatch(
      clz = AbstractPlayer.class,
      method = "renderHand"
  )
  public static class RenderQuietusGroup {
    @SpireInsertPatch(
        locator = Locator.class
    )
    public static void renderQuietusGroupWithLimbo(AbstractPlayer __instance, SpriteBatch sb) {
      QuietusModifier.group.render(sb);
    }

    private static class Locator extends SpireInsertLocator {
      @Override
      public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
        Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "limbo");
        return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
      }
    }
  }

  @SpirePatch(
      clz = AbstractCard.class,
      method = "canUse",
      paramtypez = {AbstractPlayer.class, AbstractMonster.class}
  )
  public static class CanUnplayableBeAutoplayedPatch {
    @SpirePrefixPatch
    public static SpireReturn<Boolean> canUseAutoplayCard(AbstractCard __instance) {
      if(!(Fields.allowAutoplayWhenUnplayable.get(__instance) && __instance.isInAutoplay)) {
        return SpireReturn.Continue();
      }
      return SpireReturn.Return(true);
    }
  }
}
