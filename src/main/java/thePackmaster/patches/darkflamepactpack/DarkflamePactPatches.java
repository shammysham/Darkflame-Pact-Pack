package thePackmaster.patches.darkflamepactpack;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import thePackmaster.packs.DarkflamePactPack;

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
}
