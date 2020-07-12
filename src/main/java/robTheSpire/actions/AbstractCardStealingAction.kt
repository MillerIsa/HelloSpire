package robTheSpire.actions;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import robTheSpire.cards.defaultExampleCards.DefaultUncommonPower;
import robTheSpire.cards.defaultExampleCards.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public abstract class AbstractCardStealingAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    private final CardGroup anyCard;
    private final AbstractCard.CardType type;


    protected interface CardFilter {
        boolean cardIsAllowed(AbstractCard c, Set<String> prohibited);
    }

    /**
     * @param  filter This filter should specify which types of cards can be stolen.
     */
    public AbstractCardStealingAction(AbstractCard.CardType type, CardFilter filter) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.type = type;
        this.amount = 1;
        anyCard = getCardGroup(getProhibitedCardIDs(), filter);
    }


    public void update() {
        ArrayList<AbstractCard> generatedCards = this.generateCardChoices();
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(generatedCards, CardRewardScreen.TEXT[1], true);
        } else {
            if (!this.retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
                        disCard.upgrade();
                    }

                    disCard.current_x = -1000.0F * Settings.scale;
                    if (this.amount == 1) {
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(disCard, Settings.WIDTH * 3.0f / 4.0f, Settings.HEIGHT / 2.0f));
                        System.out.println("Added card " + disCard + " and displayed deck-add effect.");
                    } else {
                        throw new IllegalStateException("Tried to steal more than one card. This is invalid because it is only possible to steal one card.");
                    }

                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                }

                this.retrieveCard = true;
            }

        }
        this.tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> derp = new ArrayList<>();

        while (derp.size() != 3) {
            boolean dupe = false;
            AbstractCard tmp;
            AbstractCard.CardRarity rarity = rollRarity();
            tmp = this.getAnyColorCard(rarity);
            System.out.println("Picked Rarity: " + rarity);
            System.out.println("Temp card picked " + tmp);

            for (AbstractCard c : derp) {
                if (c.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }

            if (!dupe) {
                derp.add(tmp.makeCopy());
            }
        }
        for (AbstractCard c : derp) {
            System.out.println(c.cardID);
        }
        return derp;
    }

    protected AbstractCard.CardRarity rollRarity() {
        return rollRarity(AbstractDungeon.cardRng);
    }


    protected AbstractCard.CardRarity rollRarity(Random rng) {
        int roll = AbstractDungeon.cardRng.random(99);
        roll += AbstractDungeon.cardBlizzRandomizer;
        return AbstractDungeon.currMapNode == null ? getCardRarityFallback(roll) : AbstractDungeon.getCurrRoom().getCardRarity(roll);
    }

    protected AbstractCard.CardRarity getCardRarityFallback(int roll) {
        System.out.println("WARNING: Current map node was NULL while rolling rarities. Using fallback card rarity method.");
        int rareRate = 3;
        if (roll < rareRate) {
            return AbstractCard.CardRarity.RARE;
        } else {
            return roll < 40 ? AbstractCard.CardRarity.UNCOMMON : AbstractCard.CardRarity.COMMON;
        }
    }

    private CardGroup getCardGroup(Set<String> prohibited, CardFilter filter) {
        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (final Map.Entry<String, AbstractCard> cardEntry : CardLibrary.cards.entrySet()) {
            AbstractCard c = cardEntry.getValue();
            if (filter.cardIsAllowed(c, prohibited) && (this.type == null || c.type == this.type)) {
                temp.addToBottom(c);
            }
        }
        System.out.println("Steal-able Thief Cards: " + temp.getCardNames().toString());
        return temp;
    }


    //Returns a card based on the rarity roll passed to this method
    protected AbstractCard getAnyColorCard(AbstractCard.CardRarity rarity) {
        anyCard.shuffle(AbstractDungeon.cardRng);
        if(this.type == AbstractCard.CardType.POWER && rarity == AbstractCard.CardRarity.COMMON){
            rarity = AbstractCard.CardRarity.UNCOMMON;
        }
        return anyCard.getRandomCard(true, rarity);
    }

    //TODO: add prohibited cards
    private static Set<String> getProhibitedCardIDs() {
        Set<String> prohibitedIDs = new HashSet<>();
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
        System.out.println("Prohibited Cards " + prohibitedIDs.toString());
        return prohibitedIDs;
    }

}