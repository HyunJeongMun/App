package ddwu.mobile.finalproject.ma01_20180965;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LikeAdapter extends CursorAdapter {
    Context context;
    LayoutInflater layoutInflater;
    int layout;

    public LikeAdapter(Context context, int layout, Cursor c){
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View listItemLayout = layoutInflater.inflate(layout, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        listItemLayout.setTag(holder);
        return listItemLayout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();

        if(holder.tvName == null){
            holder.ivImg = view.findViewById(R.id.img_item);
            holder.tvName = view.findViewById(R.id.likeName);
            holder.tvAddress = view.findViewById(R.id.likeAddress);
        }

        holder.tvName.setText(cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_NAME)));
        holder.tvAddress.setText(cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_ADDR)));

        String path = cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_PHOTO));
        if(path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            holder.ivImg.setImageBitmap(bitmap);
        }
    }

    static class ViewHolder {
        public ImageView ivImg;
        public TextView tvName;
        public TextView tvAddress;

        public ViewHolder(){
            ivImg = null;
            tvName = null;
            tvAddress = null;
        }
    }
}
