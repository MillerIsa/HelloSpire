package robTheSpire.powers

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.PowerStrings
import robTheSpire.DefaultMod

class BloodIsGoldPower(owner: AbstractCreature?) : AbstractGoldConversionPower(owner!!, 1, 2, "fading") {
    override fun updateDescription() {
        val desc = powerStrings.DESCRIPTIONS
        description = "TODO"
    }

    //Lose gold instead of HP according to conversion rate.
    override fun onLoseHp(damageAmount: Int): Int {
        return convertGoldToResource(damageAmount)
    }

    companion object {
        private val POWER_ID: String = DefaultMod.makeID(Companion::class.java.enclosingClass.simpleName)
        private var powerStrings: PowerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID)
        var NAME: String = powerStrings.NAME
        val DESCRIPTIONS: Array<String> = powerStrings.DESCRIPTIONS
    }

    init {
        name = NAME
        ID = POWER_ID
        type = PowerType.BUFF
    }
}