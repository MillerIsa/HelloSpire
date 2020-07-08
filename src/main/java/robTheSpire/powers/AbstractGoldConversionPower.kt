package robTheSpire.powers

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import robTheSpire.vfx.LosePennyEffect


abstract class AbstractGoldConversionPower(theOwner: AbstractCreature, stackAmount: Int, protected val conversionRate: Int, private val regionName: String) : AbstractPower() {

    private fun loseGold(goldLoss: Int) {
        loseGold(AbstractDungeon.player, owner, goldLoss)
    }

    private fun loseGold(goldStealer: AbstractCreature, victim: AbstractCreature, goldLoss: Int) {
        AbstractDungeon.player.loseGold(goldLoss)
        for (i in 0 until goldLoss) {
            AbstractDungeon.effectList.add(LosePennyEffect(goldStealer, victim.hb.cX, victim.hb.cY, goldStealer.hb.cX, goldStealer.hb.cY - 600, true))
        }
    }


    //returns how many resource units the gold converts into
    protected fun convertGoldToResource(resourceAmount: Int): Int {
        val rawGoldToLose = conversionRate * resourceAmount
        var goldToLose = rawGoldToLose.coerceAtMost(AbstractDungeon.player.gold)
        val remainder = goldToLose % conversionRate
        goldToLose -= remainder
        loseGold(goldToLose)
        return goldToLose / conversionRate
    }


    init {
        type = PowerType.BUFF
        this.owner = theOwner
        amount = stackAmount
        updateDescription()
        loadRegion(regionName)
    }
}