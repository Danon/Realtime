package ui.gfx;

class Frame {
    FrameAnimation parentAnimation;

    private final int id;
    final int x;
    final int y;
    final int width;
    final int height;
    final int offsetX;
    final int offsetY;

    Frame(int id, int x, int y, int width, int height, int offset, int offsetY) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.offsetX = offset;
        this.offsetY = offsetY;
    }

    Frame(int id, int x, int y, int width, int height, int offset) {
        this(id, x, y, width, height, offset, 0);
    }

    Frame(int id, int x, int y, int width, int height) {
        this(id, x, y, width, height, 0, 0);
    }

    String getSpritesheetName() {
        return parentAnimation.getSpritesheetName();
    }
}
