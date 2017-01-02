package pl.edu.pw.student.mini.gasstation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Filip Matracki on 1/2/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder>{
    private ArrayList<HistoryElement> elements_;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_DATA = 1;

    public HistoryAdapter(ArrayList<HistoryElement> elements)
    {
        elements_ = elements;
        notifyDataSetChanged();
    }

    public void setData(ArrayList<HistoryElement> data)
    {
        elements_ = data;
        notifyDataSetChanged();
    }
    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType == TYPE_HEADER)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_header_row,parent,false);
        }
        if(viewType == TYPE_DATA)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_element_row,parent,false);

        }
        return new HistoryViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position)
    {
        HistoryElement element = null;

        if(position != 0)
        {
            element = elements_.get(position - 1);
        }

        holder.bindData(element);

    }

    @Override
    public int getItemCount()
    {
        return elements_.size()+1;
    }


    public int getItemViewType(int position)
    {
        if(position == 0)
            return TYPE_HEADER;
        else
            return TYPE_DATA;
    }
}
