package robTheSpire.powers

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.PowerStrings
import robTheSpire.DefaultMod
import robTheSpire.util.TextureLoader

class BloodIsGoldPower(owner: AbstractCreature?) : AbstractGoldConversionPower(owner!!, 1, 2, "Goldicize") {

    init {
        name = NAME
        ID = POWER_ID
        type = PowerType.BUFF

        // We load those txtures here.
        region128 = AtlasRegion(tex128, 0, 0, 128, 128)
        region48 = AtlasRegion(tex48, 0, 0, 48,48)
    }

    override fun updateDescription() {
        val desc = powerStrings.DESCRIPTIONS
        description = "TODO"
    }

    //Lose gold instead of HP according to conversion rate.
    override fun onLoseHp(damageAmount: Int): Int {
        return damageAmount - convertGoldToResource(damageAmount)
    }

    companion object {
        private val POWER_ID: String = DefaultMod.makeID(Companion::class.java.enclosingClass.simpleName)
        private var powerStrings: PowerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID)
        var NAME: String = powerStrings.NAME
        val DESCRIPTIONS: Array<String> = powerStrings.DESCRIPTIONS

        private val tex128 = TextureLoader.getTexture(DefaultMod.makePowerPath("Gold_Heart_128.png"))
        private val tex48 = TextureLoader.getTexture(DefaultMod.makePowerPath("Gold_Heart_48.png"))
    }


}