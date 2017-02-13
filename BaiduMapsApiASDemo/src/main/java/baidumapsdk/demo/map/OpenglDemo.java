/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package baidumapsdk.demo.map;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapDrawFrameCallback;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.util.Log;
import baidumapsdk.demo.R;

/**
 * 此demo用来展示如何在地图绘制的每帧中再额外绘制一些用户自己的内容
 */
public class OpenglDemo extends Activity implements OnMapDrawFrameCallback {

	private static final String LTAG = OpenglDemo.class.getSimpleName();
	// 地图相关
	MapView mMapView;
	BaiduMap mBaiduMap;
	Bitmap bitmap;
	private LatLng latlng1 = new LatLng(39.97923, 116.357428);
	LatLng latlng2 = new LatLng(39.94923, 116.397428);
	LatLng latlng3 = new LatLng(39.96923, 116.437428);
	private int mProgramObject;
	private int mTexID;
	private FloatBuffer mVertices;
	private ShortBuffer mTexCoords;
	private final float[] mVerticesData = { -0.5f, -0.5f, 0, 0.5f, -0.5f, 0, -0.5f, 0.5f, 0, 0.5f, 0.5f, 0 };
	private final short[] mTexCoordsData = {0, 1, 1, 1, 0, 0, 1, 0};
	private boolean mBfirst = false;


    private List<LatLng> latLngPolygon;
	{
		latLngPolygon = new ArrayList<LatLng>();
		latLngPolygon.add(latlng1);
		latLngPolygon.add(latlng2);
		latLngPolygon.add(latlng3);
	}

	private float[] vertexs;
	private FloatBuffer vertexBuffer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opengl);
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.bmapView);

		mBaiduMap = mMapView.getMap();
		mBaiduMap.setOnMapDrawFrameCallback(this);
		bitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.ground_overlay);

        MarkerOptions marker1 = new MarkerOptions();

        marker1.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka));

        marker1.position(latlng2);

        mBaiduMap.addOverlay(marker1);

        MarkerOptions marker2 = new MarkerOptions();

        marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_markb));

        marker2.position(latlng3);
        mBaiduMap.addOverlay(marker2);

	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
        mBfirst = false;
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		// onResume 纹理失效
		textureId = -1;
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}

    @Deprecated
	public void onMapDrawFrame(GL10 gl, MapStatus drawingMapStatus) {
		if (mBaiduMap.getProjection() != null) {
//			calPolylinePoint(drawingMapStatus);
//			drawPolyline(gl, Color.argb(255, 255, 0, 0), vertexBuffer, 10, 3,
//					drawingMapStatus);
//			drawTexture(gl, bitmap, drawingMapStatus);

             drawFrame(drawingMapStatus);
//            drawFrame2(drawingMapStatus);

		}
	}

    @Override
    public void onMapDrawFrame(MapStatus drawingMapStatus) {
        if (mBaiduMap.getProjection() != null) {
//			calPolylinePoint(drawingMapStatus);
//			drawPolyline(gl, Color.argb(255, 255, 0, 0), vertexBuffer, 10, 3,
//					drawingMapStatus);
//			drawTexture(gl, bitmap, drawingMapStatus);

            drawFrame(drawingMapStatus);
        }
    }


    // 采用屏幕坐标， 有抖动，有累计误差
	private void drawFrame(MapStatus drawingMapStatus) {

        PointF p1f = mBaiduMap.getProjection().toOpenGLNormalization(latlng2 , drawingMapStatus);
        PointF p2f = mBaiduMap.getProjection().toOpenGLNormalization(latlng3 , drawingMapStatus);

        float mVerticesData[] = new float[] { p1f.x, p1f.y, 0.0f, p2f.x, p1f.y, 0.0f, p1f.x,
				p2f.y, 0.0f, p2f.x, p2f.y, 0.0f };
		mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mVertices.put(mVerticesData).position(0);

        mTexCoords = ByteBuffer.allocateDirect(mTexCoordsData.length * 2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        mTexCoords.put(mTexCoordsData).position(0);
        if(!mBfirst) {
            comipleShaderAndLinkProgram();
            loadTexture();
            mBfirst = true;
        }

        GLES20.glUseProgram(mProgramObject);

		GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, mVertices);
        GLES20.glEnableVertexAttribArray(0);

        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_SHORT, false, 0, mTexCoords);
        GLES20.glEnableVertexAttribArray(1);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexID);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}


	private void comipleShaderAndLinkProgram() {
		final String vShaderStr = "attribute vec4 a_position;    \n"
				+"attribute vec2 a_texCoords; \n"
				+"varying vec2 v_texCoords; \n"
				+ "void main()                  \n"
				+ "{                            \n"
				+ "   gl_Position = a_position;  \n"
				+"    v_texCoords = a_texCoords; \n"
				+ "}                            \n";
		final String fShaderStr = "precision mediump float;                     \n"
				+"uniform sampler2D u_Texture; \n"
				+"varying vec2 v_texCoords; \n"
				+ "void main()                                  \n"
				+ "{                                            \n"
				+ "  gl_FragColor = texture2D(u_Texture, v_texCoords) ;\n"
				+ "}                                            \n";
		int vertexShader;
		int fragmentShader;
		int programObject;
		int[] linked = new int[1];
		// Load the vertex/fragment shaders
		vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vShaderStr);
		fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fShaderStr);
		// Create the program object
		programObject = GLES20.glCreateProgram();
		if (programObject == 0) {
			return ;

		}

		GLES20.glAttachShader(programObject, vertexShader);
		GLES20.glAttachShader(programObject, fragmentShader);
		// Bind vPosition to attribute 0
		GLES20.glBindAttribLocation(programObject, 0, "a_position");
		GLES20.glBindAttribLocation(programObject, 1, "a_texCoords");
		// Link the program
		GLES20.glLinkProgram(programObject);
		// Check the link status
		GLES20.glGetProgramiv(programObject, GLES20.GL_LINK_STATUS, linked, 0);
		if (linked[0] == 0) {
			Log.e(LTAG, "Error linking program:");
			Log.e(LTAG, GLES20.glGetProgramInfoLog(programObject));
			GLES20.glDeleteProgram(programObject);
			return  ;
		}
		mProgramObject = programObject;
	}
	private int loadShader(int shaderType, String shaderSource) {
		int shader;
		int[] compiled = new int[1];
		// Create the shader object
		shader = GLES20.glCreateShader(shaderType);
		if (shader == 0) {
			return 0;

		}
		// Load the shader source
		GLES20.glShaderSource(shader, shaderSource);
		// Compile the shader
		GLES20.glCompileShader(shader);
		// Check the compile status
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] == 0) {
			Log.e(LTAG, GLES20.glGetShaderInfoLog(shader));
			GLES20.glDeleteShader(shader);
			return 0;
		}
		return shader;
	}
	private void loadTexture() {
		bitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.ground_overlay);
		if (bitmap != null) {
			int []texID = new int[1];
			GLES20.glGenTextures(1, texID, 0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texID[0]);
			mTexID = texID[0];
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
					GLES20.GL_LINEAR);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
					GLES20.GL_LINEAR);

			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
					GLES20.GL_REPEAT);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
					GLES20.GL_REPEAT);

			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			bitmap.recycle();
		}
	}
	public void calPolylinePoint(MapStatus mspStatus) {
		PointF[] polyPoints = new PointF[latLngPolygon.size()];
		vertexs = new float[3 * latLngPolygon.size()];
		int i = 0;
		for (LatLng xy : latLngPolygon) {
			polyPoints[i] = mBaiduMap.getProjection().toOpenGLLocation(xy,
					mspStatus);
			vertexs[i * 3] = polyPoints[i].x;
			vertexs[i * 3 + 1] = polyPoints[i].y;
			vertexs[i * 3 + 2] = 0.0f;
			i++;
		}
		for (int j = 0; j < vertexs.length; j++) {
			Log.d(LTAG, "vertexs[" + j + "]: " + vertexs[j]);
		}
		vertexBuffer = makeFloatBuffer(vertexs);
	}

	private FloatBuffer makeFloatBuffer(float[] fs) {
		ByteBuffer bb = ByteBuffer.allocateDirect(fs.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(fs);
		fb.position(0);
		return fb;
	}

	private void drawPolyline(GL10 gl, int color, FloatBuffer lineVertexBuffer,
			float lineWidth, int pointSize, MapStatus drawingMapStatus) {

		gl.glEnable(GL10.GL_BLEND);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		float colorA = Color.alpha(color) / 255f;
		float colorR = Color.red(color) / 255f;
		float colorG = Color.green(color) / 255f;
		float colorB = Color.blue(color) / 255f;

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVertexBuffer);
		gl.glColor4f(colorR, colorG, colorB, colorA);
		gl.glLineWidth(lineWidth);
		gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, pointSize);

		gl.glDisable(GL10.GL_BLEND);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	int textureId = -1;
	/**
	 * 使用opengl坐标绘制
	 * 
	 * @param gl
	 * @param bitmap
	 * @param drawingMapStatus
	 */
	public void drawTexture(GL10 gl, Bitmap bitmap, MapStatus drawingMapStatus) {
		PointF p1 = mBaiduMap.getProjection().toOpenGLLocation(latlng2,
				drawingMapStatus);
		PointF p2 = mBaiduMap.getProjection().toOpenGLLocation(latlng3,
				drawingMapStatus);
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 3 * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer vertices = byteBuffer.asFloatBuffer();
		vertices.put(new float[] { p1.x, p1.y, 0.0f, p2.x, p1.y, 0.0f, p1.x,
				p2.y, 0.0f, p2.x, p2.y, 0.0f });

		ByteBuffer indicesBuffer = ByteBuffer.allocateDirect(6 * 2);
		indicesBuffer.order(ByteOrder.nativeOrder());
		ShortBuffer indices = indicesBuffer.asShortBuffer();
		indices.put(new short[] { 0, 1, 2, 1, 2, 3 });

		ByteBuffer textureBuffer = ByteBuffer.allocateDirect(4 * 2 * 4);
		textureBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer texture = textureBuffer.asFloatBuffer();
		texture.put(new float[] { 0, 1f, 1f, 1f, 0f, 0f, 1f, 0f });

		indices.position(0);
		vertices.position(0);
		texture.position(0);

		// 生成纹理
		if (textureId == -1) {
			int textureIds[] = new int[1];
			gl.glGenTextures(1, textureIds, 0);
			textureId = textureIds[0];
			Log.d(LTAG, "textureId: " + textureId);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_NEAREST);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		}
	
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		// 绑定纹理ID
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertices);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture);

		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 6, GL10.GL_UNSIGNED_SHORT,
				indices);

		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_BLEND);
	}



    /**
     * Convert width to openGL width
     *
     * @param width
     * @return Width in openGL
     */
    public static float toGLWidth2(float width, MapStatus mapStatus) {

        float winRoundW = (float) Math.abs(mapStatus.winRound.right - mapStatus.winRound.left);

        return 2 * width / winRoundW - 1;
    }

    /**
     * Convert height to openGL height
     *
     * @param height
     * @return Height in openGL
     */
    public static float toGLHeight2(float height, MapStatus mapStatus) {

        float winRoundH = (float) Math.abs(mapStatus.winRound.top - mapStatus.winRound.bottom);
        return 1 - 2 * height / winRoundH;
    }


}
