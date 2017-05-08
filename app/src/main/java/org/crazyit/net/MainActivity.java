package org.crazyit.net;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.*;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import static android.content.ContentValues.TAG;


public class MainActivity extends Activity
{
	WebView show;
	EditText txt;
    String  web;
	PopupMenu popup = null;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		show = (WebView) findViewById(R.id.webshow);
		txt=(EditText)findViewById(R.id.url);
		Button Bnt=(Button)findViewById(R.id.search);
		Bnt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					web=getResources().getString(R.string.address);
				show.loadUrl(web);
			}
		});
		new Thread()
		{
			public void run()
			{
				try
				{
					// 定义一个URL对象
				///////////////////web在此线程中需重新赋值!!//
					web=getResources().getString(R.string.address);
				//	URL url = new URL("http://www.crazyit.org/"
				//			+ "attachments/month_1008/20100812_7763e970f"
				//			+ "822325bfb019ELQVym8tW3A.png");
					URL url = new URL(web);

					//URL url = new URL("http://192.168.1.125/gallery/gs/handler/getmedia.ashx?moid=3&dt=2&g=2");
					//////////访问李子恒学长创建的网页地址///////////////
					// 打开该URL对应的资源的输入流
					//InputStream is = url.openStream();
					// 从InputStream中解析出图片
				//	bitmap = BitmapFactory.decodeStream(is);
					// 发送消息、通知UI组件显示该图片
				//	handler.sendEmptyMessage(0x123);
				//	is.close();
					// 再次打开URL对应的资源的输入流
					InputStream is = url.openStream();
					// 打开手机文件对应的输出流
					//OutputStream os = openFileOutput("text.exe"
					//	, MODE_PRIVATE);////////////直接下载文件到应用程序路径下，默认为data/data/org.crazy.net
					OutputStream os= new FileOutputStream(Environment.getExternalStorageDirectory()
							+ "/documents/crazy_1.png");////////实现文件下载到SD卡
				//	OutputStream os= new FileOutputStream(Environment.getRootDirectory()
				//				+ "/storage/emulated/legacy/documents/crazy1.jpg");
				//	OutputStream os = openFileOutput("crazy1.png",MODE_WORLD_READABLE);
					byte[] buff = new byte[1024];
					int hasRead = 0;
					// 将URL对应的资源下载到本地
					while((hasRead = is.read(buff)) > 0)
					{
						os.write(buff, 0 , hasRead);
					}
					os.flush();
					is.close();
					os.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();
		show.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				onPopupButtonClick(show);
				return true;

			}
		});
	}
	public void onPopupButtonClick(View button)
	{
		// 创建PopupMenu对象
	 popup = new PopupMenu(this, button);
		// 将R.menu.popup_menu菜单资源加载到popup菜单中
		getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
		// 为popup菜单的菜单项单击事件绑定事件监听器
		popup.setOnMenuItemClickListener(
				new PopupMenu.OnMenuItemClickListener()
				{
					@Override
					public boolean onMenuItemClick(MenuItem item)
					{
						Toast.makeText(MainActivity.this,
								"您单击了【" + item.getTitle() + "】菜单项"
								, Toast.LENGTH_SHORT).show();
						return true;
					}
				});
		popup.show();
	}

	public void createPdfFromCurrentScreen(View v) {
		new Thread() {
			public void run() {
				// Get the directory for the app's private pictures directory.
				final File file = new File(
						Environment.getExternalStorageDirectory(), "demo.pdf");

				if (file.exists ()) {
					file.delete ();
				}

				FileOutputStream out = null;
				try {
					out = new FileOutputStream(file);

					PdfDocument document = new PdfDocument();
					Point windowSize = new Point();
					getWindowManager().getDefaultDisplay().getSize(windowSize);
					PdfDocument.PageInfo pageInfo =
							new PdfDocument.PageInfo.Builder(
									windowSize.x, windowSize.y, 1).create();
					PdfDocument.Page page = document.startPage(pageInfo);
					View content = getWindow().getDecorView();
					content.draw(page.getCanvas());
					document.finishPage(page);
					document.writeTo(out);
					document.close();
					out.flush();

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(MainActivity.this, "File created: "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
						}
					});
				} catch (Exception e) {
					Log.d("TAG_PDF", "File was not created: "+e.getMessage());
				} finally {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

}

