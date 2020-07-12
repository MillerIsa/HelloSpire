package robTheSpire.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.relics.ChemicalX
import com.megacrit.cardcrawl.ui.panels.EnergyPanel
import robTheSpire.powers.CommonPower

class UncommonPowerAction(private val p: AbstractPlayer, m: AbstractMonster?,
                          private val magicNumber: Int, upgraded: Boolean,
                          damageTypeForTurn: DamageType?, theFreeToPlayOnce: Boolean,
                          energyOnUse: Int) : AbstractGameAction() {
    private val freeToPlayOnce: Boolean = theFreeToPlayOnce
    private val energyOnUse: Int
    private val upgraded: Boolean
    override fun update() {
        var effect = EnergyPanel.totalCount
        if (energyOnUse != -1) {
            effect = energyOnUse
        }
        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2
            p.getRelic(ChemicalX.ID).flash()
        }
        if (upgraded) {
            ++effect
        }
        if (effect > 0) {
            for (i in 0 until effect) {
                AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(p, p,
                        CommonPower(p, p, magicNumber), magicNumber,
                        AttackEffect.BLUNT_LIGHT))
            }
            if (!freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount)
            }
        }
        isDone = true
    }

    init {
        actionType = ActionType.SPECIAL
        this.energyOnUse = energyOnUse
        this.upgraded = upgraded
    }
}