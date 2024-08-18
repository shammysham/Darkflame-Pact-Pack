package thePackmaster.patches.darkflamepactpack;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.actions.common.MonsterStartTurnAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.EnemyTurnEffect;
import javassist.CtBehavior;
import thePackmaster.cardmodifiers.darkflamepactpack.QuietusModifier;
import thePackmaster.packs.DarkflamePactPack;

import java.util.Optional;

@SuppressWarnings("unused")
public class DarkflamePactPatches {

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
    public static void Insert(UseCardAction __instance, AbstractCard ___targetCard) {
      AbstractDungeon.player.limbo.removeCard(___targetCard);

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
    @SpireInsertPatch(
        locator = Locator.class
    )
    public static SpireReturn<Void> Insert(EndTurnAction __instance) {
      __instance.isDone = true;
      for(CardQueueItem cqi : AbstractDungeon.actionManager.cardQueue) {
        if(QuietusModifier.getFrom(cqi.card).isPresent()) {
          return SpireReturn.Return();
        }
      }
      return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
      @Override
      public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
        Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractRoom.class, "skipMonsterTurn");
        return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
      }
    }
  }

  @SpirePatch(
      clz = MonsterStartTurnAction.class,
      method = SpirePatch.CONSTRUCTOR
  )
  public static class DontDelay {
    public static void Postfix(MonsterStartTurnAction __instance) {
      for (CardQueueItem cqi : AbstractDungeon.actionManager.cardQueue) {
        if (!QuietusModifier.getFrom(cqi.card).isPresent()) {
          continue;
        }

        AbstractDungeon.actionManager.actions.removeIf(action -> action instanceof WaitAction);
        return;
      }
    }
  }
}
