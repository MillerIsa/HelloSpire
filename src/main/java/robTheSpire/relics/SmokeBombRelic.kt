package robTheSpire.relics

import basemod.abstracts.CustomRelic
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.rooms.AbstractRoom
import robTheSpire.DefaultMod
import robTheSpire.DefaultMod.Companion.logger
import robTheSpire.powers.EscapeCountdownPower
import robTheSpire.util.TextureLoader
import kotlin.math.floor
import kotlin.math.pow

class SmokeBombRelic: CustomRelic(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK), ClickableRelic {
    private var usedThisCombat: Boolean = false // Check out Hubris for more examples, including other StSlib things.
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
        addToBot(ApplyPowerAction(p, p, EscapeCountdownPower(p, turnsToEscape()), 1))
        counter++
        updateDescription(p.chosenClass)
    }

    private fun turnsToEscape(): Int{
        return floor(BASE_TURNS_TO_ESCAPE * EXPONENTIAL_GROWTH_RATE.pow(counter.toDouble())).toInt()
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
        logger.info("Setting counter to: $counter")
        return DESCRIPTIONS[0] + turnsToEscape() + DESCRIPTIONS[1] + DESCRIPTIONS[2] + counter  + DESCRIPTIONS[3] + DESCRIPTIONS[4] + DESCRIPTIONS[5]
    }

    override fun updateDescription(c: AbstractPlayer.PlayerClass) {
        description = this.updatedDescription
        tips.clear()
        tips.add(PowerTip(name, description))
        initializeTips()
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
        const val BASE_TURNS_TO_ESCAPE = 2
        private const val EXPONENTIAL_GROWTH_RATE = 1.15
    }

    init {
        counter = 0
        tips.clear()
        tips.add(PowerTip(name, description))
        logger.info("Counter initialized to: $counter")
    }
}