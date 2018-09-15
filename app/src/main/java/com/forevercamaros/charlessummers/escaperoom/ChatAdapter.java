package com.forevercamaros.charlessummers.escaperoom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * Created by GleasonK on 6/25/15.
 */
public class ChatAdapter extends ArrayAdapter<ChatMessage> {
    private static final long FADE_TIMEOUT = 30000;

    private final Context context;
    private LayoutInflater inflater;
    private List<ChatMessage> values;
    private Typeface mcustom_font;

    public ChatAdapter(Context context, List<ChatMessage> values, Typeface custom_font) {
        super(context, R.layout.chat_message_row_layout, android.R.id.text1, values);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.values=values;
        mcustom_font=custom_font;
    }

    class ViewHolder {
        TextView message;
        ChatMessage chatMsg;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ChatMessage chatMsg;
        if(position >= values.size()){ chatMsg = new ChatMessage("","",0); } // Catch Edge Case
        else { chatMsg = this.values.get(position); }
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.chat_message_row_layout, parent, false);
            holder.message = (TextView) convertView.findViewById(R.id.chat_message);
            convertView.setTag(holder);
            Log.d("Adapter", "Recreating fadeout.");
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.message.setTypeface(mcustom_font);
        holder.message.setText(chatMsg.getMessage());
        holder.chatMsg=chatMsg;
        setFadeOut3(convertView, chatMsg);
        return convertView;
    }

    @Override
    public int getCount() {
        return this.values.size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position){
        if (position >= values.size()){ return -1; }
        return values.get(position).hashCode();
    }

    public void removeMsg(int loc){
        this.values.remove(loc);
        notifyDataSetChanged();
    }

    public void addMessage(ChatMessage chatMsg){
        boolean found = false;
        for (ChatMessage chatMessage:values) {
            if (chatMessage.getTimeStamp() == chatMsg.getTimeStamp()){
                found=true;
                break;
            }
        }
        if (!found){
            this.values.add(chatMsg);
            notifyDataSetChanged();
        }
    }

    private void setFadeOut2(final View view, final ChatMessage message){
        Log.i("AdapterFade", "Caling Fade2");
        view.animate().setDuration(1000).setStartDelay(2000).alpha(0)
        .withEndAction(new Runnable() {
            @Override
            public void run() {
                if (values.contains(message))
                    values.remove(message);
                notifyDataSetChanged();
                view.setAlpha(1);
            }
        });
    }

    private void setFadeOut3(final View view, final ChatMessage message){
        Log.i("AdapterFade", "Caling Fade3");
        long elapsed = System.currentTimeMillis() - message.getTimeStamp();
        if (elapsed >= FADE_TIMEOUT){
            if (values.contains(message))
                values.remove(message);
            notifyDataSetChanged();
        }
        view.animate().setStartDelay(FADE_TIMEOUT).setDuration(1500).alpha(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (values.contains(message)){
                            values.remove(message);
                        }
                        notifyDataSetChanged();
                        view.setAlpha(1);
                    }
                });
    }



    /**
     * Format the long System.currentTimeMillis() to a better looking timestamp. Uses a calendar
     *   object to format with the user's current time zone.
     * @param timeStamp
     * @return
     */
    public static String formatTimeStamp(long timeStamp){
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm.ss a");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return formatter.format(calendar.getTime());
    }

}