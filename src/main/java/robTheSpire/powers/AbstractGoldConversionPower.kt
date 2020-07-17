package robTheSpire.powers

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import robTheSpire.vfx.LosePennyEffect
import kotlin.math.floor
import kotlin.math.roundToInt


abstract class AbstractGoldConversionPower(theOwner: AbstractCreature, stackAmount: Int, protected val conversionRate: Int, private val regionName: String) : AbstractPower() {


        companion object {
            fun convertResourceToGold(){
                //TODO: write this method
            }
            fun convertGoldToResource(resourceAmount: Double, conversionRate : Double): Int {
                val rawGoldToLose = conversionRate * resourceAmount
                var goldToLose = rawGoldToLose.coerceAtMost(AbstractDungeon.player.gold.toDouble())
                val remainder = goldToLose % conversionRate
                goldToLose -= remainder
                loseGold(goldToLose.toInt())
                return (goldToLose / conversionRate).toInt()
            }
            private fun loseGold(goldStealer: AbstractCreature, victim: AbstractCreature, goldLoss: Int) {
                AbstractDungeon.player.loseGold(goldLoss)
                for (i in 0 until goldLoss) {
                    AbstractDungeon.effectList.add(LosePennyEffect(goldStealer, victim.hb.cX, victim.hb.cY, goldStealer.hb.cX, goldStealer.hb.cY - 600, true))
                }
            }
            private fun loseGold(goldLoss: Int) {
                loseGold(AbstractDungeon.player, AbstractDungeon.player, goldLoss)
            }
        }

    //returns how many resource units the gold converts into
    fun convertGoldToResource(resourceAmount: Int): Int {
       return Companion.convertGoldToResource(resourceAmount.toDouble(), conversionRate.toDouble())
    }


    init {
        type = PowerType.BUFF
        amount = stackAmount
        updateDescription()
        owner = theOwner
        //loadRegion(regionName)
    }
}