package helloSpire.cards;


import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helloSpire.DefaultMod;
import helloSpire.actions.DamageToGoldAction;
import helloSpire.characters.TheDefault;

import static helloSpire.DefaultMod.makeCardPath;

public class DamageToGoldStrike extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(DamageToGoldStrike.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Thief.png");

    // STAT DECLARATION
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int DAMAGE = 1;
    private static final int UPGRADE_PLUS_DMG = 1;

    public DamageToGoldStrike() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = 20;
        this.magicNumber = this.baseMagicNumber; //TODO: Modify Magic number
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageToGoldAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), this.magicNumber));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(5);
            this.upgradeMagicNumber(5);
        }

    }

    public AbstractCard makeCopy() {
        return new helloSpire.cards.DamageToGoldStrike();
    }

}
