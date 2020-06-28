package helloSpire.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import helloSpire.DefaultMod;
import helloSpire.characters.TheDefault;
import helloSpire.powers.CappedGoldStealPower;

import static helloSpire.DefaultMod.makeCardPath;


public class Chris extends AbstractDefaultCard {
    public static final String ID = DefaultMod.makeID(Chris.class.getSimpleName());
    private static final CardStrings cardStrings;

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }

    public Chris() {
        super(ID, cardStrings.NAME, makeCardPath("StealACard.png"), 1, cardStrings.DESCRIPTION, CardType.SKILL, TheDefault.Enums.COLOR_GRAY, CardRarity.BASIC, CardTarget.ENEMY);
        this.magicNumber = 4;
        System.out.println("Chris card initialized!");
    }


    @Override
    public void upgrade() {
        //throw new IllegalAccessException("Upgrading this card isn't allowed because I haven't implemented its upgrade yet."); //TODO: Implement this method
    }

    @Override
    public AbstractCard makeCopy() {
        return new Chris();
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL).StealGold(magicNumber);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new CappedGoldStealPower(m, 4), 4));

    }
}

/*

                Copyright 2019 EatYourBeatS
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.

     This class is MODIFIED from a file in the repository at: https://github.com/EatYourBeetS/STS-AnimatorMod/blob/master/EatYourBeetSVG/
 */