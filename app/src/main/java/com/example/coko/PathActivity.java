package com.example.coko;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class PathActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapGpsManager tmapgps = null;
    private TMapView tMapView = null;
    private static String mApiKey = "l7xx84f4860b8e1b4a5a92b716682a24c0b8";
    private static int mMarkerID;

    private ArrayList<TMapPoint> m_tmapPoint = new ArrayList<TMapPoint>();
    private ArrayList<String> mArrayMarkerID = new ArrayList<String>();
    private ArrayList<MapPoint> m_mapPoint = new ArrayList<MapPoint>();
    double gpsLatitude;
    double gpsLongitude;
    TMapData tmapdata = null;

    double latitude_array[]=new double[5];
    double longitude_array[]=new double[5];
    String name_array[]=new String[5];
    int placeid_array[]=new int[5];

    @Override
    public void onLocationChange(Location location) { //위치 변경 확인
        if (m_bTrackingMode) {
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            TMapPoint pointh = tMapView.getLocationPoint();
            gpsLatitude = pointh.getLatitude();
            gpsLongitude = pointh.getLongitude();
            Intent secondIntent=getIntent();
            double latitude0=secondIntent.getDoubleExtra("latitude0",0);
            double longitude0=secondIntent.getDoubleExtra("longitude0",0);
            String name0=secondIntent.getStringExtra("name0");
            int placeid0= (int) secondIntent.getLongExtra("placeid0",0);
            double latitude1=secondIntent.getDoubleExtra("latitude1",0);
            double longitude1=secondIntent.getDoubleExtra("longitude1",0);
            String name1=secondIntent.getStringExtra("name1");
            int placeid1= (int) secondIntent.getLongExtra("placeid1",0);
            double latitude2=secondIntent.getDoubleExtra("latitude2",0);
            double longitude2= secondIntent.getDoubleExtra("longitude2",0);
            String name2=secondIntent.getStringExtra("name2");
            int placeid2= (int) secondIntent.getLongExtra("placeid2",0);
            double latitude3=secondIntent.getDoubleExtra("latitude3",0);
            double longitude3=secondIntent.getDoubleExtra("longitude3",0);
            String name3=secondIntent.getStringExtra("name3");
            int placeid3= (int) secondIntent.getLongExtra("placeid3",0);
            double latitude4=secondIntent.getDoubleExtra("latitude4",0);
            double longitude4=secondIntent.getDoubleExtra("longitude4",0);
            String name4=secondIntent.getStringExtra("name4");
            int placeid4= (int) secondIntent.getLongExtra("placeid4",0);

            for(int i=0;i<5;i++)
            {
                if(i==0){
                    latitude_array[i]=latitude0;
                    longitude_array[i]=longitude0;
                    name_array[i]=name0;
                    placeid_array[i]=(int)placeid0;
                }
                if(i==1){
                    latitude_array[i]=latitude1;
                    longitude_array[i]=longitude1;
                    name_array[i]=name1;
                    placeid_array[i]=(int)placeid1;
                }
                if(i==2){
                    latitude_array[i]=latitude2;
                    longitude_array[i]=longitude2;
                    name_array[i]=name2;
                    placeid_array[i]=(int)placeid2;
                }
                if(i==3){
                    latitude_array[i]=latitude3;
                    longitude_array[i]=longitude3;
                    name_array[i]=name3;
                    placeid_array[i]=(int)placeid3;
                }
                if(i==4){
                    latitude_array[i]=latitude4;
                    longitude_array[i]=longitude4;
                    name_array[i]=name4;
                    placeid_array[i]=(int)placeid4;
                }

            }

            FindCarPathTask findCarPathTask = new FindCarPathTask(getApplicationContext(),tMapView); // 다중 경로화 함수 선언
            // 다중 경로화
            findCarPathTask.execute(new TMapPoint(gpsLatitude,gpsLongitude),
                    new TMapPoint(latitude0,longitude0),
                    new TMapPoint(latitude1,longitude1),
                    new TMapPoint(latitude2,longitude2),
                    new TMapPoint(latitude3,longitude3),
                    new TMapPoint(latitude4,longitude4));
        }
        m_mapPoint.add(new MapPoint(placeid_array[0], name_array[0], latitude_array[0], longitude_array[0]));
        m_mapPoint.add(new MapPoint(placeid_array[1], name_array[1], latitude_array[1], longitude_array[1]));
        m_mapPoint.add(new MapPoint(placeid_array[2], name_array[2], latitude_array[2], longitude_array[2]));
        m_mapPoint.add(new MapPoint(placeid_array[3], name_array[3], latitude_array[3], longitude_array[3]));
        m_mapPoint.add(new MapPoint(placeid_array[4], name_array[4], latitude_array[4], longitude_array[4]));

        for (int i = 0; i < m_mapPoint.size(); i++) {
            TMapPoint point = new TMapPoint(m_mapPoint.get(i).getLatitude(),
                    m_mapPoint.get(i).getLongitude());
            TMapMarkerItem item1 = new TMapMarkerItem();
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pin);

            final String markName = m_mapPoint.get(i).getName();
            int place_num = m_mapPoint.get(i).getPlace_id(); //place_id를 place_num으로 가져옴

            item1.setTMapPoint(point);
            item1.setName(markName);
            item1.setVisible(item1.VISIBLE);
            item1.setIcon(bitmap);

            //풍선뷰 안의 항목에 글을 지정
            item1.setCalloutTitle(markName);
            item1.setCalloutSubTitle(String.valueOf(i+1));//서브 타이틀 지정

            item1.setCanShowCallout(true);
            item1.setAutoCalloutVisible(true);

            Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.impo1);

            item1.setCalloutRightButtonImage(bitmap_i);

//            String strID = String.format("pmarker%d", place_num);
            final String strID = String.valueOf(place_num); // place_num을 strID로 저장
            tMapView.addMarkerItem(strID, item1);
            mArrayMarkerID.add(strID);
//            Log.v("sadasda id",strID); strID 로그로 확인

        }
        //마커풍선에서 터치(우측 버튼 클릭)시 할 행동 -> 팝업 띄우기, 2가지 버튼(취소/세부정보)
        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(final TMapMarkerItem markerItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("필요한 항목을 선택하시오.")
                        .setIcon(R.drawable.impo1)
                        .setCancelable(true);
                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.setNegativeButton("세부정보", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), InfoAcitivity.class);
                        intent.putExtra("place_id", markerItem.getID());
                        Log.v("#######id", markerItem.getID()); // 로그로 id 확인
                        startActivity(intent);
                    }
                })
                        .show();
            }
        });
        for(int i=0;i<5;i++) {
            Log.d("************", "place_id " + toString().valueOf(placeid_array[i]) + " name " + name_array[i] + " distance " + toString().valueOf(latitude_array[i]) + " longitude " + toString().valueOf(longitude_array[i]));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        mContext = this;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mapview);
        tMapView = new TMapView(this);
        linearLayout.addView(tMapView);
        tMapView.setSKTMapApiKey(mApiKey);

        /*현위치 아이콘표시*/
        tMapView.setIconVisibility(true);

        /*줌레벨*/
        tMapView.setZoomLevel(13);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps = new TMapGpsManager(PathActivity.this);
        tmapgps.setMinTime(0);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER); //연결된 인터넷으로 현 위치를 받습니다.
        tmapgps.OpenGps();


        /*화면중심을 단말의 현재위치로 이동*/
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        // 맵 화면 버튼 3가지 구성
        Button buttonZoomIn = (Button)findViewById(R.id.buttonZoomIn); // 확대 버튼
        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomIn();
            }
        });
        Button buttonZoomOut = (Button)findViewById(R.id.buttonZoomOut); // 축소 버튼
        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomOut();
            }
        });
        Button buttongps = (Button)findViewById(R.id.buttongps); // 현재위치로 화면을 옮기는 버튼
        buttongps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.setCenterPoint(gpsLongitude, gpsLatitude,true);
            }
        });

    }

    /*차로가는 경로 보여주는 함수*/
    void GetCarPath(TMapPoint startPoint, TMapPoint endPoint) {
        tmapdata = new TMapData();
        tmapdata.findPathData(startPoint, endPoint, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                tMapView.addTMapPath(polyLine);
            }
        });
    }
    //

}
