package robTheSpire.powers

import com.megacrit.cardcrawl.actions.animations.VFXAction
import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect
import robTheSpire.DefaultMod.Companion.makeID

class EscapeCountdownPower(owner: AbstractCreature?, turns: Int) : AbstractPower() {
    override fun updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[2]
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1]
        }
    }

    override fun atEndOfTurn(isPlayer: Boolean) {
        if (amount == 1 && !owner.isDying) {
            if (isPlayer) {
                escape()
            } else {
                throw IllegalArgumentException("Escape Countdown buff can only be applied to a player.")
            }
        } else {
            addToBot(ReducePowerAction(owner, owner, POWER_ID, 1))
            updateDescription()
        }
    }

    private fun escape() {
        val target: AbstractCreature = AbstractDungeon.player
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            addToBot(SkipEnemiesTurnAction()) //Skips enemy's turn, otherwise enemies can attack while you are escaping
            AbstractDungeon.getCurrRoom().smoked = true
            addToBot(VFXAction(SmokeBombEffect(target.hb.cX, target.hb.cY)))
            AbstractDungeon.player.hideHealthBar()
            AbstractDungeon.player.isEscaping = true
            AbstractDungeon.player.flipHorizontal = !AbstractDungeon.player.flipHorizontal
            AbstractDungeon.overlayMenu.endTurnButton.disable()
            AbstractDungeon.player.escapeTimer = 2.5f
        }
    }

    companion object {
        //TODO: FIX BUG where game crashes in ditto match where the opposing thief escapes before our character
        val POWER_ID = makeID("EscapeCountdownPower")
        private var powerStrings: PowerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID)
        var NAME: String? = null
        val DESCRIPTIONS: Array<String>
        val isEscapingThisTurn: Boolean
            get() = AbstractDungeon.player.hasPower(POWER_ID) && AbstractDungeon.player.getPower(POWER_ID).amount == 1

        init {
            NAME = powerStrings.NAME
            DESCRIPTIONS = powerStrings.DESCRIPTIONS
        }
    }

    init {
        name = NAME
        ID = POWER_ID
        this.owner = owner
        amount = turns
        updateDescription()
        loadRegion("fading")
    }
}