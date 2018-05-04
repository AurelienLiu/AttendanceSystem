package com.example.liuxuanchi.project.peopleManagement;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liuxuanchi.project.MyNavigationView;
import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.db.People;
import com.soundcloud.android.crop.Crop;

import java.io.File;

public class PeopleEdit extends AppCompatActivity {

    private TextView label;
    private boolean forAdd;

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_FROM_ALBUM = 2;
    private Uri photoUri;  //拍照的图片的uri
    private Uri finalUri;  //剪裁后的图片的uri
    private ImageView photo;
    private Bitmap bitmap;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_edit);

        //设置label
//        label = (TextView)findViewById(R.id.label);
//        label.setText("人员编辑");
//        ActionBar actionBar = getSupportActionBar();  //隐藏系统label
//        if (actionBar != null){
//            actionBar.hide();
//        }
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置DrawerLayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24px);
        }

        //拍照获取相片
        photo = (ImageView)findViewById(R.id.edit_photo);
        Button takePhoto = (Button)findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在SD缓存区创建File对象，存储拍摄的照片
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                File finalImage = new File(getExternalCacheDir(), "final_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (finalImage.exists()) {
                        finalImage.delete();
                    }
                    finalImage.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    photoUri = FileProvider.getUriForFile(PeopleEdit.this, "com.example.liuxuanchi." +
                            "project.fileprovider", outputImage);
                    finalUri = FileProvider.getUriForFile(PeopleEdit.this,
                            "com.example.liuxuanchi.project.fileprovider", finalImage);
                } else {
                    photoUri = Uri.fromFile(outputImage);
                    finalUri = Uri.fromFile(finalImage);
                }
                //启动相机
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        Button chooseFromAlbum = (Button)findViewById(R.id.choose_from_album);
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File finalImage = new File(getExternalCacheDir(), "final_image.jpg");
                try {
                    if (finalImage.exists()) {
                        finalImage.delete();
                    }
                    finalImage.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    finalUri = FileProvider.getUriForFile(PeopleEdit.this,
                            "com.example.liuxuanchi.project.fileprovider", finalImage);
                } else {
                    finalUri = Uri.fromFile(finalImage);
                }
                if (ContextCompat.checkSelfPermission(PeopleEdit.this, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PeopleEdit.this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });


        //判断是从添加按钮进入还是编辑按钮进入
        final Intent intent = getIntent();
        forAdd = intent.getBooleanExtra("for_add", false);

        final EditText editName = (EditText)findViewById(R.id.edit_name);
//        final RadioGroup editDepartment = (RadioGroup) findViewById(R.id.edit_department_group);
        final EditText editDepartment = (EditText)findViewById(R.id.edit_department);
//        final RadioButton editProduction = (RadioButton)findViewById(R.id.edit_production);
//        final RadioButton editMarket = (RadioButton)findViewById(R.id.edit_market);
//        final RadioButton editLogistics = (RadioButton)findViewById(R.id.edit_logistics);
//        final RadioButton editFinance = (RadioButton)findViewById(R.id.edit_finance);
        final EditText editPosition = (EditText)findViewById(R.id.edit_position);
        final EditText editPhoneNumber = (EditText)findViewById(R.id.edit_phone_number);
        final EditText editJobNumber = (EditText)findViewById(R.id.edit_job_number);
        //如果从添加按钮进入，则添加默认头像
        if (forAdd) {
            Toast.makeText(PeopleEdit.this, "添加默认头像", Toast.LENGTH_SHORT).show();
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.people_noheadshot);
        }
        //如果从编辑按钮进入，则将人员的原信息填入EditText
        if (!forAdd) {
            editName.setText(intent.getStringExtra("data_name"));
            editPosition.setText(intent.getStringExtra("data_position"));
            editPhoneNumber.setText(intent.getStringExtra("data_phone_number"));
            editJobNumber.setText(intent.getStringExtra("data_job_number"));
            editDepartment.setText(intent.getStringExtra("data_department"));
//            switch (dep) {
//                case 0:
//                    editProduction.setChecked(true);
//                    break;
//                case 1:
//                    editMarket.setChecked(true);
//                    break;
//                case 2:
//                    editLogistics.setChecked(true);
//                    break;
//                case 3:
//                    editFinance.setChecked(true);
//                    break;
//                default:
//                    break;
//            }
            byte[] bytesOfHeadshot = intent.getByteArrayExtra("data_headshot");
            bitmap = BitmapFactory.decodeByteArray(bytesOfHeadshot,0, bytesOfHeadshot.length);
            photo.setImageBitmap(bitmap);
        }





        //保存信息
        Button editEnsure = (Button)findViewById(R.id.edit_ensure);
        editEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PeopleEdit.this, "点击了确定", Toast.LENGTH_SHORT).show();
                People people = new People();

                people.setName(editName.getText().toString());
                people.setPosition(editPosition.getText().toString());
                people.setPhoneNumber(editPhoneNumber.getText().toString());
                people.setDepartment(editDepartment.getText().toString());
                people.setHeadshot(People.bitmapToArrayOfByte(bitmap));
                people.setJobNumber(editJobNumber.getText().toString());
                Toast.makeText(PeopleEdit.this, editJobNumber.getText().toString()+"", Toast.LENGTH_SHORT).show();
                //判断是添加人员还是修改人员信息
                if (forAdd){
                    people.setStatus(0);
                    people.save();
                    Toast.makeText(PeopleEdit.this, "添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    int id = intent.getIntExtra("data_id", -1);
                    people.setStatus(1);
                    people.update(id);
                    if (id > -1){
                        Toast.makeText(PeopleEdit.this, "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PeopleEdit.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                }
                Intent intent = new Intent(PeopleEdit.this, PeopleManagement.class);
                startActivity(intent);
            }
        });

        //设置navigationView的点击事件
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.people_management);
        MyNavigationView.onSelectItem(navView, PeopleEdit.this, mDrawerLayout);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片进行剪裁
//                        try {
//                            //将剪裁后的照片显示出来
//                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
//                            photo.setImageBitmap(bitmap);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        Crop.of(photoUri, finalUri).asSquare().start(PeopleEdit.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_FROM_ALBUM:
                if (resultCode == RESULT_OK) {
                    //将选择的照片进行剪裁，该照片uri通过data.getData()获得
                    Crop.of(data.getData(), finalUri).asSquare().start(PeopleEdit.this);
                }
                break;
            case Crop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    try {
                        //将剪裁后的照片显示出来
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(finalUri));
                        photo.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            default:
                break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_FROM_ALBUM);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(PeopleEdit.this, "Your Permission is denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }



}
