package ddwu.mobile.finalproject.ma01_20180965;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MediAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<MediDTO> list;
    private NetworkManager networkManager = null;

    public MediAdapter(Context context, int resource, ArrayList<MediDTO> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        networkManager = new NetworkManager(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvDivName = view.findViewById(R.id.divName);
            viewHolder.tvName = view.findViewById(R.id.name);
            viewHolder.tvAddress = view.findViewById(R.id.address);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        MediDTO dto = list.get(position);

        viewHolder.tvDivName.setText(dto.getDivName());
        viewHolder.tvName.setText(dto.getName());
        viewHolder.tvAddress.setText(dto.getAddress());

        return view;
    }


    public void setList(ArrayList<MediDTO> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //    ※ findViewById() 호출 감소를 위해 필수로 사용할 것
    static class ViewHolder {
        public TextView tvDivName = null;
        public TextView tvName = null;
        public TextView tvAddress = null;
    }
}
