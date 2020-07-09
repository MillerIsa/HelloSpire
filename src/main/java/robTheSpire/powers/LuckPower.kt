package robTheSpire.powers

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.powers.AbstractPower
import robTheSpire.DefaultMod

class LuckPower(theOwner: AbstractCreature, theAmount: Int) : AbstractPower() {


    override fun updateDescription() {
        description = "TODO" //TODO: update this description
    }

    companion object {
        @JvmField val POWER_ID: String = DefaultMod.makeID(Companion::class.java.enclosingClass.simpleName)
        private var powerStrings: PowerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID)
        var NAME: String = powerStrings.NAME
        val DESCRIPTIONS: Array<String> = powerStrings.DESCRIPTIONS
    }

    init {
        name = NAME
        ID = POWER_ID
        type = PowerType.BUFF
        amount = theAmount
        owner = theOwner
        updateDescription()
        loadRegion("armor")//TODO: change to clover graphic
    }
}
