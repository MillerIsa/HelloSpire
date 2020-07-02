package robTheSpire.cards;


import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import robTheSpire.actions.StealCardOfTypeAction;
import robTheSpire.characters.TheDefault;

import static robTheSpire.DefaultMod.makeCardPath;

public abstract class AbstractStealACardByType extends AbstractDefaultCard {
    public final CardType type;
    private final CardStrings cardStrings;

    //Extending class MUST call this constructor
    public AbstractStealACardByType(CardType type, String ID, CardStrings cardStrings) {
        super(ID, cardStrings.NAME, makeCardPath("StealACard.png"), 1, cardStrings.DESCRIPTION, CardType.SKILL, TheDefault.Enums.COLOR_GRAY, CardRarity.BASIC, CardTarget.SELF);
        //ExhaustiveVariable.setBaseValue(this,2);
        //this.magicNumber = this.baseMagicNumber = 1;
        this.exhaust = true;
        this.type = type;
        this.cardStrings = cardStrings;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new StealCardOfTypeAction(this.type));
    }

    public void upgrade() {
        if (!this.upgraded) {
            //this.magicNumber = this.baseMagicNumber = 2;
            this.exhaust = false;
            ExhaustiveVariable.setBaseValue(this, 2);
            ExhaustiveField.ExhaustiveFields.isExhaustiveUpgraded.set(this, true);
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StealASkill();
    }

}
