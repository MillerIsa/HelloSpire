package robTheSpire.powers

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.powers.MinionPower
import com.megacrit.cardcrawl.powers.RegrowPower
import com.megacrit.cardcrawl.vfx.GainPennyEffect
import robTheSpire.DefaultMod.Companion.makeID

class CappedGoldStealPower(owner: AbstractCreature?, amount: Int) : AbstractPower() {
    private var goldCap = 0
    override fun updateDescription() {
        val desc = powerStrings!!.DESCRIPTIONS
        description = desc[0] + amount + desc[1] + desc[2] + (goldCap - amount) + desc[3]
        //this.description = desc[0] + this.amount + desc[1] + (goldCap - this.amount) + desc[2];
    }

    override fun onInitialApplication() {
        super.onInitialApplication()
        val goldGain = Math.min(goldCap, amount)
        if (goldGain > 0) {
            //GameActions.Top.GainGold(goldGain);
            println("Should gain: $goldGain gold now.")
            gainGold(goldGain)
        }
    }

    private fun gainGold(goldGain: Int) {
        gainGold(AbstractDungeon.player, owner, goldGain)
    }

    private fun gainGold(target: AbstractCreature, source: AbstractCreature, goldGain: Int) {
        AbstractDungeon.player.gainGold(goldGain)
        for (i in 0 until goldGain) {
            AbstractDungeon.effectList.add(GainPennyEffect(target, source.hb.cX, source.hb.cY, target.hb.cX, target.hb.cY, true))
        } //TODO: refactor this gold gain to a method in a utility class, also refactor to generalize source and target
        //TODO: When No gold is stolen display that no gold is stolen instead of 'x' gold is stolen
    }

    override fun stackPower(stackAmount: Int) {
        val initialGold = amount
        super.stackPower(stackAmount)
        if (amount > goldCap) {
            amount = goldCap
        }
        val goldGain = amount - initialGold
        if (goldGain > 0) {
            //GameActions.Top.GainGold(goldGain);
            println("Should gain: $goldGain gold now.")
            gainGold(goldGain)
        }
    }

    companion object {
        val POWER_ID = makeID(CappedGoldStealPower::class.java.simpleName)
        private var powerStrings: PowerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID)
        var NAME: String? = null
        val DESCRIPTIONS: Array<String>

        init {
            NAME = powerStrings.NAME
            DESCRIPTIONS = powerStrings.DESCRIPTIONS
        }
    }

    init {
        type = PowerType.DEBUFF
        val m = owner as AbstractMonster
        goldCap = if (m.hasPower(MinionPower.POWER_ID) || m.hasPower(RegrowPower.POWER_ID)) {
            0
        } else if (m.type == AbstractMonster.EnemyType.BOSS) {
            50
        } else if (m.type == AbstractMonster.EnemyType.ELITE) {
            25
        } else {
            10
        }
        this.amount = Math.min(goldCap, amount)
        updateDescription()
        name = NAME
        ID = POWER_ID
        this.owner = owner
        this.amount = amount
        updateDescription()
        loadRegion("fading") //TODO Take the power Icon from "The Animator"
    }
}