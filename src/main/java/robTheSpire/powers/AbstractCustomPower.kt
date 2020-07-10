package robTheSpire.powers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.AbstractPower

abstract class AbstractCustomPower(theOwner: AbstractCreature) : AbstractPower() {

    init {
        this.owner = theOwner
    }

    companion object {
        val MY_ATLAS: TextureAtlas = TextureAtlas(Gdx.files.internal("Test_Atlas.atlas"))
    }

    override fun loadRegion(fileName: String) {
        region48 = MY_ATLAS.findRegion("48/$fileName")
        region128 = MY_ATLAS.findRegion("128/$fileName")
        println("Atlas is: $atlas")
        println("48 path is 48/$fileName")
        println("Region 48 is: $region48")
    }
}
