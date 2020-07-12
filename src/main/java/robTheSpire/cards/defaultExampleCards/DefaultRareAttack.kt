package robTheSpire.cards.defaultExampleCards

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.animations.VFXAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect
import robTheSpire.CardIgnore
import robTheSpire.DefaultMod.Companion.makeCardPath
import robTheSpire.DefaultMod.Companion.makeID
import robTheSpire.cards.AbstractDynamicCard
import robTheSpire.characters.TheDefault.Enums.COLOR_GRAY

@CardIgnore
class DefaultRareAttack : AbstractDynamicCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    // Actions the card should do.
    override fun use(p: AbstractPlayer, m: AbstractMonster?) {
        if (m != null) {
            AbstractDungeon.actionManager.addToBottom(VFXAction(WeightyImpactEffect(m.hb.cX, m.hb.cY)))
        }
        AbstractDungeon.actionManager.addToBottom(
                DamageAction(m, DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.NONE))
    }

    //Upgraded stats.
    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeDamage(UPGRADE_PLUS_DMG)
        }
    }

    companion object {
        /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * TOUCH Deal 30(35) damage.
     */
        // TEXT DECLARATION 
        val ID = makeID(DefaultRareAttack::class.java.simpleName)
        val IMG = makeCardPath("Attack.png")

        // /TEXT DECLARATION/
        // STAT DECLARATION 	
        private val RARITY = CardRarity.RARE
        private val TARGET = CardTarget.ENEMY
        private val TYPE = CardType.ATTACK
        val COLOR = COLOR_GRAY
        private const val COST = 2
        private const val DAMAGE = 30
        private const val UPGRADE_PLUS_DMG = 5
    }

    // /STAT DECLARATION/
    init {
        baseDamage = DAMAGE
    }
}