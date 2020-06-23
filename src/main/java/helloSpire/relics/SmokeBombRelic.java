package helloSpire.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import helloSpire.DefaultMod;
import helloSpire.util.TextureLoader;

import java.util.Iterator;

import static helloSpire.DefaultMod.makeRelicOutlinePath;
import static helloSpire.DefaultMod.makeRelicPath;

public class SmokeBombRelic extends CustomRelic implements ClickableRelic { // You must implement things you want to use from StSlib
    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     * StSLib for Clickable Relics
     *
     * At the start of each combat, gain 1 strength (i.e. Varja)
     */

    // ID, images, text.
    public static final String ID = DefaultMod.makeID(SmokeBombRelic.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("default_clickable_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("default_clickable_relic.png"));

    private boolean usedThisCombat = false; // Check out Hubris for more examples, including other StSlib things.

    static {
        System.out.println("Smoke Bomb Relic ID is: " + ID);
    }

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

            escape();

        }
        // See that talk action? It has DESCRIPTIONS[1] instead of just hard-coding "You are mine" inside.
        // DO NOT HARDCODE YOUR STRINGS ANYWHERE, it's really bad practice to have "Strings" in your code:

        /*
         * 1. It's bad for if somebody likes your mod enough (or if you decide) to translate it.
         * Having only the JSON files for translation rather than 15 different instances of "Dexterity" in some random cards is A LOT easier.
         *
         * 2. You don't have a centralised file for all strings for easy proof-reading. If you ever want to change a string
         * you don't have to go through all your files individually/pray that a mass-replace doesn't screw something up.
         *
         * 3. Without hardcoded strings, editing a string doesn't require a compile, saving you time (unless you clean+package).
         *
         */
    }

    private void escape() {
        AbstractCreature target = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.getCurrRoom().smoked = true;
            this.addToBot(new VFXAction(new SmokeBombEffect(target.hb.cX, target.hb.cY)));
            AbstractDungeon.player.hideHealthBar();
            AbstractDungeon.player.isEscaping = true;
            AbstractDungeon.player.flipHorizontal = !AbstractDungeon.player.flipHorizontal;
            AbstractDungeon.overlayMenu.endTurnButton.disable();
            AbstractDungeon.player.escapeTimer = 2.5F;
        }

    }


    private boolean canEscape() {
        if (!usedThisCombat && AbstractDungeon.getCurrRoom().monsters != null && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() && !AbstractDungeon.actionManager.turnHasEnded && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            return combatHasNoBossesAndBackAttack();
        }
        return false;
    }

    /*
    private boolean aMonsterHasBackAttack(){
        AbstractMonster m;
        while (var1.hasNext()){
            m = (AbstractMonster) var1.next();
            if (m.hasPower("BackAttack")) {
                return true;
            }
        }
        return false;
    }
    */

    private boolean isSurrounded() {
        return (AbstractDungeon.player.hasPower("Surrounded"));
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (isSurrounded()) {
            System.out.println("Starting pulse because a monster was killed and the charter was surrounded.");
        }
        if (canEscape() || isSurrounded()) {
            beginLongPulse();
            System.out.println("Start relic pulse @onMonsterDeath()");
        }
    }

    private boolean combatHasNoBossesAndBackAttack() {

        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        AbstractMonster m;
        do {
            if (!var1.hasNext()) {
                System.out.println("Can escape at combat start.");
                return true;
            }

            m = (AbstractMonster) var1.next();
            if (m.hasPower("BackAttack")) {
                System.out.println("Cannot escape at combat start because of back attack.");
                return false;
            }
        } while (m.type != AbstractMonster.EnemyType.BOSS);
        System.out.println("Cannot escape at combat start.");
        return false;
    }

    @Override
    public void atBattleStart() {

        usedThisCombat = false; // Make sure usedThisCombat is set to false at the start of each combat.
        if (combatHasNoBossesAndBackAttack()) {
            beginLongPulse();     // Pulse while the player can click on it.
            System.out.println("Start relic pulse @atPreBattle()");
        }
    }

    @Override
    public void onVictory() {
        stopPulse(); // Don't keep pulsing past the victory screen/outside of combat.
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}

