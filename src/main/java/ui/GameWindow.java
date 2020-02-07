package ui;

import app.Application;
import gameplay.ClientWorld;
import lombok.RequiredArgsConstructor;
import network.KeysState;
import ui.gfx.Renderer;
import util.Size;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

@RequiredArgsConstructor
public class GameWindow implements IWorldUpdateObserver, KeyListener, MouseMotionListener, MouseListener {
    static final int VIEW_WIDTH = 800, VIEW_HEIGHT = 510;

    private final JFrame jFrame = new JFrame();

    private final IKeyStateNotifier keyStateNotifier;
    private final Size windowSize;
    private final ClientWorld world;
    private final Chat chat;
    private final Renderer renderer;
    private final KeysState keysState = new KeysState();
    private final KeysState prevKeysState = new KeysState();

    private boolean left, right, prevLeft, prevRight;

    void showGameWindow() {
        jFrame.setTitle("Realtime | Game playing...");
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Graphics graphics = showAndGetWindowGraphics();
        renderer.setScalingTo(new Size(jFrame.getWidth(), jFrame.getHeight()));
        renderer.drawOn(graphics);

        jFrame.addKeyListener(this);
        jFrame.addMouseListener(this);
        jFrame.addMouseMotionListener(this);
    }

    private Graphics showAndGetWindowGraphics() {
        String type = Application.RunOptions.getText("-Display");

        if (type.equals("Fullscreen")) {
            return useFullscreen();
        }

        if (type.equals("Window")) {
            jFrame.setSize(windowSize.getWidth(), windowSize.getHeight());
            return useWindowed();
        }

        throw new RuntimeException("Invalid display type");
    }

    private Graphics useFullscreen() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            jFrame.setUndecorated(true);
            gd.setFullScreenWindow(jFrame);
        } else {
            System.err.println("Full screen not supported.");
        }

        return jFrame.getGraphics();
    }

    private Graphics useWindowed() {
        JPanel viewport = new JPanel();
        viewport.setSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
        jFrame.getContentPane().add(viewport);
        jFrame.setVisible(true);

        return viewport.getGraphics();
    }

    @Override
    public void worldUpdated() {
        // if keysState changed
        if ((!keysState.equals(prevKeysState)) || (prevLeft != left) || (prevRight != right)) {
            keyStateNotifier.sendCurrentMove(keysState, left, right);
            prevKeysState.set(keysState);
            prevLeft = left;
            prevRight = right;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (chat.isTextFieldShown()) {
            chat.keyPressed(e.getKeyChar());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                if (!chat.toggleTextField()) {
                    if (!chat.isTextEmpty()) {
                        keyStateNotifier.sendTextMessage(chat.getText());
                        chat.showHistory();
                        chat.clearText();
                    }
                }
                return;
            case KeyEvent.VK_ESCAPE:
                if (chat.isTextEmpty()) {
                    chat.hideTextField();
                } else {
                    chat.clearText();
                }
                return;
        }
        if (chat.isTextFieldShown()) return;
        switch (e.getKeyChar()) {
            case 'w':
                keysState.Up = true;
                break;
            case 's':
                keysState.Down = true;
                break;
            case 'a':
                keysState.Left = true;
                break;
            case 'd':
                keysState.Right = true;
                break;
        }
        this.world.movePlayerCharacter(keysState); // client side interpolation
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (chat.isTextFieldShown()) {
            return;
        }
        switch (e.getKeyChar()) {
            case 'w':
                keysState.Up = false;
                break;
            case 's':
                keysState.Down = false;
                break;
            case 'a':
                keysState.Left = false;
                break;
            case 'd':
                keysState.Right = false;
                break;
        }
        this.world.movePlayerCharacter(keysState); // client side interpolation
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                left = true;
                break;
            case MouseEvent.BUTTON3:
                right = true;
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                left = false;
                break;
            case MouseEvent.BUTTON3:
                right = false;
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
