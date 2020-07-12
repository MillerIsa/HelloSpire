package robTheSpire.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.utility.WaitAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect

class DamageToGoldAction(target: AbstractCreature?, private val info: DamageInfo, effect: AttackEffect?) : AbstractGameAction() {
    override fun update() {
        if (duration == 0.5f) {
            AbstractDungeon.effectList.add(FlashAtkImgEffect(target.hb.cX, target.hb.cY, attackEffect))
        }
        tickDuration()
        if (isDone) {
            target.damage(info)
            //this.addToTop(new HealAction(this.source, this.source, this.target.lastDamageTaken));
            //FlatGoldAction(AbstractCreature target, DamageInfo info, int goldAmount)
            if (target.lastDamageTaken > 0) {
                addToTop(FlatGoldAction(target, info, target.lastDamageTaken))
                addToTop(WaitAction(0.1f))
            }
        }
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions()
        }
    }

    init {
        this.setValues(target, info)
        actionType = ActionType.DAMAGE
        attackEffect = effect
    }
}