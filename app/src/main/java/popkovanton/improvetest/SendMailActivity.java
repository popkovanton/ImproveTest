package popkovanton.improvetest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import popkovanton.improvetest.data.Data;

public class SendMailActivity extends AppCompatActivity {

    private TextView emailText;
    private TextView phoneText;
    private TextView passText;
    private ImageView photoImage;
    private Button buttonSend;
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        data = (Data) intent.getSerializableExtra("data");

        final Bitmap bitmap = BitmapFactory.decodeFile(data.getmCurrentPhotoPath());
        buttonSend = findViewById(R.id.buttonSend);
        emailText = findViewById(R.id.emailSend);
        phoneText = findViewById(R.id.phoneSend);
        passText = findViewById(R.id.passSend);
        photoImage = findViewById(R.id.photoSend);

        passText.setText(data.getPassword());
        phoneText.setText(data.getPhone());
        emailText.setText(data.getEmail());
        photoImage.setImageBitmap(bitmap);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // отправка email
                String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"title", null);
                Uri bmpUri = Uri.parse(pathofBmp);
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + ": данные анкеты");
                email.putExtra(Intent.EXTRA_TEXT, "Email: " + data.getEmail() + "\n\rPhone: " + data.getPhone() + "\n\r");
                email.putExtra(Intent.EXTRA_STREAM, bmpUri);

                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Выберите почтовый клиент"));
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
