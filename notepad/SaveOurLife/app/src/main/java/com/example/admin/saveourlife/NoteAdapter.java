package com.example.admin.saveourlife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<NotePad> {
    public int resourceID;
    public NoteAdapter(Context context, int resource, List<NotePad> objects){
        super(context,resource,objects);
        resourceID = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        NotePad notePad = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.title = view.findViewById(R.id.noteName);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.title.setText(notePad.getTitle());
        return view;
    }

    class ViewHolder{
        TextView title;
    }

}
