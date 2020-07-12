package robTheSpire.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.GainPennyEffect
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect
import robTheSpire.powers.LuckPower

class FlatGoldAction : AbstractGameAction {
    private val increaseGold: Int
    private var info: DamageInfo? = null

    constructor(target: AbstractCreature?, info: DamageInfo?, goldAmount: Int) {
        var increaseGold1 = goldAmount
        this.info = info
        this.setValues(target, info)
        if (AbstractDungeon.player.hasPower(LuckPower.POWER_ID)) {
            increaseGold1 += AbstractDungeon.player.getPower(LuckPower.POWER_ID).amount
        }
        increaseGold = increaseGold1
        actionType = ActionType.DAMAGE
        duration = 0.1f
    }

    constructor(target: AbstractCreature?, goldAmount: Int) {
        var increaseGold1 = goldAmount
        if (AbstractDungeon.player.hasPower(LuckPower.POWER_ID)) {
            increaseGold1 += AbstractDungeon.player.getPower(LuckPower.POWER_ID).amount
        }
        increaseGold = increaseGold1
        actionType = ActionType.DAMAGE
        duration = 0.1f
        this.target = target
        source = AbstractDungeon.player
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