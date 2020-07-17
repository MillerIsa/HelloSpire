package robTheSpire.actions

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.random.Random
import robTheSpire.characters.TheDefault.Enums.COLOR_GRAY

class StealCardAction : AbstractCardStealingAction(null, { c: AbstractCard, prohibited: Set<String?>? -> c.color != AbstractCard.CardColor.COLORLESS && c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS && c.color == COLOR_GRAY && !prohibited?.contains(c.cardID)!! }) {
    override fun rollRarity(rng: Random?): CardRarity {
        var roll = AbstractDungeon.cardRng.random(99)
        roll += AbstractDungeon.cardBlizzRandomizer
        roll += RARITY_FLAT_PERCENTAGE_BUFF
        println("Rolled a $roll")
        return if (AbstractDungeon.currMapNode == null) getCardRarityFallback(roll) else AbstractDungeon.getCurrRoom().getCardRarity(roll)
    }

    override fun getCardRarityFallback(roll: Int): CardRarity {
        println("WARNING: Current map node was NULL while rolling rarities. Using fallback card rarity method.")
        val rareRate = 3 + RARITY_FLAT_PERCENTAGE_BUFF
        return if (roll < rareRate) {
            CardRarity.RARE
        } else {
            if (roll < 40) CardRarity.UNCOMMON else CardRarity.COMMON
        }
    }

    companion object {
        private const val RARITY_FLAT_PERCENTAGE_BUFF = -5//NOTE: Negative numbers buff rarity
    }
}