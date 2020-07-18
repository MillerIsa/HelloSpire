package robTheSpire.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.GainPennyEffect
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect

class FlatGoldAction : AbstractGameAction {
    private val increaseGold: Int
    private var info: DamageInfo? = null

    constructor(target: AbstractCreature?, info: DamageInfo?, goldAmount: Int) {
        this.info = info
        this.setValues(target, info)
        actionType = ActionType.DAMAGE
        duration = 0.1f
        increaseGold = goldAmount
    }

    constructor(target: AbstractCreature?, goldAmount: Int) {
        actionType = ActionType.DAMAGE
        duration = 0.1f
        this.target = target
        source = AbstractDungeon.player
        increaseGold = goldAmount
    }

    override fun update() {
        if (duration == 0.1f && target != null) {
            AbstractDungeon.effectList.add(FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.BLUNT_HEAVY))
            // gain gold --------------------------------------------------
            AbstractDungeon.player.gainGold(increaseGold)
            for (i in 0 until increaseGold) {
                AbstractDungeon.effectList.add(GainPennyEffect(source, target.hb.cX, target.hb.cY, source.hb.cX, source.hb.cY, true))
            }
            // gain gold --------------------------------------------------
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions()
            }
        }
        tickDuration()
    }

    companion object {
        private const val DURATION = 0.1f
    }
}