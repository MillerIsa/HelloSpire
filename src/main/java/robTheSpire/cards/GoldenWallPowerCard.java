package robTheSpire.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import robTheSpire.DefaultMod;
import robTheSpire.characters.TheDefault;
import robTheSpire.powers.GoldenWallPower;

import static robTheSpire.DefaultMod.makeCardPath;

public class GoldenWallPowerCard extends AbstractDynamicCard{

        public static final String ID = DefaultMod.makeID(GoldenWallPowerCard.class.getSimpleName());
        public static final String IMG = makeCardPath("Power.png");


        private static final CardRarity RARITY = CardRarity.UNCOMMON;
        private static final CardTarget TARGET = CardTarget.SELF;
        private static final CardType TYPE = CardType.POWER;
        public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

        private static final int COST = 1;
        private static final int UPGRADED_COST = 0;

        private static final int DAMAGE = 0;
        private static final int UPGRADE_PLUS_DMG = 0;

        private static final int ARMOR = 5;


        public GoldenWallPowerCard() {
            super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
            baseDamage = DAMAGE;
        }


        // Actions the card should do.
        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GoldenWallPower(p, ARMOR), ARMOR));
        }


        // Upgraded stats.
        @Override
        public void upgrade() {
            if (!upgraded) {
                upgradeName();
                upgradeBaseCost(UPGRADED_COST);
                initializeDescription();
            }
        }
}
