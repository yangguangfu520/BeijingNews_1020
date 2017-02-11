package com.shangguigu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ListView listView;
	private SpannableStringBuilder ssb;
	List<SpannableStringBuilder> spannablesbList;
	
	private final int line_color = 0xAAAAAAAA;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView text = new TextView(this);
		StringBuilder sb = new StringBuilder("我是HTML:");
		sb.append("<img src=\"" + R.drawable.icon_pic + "\">");

		CustomImageGetter imageGetter = new CustomImageGetter(this, CustomImageGetter.DEFAULT,CustomImageGetter.DEFAULT);

		text.setText(Html.fromHtml(sb.toString(),imageGetter, null)); 
		
		listView = (ListView) findViewById(R.id.lvShowText);
		setSpannableSBText();


		//ListView添加头
		listView.addHeaderView(text);
		listView.setAdapter(new ListViewAdapter(MainActivity.this, spannablesbList));
	}

	private void setSpannableSBText() {
		if (spannablesbList == null)
			spannablesbList = new ArrayList<SpannableStringBuilder>();

		/**
		 * Mark the specified range of text with the specified object.<br>
		 * 标记指定的范围使用指定的Object The flags determine how the span will behave when
		 * text is inserted at the start or end of the span's range.<br>
		 * flags决定了范围的开闭情况
		 */
		ssb = new SpannableStringBuilder("为指定的区间[1,4)设置指定的颜色");
		ssb.setSpan(new ForegroundColorSpan(Color.GREEN), 1, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 基本使用_1：为指定的区间设置指定的颜色
		spannablesbList.add(ssb);

		ssb = new SpannableStringBuilder("追加字符");
		ssb.append("fuck!");// 基本使用_2:追加字符
		ssb.setSpan(new ForegroundColorSpan(Color.RED), 4, 8, Spannable.SPAN_MARK_POINT);
		spannablesbList.add(ssb);
		
		ssb = new SpannableStringBuilder("设置字体背景色 ");
		ssb.setSpan(new BackgroundColorSpan(Color.GRAY), new String("设置字体").length(), new String("设置字体背景色 ").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置背景色为青色
		spannablesbList.add(ssb);
	
		ssb = new SpannableStringBuilder("设置字体背景色 Long值方式");
		ssb.setSpan(new BackgroundColorSpan(line_color), new String("设置字体").length(), new String("设置字体背景色 ").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置背景色为青色
		spannablesbList.add(ssb);


		ssb = new SpannableStringBuilder("设置下划线");
		// 设置下划线
		ssb.setSpan(new UnderlineSpan(), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannablesbList.add(ssb);

		ssb = new SpannableStringBuilder("设置删除线");
		ssb.setSpan(new StrikethroughSpan(),  0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannablesbList.add(ssb);

		ssb = new SpannableStringBuilder("设置上下标:y=x3+An");
		//
		ssb.setSpan(new SuperscriptSpan(), new String("设置上下标:y=x").length(), new String("设置上下标:y=x3").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 下标
		ssb.setSpan(new SubscriptSpan(),  new String("设置上下标:y=x3+A").length(), new String("设置上下标:y=x3+An").length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 上标
		spannablesbList.add(ssb);
		
		ssb = new SpannableStringBuilder("超级链接:电话 ");
		ssb.setSpan(new URLSpan("tel:13912345678"), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 电话
		spannablesbList.add(ssb);

		ssb = new SpannableStringBuilder("超级链接:邮件 ");
		ssb.setSpan(new URLSpan("mailto:webmaster@google.com"),5, 7,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 邮件
		ssb.setSpan(new ForegroundColorSpan(Color.YELLOW),5, 7,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannablesbList.add(ssb);

		ssb = new SpannableStringBuilder("超级链接:网络 ");
		ssb.setSpan(new URLSpan("https://www.baidu.com"), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 网络
		ssb.setSpan(new ForegroundColorSpan(Color.LTGRAY),5, 7,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannablesbList.add(ssb);

		ssb = new SpannableStringBuilder("超级链接:短信 ");
		ssb.setSpan(new URLSpan("sms:13912345678"), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 短信
		ssb.setSpan(new ForegroundColorSpan(Color.BLUE),5, 7,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannablesbList.add(ssb);

		ssb = new SpannableStringBuilder("超级链接:地图 ");
		ssb.setSpan(new URLSpan("geo:38.899533,-77.036476"), 5, 7,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 
		ssb.setSpan(new ForegroundColorSpan(Color.GREEN),5, 7,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannablesbList.add(ssb);

		// 注意：设置链接后，指定区间的文本会变成蓝色，会遮住以前设置的颜色，所以应在设置链接后再为指定区间的文字设置颜色
		ssb = new SpannableStringBuilder("设置链接:文本 ");
		ssb.setSpan(new URLSpan("cacaca") {
			@Override
			public void onClick(View widget) {
				Toast.makeText(MainActivity.this, "点击了设置的链接", 0).show();
			}
		}, 5, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new ForegroundColorSpan(Color.RED),5, 7,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannablesbList.add(ssb);
		
		ssb = new SpannableStringBuilder("设置项目符号");
		ssb.setSpan(new BulletSpan(android.text.style.BulletSpan.STANDARD_GAP_WIDTH, Color.GREEN),  0, new String("设置项目符号").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 第一个参数表示项目符号占用的宽度，第二个参数为项目符号的颜色
		spannablesbList.add(ssb);
		
		ssb = new SpannableStringBuilder("设置字体样式正常，粗体，斜体，粗斜体 ");
		// 设置字体样式正常，粗体，斜体，粗斜体
		ssb.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), 6, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 正常
		ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 9, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 粗体
		ssb.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 12, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 斜体
		ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 15, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 粗斜体
		spannablesbList.add(ssb);

		// 设置字体(default,default-bold,monospace,serif,sans-serif)
		String str = "设置字体(default,default-bold,monospace,serif,sans-serif)";
		ssb = new SpannableStringBuilder(str);
		ssb.setSpan(new TypefaceSpan("default"), 0, new String("设置字体(default,").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new TypefaceSpan("default-bold"), new String("设置字体(default,").length(), new String("设置字体(default,default-bold,").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new TypefaceSpan("monospace"), new String("设置字体(default,default-bold,").length(), new String("设置字体(default,default-bold,monospace,").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new TypefaceSpan("serif"), new String("设置字体(default,default-bold,monospace,").length(), new String("设置字体(default,default-bold,monospace,serif,").length(),
		Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new TypefaceSpan("sans-serif"), new String("设置字体(default,default-bold,monospace,serif,").length(), new String("设置字体(default,default-bold,monospace,serif,sans-serif)").length(),
		Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannablesbList.add(ssb);

		ssb = new SpannableStringBuilder("设置字体大小(绝对值：单位:像素/单位:像素)");
		ssb.setSpan(new AbsoluteSizeSpan(20),new String("设置字体大小(绝对值,").length(), new String("设置字体大小(绝对值,单位:像素,").length(),  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new AbsoluteSizeSpan(20, true),new String("设置字体大小(绝对值,单位:像素,").length(), new String("设置字体大小(绝对值,单位:像素,单位:像素)").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 第二个参数boolean
																								// dip，如果为true，表示前面的字体大小单位为dip，否则为像素，上同
		spannablesbList.add(ssb);

		ssb = new SpannableStringBuilder("设置字体大小（相对值：一半/两倍,单位：像素） 参数表示为默认字体大小的多少倍 ");
		ssb.setSpan(new RelativeSizeSpan(0.5f), new String("设置字体大小（相对值：").length(), new String("设置字体大小（相对值：一半/").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 0.5f表示默认字体大小的一半
		ssb.setSpan(new RelativeSizeSpan(2.0f), new String("设置字体大小（相对值：一半/").length(), new String("设置字体大小（相对值：一半/两倍,").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍
		spannablesbList.add(ssb);
		
		ssb = new SpannableStringBuilder("我的后面添加图片：  ");
		ssb.setSpan(new ImageSpan(this, R.drawable.ic_launcher), 9, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		
		spannablesbList.add(ssb);
		
		ssb = new SpannableStringBuilder("我的中 间添加图片  ");
		ssb.setSpan(new ImageSpan(this, R.drawable.ic_launcher), 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		
		spannablesbList.add(ssb);
		
		ssb = new SpannableStringBuilder("图片点击事件的处理  ");
		ssb.setSpan(new ImageSpan(this, R.drawable.ic_launcher), 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		ssb.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
					Toast.makeText(MainActivity.this, "图片点击事件的处理 ", 0).show();
			}
		}, 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  


		spannablesbList.add(ssb);
		
		ssb = new SpannableStringBuilder("更复杂的点击效果");
		ssb.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
					Toast.makeText(MainActivity.this, "更复杂的点击效果", 0).show();
			}
		}, 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		
		spannablesbList.add(ssb);
		
		ssb = new SpannableStringBuilder("更复杂的点击效果");
		ssb.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
					Toast.makeText(MainActivity.this, "更复杂的点击效果", 0).show();
			}
			public void updateDrawState(TextPaint ds) {  
                ds.setUnderlineText(false);  
        }  
		}, 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		
		spannablesbList.add(ssb);
	}
	
	public static class CustomImageGetter implements Html.ImageGetter {
		public static final int DEFAULT = -1;
		int mRight;
		int mBottom;
		Context mContext;

		public CustomImageGetter(Context context, int right, int bottom) {
			mRight = right;
			mBottom = bottom;
			mContext = context;
		}

		@Override
		public Drawable getDrawable(String source) {
			int id = Integer.parseInt(source);
			Drawable d = mContext.getResources().getDrawable(id);
			if (null != d) {
				d.setBounds(0, 0, mRight == DEFAULT ? d.getIntrinsicWidth() : mRight,
						mBottom == DEFAULT ? d.getIntrinsicHeight() : mBottom);
			}
			return d;
		}
	}

}
