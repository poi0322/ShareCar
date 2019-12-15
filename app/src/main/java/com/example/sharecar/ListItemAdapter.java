package com.example.sharecar;

import android.content.Context;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sharecar.DataSet.Write;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class ListItemAdapter extends BaseAdapter {
    private ArrayList<Write>driverLists = new ArrayList<>();

    @Override
    public int getCount() {
        return driverLists.size();
    }

    @Override
    public Write getItem(int position) {
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
            convertView = inflater.inflate(R.layout.driver_list, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView start = convertView.findViewById(R.id.list_start);
        TextView dest = convertView.findViewById(R.id.list_dest);
        TextView time = convertView.findViewById(R.id.list_time);

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */

        Write mListItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */

//        mListItem.getDepartTime();
//        mListItem.getStartLatitude();
//        mListItem.getStartLongitude();
//
//        mListItem.getDestLatitude();
//        mListItem.getDestLongitude();

        Geocoder geocoder = new Geocoder(context,Locale.getDefault());

        String st = null;
        String ds = null;
        try {
            st = geocoder.getFromLocation(mListItem.getStartLatitude(), mListItem.getStartLongitude(), 1).get(0).getAddressLine(0);
            ds = geocoder.getFromLocation(mListItem.getDestLatitude(), mListItem.getDestLongitude(), 1).get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        };


        start.setText(st);
        dest.setText(ds);
        time.setText(mListItem.getDepartTime());

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */
        //참고 : https://mailmail.tistory.com/6
        return convertView;
    }

    public void addItem(Write write){

        driverLists.add(write);
    }
}
