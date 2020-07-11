package robTheSpire.cards;


import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import robTheSpire.DefaultMod;
import robTheSpire.actions.StealCardAction;

import robTheSpire.characters.TheDefault;

import static robTheSpire.DefaultMod.makeCardPath;

public class StealACard extends AbstractDefaultCard {
    public static final String ID = DefaultMod.makeID(StealACard.class.getSimpleName());
    private static final CardStrings cardStrings;

    public StealACard() {
        super(ID, cardStrings.NAME, makeCardPath("StealACard.png"), 2, cardStrings.DESCRIPTION, CardType.SKILL, TheDefault.Enums.COLOR_GRAY, CardRarity.BASIC, CardTarget.SELF);
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new StealCardAction());
        System.out.println("Using StealACard!");
    }

    public void upgrade() {
        if (!this.upgraded) {
//            this.exhaust = false;
//            ExhaustiveVariable.setBaseValue(this, 2);
//            ExhaustiveField.ExhaustiveFields.isExhaustiveUpgraded.set(this, true);
            this.upgradeBaseCost(1);
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new robTheSpire.cards.StealACard();
    }


    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        System.out.println("ID for StealACard is: " + ID);
    }
}
