package com.example.liuxuanchi.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by liuxuanchi on 2017/12/23.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private List<People> myPeopleList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View peopleView;
        ImageView peopleImage;
        TextView peopleName;

        public ViewHolder(View view){
            super(view);
            peopleView = view;
            peopleImage = (ImageView)view.findViewById(R.id.people_image);
            peopleName = (TextView)view.findViewById(R.id.people_name);
        }
    }

    public PeopleAdapter(List<People> list){
        myPeopleList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.peopleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final People people = myPeopleList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setTitle(people.getName());

                builder.setNeutralButton("查看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(parent.getContext(), "进入查看页面", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(parent.getContext(), PeopleInformation.class);
                        putInfoToIntent(intent, people);
                        parent.getContext().startActivity(intent);
                    }
                });
                builder.setNegativeButton("编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(parent.getContext(), "进入编辑页面", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(parent.getContext(), PeopleEdit.class);
                        putInfoToIntent(intent, people);
                        parent.getContext().startActivity(intent);
                    }
                });
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(parent.getContext());
                        builder1.setTitle("确认删除");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                people.delete();
                                Intent intent = new Intent(parent.getContext(), PeopleManagement.class);
                                parent.getContext().startActivity(intent);
                            }
                        });
                        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder1.show();
                    }
                });
                builder.setCancelable(true);
                builder.show();

            }
        });

        return holder;
    }

    private void putInfoToIntent(Intent intent, People people) {
        intent.putExtra("data_id",people.getId());
        intent.putExtra("data_name", people.getName());
        intent.putExtra("data_department",people.getDepartment());
        intent.putExtra("data_position",people.getPosition());
        intent.putExtra("data_phone_number",people.getPhoneNumber());
        intent.putExtra("data_headshot", people.getHeadshot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        People people = myPeopleList.get(position);
        //holder.peopleImage.setImageResource(setPeopleImage(Department.intToDepartment(people.getDepartment())));
        holder.peopleImage.setImageBitmap(BitmapFactory.decodeByteArray(people.getHeadshot(),
                0, people.getHeadshot().length));
        holder.peopleName.setText(people.getName());
    }

    @Override
    public int getItemCount() {
        return this.myPeopleList.size();
    }


    public static int setPeopleImage(Department department){
        int imageId;
        switch(department){
            case LOGISTICS:
                imageId = R.drawable.green;
                break;
            case PRODUCTION:
                imageId = R.drawable.red;
                break;
            case MARKET:
                imageId = R.drawable.blue;
                break;
            case FINANCE:
                imageId = R.drawable.yellow;
                break;
            default:
                imageId = R.drawable.gary;
                break;
        }
        return imageId;
    }


}
