package be.ac.ulg.info0940.g08.dungeoncrawler;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import be.ac.ulg.info0940.g08.dungeoncrawler.GameLogic.GameLogic;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.GameLoader;
import be.ac.ulg.info0940.g08.dungeoncrawler.GameModel.LevelLoader.LevelLoader;
import be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine.GlSurface;

public class MainActivity extends Activity {
    private GlSurface glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting the window to full screen and turn the title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // OpenGL stuff
        glSurfaceView = new GlSurface(this);
        setContentView(R.layout.activity_main);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gamelayout);
        RelativeLayout.LayoutParams glParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(glSurfaceView, glParam);

        // GameLoader load the game and the init map!!
        GameLoader mod = new GameLoader(this, "init");
        LevelLoader loader = mod.getLevelLoader();

        // Create the logic and give to it the control
        GameLogic logic = new GameLogic(loader, mod.getInitMap(), glSurfaceView, this);
        logic.startGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }
}
