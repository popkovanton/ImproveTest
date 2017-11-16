package popkovanton.improvetest;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import popkovanton.improvetest.data.Data;
import popkovanton.improvetest.validator.EmailValidator;
import popkovanton.improvetest.validator.PasswordValidator;
import popkovanton.improvetest.validator.PhoneValidator;


public class MainActivity extends AppCompatActivity implements
        View.OnFocusChangeListener {
// Привет, Improveцы!
    private EmailValidator emailValidator;
    private PhoneValidator phoneValidator;
    private PasswordValidator passwordValidator;
    private Data data;


    private EditText emailText;
    private EditText passText;
    private EditText phoneText;
    private TextInputLayout passLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout phoneLayout;
    private TextInputLayout photoLayout;
    private Button button;
    private ImageView buttonPhoto;
    int calc;
    String validPhone;
    private Intent intent;

    private File mPhotoFile;
    private Bitmap mBitmap;
    private String mCurrentPhotoPath;

    private static final int REQUEST_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailValidator = new EmailValidator();
        phoneValidator = new PhoneValidator();
        passwordValidator = new PasswordValidator();
        data = new Data();


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        button = findViewById(R.id.buttonView);
        buttonPhoto = findViewById(R.id.buttonPhoto);
        emailText = findViewById(R.id.emailMain);
        passText = findViewById(R.id.passMain);
        phoneText = findViewById(R.id.phoneMain);
        passLayout = findViewById(R.id.passLayout);
        emailLayout = findViewById(R.id.emailLayout);
        phoneLayout = findViewById(R.id.phoneLayout);
        photoLayout = findViewById(R.id.photoLayout);

        passText.setOnFocusChangeListener(this);
        emailText.setOnFocusChangeListener(this);
        phoneText.setOnFocusChangeListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calc = 0;
                intent = new Intent(MainActivity.this, SendMailActivity.class);
                boolean validEmail = emailValidator.validate(emailText.getText().toString());
                boolean validPass = passwordValidator.validate(passText.getText().toString());

                if (phoneText.getText().length() == 0 || phoneText.getText().length() < 11) {
                    phoneLayout.setError(getString(R.string.error_phone));
                } else {
                    validPhone = phoneValidator.validate(phoneText.getText().toString());
                    calc++;
                    data.setPhone(validPhone);
                }
                if (!validEmail) {
                    emailLayout.setError(getString(R.string.error_email));
                } else {
                    calc++;
                    data.setEmail(emailText.getText().toString());
                }

                if (passText.getText().length() < 6){
                    passLayout.setError(getString(R.string.error_pass_length));
                }  else if (!validPass){
                    passLayout.setError(getString(R.string.error_pass));
                } else {
                    calc++;
                    data.setPassword(passText.getText().toString());
                }
                if (mBitmap == null) {
                    photoLayout.setError(getString(R.string.file_error));
                } else {
                    calc++;
                    data.setmCurrentPhotoPath(mCurrentPhotoPath);
                }
                if (calc == 4) {
                    intent.putExtra("data", data);
                    startActivity(intent);
                }
            }
        });

        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(view);
            }
        });
    }


    @Override
    public void onFocusChange(View view, boolean b) {
        passLayout.setErrorEnabled(false);
        emailLayout.setErrorEnabled(false);
        phoneLayout.setErrorEnabled(false);
        photoLayout.setErrorEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PHOTO) {
                if (mCurrentPhotoPath != null) {
                    bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                    Toast.makeText(this, R.string.take_photo, Toast.LENGTH_SHORT).show();
                }
            }
            mBitmap = bitmap;
            buttonPhoto.setImageBitmap(mBitmap);
        }

    }


    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // убедимся, что камера активна
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // создаем Файл, для сохранения фото
            if (mPhotoFile == null) {
                try {
                    mPhotoFile = createImageFile();
                    Toast.makeText(this, mPhotoFile.getName().toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                    // Ошибка при создании файла
                    Toast.makeText(this, R.string.file_error, Toast.LENGTH_SHORT).show();
                }
            }
            // Продолжаем только если файл успешно создан
            if (mPhotoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takePictureIntent, REQUEST_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // создание имя файла
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // сохраняем путь файла
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
