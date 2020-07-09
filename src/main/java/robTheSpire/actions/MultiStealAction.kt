package robTheSpire.actions

import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.utility.WaitAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect


class MultiStealAction(target: AbstractCreature?, private val info: DamageInfo) : AbstractGameAction() {
    override fun update() {
        if (duration == 0.01f && target != null && target.currentHealth > 0) {
            if (info.type != DamageInfo.DamageType.THORNS && info.owner.isDying) {
                isDone = true
                return
            }
            AbstractDungeon.effectList.add(FlashAtkImgEffect(target.hb.cX + MathUtils.random(-100.0f * Settings.scale, 100.0f * Settings.scale), target.hb.cY + MathUtils.random(-100.0f * Settings.scale, 100.0f * Settings.scale), attackEffect))
        }
        tickDuration()
        if (isDone && target != null && target.currentHealth > 0) {
            target.damage(info)
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions()
            }
            addToTop(WaitAction(0.1f))
        }
        if (target == null) {
            isDone = true
        }
    }

    init {
        this.setValues(target, this.info)
        actionType = ActionType.DAMAGE
        attackEffect = AttackEffect.BLUNT_LIGHT
        duration = 0.01f
    }
}