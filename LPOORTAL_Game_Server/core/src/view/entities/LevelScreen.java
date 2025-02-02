package view.entities;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.lpoortal.game.LpoortalGame;
import com.lpoortal.game.network.NetworkManager;

import controller.GameController;
import model.GameModel;
import model.entities.CursorModel;
import model.entities.DrawnLineModel;
import model.entities.InkJarModel;
import model.entities.PortalModel;
import model.entities.StickmanModel;

import static controller.GameController.LEVEL_HEIGHT;
import static controller.GameController.LEVEL_WIDTH;

public class LevelScreen extends ScreenAdapter {
    /**
     * Used to debug the position of the physics fixtures
     */
    private static final boolean DEBUG_PHYSICS = false;

    /**
     * How much meters does a pixel represent.
     */
    public final static float PIXEL_TO_METER = 0.04f;

    /**
     * The width of the viewport in meters. The height is
     * automatically calculated using the screen ratio.
     */
    private static final float VIEWPORT_WIDTH = 50;
    
    /**
     * The game this screen belongs to.
     */
    private final LpoortalGame game;
    
    /**
     * The camera used to show the viewport.
     */
    private final OrthographicCamera camera;

    /**
     * A renderer used to debug the physical fixtures.
     */
    private Box2DDebugRenderer debugRenderer;

    /**
     * The transformation matrix used to transform meters into
     * pixels in order to show fixtures in their correct places.
     */
    private Matrix4 debugCamera;

    /**
     * A manager for texture assets, responsible for loading the sprites/animations
     */
	private TextureManager textureManager;

    /**
     * Creates this screen.
     *
     * @param game The game this screen belongs to
     */
    public LevelScreen(LpoortalGame game) {
        this.game = game;

        loadAssets();

        camera = createCamera();
    }

    /**
     * Creates the camera used to show the viewport.
     *
     * @return the camera
     */
    private OrthographicCamera createCamera() {
        OrthographicCamera camera = new OrthographicCamera(
        		getWidthPixels(), 
        		getWidthPixels() * getViewportRatio());

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        if (DEBUG_PHYSICS) {
            debugRenderer = new Box2DDebugRenderer();
            debugCamera = camera.combined.cpy();
            debugCamera.scl(1 / PIXEL_TO_METER);
        }

        return camera;
    }

    private float getWidthPixels() {
    	return VIEWPORT_WIDTH / PIXEL_TO_METER;
	}

	private float getViewportRatio() {
    	return ((float) Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth());
	}

	/**
     * Loads the assets needed by this screen.
     */
    private void loadAssets() {
        this.textureManager = this.game.getTextureManager();
    }

    /**
     * Renders this screen.
     *
     * @param delta time since last renders in seconds.
     */
    @Override
    public void render(float delta) {
        GameController.getInstance().removeFlagged();

        GameController.getInstance().update(delta);
        game.getBatch().setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor( 255/255f, 255/255f, 255/255f, 1 );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        game.getBatch().begin();
        drawBackground();
        drawEntities();
        game.getBatch().end();

        if (DEBUG_PHYSICS) {
            debugCamera = camera.combined.cpy();
            debugCamera.scl(1 / PIXEL_TO_METER);
            debugRenderer.render(GameController.getInstance().getWorld(), debugCamera);
        }
    }

    /**
     * Draws the entities to the screen.
     */
    private void drawEntities() {
        List<DrawnLineModel> lines = GameModel.getInstance().getDrawnLines();
        for (DrawnLineModel line : lines) {
            EntityView view = new DrawnLineView(game);
            view.update(line);
            view.draw(game.getBatch());
        }
        
        List<InkJarModel> inkJars = GameModel.getInstance().getInkJars();
        for (InkJarModel model : inkJars) {
            EntityView view = ViewFactory.makeView(game, model);
            view.update(model);
            view.draw(game.getBatch());
        }


        CursorModel cursorModel = GameModel.getInstance().getCursor();
        EntityView view = ViewFactory.makeView(game, cursorModel);
        

        view.update(cursorModel);
        view.draw(game.getBatch());
        
        PortalModel portalModel = GameModel.getInstance().getPortal();
        view = ViewFactory.makeView(game, portalModel);
        
        view.update(portalModel);
        view.draw(game.getBatch());
        
        StickmanModel stickmanModel = GameModel.getInstance().getStickman();
        view = ViewFactory.makeView(game, stickmanModel);
        
        view.update(stickmanModel);
        view.draw(game.getBatch());

    }

    /**
     * Draws the background
     */
    private void drawBackground() {
        Texture background = this.textureManager.getBackground();
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
       /* game.getBatch().draw(background, 0, 0, 0, 0, 
        				(int)(LEVEL_WIDTH / PIXEL_TO_METER),
        				(int) (LEVEL_HEIGHT / PIXEL_TO_METER));*/
        
        Sprite s = new Sprite(background);
        s.setPosition(0,0);
        s.setSize(getWidthPixels(), getWidthPixels() / getViewportRatio());
        s.draw(game.getBatch());
    }
}
