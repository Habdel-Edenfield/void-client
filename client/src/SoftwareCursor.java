import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Colour mouse cursors (mine, attack, take...) where Java can only show two colour ones, as on Linux, where
 * {@link Toolkit#createCustomCursor} turns the icon into a grey and black shape.
 * <p>
 * There the system pointer is hidden while the game wants a custom cursor, and the cursor sprite is drawn at the mouse
 * with the game's own toolkit at the end of every frame, so it works in every display mode. The area under it is
 * marked to be redrawn on the next frame, as the game does for the mouse over text, so it leaves no trail.
 */
final class SoftwareCursor {
    static final boolean NEEDED = needed();

    private static Class207 sprite;
    private static int hotspotX;
    private static int hotspotY;
    private static ha toolkit;
    private static Class105 drawable;
    private static Component listening;
    private static volatile int mouseX = -1;
    private static volatile int mouseY = -1;
    private static final int[] clip = new int[4];

    private static final MouseAdapter MOUSE = new MouseAdapter() {
        @Override
        public void mouseMoved(MouseEvent event) {
            mouseX = event.getX();
            mouseY = event.getY();
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            mouseMoved(event);
        }

        @Override
        public void mouseExited(MouseEvent event) {
            mouseX = -1;
            mouseY = -1;
        }
    };

    private static boolean needed() {
        try {
            return Toolkit.getDefaultToolkit().getMaximumCursorColors() <= 2;
        } catch (Throwable t) {
            return false;
        }
    }

    static void set(Class207 sprite, int hotspotX, int hotspotY, Component component) {
        SoftwareCursor.sprite = sprite;
        SoftwareCursor.hotspotX = hotspotX;
        SoftwareCursor.hotspotY = hotspotY;
        drawable = null;
        if (component != listening) {
            if (listening != null) {
                listening.removeMouseListener(MOUSE);
                listening.removeMouseMotionListener(MOUSE);
            }
            if (component != null) {
                component.addMouseListener(MOUSE);
                component.addMouseMotionListener(MOUSE);
                Point point = component.getMousePosition();
                if (point != null) {
                    mouseX = point.x;
                    mouseY = point.y;
                }
            }
            listening = component;
        }
    }

    static void clear() {
        sprite = null;
        drawable = null;
    }

    /** Draws the cursor over the finished frame of [ha] */
    static void draw(ha ha) {
        Class207 sprite = SoftwareCursor.sprite;
        int x = mouseX;
        int y = mouseY;
        if (sprite == null || ha == null || x < 0) {
            return;
        }
        if (drawable == null || toolkit != ha) {
            drawable = ha.method3691(sprite, true);
            toolkit = ha;
        }
        x -= hotspotX;
        y -= hotspotY;
        ha.K(clip);
        ha.la();
        drawable.method974(x, y);
        ha.KA(clip[0], clip[1], clip[2], clip[3]);
        Class13.method226(x, sprite.method1510(), y, 0, sprite.method1522());
    }
}
