package robTheSpire.powers

import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.PowerStrings
import robTheSpire.DefaultMod
import robTheSpire.cards.MultiStealAttack


class GoldenWallPower(theOwner: AbstractCreature, armorAmt: Int) : AbstractGoldConversionPower(theOwner, armorAmt,2, "armor") {
    override fun playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_METALLICIZE", 0.05f)
    }

    override fun updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + conversionRate + DESCRIPTIONS[2]
    }

    override fun atEndOfTurnPreEndTurnCards(isPlayer: Boolean) {
        flash() //TODO: Only flash when you would gain at least one block
        val blockToGain: Int = convertGoldToResource(amount)
        println("Should gain $blockToGain block.")
        addToBot(GainBlockAction(owner, owner, blockToGain))
    }



    companion object {
        val POWER_ID: String = DefaultMod.makeID(Companion::class.java.enclosingClass.simpleName)
        private var powerStrings: PowerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID)
        var NAME: String = powerStrings.NAME
        val DESCRIPTIONS: Array<String> = powerStrings.DESCRIPTIONS
    }

    init {
        name = NAME
        ID = MultiStealAttack.ID//POWER_ID
        type = PowerType.BUFF
        amount = armorAmt
    }
}
