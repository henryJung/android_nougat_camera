package com.app.provider_camera;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.provider_camera.util.OnPermssionCallBackListener;
import com.app.provider_camera.util.ProviderUtil;
import com.app.provider_camera.util.RuntimeUtil;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

  private static final int REQUEST_TAKE_PHOTO = 1;
  private static final int REQUEST_GALLERY = 2;

  private Button btnCamera;
  private Button btnGallery;
  private ImageView ivPreview;

  private Uri outputFileUri;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initView();
  }

  private void initView() {
    btnCamera = (Button) findViewById(R.id.camera);
    btnGallery = (Button) findViewById(R.id.gallery);

    ivPreview = (ImageView) findViewById(R.id.image_preview);

  }

  public void onCamera(View view) {
    RuntimeUtil.checkPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, RuntimeUtil.PERMISSION_ALBUM, new OnPermssionCallBackListener() {
      @Override
      public void OnGrantPermission() {
        RuntimeUtil.checkPermission(MainActivity.this, getWindow().getDecorView(), Manifest.permission.CAMERA, RuntimeUtil.PERMISSION_CAMERA, null, new OnPermssionCallBackListener() {
          @Override
          public void OnGrantPermission() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
              outputFileUri = ProviderUtil.getOutputMediaFileUri(getBaseContext());
              intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
              startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
          }
        });
      }
    });
  }

  public void onGallery(View view) {
    RuntimeUtil.checkPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, RuntimeUtil.PERMISSION_ALBUM, new OnPermssionCallBackListener() {
      @Override
      public void OnGrantPermission() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
      }
    });
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == RuntimeUtil.PERMISSION_CAMERA) {
      if (RuntimeUtil.verifyPermissions(MainActivity.this, grantResults)) {
        onCamera(btnCamera);
      }
    } else if (requestCode == RuntimeUtil.PERMISSION_ALBUM) {
      if (RuntimeUtil.verifyPermissions(MainActivity.this, getWindow().getDecorView(), grantResults)) {
        onGallery(btnGallery);
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == REQUEST_GALLERY) {
        outputFileUri = data.getData();
      }

      if (outputFileUri != null) {
        drawFile();
      }
    }
  }

  private void drawFile() {
    Bitmap bitmapImage;
    try {
      bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), outputFileUri);
    } catch (IOException e) {
      e.printStackTrace();
      Toast.makeText(getBaseContext(), "IOException:" + e.getMessage(), Toast.LENGTH_SHORT).show();
      return;
    }
    showImage(bitmapImage);
  }

  private void showImage(Bitmap bitmap) {
    Drawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
    ivPreview.setImageDrawable(bitmapDrawable);
  }

}
