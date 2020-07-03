package robTheSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import robTheSpire.DefaultMod;
import robTheSpire.powers.EscapeCountdownPower;
import robTheSpire.util.TextureLoader;

import java.util.Iterator;

import static robTheSpire.DefaultMod.makeRelicOutlinePath;
import static robTheSpire.DefaultMod.makeRelicPath;

public class SmokeBombRelic extends CustomRelic implements ClickableRelic { // You must implement things you want to use from StSlib
    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     * StSLib for Clickable Relics
     */

    // ID, images, text.
    public static final String ID = DefaultMod.makeID(SmokeBombRelic.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("default_clickable_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("default_clickable_relic.png"));

    public static final int TURNS_TO_ESCAPE = 2;

    private boolean usedThisCombat = false; // Check out Hubris for more examples, including other StSlib things.


    public SmokeBombRelic() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK);
        tips.clear();
        tips.add(new PowerTip(name, description));
    }


    @Override
    public void onRightClick() {// On right click
        if (!isObtained || !canEscape()) {// If it has been used this combat, or the player doesn't actually have the relic (i.e. it's on display in the shop room)
            return; // Don't do anything.
        }
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) { // Only if you're in combat
            usedThisCombat = true; // Set relic as "Used this turn"
            flash(); // Flash
            stopPulse(); // And stop the pulsing animation (which is started in atPreBattle() below)
            prepareEscape();
        }
    }

    private void prepareEscape(){
        final AbstractPlayer p = AbstractDungeon.player;
        this.addToBot(new ApplyPowerAction(p, p, new EscapeCountdownPower(p, TURNS_TO_ESCAPE), 1));
    }


    private boolean canEscape() {
        if (!usedThisCombat && AbstractDungeon.getCurrRoom().monsters != null && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() && !AbstractDungeon.actionManager.turnHasEnded && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            return combatHasNoBossesAndBackAttack();
        }
        return false;
    }


    private boolean combatHasNoBossesAndBackAttack() {
        Iterator<AbstractMonster> var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        AbstractMonster m;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            m = var1.next();
            if (m.hasPower("BackAttack")) {
                return false;
            }
        } while (m.type != AbstractMonster.EnemyType.BOSS);
        return false;
    }

    private boolean isSurrounded() {
        return (AbstractDungeon.player.hasPower("Surrounded"));
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (canEscape() || isSurrounded()) {
            beginLongPulse();
        }
    }


    @Override
    public void atBattleStart() {
        usedThisCombat = false; // Make sure usedThisCombat is set to false at the start of each combat.
        if (combatHasNoBossesAndBackAttack()) {
            beginLongPulse();     // Pulse while the player can click on it.
        }
    }

    @Override
    public void onVictory() {
        stopPulse(); // Don't keep pulsing past the victory screen/outside of combat.
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TURNS_TO_ESCAPE + DESCRIPTIONS[1];
    }

}

