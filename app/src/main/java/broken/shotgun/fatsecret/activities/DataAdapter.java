package broken.shotgun.fatsecret.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import broken.shotgun.fatsecret.R;

/**
 * Created by white on 10/20/2017.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

    private Context mContext;
    private CurrentData[] mData;
    public DataAdapter(Context context, CurrentData[] currentDatas){
        mContext=context;
        mData=currentDatas;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_recyler_acivity,parent,false);
        DataViewHolder viewHolder= new DataViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        holder.bindFoodData(mData[position]);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView name_label;
        public TextView description_label;
        public DataViewHolder(View itemView) {
            super(itemView);
            name_label = itemView.findViewById(R.id.food_name_tv);
            description_label =  itemView.findViewById(R.id.description_tv);

        }
        public void bindFoodData(CurrentData currentData){
            name_label.setText(currentData.getFood_name());
            description_label.setText(currentData.getFood_description());

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String name=name_label.getText().toString();
            String Description=description_label.getText().toString();
            Toast.makeText(mContext,name+" "+Description,Toast.LENGTH_LONG).show();
        }
    }



}
