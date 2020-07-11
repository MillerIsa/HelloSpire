package robTheSpire.relics

import basemod.abstracts.CustomRelic
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.rooms.AbstractRoom
import robTheSpire.DefaultMod
import robTheSpire.powers.EscapeCountdownPower
import robTheSpire.util.TextureLoader

class SmokeBombRelic: CustomRelic(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK), ClickableRelic {
    private var usedThisCombat = false // Check out Hubris for more examples, including other StSlib things.
    override fun onRightClick() { // On right click
        if (!isObtained || !canEscape()) { // If it has been used this combat, or the player doesn't actually have the relic (i.e. it's on display in the shop room)
            return  // Don't do anything.
        }
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) { // Only if you're in combat
            usedThisCombat = true // Set relic as "Used this turn"
            flash() // Flash
            stopPulse() // And stop the pulsing animation (which is started in atPreBattle() below)
            prepareEscape()
        }
    }

    private fun prepareEscape() {
        val p = AbstractDungeon.player
        addToBot(ApplyPowerAction(p, p, EscapeCountdownPower(p, TURNS_TO_ESCAPE), 1))
    }

    private fun canEscape(): Boolean {
        return if (!usedThisCombat && AbstractDungeon.getCurrRoom().monsters != null && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() && !AbstractDungeon.actionManager.turnHasEnded && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            combatHasNoBossesAndBackAttack()
        } else false
    }

    private fun combatHasNoBossesAndBackAttack(): Boolean {
        val var1: Iterator<AbstractMonster> = AbstractDungeon.getCurrRoom().monsters.monsters.iterator()
        var m: AbstractMonster
        do {
            if (!var1.hasNext()) {
                return true
            }
            m = var1.next()
            if (m.hasPower("BackAttack")) {
                return false
            }
        } while (m.type != AbstractMonster.EnemyType.BOSS)
        return false
    }

    private val isSurrounded: Boolean
        get() = AbstractDungeon.player.hasPower("Surrounded")

    override fun onMonsterDeath(m: AbstractMonster) {
        if (canEscape() || isSurrounded) {
            beginLongPulse()
        }
    }

    override fun atBattleStart() {
        usedThisCombat = false // Make sure usedThisCombat is set to false at the start of each combat.
        if (combatHasNoBossesAndBackAttack()) {
            beginLongPulse() // Pulse while the player can click on it.
        }
    }

    override fun onVictory() {
        stopPulse() // Don't keep pulsing past the victory screen/outside of combat.
    }

    // Description
    override fun getUpdatedDescription(): String {
        return DESCRIPTIONS[0] + TURNS_TO_ESCAPE + DESCRIPTIONS[1]
    }

    companion object {
        // You must implement things you want to use from StSlib
        /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     * StSLib for Clickable Relics
     */
        // ID, images, text.
        @JvmField val ID: String = DefaultMod.makeID(SmokeBombRelic::class.java.simpleName)
        private val IMG = TextureLoader.getTexture(DefaultMod.makeRelicPath("Smoke_Bomb_Relic.png"))
        private val OUTLINE = TextureLoader.getTexture(DefaultMod.makeRelicOutlinePath("Smoke_Bomb_Relic.png"))
        const val TURNS_TO_ESCAPE = 2
    }

    init {
        tips.clear()
        tips.add(PowerTip(name, description))
    }
}