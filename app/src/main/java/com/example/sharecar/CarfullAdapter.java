package com.example.sharecar;

import android.content.Context;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sharecar.DataSet.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CarfullAdapter extends BaseAdapter {
    private ArrayList<User>driverLists = new ArrayList<>();

    @Override
    public int getCount() {
        return driverLists.size();
    }

    @Override
    public User getItem(int position) {
        return driverLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();


        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.pass_list, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView name = convertView.findViewById(R.id.pass_name);
        TextView location = convertView.findViewById(R.id.location);
        TextView birth = convertView.findViewById(R.id.location);

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */

        User mListItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */

//        mListItem.getDepartTime();
//        mListItem.getStartLatitude();
//        mListItem.getStartLongitude();
//
//        mListItem.getDestLatitude();
//        mListItem.getDestLongitude();

        Geocoder geocoder = new Geocoder(context,Locale.getDefault());

        System.out.println(mListItem.getLatitude()+":"+ mListItem.getLongitude()+"");
        String st = null;
        try {
            st = geocoder.getFromLocation(mListItem.getLatitude(), mListItem.getLongitude(), 1).get(0).getAddressLine(0);
        } catch (IOException |IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

                Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String age;
        int iage = Integer.parseInt(sdf.format(date)) / 10000 -
                Integer.parseInt(mListItem.getUserBirth()) / 10000;
        age = String.valueOf(iage);
        name.setText("이름 : "+mListItem.getUserName()+" / 나이 : 만 "+age+"세");
        location.setText(st);


        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */
        //참고 : https://mailmail.tistory.com/6
        return convertView;
    }

    public void addItem(User user){

        driverLists.add(user);
    }
}
