package apps.bglx.com.m_update;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import apps.bglx.com.m_update.imageTransformations.BlurTransform;
import apps.bglx.com.m_update.imageTransformations.CircleTransform;
import apps.bglx.com.m_update.imageTransformations.DeepBlurTransform;

public class ArtistInfos extends AppCompatActivity {

    public static Activity infoAct;
    GetInfo get = new GetInfo();
    BlurTransform blur = new BlurTransform();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_infos);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle b = getIntent().getExtras();
        final int id = b.getInt("id");

        new LongOperation().execute(String.valueOf(id));

        Button delete = (Button) findViewById(R.id.info_delete_button);

        final DatabaseManager databaseManager = new DatabaseManager(this);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseManager.deleteArtist(String.valueOf(id));
                MainActivity.mainAct.finish();
                Intent i = new Intent(ArtistInfos.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        databaseManager.close();


    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        String artistPicture, artistName, nbAlbum;

        @Override
        protected String doInBackground(String... info) {
            try {
                String id = info[0];
                List<String> artistInfos = get.getArtistPage(String.valueOf(id));
                artistPicture = artistInfos.get(0);
                artistName = artistInfos.get(1);
                nbAlbum = artistInfos.get(2);
                } catch (Exception e) {
                System.out.println("Artist Infos Error");
                }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            final ImageView artistImageView = (ImageView) findViewById(R.id.info_artist_image);
            final ImageView backgroundImageView = (ImageView) findViewById(R.id.info_background);
            Picasso.get().load(artistPicture).transform(new CircleTransform()).into(artistImageView);
            Picasso.get().load(artistPicture).transform(new DeepBlurTransform()).into(backgroundImageView);
            TextView artistText = (TextView) findViewById(R.id.info_artist_text);
            artistText.setText(artistName);
            TextView nbAlbumText = (TextView) findViewById(R.id.info_nb_album);
            nbAlbum = nbAlbum + " albums";
            nbAlbumText.setText(nbAlbum);
        }

    }

}

