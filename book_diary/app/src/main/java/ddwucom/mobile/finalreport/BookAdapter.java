package ddwucom.mobile.finalreport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<BookDTO> bookList;
    private LayoutInflater layoutInflater;

    public BookAdapter(Context context, int layout, ArrayList<BookDTO> bookList){
        this.context = context;
        this.layout = layout;
        this.bookList = bookList;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bookList.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        ViewHolder holder;

        if(convertView == null){
            convertView = layoutInflater.inflate(layout, parent, false);

            holder = new ViewHolder();

            holder.image = (ImageView)convertView.findViewById(R.id.image);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.author = (TextView)convertView.findViewById(R.id.author);
            holder.price = (TextView)convertView.findViewById(R.id.price);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }


        holder.image.setImageResource(bookList.get(pos).getCover());
        holder.title.setText(bookList.get(pos).getTitle());
        holder.author.setText(bookList.get(pos).getAuthor());
        holder.price.setText(String.valueOf(bookList.get(pos).getPrice()) + "원");

        return convertView;
    }
    static class ViewHolder{
        ImageView image;
        TextView title ;
        TextView author;
        TextView price;
    }
}
