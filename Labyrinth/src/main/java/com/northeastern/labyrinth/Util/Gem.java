package com.northeastern.labyrinth.Util;

/**
 * Represents all available gems
 */
public enum Gem {
    ALEXANDRITE_PEAR_SHAPE,
    ALEXANDRITE,
    ALMANDINE_GARNET,
    AMETHYST,
    AMETRINE,
    AMMOLITE,
    APATITE,
    APLITE,
    APRICOT_SQUARE_RADIANT,
    AQUAMARINE,
    AUSTRALIAN_MARQUISE,
    AVENTURINE,
    AZURITE,
    BERYL,
    BLACK_OBSIDIAN,
    BLACK_ONYX,
    BLACK_SPINEL_CUSHION,
    BLUE_CEYLON_SAPPHIRE,
    BLUE_CUSHION,
    BLUE_PEAR_SHAPE,
    BLUE_SPINEL_HEART,
    BULLS_EYE,
    CARNELIAN,
    CHROME_DIOPSIDE,
    CHRYSOBERYL_CUSHION,
    CHRYSOLITE,
    CITRINE_CHECKERBOARD,
    CITRINE,
    CLINOHUMITE,
    COLOR_CHANGE_OVAL,
    CORDIERITE,
    DIAMOND,
    DUMORTIERITE,
    EMERALD,
    FANCY_SPINEL_MARQUISE,
    GARNET,
    GOLDEN_DIAMOND_CUT,
    GOLDSTONE,
    GRANDIDIERITE,
    GRAY_AGATE,
    GREEN_AVENTURINE,
    GREEN_BERYL_ANTIQUE,
    GREEN_BERYL,
    GREEN_PRINCESS_CUT,
    GROSSULAR_GARNET,
    HACKMANITE,
    HELIOTROPE,
    HEMATITE,
    IOLITE_EMERALD_CUT,
    JASPER,
    JASPILITE,
    KUNZITE_OVAL,
    KUNZITE,
    LABRADORITE,
    LAPIS_LAZULI,
    LEMON_QUARTZ_BRIOLETTE,
    MAGNESITE,
    MEXICAN_OPAL,
    MOONSTONE,
    MORGANITE_OVAL,
    MOSS_AGATE,
    ORANGE_RADIANT,
    PADPARADSCHA_OVAL,
    PADPARADSCHA_SAPPHIRE,
    PERIDOT,
    PINK_EMERALD_CUT,
    PINK_OPAL,
    PINK_ROUND,
    PINK_SPINEL_CUSHION,
    PRASIOLITE,
    PREHNITE,
    PURPLE_CABOCHON,
    PURPLE_OVAL,
    PURPLE_SPINEL_TRILLION,
    PURPLE_SQUARE_CUSHION,
    RAW_BERYL,
    RAW_CITRINE,
    RED_DIAMOND,
    RED_SPINEL_SQUARE_EMERALD_CUT,
    RHODONITE,
    ROCK_QUARTZ,
    ROSE_QUARTZ,
    RUBY_DIAMOND_PROFILE,
    RUBY,
    SPHALERITE,
    SPINEL,
    STAR_CABOCHON,
    STILBITE,
    SUNSTONE,
    SUPER_SEVEN,
    TANZANITE_TRILLION,
    TIGERS_EYE,
    TOURMALINE_LASER_CUT,
    TOURMALINE,
    UNAKITE,
    WHITE_SQUARE,
    YELLOW_BAGUETTE,
    YELLOW_BERYL_OVAL,
    YELLOW_HEART,
    YELLOW_JASPER,
    ZIRCON,
    ZOISITE;

    /**
     * Decodes a gem name to enum. Case insenitive. Allows '-' or '_' as dividers
     * For example, yeLLoW-HearT and yellow_heart will both decode to Gem.YELLOW_HEART
     */
    public static Gem decodeToEnum(String gemName) {
        String formattedString = gemName.toUpperCase().replaceAll("-", "_");
        return Gem.valueOf(formattedString);
    }

    /**
     * Turns an enum into expected file name.
     * For example, YELLOW_HEART becomes yellow_heart.png
     */
    public static String enumToFileName(Gem gem) {
        return enumToString(gem) + ".png";
    }

    /**
     * Turns an enum into string. Turns to lower case and replaces '_' to '-'
     * For example, YELLOW_HEART becomes yellow_heart
     */
    public static String enumToString(Gem gem) {
        return gem.toString().toLowerCase().replaceAll("_", "-");
    }

}
