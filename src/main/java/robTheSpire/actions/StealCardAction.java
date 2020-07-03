package robTheSpire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import robTheSpire.cards.*;
import robTheSpire.characters.TheDefault;

import java.util.*;

public class StealCardAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    private static final CardType CARD_TYPE = null;
    private final CardGroup anyCard;

    public StealCardAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = 1;
        anyCard = getCardGroup(getProhibitedCardIDs());
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
                        //AbstractDungeon.player.masterDeck.addToTop(disCard);
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
            tmp = this.getAnyColorCard(AbstractDungeon.rollRarity());

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

        return derp;
    }
/*
    private void obtainCard(AbstractCard c){
        AbstractDungeon AddCardToDeckAction
    }
    */
/*
    public void moveToDiscardPile(final AbstractCard c) {
        this.resetCardBeforeMoving(c);
        c.shrink();
        c.darken(false);
        AbstractDungeon.getCurrRoom().souls.discard(c);
        AbstractDungeon.player.onCardDrawOrDiscard();
    }*/

    private static CardGroup getCardGroup(Set<String> prohibited) {
        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (final Map.Entry<String, AbstractCard> cardEntry : CardLibrary.cards.entrySet()) {
            AbstractCard c = cardEntry.getValue();
//            System.out.println("c's cardID" + c.cardID);
//            System.out.println("prohibited.contains(c.cardID): " + prohibited.contains(c.cardID));
            if (c.color != AbstractCard.CardColor.COLORLESS && c.type != AbstractCard.CardType.CURSE &&
                    c.type != AbstractCard.CardType.STATUS && c.color == TheDefault.Enums.COLOR_GRAY &&
                    !prohibited.contains(c.cardID)) {
                temp.addToBottom(c);
            }
        }
       System.out.println("Stealable Thief Cards: " + temp.getCardNames().toString());
        return temp;
    }

    //Returns a card based on the rarity roll passed to this method
    private AbstractCard getAnyColorCard(final AbstractCard.CardRarity rarity) {
        anyCard.shuffle(AbstractDungeon.cardRng);
        return anyCard.getRandomCard(true/*, rarity*/);//TODO: reimpliment Rarity behavior
    }

    //TODO: add prohibited cards
    private static Set<String> getProhibitedCardIDs() {
        Set<String> prohibitedIDs = new HashSet<>();
        prohibitedIDs.add(DefaultAttackWithVariable.ID);
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
        prohibitedIDs.add(DefaultSecondMagicNumberSkill.ID);
        System.out.println("Prohibited Cards " + prohibitedIDs.toString());
        return prohibitedIDs;
    }

}
