package com.example.rasheduzzaman.interfacedemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class SignUPActivity extends Activity
{
	EditText editTextUserName,editTextPassword,editTextConfirmPassword,email,phone,homeaddr;
	Button btnCreateAccount,uploadbtn;
	ImageView image;
	public byte[] imgarray;
	private static int LOAD_IMAGE_RESULTS = 1;

	LoginDataBaseAdapter loginDataBaseAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);   //remove title bar
		setContentView(R.layout.signup);

		// get Instance  of Database Adapter
		loginDataBaseAdapter = new LoginDataBaseAdapter(this);
		loginDataBaseAdapter = loginDataBaseAdapter.open();

		// Get Refferences of Views
		uploadbtn = (Button) findViewById(R.id.button);
		editTextUserName = (EditText) findViewById(R.id.editTextUserName);
		email = (EditText) findViewById(R.id.editText);
		phone = (EditText) findViewById(R.id.editText2);
		homeaddr = (EditText) findViewById(R.id.editText3);
		image = (ImageView) findViewById(R.id.imageView);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);

		btnCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);



		uploadbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Create the Intent for Image Gallery.
				Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				// Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
				startActivityForResult(i, LOAD_IMAGE_RESULTS);
			}
		});


		btnCreateAccount.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				String userName = editTextUserName.getText().toString();
				String email1 = email.getText().toString();
				String phone1 = phone.getText().toString();
				String homeaddr1 = homeaddr.getText().toString();
				String password = editTextPassword.getText().toString();
				String confirmPassword = editTextConfirmPassword.getText().toString();

				// check if any of the fields are vaccant
				if (userName.equals("") || password.equals("") || confirmPassword.equals("")) {
					Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
					return;
				}
				// check if both password matches
				if (!password.equals(confirmPassword)) {
					Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
					return;
				} else {
					// Save the Data in Database
					loginDataBaseAdapter.insertEntry(userName, password, email1, phone1, homeaddr1,imgarray);	//==*****+++
					Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(SignUPActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Here we need to check if the activity that was triggers was the Image Gallery.
		// If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
		// If the resultCode is RESULT_OK and there is some data we know that an image was picked.
		if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
			// Let's read picked image data - its URI
			Uri pickedImage = data.getData();
			// Let's read picked image path using content resolver
			String[] filePath = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
			cursor.moveToFirst();
			String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

			// Now we need to set the GUI ImageView data with data read from the picked file.
			Bitmap bm=BitmapFactory.decodeFile(imagePath);
			image.setImageBitmap(bm);

			// At the end remember to close the cursor or you will end with the RuntimeException!
			cursor.close();

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte imageInByte[] = stream.toByteArray();
			imgarray =imageInByte;

		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		loginDataBaseAdapter.close();
	}
}