package com.Saran.ribbit;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Saran.ribbit.constants.ParseConstants;
import com.parse.ParseObject;

public class MessageAdapter extends ArrayAdapter<ParseObject> {
	Context mContext;
	List<ParseObject> mMessages;
	int mLayoutResource;

	public MessageAdapter(Context context, int resource,List<ParseObject> messages) {
		super(context, resource, messages);
		mContext = context;
		mMessages = messages;
		mLayoutResource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Must override this method. Adapter use this to create view for each List item
		ViewHolder holder;
		
		//if there is no view, then create it, attach imageview & textview
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(mLayoutResource, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			
			//attaches holder's imageview to imageview of convertView
			holder.inboxItemImageView = (ImageView) convertView.findViewById(R.id.message_icon);
			
			//attaches holder's textview to textview of convertView
			holder.inboxItemTextView = (TextView) convertView.findViewById(R.id.sender_label);
		}else{
			//Means we can reuse view (like cell in iOS). imageview & textview is already attached
			holder = (ViewHolder) convertView.getTag();
		}
		
		ParseObject message = mMessages.get(position);
		
		if(message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.FILE_TYPE_IMAGE)){
			holder.inboxItemImageView.setImageResource(R.drawable.ic_action_picture);
		}else{
			holder.inboxItemImageView.setImageResource(R.drawable.ic_action_play_over_video);
		}
		
		holder.inboxItemTextView.setText(message.getString(ParseConstants.KEY_SENDER_NAME));
		
		
		return convertView;
	}
	
	//ViewHolder pattern to set the data to custom elements in Listitem
	private static class ViewHolder{
		ImageView inboxItemImageView;
		TextView inboxItemTextView;
	}
	
	//This is to refresh the datastructure associated to list and scrollview retain its position.
	public void refill(List<ParseObject> messages) {
		mMessages.clear();
		mMessages.addAll(messages);
		notifyDataSetChanged();
	}

}
