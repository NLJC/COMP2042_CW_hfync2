import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;



public class InfoBoard extends JComponent implements MouseListener, MouseMotionListener {

    private static final String GAME_TITLE = "Brick Destroy";
    private static final String RULE1 = "Space to pause/continue game, esc to open menu";
    private static final String RULE2 = "A and D to move player paddle";
    private static final String RULE3 = "F1 to open Debug Console";
    private static final String MENU_TEXT = "Menu";
    private static final String START_TEXT = "Start";

    private static final Color BG_COLOR = new Color(0, 0, 0, 0);
    private static final Color BORDER_COLOR = new Color(200,8,21); //Venetian Red
    private static final Color DASH_BORDER_COLOR = new  Color(255, 216, 0);//school bus yellow
    private static final Color TEXT_COLOR = Color.WHITE;//new Color(16, 52, 166);//egyptian blue
    private static final Color CLICKED_BUTTON_COLOR = Color.WHITE.darker();
    private static final Color CLICKED_TEXT = Color.WHITE.darker();
    private static final int BORDER_SIZE = 5;
    private static final float[] DASHES = {12,6};

    private Rectangle menuFace;
    private Rectangle menuButton;
    private Rectangle startButton;


    private BasicStroke borderStoke;
    private BasicStroke borderStoke_noDashes;

    private Font rulesFont;
    private Font gameTitleFont;
    private Font buttonFont;

    private GameFrame owner;

    private boolean menuClicked;
    private boolean startClicked;


    /**
     * @param owner
     * @param area
     */
    public InfoBoard(GameFrame owner, Dimension area) {

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.owner = owner;

        menuFace = new Rectangle(new Point(0, 0), area);
        this.setPreferredSize(area);

        Dimension btnDim = new Dimension(area.width / 3, area.height / 12);
        menuButton = new Rectangle(btnDim);
        startButton = new Rectangle(btnDim);

        borderStoke = new BasicStroke(BORDER_SIZE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, DASHES, 0);
        borderStoke_noDashes = new BasicStroke(BORDER_SIZE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        rulesFont = new Font("Noto Mono", Font.PLAIN, 15);
        gameTitleFont = new Font("Noto Mono", Font.BOLD, 30);
        buttonFont = new Font("Monospaced", Font.PLAIN, startButton.height - 2);


    }


    /**
     * @param g
     */
    public void paint(Graphics g) {
        drawMenu((Graphics2D) g);
    }


    /**
     * @param g2d
     */
    public void drawMenu(Graphics2D g2d) {

        drawContainer(g2d);

        /*
        all the following method calls need a relative
        painting directly into the HomeMenu rectangle,
        so the translation is made here so the other methods do not do that.
         */
        Color prevColor = g2d.getColor();
        Font prevFont = g2d.getFont();

        double x = menuFace.getX();
        double y = menuFace.getY();

        g2d.translate(x, y);

        //methods calls
        drawText(g2d);
        drawButton(g2d);
        //end of methods calls

        g2d.translate(-x, -y);
        g2d.setFont(prevFont);
        g2d.setColor(prevColor);
    }

    /**
     * @param g2d
     */
    private void drawContainer(Graphics2D g2d) {
        Color prev = g2d.getColor();

        Image backgroundimage = Toolkit.getDefaultToolkit().getImage("Brick wall background.jpg");
        g2d.drawImage(backgroundimage, 0, 0, this);

        g2d.setColor(BG_COLOR);
        g2d.fill(menuFace);

        Stroke tmp = g2d.getStroke();

        g2d.setStroke(borderStoke_noDashes);
        g2d.setColor(DASH_BORDER_COLOR);
        g2d.draw(menuFace);

        g2d.setStroke(borderStoke);
        g2d.setColor(BORDER_COLOR);
        g2d.draw(menuFace);

        g2d.setStroke(tmp);

        g2d.setColor(prev);
    }

    /**
     * @param g2d
     */
    private void drawText(Graphics2D g2d) {

        g2d.setColor(TEXT_COLOR);

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D gameTitleRect = gameTitleFont.getStringBounds(GAME_TITLE, frc);
        Rectangle2D rule1Rect = rulesFont.getStringBounds(RULE1, frc);
        Rectangle2D rule2Rect = rulesFont.getStringBounds(RULE2, frc);
        Rectangle2D rule3Rect = rulesFont.getStringBounds(RULE3, frc);

        int sX, sY;

        sX = (int) (menuFace.getWidth() - gameTitleRect.getWidth()) / 2;
        sY = (int) (menuFace.getHeight() / 4);

        g2d.setFont(gameTitleFont);
        g2d.drawString(GAME_TITLE, sX, sY);

        sX = (int) (menuFace.getWidth() - rule1Rect.getWidth()) / 2;
        sY += (int) rule1Rect.getHeight() * 1.1;//add 10% of String height between the two strings

        g2d.setFont(rulesFont);
        g2d.drawString(RULE1, sX, sY);

        sX = (int) (menuFace.getWidth() - rule2Rect.getWidth()) / 2;
        sY += (int) rule2Rect.getHeight() * 1.1;

        g2d.setFont(rulesFont);
        g2d.drawString(RULE2, sX, sY);

        sX = (int) (menuFace.getWidth() - rule3Rect.getWidth()) / 2;
        sY += (int) rule3Rect.getHeight() * 1.1;

        g2d.setFont(rulesFont);
        g2d.drawString(RULE3, sX, sY);
    }

    /**
     * @param g2d
     */
    private void drawButton(Graphics2D g2d) {

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D mTxtRect = buttonFont.getStringBounds(MENU_TEXT, frc);
        Rectangle2D txtRect = buttonFont.getStringBounds(START_TEXT, frc);

        g2d.setFont(buttonFont);

        int x = (menuFace.width - startButton.width) / 2;
        int y = (int) ((menuFace.height - startButton.height) * 0.8);

        startButton.setLocation(x, y);

        x = (int) (startButton.getWidth() - txtRect.getWidth()) / 2;
        y = (int) (startButton.getHeight() - txtRect.getHeight()) / 2;

        x += startButton.x;
        y += startButton.y + (startButton.height * 0.9);


        if (startClicked) {
            Color tmp = g2d.getColor();
            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(startButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(START_TEXT, x, y);
            g2d.setColor(tmp);
        } else {
            g2d.draw(startButton);
            g2d.drawString(START_TEXT, x, y);
        }

        x = startButton.x;
        y = startButton.y;

        y *= 0.8;


        menuButton.setLocation(x, y);


        x = (int) (menuButton.getWidth() - mTxtRect.getWidth()) / 2;
        y = (int) (menuButton.getHeight() - mTxtRect.getHeight()) / 2;

        x += menuButton.x;
        y += menuButton.y + (startButton.height * 0.9);

        if (menuClicked) {
            Color tmp = g2d.getColor();

            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(menuButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(MENU_TEXT, x, y);
            g2d.setColor(tmp);
        } else {
            g2d.draw(menuButton);
            g2d.drawString(MENU_TEXT, x, y);
        }

    }

    /**
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if (startButton.contains(p)) {
            owner.enableGameBoard();

        } else if (menuButton.contains(p)) {
            owner.enableHomeMenu();
        }
    }

    /**
     * @param mouseEvent
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if (startButton.contains(p)) {
            startClicked = true;
            repaint(startButton.x, startButton.y, startButton.width + 1, startButton.height + 1);

        } else if (menuButton.contains(p)) {
            menuClicked = true;
            repaint(menuButton.x, menuButton.y, menuButton.width + 1, menuButton.height + 1);
        }
    }

    /**
     * @param mouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (startClicked) {
            startClicked = false;
            repaint(startButton.x, startButton.y, startButton.width + 1, startButton.height + 1);
        } else if (menuClicked) {
            menuClicked = false;
            repaint(menuButton.x, menuButton.y, menuButton.width + 1, menuButton.height + 1);
        }
    }

    /**
     * @param mouseEvent
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    /**
     * @param mouseEvent
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }


    /**
     * @param mouseEvent
     */
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    /**
     * @param mouseEvent
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if (startButton.contains(p) || menuButton.contains(p))
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else
            this.setCursor(Cursor.getDefaultCursor());
    }
}