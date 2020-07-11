package robTheSpire.powers

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.powers.AbstractPower
import robTheSpire.DefaultMod
import robTheSpire.util.TextureLoader

class LuckPower(theOwner: AbstractCreature, theAmount: Int) : AbstractPower() {

    init {
        region128 = AtlasRegion(tex128, 0, 0, 128, 128)
        region48 = AtlasRegion(tex48, 0, 0, 48, 48)
        name = NAME
        ID = POWER_ID
        type = PowerType.BUFF
        amount = theAmount
        owner = theOwner
        updateDescription()
        //loadRegion("armor")//TODO: change to clover graphic
    }


    override fun updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]
    }

    companion object {
        @JvmField val POWER_ID: String = DefaultMod.makeID(Companion::class.java.enclosingClass.simpleName)
        private var powerStrings: PowerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID)
        var NAME: String = powerStrings.NAME
        val DESCRIPTIONS: Array<String> = powerStrings.DESCRIPTIONS

        private val tex128 = TextureLoader.getTexture(DefaultMod.makePowerPath("Glowing_Goldicize_128.png"))
        private val tex48 = TextureLoader.getTexture(DefaultMod.makePowerPath("Glowing_Goldicize_48.png"))
    }
}
