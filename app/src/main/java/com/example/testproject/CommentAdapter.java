package com.example.testproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CommentAdapter extends BaseAdapter {

    public ArrayList<Board> boardArrayList = null;

    public CommentAdapter() {
        boardArrayList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return boardArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return boardArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) { // 이때 다른 xml 에서불러오는 view 의 객체값이 존재하지 않는다면 .. 최초 한번 실행
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_comment_list, parent, false); // 다른 xml 에 있는 view객체를 가지고 올것임
        }

        // 불러오는 view 의 객체로 id를 주입함

        TextView textView_Cname = (TextView) convertView.findViewById(R.id.textView_Cname);
        TextView textView_Cdate = (TextView) convertView.findViewById(R.id.textView_Cdate);
        TextView textView_Ccomment = (TextView) convertView.findViewById(R.id.textView_Ccomment);

        Board board = boardArrayList.get(position); // 아래 addItem 에서 넣은 값을 다시 board에 넣는 방식을 취함

        // 그 값을 세팅을 함
        textView_Cname.setText(board.getName());
        textView_Ccomment.setText(board.getComment());
        textView_Cdate.setText(board.getDate());

        return convertView; //view를 return 함
    }

    //아이템 데이터 추가를 위한 함수, 개발자가 원하는대로 작성 가능.
    public void addItem(String name, String date, String comment) {
        Board item = new Board();

        item.setDate(date);
        item.setName(name);
        item.setComment(comment);

        boardArrayList.add(item);
    }
}
