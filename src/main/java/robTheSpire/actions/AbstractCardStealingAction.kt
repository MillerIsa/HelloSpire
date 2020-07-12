package robTheSpire.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity
import com.megacrit.cardcrawl.cards.AbstractCard.CardType
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.CardLibrary
import com.megacrit.cardcrawl.random.Random
import com.megacrit.cardcrawl.screens.CardRewardScreen
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect
import java.util.*

abstract class AbstractCardStealingAction(type: CardType?, filter: (AbstractCard, Set<String?>?) -> Boolean) : AbstractGameAction() {
    private var retrieveCard = false
    private val anyCard: CardGroup
    private val type: CardType?

    public interface CardFilterInterface {
        fun cardIsAllowed(c: AbstractCard, prohibited: Set<String?>?): Boolean
    }

    override fun update() {
        val generatedCards = generateCardChoices()
        if (duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(generatedCards, CardRewardScreen.TEXT[1], true)
        } else {
            if (!retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    val disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy()
                    if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
                        disCard.upgrade()
                    }
                    disCard.current_x = -1000.0f * Settings.scale
                    if (amount == 1) {
                        AbstractDungeon.effectList.add(ShowCardAndObtainEffect(disCard, Settings.WIDTH * 3.0f / 4.0f, Settings.HEIGHT / 2.0f))
                        println("Added card $disCard and displayed deck-add effect.")
                    } else {
                        throw IllegalStateException("Tried to steal more than one card. This is invalid because it is only possible to steal one card.")
                    }
                    AbstractDungeon.cardRewardScreen.discoveryCard = null
                }
                retrieveCard = true
            }
        }
        tickDuration()
    }

    private fun generateCardChoices(): ArrayList<AbstractCard> {
        val derp = ArrayList<AbstractCard>()
        while (derp.size != 3) {
            var dupe = false
            var tmp: AbstractCard
            val rarity = rollRarity()
            tmp = getAnyColorCard(rarity)
            println("Picked Rarity: $rarity")
            println("Temp card picked $tmp")
            for (c in derp) {
                if (c.cardID == tmp.cardID) {
                    dupe = true
                    break
                }
            }
            if (!dupe) {
                derp.add(tmp.makeCopy())
            }
        }
        for (c in derp) {
            println(c.cardID)
        }
        return derp
    }

    protected fun rollRarity(): CardRarity {
        return rollRarity(AbstractDungeon.cardRng)
    }

    protected open fun rollRarity(rng: Random?): CardRarity {
        var roll = AbstractDungeon.cardRng.random(99)
        roll += AbstractDungeon.cardBlizzRandomizer
        return if (AbstractDungeon.currMapNode == null) getCardRarityFallback(roll) else AbstractDungeon.getCurrRoom().getCardRarity(roll)
    }

    protected open fun getCardRarityFallback(roll: Int): CardRarity {
        println("WARNING: Current map node was NULL while rolling rarities. Using fallback card rarity method.")
        val rareRate = 3
        return if (roll < rareRate) {
            CardRarity.RARE
        } else {
            if (roll < 40) CardRarity.UNCOMMON else CardRarity.COMMON
        }
    }

    private fun getCardGroup(prohibited: Set<String?>, filter:  (AbstractCard, Set<String?>?) -> Boolean): CardGroup {
        val temp = CardGroup(CardGroup.CardGroupType.UNSPECIFIED)
        for ((_, c) in CardLibrary.cards) {
            if (filter(c, prohibited) && (type == null || c.type == type)) {
                temp.addToBottom(c)
            }
        }
        println("Steal-able Thief Cards: " + temp.cardNames.toString())
        return temp
    }

    //Returns a card based on the rarity roll passed to this method
    protected fun getAnyColorCard(rarity: CardRarity): AbstractCard {
        var rarity = rarity
        anyCard.shuffle(AbstractDungeon.cardRng)
        if (type == CardType.POWER && rarity == CardRarity.COMMON) {
            rarity = CardRarity.UNCOMMON
        }
        return anyCard.getRandomCard(true, rarity)
    }

    companion object {
        /*prohibitedIDs.add(DefaultAttackWithVariable.ID);
        prohibitedIDs.add(DefaultCommonAttack.ID);
        prohibitedIDs.add(DefaultCommonPower.ID);
        prohibitedIDs.add(DefaultCommonSkill.ID);
        prohibitedIDs.add(DefaultRareSkill.ID);
        prohibitedIDs.add(DefaultRareAttack.ID);
        prohibitedIDs.add(DefaultRarePower.ID);
        prohibitedIDs.add(DefaultUncommonSkill.ID);
        prohibitedIDs.add(DefaultUncommonAttack.ID);
        prohibitedIDs.add(DefaultUncommonPower.ID);
        prohibitedIDs.add(OrbSkill.ID);
        prohibitedIDs.add(DefaultSecondMagicNumberSkill.ID); */
        //TODO: add prohibited cards
        private val prohibitedCardIDs: Set<String?>
            private get() {
                val prohibitedIDs: Set<String?> = HashSet()
                /*prohibitedIDs.add(DefaultAttackWithVariable.ID);
        prohibitedIDs.add(DefaultCommonAttack.ID);
        prohibitedIDs.add(DefaultCommonPower.ID);
        prohibitedIDs.add(DefaultCommonSkill.ID);
        prohibitedIDs.add(DefaultRareSkill.ID);
        prohibitedIDs.add(DefaultRareAttack.ID);
        prohibitedIDs.add(DefaultRarePower.ID);
        prohibitedIDs.add(DefaultUncommonSkill.ID);
        prohibitedIDs.add(DefaultUncommonAttack.ID);
        prohibitedIDs.add(DefaultUncommonPower.ID);
        prohibitedIDs.add(OrbSkill.ID);
        prohibitedIDs.add(DefaultSecondMagicNumberSkill.ID); */println("Prohibited Cards $prohibitedIDs")
                return prohibitedIDs
            }
    }

    /**
     * @param  filter This filter should specify which types of cards can be stolen.
     */
    init {
        actionType = ActionType.CARD_MANIPULATION
        duration = Settings.ACTION_DUR_FAST
        this.type = type
        amount = 1
        anyCard = getCardGroup(prohibitedCardIDs, filter)
    }
}