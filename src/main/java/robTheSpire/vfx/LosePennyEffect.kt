package robTheSpire.vfx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import com.megacrit.cardcrawl.vfx.ShineLinesEffect

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
class LosePennyEffect(owner: AbstractCreature, x: Float, y: Float, targetX: Float, targetY: Float, showGainEffect: Boolean) : AbstractGameEffect() {
    private var rotationSpeed: Float
    private var x: Float
    private var y: Float
    private var vX: Float
    private var vY: Float
    private val targetX: Float
    private val targetY: Float
    private var alpha = 0.0f
    private var suctionTimer = 0.7f
    private var staggerTimer: Float
    private val showGainEffect: Boolean
    private val owner: AbstractCreature

    constructor(x: Float, y: Float) : this(AbstractDungeon.player, x, y, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true) {}

    override fun update() {
        if (staggerTimer > 0.0f) {
            staggerTimer -= Gdx.graphics.deltaTime
        } else {
            if (alpha != 1.0f) {
                alpha += Gdx.graphics.deltaTime * 2.0f
                if (alpha > 1.0f) {
                    alpha = 1.0f
                }
                color.a = alpha
            }
            rotation += Gdx.graphics.deltaTime * rotationSpeed
            x += Gdx.graphics.deltaTime * vX
            y += Gdx.graphics.deltaTime * vY
            vY -= Gdx.graphics.deltaTime * GRAVITY
            if (suctionTimer > 0.0f) {
                suctionTimer -= Gdx.graphics.deltaTime
            } else {
                vY = MathUtils.lerp(vY, 0.0f, Gdx.graphics.deltaTime * 5.0f)
                vX = MathUtils.lerp(vX, 0.0f, Gdx.graphics.deltaTime * 5.0f)
                x = MathUtils.lerp(x, targetX, Gdx.graphics.deltaTime * 4.0f)
                y = MathUtils.lerp(y, targetY, Gdx.graphics.deltaTime * 4.0f)
                if (Math.abs(x - targetX) < 20.0f) {
                    isDone = true
                    if (MathUtils.randomBoolean()) {
                        CardCrawlGame.sound.play("GOLD_GAIN", 0.1f)
                    }
                    if (!owner.isPlayer) {
                        owner.gainGold(1)
                    }
                    AbstractDungeon.effectsQueue.add(ShineLinesEffect(x, y))
                    var textEffectFound = false
                    var var2: Iterator<*> = AbstractDungeon.effectList.iterator()
                    var e: AbstractGameEffect?
                    while (var2.hasNext()) {
                        e = var2.next() as AbstractGameEffect?
                        if (e is LoseGoldTextEffect && e.ping(1)) {
                            textEffectFound = true
                            break
                        }
                    }
                    if (!textEffectFound) {
                        var2 = AbstractDungeon.effectsQueue.iterator()
                        while (var2.hasNext()) {
                            e = var2.next() as AbstractGameEffect
                            if (e is LoseGoldTextEffect && e.ping(1)) {
                                textEffectFound = true
                            }
                        }
                    }
                    if (!textEffectFound && showGainEffect) {
                        AbstractDungeon.effectsQueue.add(LoseGoldTextEffect(1))
                    }
                }
            }
        }
    }

    override fun render(sb: SpriteBatch) {
        if (staggerTimer <= 0.0f) {
            sb.color = color
            sb.draw(img, x, y, img.packedWidth.toFloat() / 2.0f, img.packedHeight.toFloat() / 2.0f, img.packedWidth.toFloat(), img.packedHeight.toFloat(), scale, scale, rotation)
        }
    }

    override fun dispose() {}

    companion object {
        private var GRAVITY = 0f
        private var START_VY = 0f
        private var START_VY_JITTER = 0f
        private var START_VX = 0f
        private var START_VX_JITTER = 0f
        private var TARGET_JITTER = 0f

        init {
            GRAVITY = 2000.0f * Settings.scale
            START_VY = 800.0f * Settings.scale
            START_VY_JITTER = 400.0f * Settings.scale
            START_VX = 200.0f * Settings.scale
            START_VX_JITTER = 300.0f * Settings.scale
            TARGET_JITTER = 50.0f * Settings.scale
        }
    }

    private var img: AtlasRegion = if (MathUtils.randomBoolean()) {
        ImageMaster.COPPER_COIN_1
    } else {
        ImageMaster.COPPER_COIN_2
    }
    init {
        this.x = x - img.packedWidth.toFloat() / 2.0f
        this.y = y - img.packedHeight.toFloat() / 2.0f
        this.targetX = targetX + MathUtils.random(-TARGET_JITTER, TARGET_JITTER)
        this.targetY = targetY + MathUtils.random(-TARGET_JITTER, TARGET_JITTER * 2.0f)
        this.showGainEffect = showGainEffect
        this.owner = owner
        staggerTimer = MathUtils.random(0.0f, 0.5f)
        vX = MathUtils.random(START_VX - 50.0f * Settings.scale, START_VX_JITTER)
        rotationSpeed = MathUtils.random(500.0f, 2000.0f)
        if (MathUtils.randomBoolean()) {
            vX = -vX
            rotationSpeed = -rotationSpeed
        }
        vY = MathUtils.random(START_VY, START_VY_JITTER)
        scale = Settings.scale
        color = Color(1.0f, 1.0f, 1.0f, 0.0f)
    }
}