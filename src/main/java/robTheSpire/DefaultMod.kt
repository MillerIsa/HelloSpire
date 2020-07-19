package robTheSpire

import basemod.*
import basemod.interfaces.*
import com.badlogic.gdx.Gdx
import com.evacipated.cardcrawl.mod.stslib.Keyword
import com.evacipated.cardcrawl.modthespire.Loader
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.google.gson.Gson
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.TheCity
import com.megacrit.cardcrawl.helpers.CardHelper
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.localization.*
import com.megacrit.cardcrawl.unlock.UnlockTracker
import javassist.NotFoundException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.clapper.util.classutil.*
import robTheSpire.characters.TheDefault
import robTheSpire.events.IdentityCrisisEvent
import robTheSpire.potions.PlaceholderPotion
import robTheSpire.relics.Default.BottledPlaceholderRelic
import robTheSpire.relics.SmokeBombRelic
import robTheSpire.util.IDCheckDontTouchPls
import robTheSpire.util.TextureLoader
import robTheSpire.variables.DefaultCustomVariable
import robTheSpire.variables.DefaultSecondMagicNumber
import robTheSpire.variables.GoldVariable
import java.io.File
import java.io.InputStreamReader
import java.lang.reflect.Modifier
import java.net.URISyntaxException
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.Consumer

//TODO: DON'T MASS RENAME/REFACTOR
// Please don't just mass replace "theDefault" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 3 places.
// I comment those places below, under the place where you set your ID.
//TODO: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "theDefault:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "theDefault". You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier
/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */
@SpireInitializer
class DefaultMod: EditCardsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber, EditCharactersSubscriber, PostInitializeSubscriber {
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    // =============== LOAD THE CHARACTER =================
    override fun receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheDefault.Enums.THE_DEFAULT.toString())
        BaseMod.addCharacter(TheDefault("the Default", TheDefault.Enums.THE_DEFAULT),
                THE_DEFAULT_BUTTON, THE_DEFAULT_PORTRAIT, TheDefault.Enums.THE_DEFAULT)
        receiveEditPotions()
        logger.info("Added " + TheDefault.Enums.THE_DEFAULT.toString())
    }

    // =============== /LOAD THE CHARACTER/ =================
    // =============== POST-INITIALIZE =================
    override fun receivePostInitialize() {
        logger.info("Loading badge image and mod options")

        // Load the Mod Badge
        val badgeTexture = TextureLoader.getTexture(BADGE_IMAGE)

        // Create the Mod Menu
        val settingsPanel = ModPanel()

        // Create the on/off button:
        val enableNormalsButton = ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,  // Position (trial and error it), color, font
                enablePlaceholder,  // Boolean it uses
                settingsPanel,  // The mod panel in which this button will be in
                Consumer { label: ModLabel? -> },  // thing??????? idk
                Consumer { button: ModToggleButton ->  // The actual button:
                    enablePlaceholder = button.enabled // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        val config = SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings)
                        config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder)
                        config.save()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
        settingsPanel.addUIElement(enableNormalsButton) // Add the button to the settings panel. Button is a go.
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel)


        // =============== EVENTS =================

        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to have a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"
        BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent::class.java, TheCity.ID)

        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options")
    }

    // =============== / POST-INITIALIZE/ =================
    // ================ ADD POTIONS ===================
    fun receiveEditPotions() {
        logger.info("Beginning to edit potions")

        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_DEFAULT".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        //BaseMod.addPotion(PlaceholderPotion::class.java, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, TheDefault.Enums.THE_DEFAULT)
        logger.info("Done editing potions")
    }

    // ================ /ADD POTIONS/ ===================
    // ================ ADD RELICS ===================
    override fun receiveEditRelics() {
        logger.info("Adding relics")

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(SmokeBombRelic(), TheDefault.Enums.COLOR_GRAY)

        // This adds a relic to the Shared pool. Every character can find this relic.
        //BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);

        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        //UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID)
        logger.info("Done adding relics!")
    }

    // ================ /ADD RELICS/ ===================
    // ================ ADD CARDS ===================
    override fun receiveEditCards() {
        logger.info("Adding variables")
        //Ignore this
        pathCheck()
        // Add the Custom Dynamic Variables
        logger.info("Add variables")
        // Add the Custom Dynamic variables
        BaseMod.addDynamicVariable(DefaultCustomVariable())
        BaseMod.addDynamicVariable(DefaultSecondMagicNumber())
        BaseMod.addDynamicVariable(GoldVariable())
        logger.info("Adding cards")
        // Add the cards
        // Don't comment out/delete these cards (yet). You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.
        /*
        BaseMod.addCard(new OrbSkill());
        BaseMod.addCard(new DefaultSecondMagicNumberSkill());
        BaseMod.addCard(new DefaultCommonAttack());
        BaseMod.addCard(new DefaultAttackWithVariable());
        BaseMod.addCard(new DefaultCommonSkill());
        BaseMod.addCard(new DefaultCommonPower());
        BaseMod.addCard(new DefaultUncommonSkill());
        BaseMod.addCard(new DefaultUncommonAttack());
        BaseMod.addCard(new DefaultUncommonPower());
        BaseMod.addCard(new DefaultRareAttack());
        BaseMod.addCard(new DefaultRareSkill());
        BaseMod.addCard(new DefaultRarePower());
        BaseMod.addCard(new StrikeGold());
        BaseMod.addCard(new StealACard());



        logger.info("Making sure the cards are unlocked.");
        // Unlock the cards
        // This is so that they are all "seen" in the library, for people who like to look at the card list
        // before playing your mod.
        UnlockTracker.unlockCard(OrbSkill.ID);
        UnlockTracker.unlockCard(DefaultSecondMagicNumberSkill.ID);
        UnlockTracker.unlockCard(DefaultCommonAttack.ID);
        UnlockTracker.unlockCard(DefaultAttackWithVariable.ID);
        UnlockTracker.unlockCard(DefaultCommonSkill.ID);
        UnlockTracker.unlockCard(DefaultCommonPower.ID);
        UnlockTracker.unlockCard(DefaultUncommonSkill.ID);
        UnlockTracker.unlockCard(DefaultUncommonAttack.ID);
        UnlockTracker.unlockCard(DefaultUncommonPower.ID);
        UnlockTracker.unlockCard(DefaultRareAttack.ID);
        UnlockTracker.unlockCard(DefaultRareSkill.ID);
        UnlockTracker.unlockCard(DefaultRarePower.ID);
        UnlockTracker.unlockCard(StrikeGold.ID);
        UnlockTracker.unlockCard(StealACard.ID);

        logger.info("Done adding cards!");
         */try {
            autoAddCards()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: NotFoundException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun receiveEditStrings() {
        logger.info("You seeing this?")
        logger.info("Beginning to edit strings for mod with ID: $modID")

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings::class.java,
                modID + "Resources/localization/eng/DefaultMod-Card-Strings.json")

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings::class.java,
                modID + "Resources/localization/eng/DefaultMod-Power-Strings.json")

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings::class.java,
                modID + "Resources/localization/eng/DefaultMod-Relic-Strings.json")

        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings::class.java,
                modID + "Resources/localization/eng/DefaultMod-Event-Strings.json")

        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings::class.java,
                modID + "Resources/localization/eng/DefaultMod-Potion-Strings.json")

        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings::class.java,
                modID + "Resources/localization/eng/DefaultMod-Character-Strings.json")

        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings::class.java,
                modID + "Resources/localization/eng/DefaultMod-Orb-Strings.json")
        logger.info("Done editing strings")
    }

    // ================ /LOAD THE TEXT/ ===================
    // ================ LOAD THE KEYWORDS ===================
    override fun receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        val gson = Gson()
        val json = Gdx.files.internal(modID + "Resources/localization/eng/DefaultMod-Keyword-Strings.json").readString(StandardCharsets.UTF_8.toString())
        val keywords = gson.fromJson(json, Array<Keyword>::class.java)
        if (keywords != null) {
            for (keyword in keywords) {
                BaseMod.addKeyword( /*getModID().toLowerCase()*/"", keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION)
                println("Keywords are" + Arrays.toString(keyword.NAMES))
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }

    companion object {
        // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
        // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
       val logger: Logger = LogManager.getLogger(DefaultMod::class.java.name)

        // NO
        // DOUBLE NO
        // NU-UH
        var modID: String? = null
            private set (ID: String?) { // DON'T EDIT
            val coolG = Gson() // EY DON'T EDIT THIS
            //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
            val `in` = DefaultMod::class.java.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json") // DON'T EDIT THIS ETHER
            val EXCEPTION_STRINGS = coolG.fromJson(InputStreamReader(`in`, StandardCharsets.UTF_8), IDCheckDontTouchPls::class.java) // OR THIS, DON'T EDIT IT
            logger.info("You are attempting to set your mod ID as: $ID") // NO WHY
            field = if (ID == EXCEPTION_STRINGS.DEFAULTID) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
                throw RuntimeException(EXCEPTION_STRINGS.EXCEPTION) // THIS ALSO DON'T EDIT
            } else if (ID == EXCEPTION_STRINGS.DEVID) { // NO
                EXCEPTION_STRINGS.DEFAULTID // DON'T
            } else { // NO EDIT AREA
                ID // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
            } // NO
            logger.info("Success! ID is $field") // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
        } // NO

        // Mod-settings settings. This is if you want an on/off savable button
        var theDefaultDefaultSettings = Properties()
        const val ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder"
        var enablePlaceholder = true // The boolean we'll be setting on/off (true/false)

        //This is for the in-game mod settings panel.
        private const val MODNAME = "Rob the Spire Mod"
        private const val AUTHOR = "Isaiah Miller" // And pretty soon - You!
        private const val DESCRIPTION = "A base for Slay the Spire to start your own mod from, feat. the Default."

        // =============== INPUT TEXTURE LOCATION =================
        // Colors (RGB)
        // Character Color
        @JvmStatic
        val DEFAULT_GRAY = CardHelper.getColor(64.0f, 70.0f, 70.0f)

        // Potion Colors in RGB
        val PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f) // Orange-ish Red
        val PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f) // Near White
        val PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f) // Super Dark Red/Brown

        // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
        // Card backgrounds - The actual rectangular card.
        private const val ATTACK_DEFAULT_GRAY = "robTheSpireResources/images/512/bg_attack_gold.png"
        private const val SKILL_DEFAULT_GRAY = "robTheSpireResources/images/512/bg_skill_gold.png"
        private const val POWER_DEFAULT_GRAY = "robTheSpireResources/images/512/bg_power_gold_light.png"
        private const val ENERGY_ORB_DEFAULT_GRAY = "robTheSpireResources/images/512/card_default_gray_orb.png"
        private const val CARD_ENERGY_ORB = "robTheSpireResources/images/512/card_small_orb.png"
        private const val ATTACK_DEFAULT_GRAY_PORTRAIT = "robTheSpireResources/images/1024/bg_attack_gold.png"
        private const val SKILL_DEFAULT_GRAY_PORTRAIT = "robTheSpireResources/images/1024/bg_skill_gold.png"
        private const val POWER_DEFAULT_GRAY_PORTRAIT = "robTheSpireResources/images/1024/bg_power_gold.png"
        private const val ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = "robTheSpireResources/images/1024/card_default_gray_orb.png"

        // Character assets
        private const val THE_DEFAULT_BUTTON = "robTheSpireResources/images/charSelect/Button.png"
        private const val THE_DEFAULT_PORTRAIT = "robTheSpireResources/images/charSelect/Thief_Portrait.png"
        const val THE_DEFAULT_SHOULDER_1 = "robTheSpireResources/images/char/defaultCharacter/shoulder.png"
        const val THE_DEFAULT_SHOULDER_2 = "robTheSpireResources/images/char/defaultCharacter/shoulder2.png"
        const val THE_DEFAULT_CORPSE = "robTheSpireResources/images/char/defaultCharacter/corpse.png"

        //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
        const val BADGE_IMAGE = "robTheSpireResources/images/Badge.png"

        // Atlas and JSON files for the Animations
        const val THE_DEFAULT_SKELETON_ATLAS = "robTheSpireResources/images/char/looter/skeleton.atlas"
        const val THE_DEFAULT_SKELETON_JSON = "robTheSpireResources/images/char/looter/skeleton.json"

        // =============== MAKE IMAGE PATHS =================
        @JvmStatic
        fun makeCardPath(resourcePath: String): String {
            return modID + "Resources/images/cards/" + resourcePath
        }
        @JvmStatic
        fun makeRelicPath(resourcePath: String): String {
            return modID + "Resources/images/relics/" + resourcePath
        }
        @JvmStatic
        fun makeRelicOutlinePath(resourcePath: String): String {
            return modID + "Resources/images/relics/outline/" + resourcePath
        }
        @JvmStatic
        fun makeOrbPath(resourcePath: String): String {
            return modID + "Resources/orbs/" + resourcePath
        }
        @JvmStatic
        fun makePowerPath(resourcePath: String): String {
            return modID + "Resources/images/powers/" + resourcePath
        }
        @JvmStatic
        fun makeEventPath(resourcePath: String): String {
            return modID + "Resources/images/events/" + resourcePath
        }

        // ====== NO EDIT AREA ======
        // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
        // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP


        private fun pathCheck() { // ALSO NO
            val coolG = Gson() // NNOPE DON'T EDIT THIS
            //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
            val `in` = DefaultMod::class.java.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json") // DON'T EDIT THISSSSS
            val EXCEPTION_STRINGS = coolG.fromJson(InputStreamReader(`in`, StandardCharsets.UTF_8), IDCheckDontTouchPls::class.java) // NAH, NO EDIT
            val packageName = DefaultMod::class.java.getPackage().name // STILL NO EDIT ZONE
            val resourcePathExists = Gdx.files.internal(modID + "Resources") // PLEASE DON'T EDIT THINGS HERE, THANKS
            if (modID != EXCEPTION_STRINGS.DEVID) { // LEAVE THIS EDIT-LESS
                if (packageName != modID) { // NOT HERE ETHER
                    throw RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + modID) // THIS IS A NO-NO
                } // WHY WOULD U EDIT THIS
                if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                    throw RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + modID + "Resources") // NOT THIS
                } // NO
            } // NO
        } // NO

        // ====== YOU CAN EDIT AGAIN ======
        @JvmStatic
        fun initialize() {
            logger.info("========================= Initializing Rob the Spire Mod. =========================")
            val defaultmod = DefaultMod()
            logger.info("========================= /Rob the Spire Mod initialized./ =========================")
        }

        @Throws(URISyntaxException::class, IllegalAccessException::class, InstantiationException::class, NotFoundException::class, ClassNotFoundException::class)
        private fun autoAddCards() {
            val finder = ClassFinder()
            val url = DefaultMod::class.java.protectionDomain.codeSource.location
            finder.add(File(url.toURI()))
            val filter: ClassFilter = AndClassFilter(
                    NotClassFilter(InterfaceOnlyClassFilter()),
                    NotClassFilter(AbstractClassFilter()),
                    ClassModifiersClassFilter(Modifier.PUBLIC),
                    CardFilter()
            )
            val foundClasses: Collection<ClassInfo> = ArrayList()
            finder.findClasses(foundClasses, filter)
            for (classInfo in foundClasses) {
                val cls = Loader.getClassPool()[classInfo.className]
                if (cls.hasAnnotation(CardIgnore::class.java)) {
                    continue
                }
                var isCard = false
                var superCls = cls
                while (superCls != null) {
                    superCls = superCls.superclass
                    if (superCls == null) {
                        break
                    }
                    if (superCls.name == AbstractCard::class.java.name) {
                        isCard = true
                        break
                    }
                }
                if (!isCard) {
                    continue
                }
                println(classInfo.className)
                val card = Loader.getClassPool().classLoader.loadClass(cls.name).newInstance() as AbstractCard
                BaseMod.addCard(card)
                if (cls.hasAnnotation(CardNoSeen::class.java)) {
                    UnlockTracker.hardUnlockOverride(card.cardID)
                } else {
                    UnlockTracker.unlockCard(card.cardID)
                }
            }
        }

        // ================ /LOAD THE KEYWORDS/ ===================
        // this adds "ModName:" before the ID of any card/relic/power etc.
        // in order to avoid conflicts if any other mod uses the same ID.
        @JvmStatic
        fun makeID(idText: String): String {
            return "$modID:$idText"
        }
    }

    // =============== /MAKE IMAGE PATHS/ =================
    // =============== /INPUT TEXTURE LOCATION/ =================
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    init {
        logger.info("Subscribe to BaseMod hooks")
        BaseMod.subscribe(this)

        /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */modID = "robTheSpire"
        // cool
        // TODO: NOW READ THIS!!!!!!!!!!!!!!!:

        // 1. Go to your resources folder in the project panel, and refactor> rename theDefaultResources to
        // yourModIDResources.

        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project)
        // replace all instances of theDefault with yourModID.
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.

        // 3. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than theDefault. They get loaded before getID is a thing.
        logger.info("Done subscribing")
        logger.info("Creating the color " + TheDefault.Enums.COLOR_GRAY.toString())
        BaseMod.addColor(TheDefault.Enums.COLOR_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB)
        logger.info("Done creating the color")
        logger.info("Adding mod settings")
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theDefaultDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE") // This is the default setting. It's actually set...
        try {
            val config = SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings) // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load() // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        logger.info("Done adding mod settings")
    }
}