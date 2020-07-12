package robTheSpire.cards

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.localization.CardStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import robTheSpire.CardIgnore
import robTheSpire.DefaultMod.Companion.makeCardPath
import robTheSpire.DefaultMod.Companion.makeID
import robTheSpire.characters.TheDefault.Enums.COLOR_GRAY
import robTheSpire.powers.CappedGoldStealPower




@CardIgnore
class Chris : AbstractDefaultCard(ID, cardStrings.NAME, makeCardPath("StealACard.png"), 1, cardStrings.DESCRIPTION, CardType.ATTACK, COLOR_GRAY, CardRarity.COMMON, CardTarget.ENEMY) {
    companion object {
        val ID = makeID(Chris::class.java.simpleName)
        private var cardStrings: CardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
    }

    override fun upgrade() {
        //throw new IllegalAccessException("Upgrading this card isn't allowed because I haven't implemented its upgrade yet."); //TODO: Implement this method
    }

    override fun makeCopy(): AbstractCard {
        return Chris()
    }

    override fun use(p: AbstractPlayer, m: AbstractMonster?) {
        //GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL).StealGold(magicNumber);
        AbstractDungeon.actionManager.addToBottom(ApplyPowerAction(m, p, CappedGoldStealPower(m, 4), 4))
    }

    init {
        magicNumber = 4
        println("Chris card initialized!")
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