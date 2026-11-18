package com.vortessence.mahjong.ui.components.tiles

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.vortessence.mahjong.R
import com.vortessence.mahjong.data.model.TileSet

/** Natural pattern of the face material, also visible on new tiles. */
enum class GrainKind { NONE, BONE_PORES, CELLULOID_LINES, WOOD_GRAIN, STONE_VEINS }

/**
 * One colour variant of a tile set.
 *
 * A tile is built from the face material (face and the upper part of its sides) and a backing
 * material (the lower part of the sides). [backingStart] is where the backing begins, as a
 * fraction of the tile's thickness; 1 makes a one-tone tile made of a single material.
 */
class TileColorway(
    @param:StringRes val labelRes: Int,
    val faceLight: Color,
    val faceDark: Color,
    val sideLight: Color,
    val sideDeep: Color,
    val back: Color = sideLight,
    val backDeep: Color = sideDeep,
    val backingStart: Float = 1f,
)

/** How the symbols look: [saturation] 1 keeps the artwork colours, [monoTint] recolours them. */
class InkStyle(
    val saturation: Float = 1f,
    val brightness: Float = 1f,
    val monoTint: Color? = null,
    val monoAmount: Float = 0f,
)

/**
 * How the material ages. Amounts are roughly 0..1; counts are per tile.
 *
 * - [faceTint] / [backTint]: colour the material shifts toward (multiplied on).
 * - [grime], [yellowing]: dirt along the rim, uneven discolouring of the face.
 * - [rubOff]: patches where the paint is gone; [rubSpotScale] small for paint drop-outs,
 *   large for print rubbing away.
 * - [inkFade]: paint fading toward the face colour; [inkGrime]: dirt settling in engraved grooves.
 */
class WearStyle(
    val faceTint: Color = Color.White,
    val backTint: Color = Color.White,
    val grime: Float = 0f,
    val yellowing: Float = 0f,
    val scratches: Int = 0,
    val scratchStrength: Float = 0.6f,
    val cracks: Int = 0,
    val dents: Int = 0,
    val chips: Int = 0,
    val stains: Int = 0,
    val rubOff: Float = 0f,
    val rubSpotScale: Float = 1f,
    val inkFade: Float = 0f,
    val inkGrime: Float = 0f,
    val ink: InkStyle,
    val gloss: Float,
)

/**
 * Everything that makes a tile set look like its material.
 *
 * [gloss] is the strength of the lamp's reflection, [glossSharpness] goes from a soft, waxy sheen
 * (0) to a small, glassy hot spot (1). [rim] is how much the polished edge catches the light.
 * [engraving] is how deeply the symbols are cut into the face (0 for printed ones); it adds a
 * shadow along the upper-left wall of each groove.
 */
class TileMaterial(
    val set: TileSet,
    val colorways: List<TileColorway>,
    val grain: GrainKind = GrainKind.NONE,
    val gloss: Float,
    val glossSharpness: Float,
    val rim: Float,
    val engraving: Float = 0f,
    val ink: InkStyle = InkStyle(),
    val worn: WearStyle,
) {
    fun colorway(index: Int): TileColorway = colorways.getOrElse(index) { colorways.first() }
}

object TileMaterials {

    fun of(set: TileSet): TileMaterial = when (set) {
        TileSet.BONE_BAMBOO -> BoneBamboo
        TileSet.CATALIN -> Catalin
        TileSet.CELLULOID -> Celluloid
        TileSet.MELAMINE -> Melamine
        TileSet.RIICHI -> Riichi
        TileSet.ACRYLIC -> Acrylic
        TileSet.WOOD -> Wood
        TileSet.JADE -> Jade
        TileSet.TRAVEL -> Travel
    }

    /** Pre-1950s: cream bone with dark pores, dovetailed onto a thick bamboo back. */
    private val BoneBamboo = TileMaterial(
        set = TileSet.BONE_BAMBOO,
        colorways = listOf(
            TileColorway(
                labelRes = R.string.tile_colorway_natural,
                faceLight = Color(0xFFF6EEDC), faceDark = Color(0xFFEADFC6),
                sideLight = Color(0xFFDCCDB0), sideDeep = Color(0xFFC9B793),
                back = Color(0xFFC9A266), backDeep = Color(0xFFA9813F),
                backingStart = 0.45f,
            ),
        ),
        grain = GrainKind.BONE_PORES,
        gloss = 0.12f, glossSharpness = 0f, rim = 0.6f,
        // Hand-cut, fairly deep
        engraving = 0.9f,
        // Earthy pigments
        ink = InkStyle(saturation = 0.8f, brightness = 1.02f),
        worn = WearStyle(
            faceTint = Color(0xFFF0DDB4), backTint = Color(0xFFC8A27A),
            grime = 0.16f, yellowing = 0.8f, scratches = 3, stains = 2, chips = 10,
            // Paint wears out of the hand-cut grooves, which fill with dirt
            rubOff = 0.25f, rubSpotScale = 0.6f,
            inkFade = 0.3f, inkGrime = 0.15f,
            ink = InkStyle(saturation = 0.75f, brightness = 1.02f),
            gloss = 0.08f,
        ),
    )

    /** 1920s–50s resin: glassy and heavy, ages from ivory to deep butterscotch. */
    private val Catalin = TileMaterial(
        set = TileSet.CATALIN,
        colorways = listOf(
            TileColorway(
                labelRes = R.string.tile_colorway_green_back,
                faceLight = Color(0xFFFBF4E2), faceDark = Color(0xFFF1E6CC),
                sideLight = Color(0xFFE2D3B4), sideDeep = Color(0xFFD0BE98),
                back = Color(0xFF2F6B45), backDeep = Color(0xFF1F4F31),
                backingStart = 0.55f,
            ),
            TileColorway(
                labelRes = R.string.tile_colorway_red_back,
                faceLight = Color(0xFFFBF4E2), faceDark = Color(0xFFF1E6CC),
                sideLight = Color(0xFFE2D3B4), sideDeep = Color(0xFFD0BE98),
                back = Color(0xFF9A2A22), backDeep = Color(0xFF741A15),
                backingStart = 0.55f,
            ),
            TileColorway(
                labelRes = R.string.tile_colorway_solid,
                faceLight = Color(0xFFFBF4E2), faceDark = Color(0xFFF1E6CC),
                sideLight = Color(0xFFE2D3B4), sideDeep = Color(0xFFD0BE98),
            ),
        ),
        gloss = 0.38f, glossSharpness = 0.6f, rim = 1f,
        engraving = 0.9f,
        ink = InkStyle(saturation = 1.15f, brightness = 0.95f),
        worn = WearStyle(
            faceTint = Color(0xFFF4C988), backTint = Color(0xFFD8C8B0),
            grime = 0.07f, yellowing = 0.6f, scratches = 2, chips = 3,
            // Paint stays strong against the darker resin, but some drops out
            rubOff = 0.35f, rubSpotScale = 0.35f,
            ink = InkStyle(saturation = 1.25f, brightness = 0.9f),
            gloss = 0.3f,
        ),
    )

    /** "French ivory": cream with fine grain lines, on a bamboo back. */
    private val Celluloid = TileMaterial(
        set = TileSet.CELLULOID,
        colorways = listOf(
            TileColorway(
                labelRes = R.string.tile_colorway_bamboo_back,
                faceLight = Color(0xFFF8F1E1), faceDark = Color(0xFFEEE3CC),
                sideLight = Color(0xFFDED0B6), sideDeep = Color(0xFFCDBD9C),
                back = Color(0xFFC49A5E), backDeep = Color(0xFFA07A3E),
                backingStart = 0.6f,
            ),
        ),
        grain = GrainKind.CELLULOID_LINES,
        gloss = 0.18f, glossSharpness = 0.2f, rim = 0.8f,
        // Often only lightly stamped
        engraving = 0.5f,
        ink = InkStyle(saturation = 0.95f),
        worn = WearStyle(
            faceTint = Color(0xFFF3E2BC),
            grime = 0.12f, yellowing = 1f, scratches = 2, cracks = 2, stains = 1, chips = 4,
            rubOff = 0.1f, rubSpotScale = 0.6f,
            inkFade = 0.25f,
            ink = InkStyle(saturation = 0.8f),
            gloss = 0.12f,
        ),
    )

    /** Standard modern Chinese set: bright white, glossy, vivid, with a solid coloured back. */
    private val Melamine = TileMaterial(
        set = TileSet.MELAMINE,
        colorways = listOf(
            melamine(R.string.tile_colorway_green_back, Color(0xFF15885A), Color(0xFF0E6A45)),
            melamine(R.string.tile_colorway_blue_back, Color(0xFF2266C0), Color(0xFF174C93)),
            melamine(R.string.tile_colorway_pink_back, Color(0xFFE0679A), Color(0xFFBD4A7C)),
        ),
        gloss = 0.42f, glossSharpness = 0.7f, rim = 1f,
        engraving = 0.7f,
        ink = InkStyle(saturation = 1.3f),
        worn = WearStyle(
            faceTint = Color(0xFFFAF8F2), backTint = Color(0xFFF0F0F0),
            grime = 0.05f, yellowing = 0.15f, scratches = 7, scratchStrength = 0.5f, chips = 2,
            inkFade = 0.1f,
            ink = InkStyle(saturation = 1.1f),
            gloss = 0.32f,
        ),
    )

    private fun melamine(@StringRes label: Int, back: Color, backDeep: Color) = TileColorway(
        labelRes = label,
        faceLight = Color(0xFFFFFFFF), faceDark = Color(0xFFF3F4F2),
        sideLight = Color(0xFFE6E8E4), sideDeep = Color(0xFFD7DAD5),
        back = back, backDeep = backDeep,
        backingStart = 0.42f,
    )

    /** Japanese urea resin: warm ivory, crisp edges and crisp, vivid engraving. */
    private val Riichi = TileMaterial(
        set = TileSet.RIICHI,
        colorways = listOf(
            riichi(R.string.tile_colorway_yellow_back, Color(0xFFE8B23A), Color(0xFFC8921F)),
            riichi(R.string.tile_colorway_blue_back, Color(0xFF3B6CB0), Color(0xFF2A5290)),
            riichi(R.string.tile_colorway_green_back, Color(0xFF3D9462), Color(0xFF2C7449)),
        ),
        gloss = 0.3f, glossSharpness = 0.4f, rim = 0.95f,
        engraving = 0.8f,
        ink = InkStyle(saturation = 1.2f, brightness = 0.97f),
        worn = WearStyle(
            faceTint = Color(0xFFF8EDD6),
            grime = 0.06f, yellowing = 0.3f, scratches = 2, scratchStrength = 0.4f, chips = 1,
            inkFade = 0.05f,
            ink = InkStyle(saturation = 1.1f, brightness = 0.97f),
            gloss = 0.26f,
        ),
    )

    private fun riichi(@StringRes label: Int, back: Color, backDeep: Color) = TileColorway(
        labelRes = label,
        faceLight = Color(0xFFFFF9EC), faceDark = Color(0xFFF5EBD6),
        sideLight = Color(0xFFE8DCC2), sideDeep = Color(0xFFD8C9A9),
        back = back, backDeep = backDeep,
        backingStart = 0.45f,
    )

    /** Pure white face on a translucent, jewel-like back; very glossy until scratches haze it. */
    private val Acrylic = TileMaterial(
        set = TileSet.ACRYLIC,
        colorways = listOf(
            acrylic(R.string.tile_colorway_sapphire, Color(0xFF5B9BF0), Color(0xFF1B47A8)),
            acrylic(R.string.tile_colorway_emerald, Color(0xFF45D493), Color(0xFF0F7A48)),
            acrylic(R.string.tile_colorway_ruby, Color(0xFFF2667F), Color(0xFFA0182E)),
        ),
        gloss = 0.55f, glossSharpness = 0.9f, rim = 1f,
        engraving = 0.7f,
        ink = InkStyle(saturation = 1.2f),
        worn = WearStyle(
            // Fine scratches everywhere turn the surface hazy
            faceTint = Color(0xFFEEEEEC), backTint = Color(0xFFE0E0E0),
            grime = 0.04f, scratches = 14, scratchStrength = 0.6f, chips = 6,
            rubOff = 0.12f, rubSpotScale = 0.5f,
            inkFade = 0.08f,
            ink = InkStyle(saturation = 1f),
            gloss = 0.18f,
        ),
    )

    private fun acrylic(@StringRes label: Int, back: Color, backDeep: Color) = TileColorway(
        labelRes = label,
        faceLight = Color(0xFFFFFFFF), faceDark = Color(0xFFF2F4F7),
        sideLight = Color(0xFFE8EBEF), sideDeep = Color(0xFFD9DDE3),
        back = back, backDeep = backDeep,
        backingStart = 0.42f,
    )

    /** Solid wood: visible grain, matte, symbols burned in and soaked into the wood. */
    private val Wood = TileMaterial(
        set = TileSet.WOOD,
        colorways = listOf(
            TileColorway(
                labelRes = R.string.tile_colorway_light_wood,
                faceLight = Color(0xFFDDB784), faceDark = Color(0xFFC99D66),
                sideLight = Color(0xFFB88752), sideDeep = Color(0xFF9C6E3E),
            ),
            TileColorway(
                labelRes = R.string.tile_colorway_dark_wood,
                faceLight = Color(0xFFA77A50), faceDark = Color(0xFF8C603A),
                sideLight = Color(0xFF6E4728), sideDeep = Color(0xFF58381F),
            ),
        ),
        grain = GrainKind.WOOD_GRAIN,
        gloss = 0.05f, glossSharpness = 0f, rim = 0.35f,
        // Burned in rather than cut: only a shallow groove
        engraving = 0.4f,
        ink = InkStyle(saturation = 0.5f, brightness = 0.92f, monoTint = Color(0xFF3A2212), monoAmount = 0.35f),
        worn = WearStyle(
            faceTint = Color(0xFFE2C9A8),
            grime = 0.18f, scratches = 3, dents = 6, chips = 6,
            rubOff = 0.15f, rubSpotScale = 0.8f,
            inkFade = 0.15f,
            ink = InkStyle(saturation = 0.4f, brightness = 0.92f, monoTint = Color(0xFF3A2212), monoAmount = 0.35f),
            gloss = 0.02f,
        ),
    )

    /** Luxury stone: veined, wet-looking polish, engraving filled with gold. */
    private val Jade = TileMaterial(
        set = TileSet.JADE,
        colorways = listOf(
            TileColorway(
                labelRes = R.string.tile_colorway_green_jade,
                faceLight = Color(0xFF86BC9A), faceDark = Color(0xFF5A9473),
                sideLight = Color(0xFF4C8464), sideDeep = Color(0xFF386A4E),
            ),
            TileColorway(
                labelRes = R.string.tile_colorway_white_jade,
                faceLight = Color(0xFFF1F3E9), faceDark = Color(0xFFDEE2D2),
                sideLight = Color(0xFFCBD0BE), sideDeep = Color(0xFFB6BCA8),
            ),
        ),
        grain = GrainKind.STONE_VEINS,
        gloss = 0.5f, glossSharpness = 0.8f, rim = 1f,
        // Deeply cut into the stone
        engraving = 1f,
        ink = InkStyle(monoTint = Color(0xFFF2CA5C), monoAmount = 1f),
        worn = WearStyle(
            // Stone barely changes; only the corners chip
            faceTint = Color(0xFFF6F6F0),
            grime = 0.04f, scratches = 1, scratchStrength = 0.3f, chips = 5,
            inkFade = 0.1f,
            ink = InkStyle(monoTint = Color(0xFFE6BC52), monoAmount = 1f),
            gloss = 0.45f,
        ),
    )

    /** Light plastic travel set: flat white, printed symbols. */
    private val Travel = TileMaterial(
        set = TileSet.TRAVEL,
        colorways = listOf(
            TileColorway(
                labelRes = R.string.tile_colorway_white,
                faceLight = Color(0xFFFDFDFB), faceDark = Color(0xFFF0F0EC),
                sideLight = Color(0xFFDADAD4), sideDeep = Color(0xFFC8C8C0),
            ),
        ),
        gloss = 0.25f, glossSharpness = 0.3f, rim = 0.8f,
        ink = InkStyle(saturation = 1.25f, brightness = 1.02f),
        worn = WearStyle(
            faceTint = Color(0xFFF4EBCF),
            grime = 0.1f, yellowing = 0.6f, scratches = 8, scratchStrength = 0.8f, chips = 3,
            // The print rubs away in patches
            rubOff = 0.6f, rubSpotScale = 1f,
            inkFade = 0.15f,
            ink = InkStyle(saturation = 1f, brightness = 1.02f),
            gloss = 0.18f,
        ),
    )
}
