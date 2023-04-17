import java.awt.*;

/**
 * Modified FlowLayout to support dynamic wrapping of components.
 */
public class WrapLayout extends FlowLayout {
    public WrapLayout() {
        super();
    }

    /**
     * Gets the size the container needs to be.
     * @param target The container to be laid out.
     * @return the size the container needs to be.
     */
    @Override
    public Dimension preferredLayoutSize(Container target) {
        return calculateSize(target);
    }

    /**
     * Calculate's the size of the container.
     * @param target The container to be laid out.
     * @return The size of the container.
     */
    private Dimension calculateSize(Container target) {
        synchronized (target.getTreeLock()) {
            int hgap = getHgap();
            int vgap = getVgap();
            int currentWidth = target.getWidth();

            // If the container doesn't have a size yet, behave like a regular FlowLayout.
            if (currentWidth == 0) {
                currentWidth = Integer.MAX_VALUE;
            }

            Insets insets = target.getInsets();
            if (insets == null){
                insets = new Insets(0, 0, 0, 0);
            }
            int requiredWidth = 0; // Tracks the width of the widest row

            int maxWidth = currentWidth - (insets.left + insets.right + hgap * 2);
            int numOfChildren = target.getComponentCount();
            int containerWidth = 0; // Tracks the width of the current row
            int containerHeight = insets.top + vgap; // FlowLayout starts by adding vgap, so do that here too.
            int rowHeight = 0;

            // Iterate through the components and determine the size of the container.
            for (int i = 0; i < numOfChildren; i++) {
                Component component = target.getComponent(i);
                if (component.isVisible()) {
                    Dimension size = component.getPreferredSize();
                    // If start of new row, or if component fits on current row
                    if ((containerWidth == 0) || ((containerWidth + size.width) <= maxWidth)) {
                        if (containerWidth > 0) { containerWidth += hgap; }
                        containerWidth += size.width;
                        rowHeight = Math.max(rowHeight, size.height);
                    } else {
                        // New row needed
                        containerWidth = size.width;
                        containerHeight += vgap + rowHeight;
                        rowHeight = size.height;
                    }
                    requiredWidth = Math.max(requiredWidth, containerWidth);
                }
            }
            containerHeight += rowHeight;
            containerHeight += insets.bottom;
            return new Dimension(requiredWidth+insets.left+insets.right, containerHeight);
        }
    }
}