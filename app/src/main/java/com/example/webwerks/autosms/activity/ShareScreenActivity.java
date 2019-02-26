package com.example.webwerks.autosms.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.utils.Prefs;
import com.example.webwerks.autosms.utils.Validation;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareScreenActivity extends BaseActivity {

    @BindView(R.id.imgClose)
    ImageView imgClose;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.txtRewardPercent)
    TextView txtRewardPercent;

    static final int PICK_CONTACT = 1;
    static final int PERMISSION_REQUEST_CONTACT = 100;
    Context context;
    String mobile;

    public static void open(DashboardActivity activity) {
        activity.startActivity(new Intent(activity, ShareScreenActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_share_screen;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initViews() {
        context = this;
        etMobile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (etMobile.getRight() - etMobile.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                       // askForContactPermission();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    @OnClick({R.id.imgClose, R.id.btnShareNow, R.id.txtRewardPercent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgClose:
                onBackPressed();
                break;
            case R.id.btnShareNow:
                sendInvitation();
                break;
        }
    }

    public void askForContactPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);
                }
            } else {
                getContact();
            }
        } else {
            getContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact();

                } else {
                    showToast("No permission for contacts");
                }
                return;
            }
        }
    }


    //code
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            mobile = phones.getString(phones.getColumnIndex("data1"));
                            // Log.d("TAGA", mobile);
                            etMobile.setText(mobile);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Log.d("TAGA", name);
                    }
                }
                break;
        }
    }


    private void getContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }


    private void sendInvitation() {

        mobile = etMobile.getText().toString();
        Log.d("TAGA", mobile);
//        String mobile = "7883555555";
        String link = "www.MobileRewards.com";
        String code = Prefs.getActivationCode(getApplicationContext());
//        String code = "KP";


        if (!Validation.isValidPhone(mobile)) {
            showToast("Mobile number is not Valid");
        } else {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
            smsIntent.setData(Uri.parse("smsto: " + mobile));  // This ensures only SMS apps respond
            smsIntent.putExtra("sms_body", "I'am using the Mobile Reward Application. I'am inviting you to download the app with the help of below link and activation code.\n"
                    + "link: " + link + "\n" + "activation code: " + code);
            if (smsIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                startActivity(smsIntent);
            }
            etMobile.setText("");
        }
    }



}
