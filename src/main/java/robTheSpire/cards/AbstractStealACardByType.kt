package robTheSpire.cards

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.DefaultMod.Companion.makeCardPath
import robTheSpire.actions.StealCardOfTypeAction
import robTheSpire.characters.TheDefault.Enums.COLOR_GRAY

abstract class AbstractStealACardByType @JvmOverloads constructor(theType: CardType, ID: String?, cardStrings: CardStrings, rarity: CardRarity? = CardRarity.COMMON, cardPath: String) : AbstractDefaultCard(ID, cardStrings.NAME,cardPath, 2, cardStrings.DESCRIPTION, CardType.SKILL, COLOR_GRAY, rarity, CardTarget.SELF) {
    private val cardStrings: CardStrings

    override fun use(p: AbstractPlayer, m: AbstractMonster?) {
        addToBot(StealCardOfTypeAction(this.type))
    }

    override fun upgrade() {
        if (!upgraded) {
            //this.magicNumber = this.baseMagicNumber = 2;
            upgradeBaseCost(1)
            //            this.exhaust = false;
//            ExhaustiveVariable.setBaseValue(this, 2);
//            ExhaustiveField.ExhaustiveFields.isExhaustiveUpgraded.set(this, true);
            upgradeName()
            rawDescription = cardStrings.UPGRADE_DESCRIPTION
            initializeDescription()
        }
    }

    //Extending class MUST call this constructor
    init {
        //ExhaustiveVariable.setBaseValue(this,2);
        //this.magicNumber = this.baseMagicNumber = 1;
        exhaust = true
        this.type = theType
        this.cardStrings = cardStrings
    }
}