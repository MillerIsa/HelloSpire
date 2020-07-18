package robTheSpire.cards

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import robTheSpire.powers.LuckPower

abstract class AbstractGoldStealingCard(id: String?,
                                         img: String?,
                                         cost: Int,
                                         type: CardType?,
                                         color: CardColor?,
                                         rarity:CardRarity?,
                                         target:CardTarget?): AbstractDynamicCard(id, img, cost, type, color, rarity, target) {


    override fun applyPowers() {
        super.applyPowers()
        if(AbstractDungeon.player.hasPower(LuckPower.POWER_ID)){
            val luck: Int = AbstractDungeon.player.getPower(LuckPower.POWER_ID).amount
            goldNumber = baseGoldNumber + luck
            isGoldenNumberModified = true
        }
    }
    var goldNumber// Just like magic number, or any number for that matter, we want our regular, modifiable stat
            = 0
    var baseGoldNumber // And our base stat - the number in it's base state. It will reset to that by default.
            = 0
    var isUpgradedGoldenNumber// A boolean to check whether the number has been upgraded or not.
            = false
    var isGoldenNumberModified// A boolean to check whether the number has been modified or not, for coloring purposes. (red/green)
           = false

    override fun displayUpgrades() { // Display the upgrade - when you click a card to upgrade it
        super.displayUpgrades()
        if (isUpgradedGoldenNumber) { // If we set upgradedDefaultSecondMagicNumber = true in our card.
            goldNumber = baseGoldNumber// Show how the number changes, as out of combat, the base number of a card is shown.
            isGoldenNumberModified= true // Modified = true, color it green to highlight that the number is being changed.
        }
    }

    fun upgradeGoldenNumber(amount: Int) { // If we're upgrading (read: changing) the number. Note "upgrade" and NOT "upgraded" - 2 different things. One is a boolean, and then this one is what you will usually use - change the integer by how much you want to upgrade.
        baseGoldNumber += amount // Upgrade the number by the amount you provide in your card.
        goldNumber = baseGoldNumber// Set the number to be equal to the base value.
        isUpgradedGoldenNumber = true // Upgraded = true - which does what the above method does.
    }

    init {
        // Set all the things to their default values.
        isCostModified = false
        isCostModifiedForTurn = false
        isDamageModified = false
        isBlockModified = false
        isMagicNumberModified = false
        isDefaultSecondMagicNumberModified = false
        println("Image to load: $img")
    }
}