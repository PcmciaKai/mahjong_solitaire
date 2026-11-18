package com.vortessence.mahjong.data.model

import androidx.annotation.StringRes
import com.vortessence.mahjong.R

/**
 * The material the tiles are made of. How each one looks (colours, gloss, ink, wear) is defined in
 * [com.vortessence.mahjong.ui.components.tiles.TileMaterials].
 */
enum class TileSet(@param:StringRes val labelRes: Int, @param:StringRes val descriptionRes: Int) {
    BONE_BAMBOO(R.string.tile_set_bone_bamboo, R.string.tile_set_bone_bamboo_description),
    CATALIN(R.string.tile_set_catalin, R.string.tile_set_catalin_description),
    CELLULOID(R.string.tile_set_celluloid, R.string.tile_set_celluloid_description),
    MELAMINE(R.string.tile_set_melamine, R.string.tile_set_melamine_description),
    RIICHI(R.string.tile_set_riichi, R.string.tile_set_riichi_description),
    ACRYLIC(R.string.tile_set_acrylic, R.string.tile_set_acrylic_description),
    WOOD(R.string.tile_set_wood, R.string.tile_set_wood_description),
    JADE(R.string.tile_set_jade, R.string.tile_set_jade_description),
    TRAVEL(R.string.tile_set_travel, R.string.tile_set_travel_description);

    companion object {
        val Default = BONE_BAMBOO
    }
}
