package uk.ac.tees.cupcake.utils;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class ColourUtility {
    
    /**
     * Prevents instantiation
     */
    private ColourUtility() {
    
    }
    
    /**
     * Takes a colour in the RGB colour format and adds an alpha component to it.
     *
     * @param alpha the alpha component
     * @param rgb the initial colour
     * @return rgb with alpha applied
     */
    public static int setAlpha(int alpha, int rgb) {
        //bit of bit manipulation to add alpha component to the colour
        return (alpha << 24) | (rgb & 0x00FFFFFF);
    }
    
}