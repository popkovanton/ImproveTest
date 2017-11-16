package popkovanton.improvetest.data;


import android.graphics.Bitmap;
import java.io.Serializable;

// адаптер данных (конечно, лучше было сделать фрагментами, но времени переделать нет)

public class Data implements Serializable {

    private String email;
    private String phone;
    private String password;
    private String mCurrentPhotoPath;

    public String getmCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setmCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
