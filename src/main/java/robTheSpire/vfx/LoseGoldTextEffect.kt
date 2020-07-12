package robTheSpire.vfx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.localization.UIStrings
import com.megacrit.cardcrawl.vfx.AbstractGameEffect

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
class LoseGoldTextEffect(startingAmount: Int) : AbstractGameEffect() {
    private var gold = 0
    private var reachedCenter = false
    private val x: Float
    private var y: Float
    private val destinationY: Float
    private var waitTimer = 1.0f
    private var fadeTimer = 1.0f
    override fun update() {
        if (waitTimer > 0.0f) {
            gold = totalGold
            if (!reachedCenter && y != destinationY) {
                y = MathUtils.lerp(y, destinationY, Gdx.graphics.deltaTime * 9.0f)
                if (Math.abs(y - destinationY) < Settings.UI_SNAP_THRESHOLD) {
                    y = destinationY
                    reachedCenter = true
                }
            } else {
                waitTimer -= Gdx.graphics.deltaTime
                if (waitTimer < 0.0f) {
                    gold = totalGold
                } else {
                    gold = totalGold
                }
            }
        } else {
            y += Gdx.graphics.deltaTime * FADE_Y_SPEED
            fadeTimer -= Gdx.graphics.deltaTime
            color.a = fadeTimer
            if (fadeTimer < 0.0f) {
                isDone = true
            }
        }
    }

    fun ping(amount: Int): Boolean {
        return if (waitTimer > 0.0f) {
            waitTimer = 1.0f
            totalGold += amount
            true
        } else {
            false
        }
    }

    override fun render(sb: SpriteBatch) {
        if (!isDone) {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, "- " + Integer.toString(gold) + TEXT[0], x, y, color)
        }
    }

    override fun dispose() {}

    companion object {
        private var uiStrings: UIStrings = CardCrawlGame.languagePack.getUIString("GainGoldTextEffect")
        val TEXT: Array<String>
        private var totalGold: Int
        private const val WAIT_TIME = 1.0f
        private var FADE_Y_SPEED = 0f
        private const val TEXT_DURATION = 3.0f

        init {
            TEXT = uiStrings.TEXT
            totalGold = 0
            FADE_Y_SPEED = 100.0f * Settings.scale
        }
    }

    init {
        x = AbstractDungeon.player.hb.cX
        y = AbstractDungeon.player.hb.cY
        destinationY = y + 150.0f * Settings.scale
        duration = 3.0f
        startingDuration = 3.0f
        reachedCenter = false
        gold = startingAmount
        totalGold = startingAmount
        color = Color.RED.cpy()
    }
}