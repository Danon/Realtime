package ui.gfx;

import app.Application;
import gameplay.Character;
import gameplay.ClientWorld;
import gameplay.Point;
import gameplay.geometry.Rectangle;
import gameplay.scene.Floor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ui.Chat;
import ui.gfx.blur.filter.BoxBlurFilter;
import ui.gfx.frame.Animation;
import ui.gfx.frame.Frame;
import ui.gfx.frame.FrameAnimation;
import ui.gfx.resources.Resources;
import ui.gfx.shadows.MasterOfShadows;
import util.Size;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public final class Renderer implements IRenderObserver {
    private final Drawer draw;
    private final Graphics2D canvas;
    private final BufferedImage backBuffer;
    private final MasterOfShadows zed;

    private Graphics graphics;
    private Insets displaySize;
    private final Size viewSize;
    private Size windowSize;
    private ClientWorld world;
    private Chat chat;
    private FontMetrics fontMetrics;

    private BufferedImage tmp;
    private Graphics2D tmpG;

    public static int
            CAMERA_SIDE_MARGIN = 0,
            CAMERA_BOTTOM_MARGIN = 0,
            CAMERA_TOP_MARGIN = 0;

    public Renderer(Size viewSize) {
        backBuffer = new BufferedImage(viewSize.getWidth(), viewSize.getHeight(), TYPE_INT_ARGB);
        canvas = backBuffer.createGraphics();

        this.viewSize = viewSize;
        this.draw = new Drawer(canvas, viewSize.getWidth(), viewSize.getHeight());

        Font defaultFont = new Font("Arial", Font.PLAIN, 15);
        canvas.setFont(defaultFont);
        fontMetrics = canvas.getFontMetrics(defaultFont);

        zed = new MasterOfShadows(0, 0, viewSize.getWidth(), viewSize.getHeight() + Floor.HEIGHT);
    }

    public void createCanvas(Size size) {
        this.windowSize = size;
        this.displaySize = this.getDisplaySize();

        this.tmp = new BufferedImage(windowSize.getWidth(), windowSize.getHeight(), TYPE_INT_ARGB);
        this.tmpG = this.tmp.createGraphics();
    }

    public void drawOn(Graphics graphics) {
        this.graphics = graphics;
    }

    public void attachWorld(ClientWorld world) {
        this.world = world;
        for (Floor floor : this.world.getMap().getFloors()) {
            zed.addObstacle(floor.asShape());
        }
    }

    public void attachChat(Chat chat) {
        this.chat = chat;
    }

    private Insets getDisplaySize() {
        int displayLeft, displayTop, displayWidth, displayHeight;

        if (Application.RunOptions.isUsedWithValue("-View", "Stretch")) {
            displayWidth = windowSize.getWidth();
            displayHeight = windowSize.getHeight();
        } else if (Application.RunOptions.isUsedWithValue("-View", "Normal")) {
            displayWidth = viewSize.getWidth();
            displayHeight = viewSize.getHeight();
        } else //if (Application.RunOptions.isUsedWithValue("-View", "Fit"))
        {
            double windowRatio = (double) windowSize.getWidth() / (double) windowSize.getHeight();
            double viewRatio = (double) viewSize.getWidth() / (double) viewSize.getHeight();

            if (windowRatio > viewRatio) {
                displayWidth = (int) ((double) windowSize.getHeight() * viewRatio);
                displayHeight = windowSize.getHeight();
            } else {
                displayWidth = windowSize.getWidth();
                displayHeight = (int) ((double) windowSize.getWidth() / viewRatio);
            }
        }

        displayLeft = (windowSize.getWidth() - displayWidth) / 2;
        displayTop = (windowSize.getHeight() - displayHeight) / 2;

        return new Insets(
                displayTop,
                displayLeft,
                displayHeight + displayTop,
                displayWidth + displayLeft
        );
    }

    private void drawToWindow() {
        graphics.drawImage(backBuffer,
                displaySize.left, displaySize.top, displaySize.right, displaySize.bottom,
                0, 0, viewSize.getWidth(), viewSize.getHeight(), null);
    }

    @Override
    public void render(gameplay.Character[] characters, int playerCharacterId) {
        draw.freeCamera();

        Point playerPos = getPlayerPosition(characters, playerCharacterId);

        canvas.setColor(Color.decode("#549CAE"));
        draw.fill(new Rectangle(0, 0, viewSize.getWidth(), viewSize.getHeight()));

        Rectangle borders = new Rectangle(
                0, 0,
                viewSize.getWidth(),
                viewSize.getHeight());

        if (characters.length > 0) {
            draw.useCamera(playerPos.sub(viewSize.getWidth() / 2.0, viewSize.getHeight() / 2.0));
        } else {
            draw.useCamera(new Point(0, 0));
        }

        draw.getCamera().cap(borders, viewSize.getWidth(), viewSize.getHeight());

        drawBackground(borders);
        draw.freeCamera();

        tmpG.setColor(new Color(0, 0, 0, .2f));
        tmpG.setComposite(AlphaComposite.Clear);
        tmpG.fillRect(0, 0, tmp.getWidth(), tmp.getHeight());
        tmpG.setComposite(AlphaComposite.SrcOver);

        tmpG.fill(invertedLightShape(playerPos.addY(60)));

        world.getMap().getFloors().forEach(draw::floor);
        world.getMap().getLadders().forEach(draw::ladder);

        for (gameplay.Character character : characters) {
            drawCharacter(character);
        }

        canvas.setColor(Color.black);
        canvas.drawImage(tmp, new BoxBlurFilter(4), 0, 0);

        draw.freeCamera();

        if (chat.isHistoryShown()) {
            int height = 40;
            for (String message : chat.getPreparedMessages()) {
                draw.text(message, new Point(15, height += 18));
            }
        }
        if (chat.isTextFieldShown()) {
            draw.borders(new Rectangle(10, viewSize.getHeight() - 40, 400, 20));
            draw.text(chat.getText(), new Point(17, viewSize.getHeight() - 24));
            int textWidth = fontMetrics.stringWidth(chat.getText());
            draw.line(new Point(15 + textWidth + 4, viewSize.getHeight() - 24),
                    new Point(15 + textWidth + 4, viewSize.getHeight() - 36)
            );
        }

        draw.useCamera();

        canvas.setColor(Color.red);

        drawToWindow();
    }

    private void drawCharacter(Character character) {
        draw.frame(
                getCharacterFrame(character),
                character.getPosition(),
                character.isTurnedRight() ? Flip.None : Flip.Horizontally
        );

        draw.text(String.format("%s %d", character.getDisplayName(), character.shared.hp),
                character.getPosition().sub(50, 60)
        );

        drawHealthBar(character);
    }

    private void drawHealthBar(Character character) {
        canvas.setColor(Color.red);

        Rectangle hpBar = new Rectangle(character.getPosition().sub(20, 80), 50, 6);
        draw.borders(hpBar);

        hpBar.width = (int) Math.round(hpBar.width * ((double) character.getHp() / 500.0));
        draw.fill(hpBar);
    }

    private Frame getCharacterFrame(Character character) {
        Pair<Animation, Integer> result = getFrameAnimationAndIteration(character);

        return Resources.spritesheet
                .animation(result.getKey())
                .getFrameIterate(result.getValue());
    }

    private Pair<Animation, Integer> getFrameAnimationAndIteration(Character character) {

        if (character.isOnTheGround()) {
            if (character.isWalking()) {
                return new Pair<>(Animation.RUN, character.common.runFrame);
            }
            if (character.isBasicAttack()) {
                return new Pair<>(Animation.BASIC, character.common.basicFrame);
            }
            if (character.isShooting()) {
                return new Pair<>(Animation.SHOOTING, character.common.shootFrame);
            }
            return new Pair<>(Animation.IDLE, 0);
        }

        if (character.isClimbing()) {
            return new Pair<>(Animation.CLIMBING, character.common.climbFrame);
        }
        if (character.isBasicAttack()) {
            return new Pair<>(Animation.BASIC_AIR, character.common.basicFrame);
        }
        if (character.isShooting()) {
            return new Pair<>(Animation.MIDAIR_GUN, character.common.shootFrame);
        }

        return new Pair<>(Animation.MIDAIR, character.isJumping() ? character.common.jumpFrame : FrameAnimation.Speed.MidAir * 5);
    }

    private void drawBackground(Rectangle borders) {
        for (int i = 0; i <= Math.ceil(borders.width / 167); i++) {
            draw.image(
                    "background.png",
                    new Point(
                            CAMERA_SIDE_MARGIN + i * 167,
                            viewSize.getHeight() - borders.height),
                    DrawFrom.MiddleTop);
        }
    }

    private Point getPlayerPosition(Character[] characters, int playerCharacterId) {
        for (Character character : characters) {
            if (character.getCharacterId() == playerCharacterId) {
                return character.getPosition();
            }
        }
        throw new AssertionError("Index not contained in array.");
    }

    private Shape lightShape(Point player) {
        Path2D shadowShape = new Path2D.Double();
        zed.getFieldOfView(player, new IntersectionIterable() {
            @Override
            public void iterateFirst(double x, double y) {
                shadowShape.moveTo(x, y);
            }

            @Override
            public void iterateNext(double x, double y) {
                shadowShape.lineTo(x, y);
            }

            @Override
            public void onFinish() {
                shadowShape.closePath();
            }
        });

        AffineTransform at = new AffineTransform();
        Camera camera = draw.getCamera();
        at.translate(-camera.getX(), camera.getY() + 32 + viewSize.getHeight());
        at.scale(1.0, -1.0);
        shadowShape.transform(at);

        return shadowShape;
    }

    private Shape invertedLightShape(Point player) {
        Shape clip = lightShape(player);

        Area view = new Area(clip);
        Area window = new Area(new java.awt.Rectangle(0, 0, viewSize.getWidth(), viewSize.getHeight()));

        window.subtract(view);

        return window;
    }

    @Getter
    @RequiredArgsConstructor
    private static class Pair<K, V> {
        private final K key;
        private final V value;
    }
}
