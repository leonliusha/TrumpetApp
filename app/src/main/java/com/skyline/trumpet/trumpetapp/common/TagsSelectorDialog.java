package com.skyline.trumpet.trumpetapp.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.skyline.trumpet.trumpetapp.R;
import com.skyline.trumpet.trumpetapp.model.Broadcast;
import com.skyline.trumpet.trumpetapp.model.Tag;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/7/17.
 */
public class TagsSelectorDialog {
    private Activity activity;
    private AlertDialog alertDialog;
    private TableLayout tl_tags;
    private List<Tag> tags;
    private String title;
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
    private int font_color;
    private Set<CheckBox> selectedTag = new HashSet<>();
    private Broadcast broadcast;

    public TagsSelectorDialog(Activity activity,String title,List<Tag> tags,int font_color, Broadcast broadcast){
        this.activity = activity;
        this.title = title;
        this.tags = tags;
        this.font_color = font_color;
        this.broadcast = broadcast;
    }

    public void initDialog(final TextView tv_selectedTags, Context context){
        LinearLayout tagsLayout = (LinearLayout)activity.getLayoutInflater().inflate(R.layout.activity_tags_selector,null);
        tl_tags = (TableLayout) tagsLayout.findViewById(R.id.tl_tags);
        tl_tags.setStretchAllColumns(true);
        int count = 0;
        int rowNum = tags.size() / 5 + 1;
        for(int row=0;row<rowNum;row++){
            TableRow tableRow =new TableRow(context);
            for(int col=0; col<5 && count < tags.size(); col++){
                final CheckBox checkBox = new CheckBox(context);
                checkBox.setText(tags.get(count).getTag_name());
                checkBox.setId((int)tags.get(count).getTag_id());
                checkBox.setTextColor(font_color);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            selectedTag.add(checkBox);
                        }else{
                            selectedTag.remove(checkBox);
                        }

                    }
                });
                count++;
                tableRow.addView(checkBox);
            }
            tl_tags.addView(tableRow,new TableLayout.LayoutParams(FP,WC));
        }

        alertDialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(tagsLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        tv_selectedTags.setText(constructValueOfSelectedTags());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                }).show();
       // return alertDialog;
    }

    private String constructValueOfSelectedTags(){
        StringBuilder tagsValue = new StringBuilder();
        StringBuilder tagsId = new StringBuilder();
        Iterator<CheckBox> iterator = selectedTag.iterator();
        CheckBox checkBox;
        while(iterator.hasNext()){
            checkBox = iterator.next();
            tagsValue.append(checkBox.getText());
            tagsValue.append(" ");
            tagsId.append(checkBox.getId());
            tagsId.append(" ");
        }
        broadcast.setTagsId(tagsId.toString());
        return tagsValue.toString();
    }


}
