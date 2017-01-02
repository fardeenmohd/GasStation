package pl.edu.pw.student.mini.gasstation;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Filip Matracki on 1/2/2017.
 */

public class HistoryViewHolder extends RecyclerView.ViewHolder{
    private int viewType_;
    private TextView dateTv;
    private TextView nameTv;
    private TextView priceTv;

    public HistoryViewHolder(View itemView, int viewType)
    {
        super(itemView);
        viewType_ = viewType;
        dateTv = ((TextView) itemView.findViewById(R.id.date_tv));
        nameTv = ((TextView)itemView.findViewById(R.id.name_tv));
        priceTv = ((TextView) itemView.findViewById(R.id.price_tv));


    }

    public void bindData(HistoryElement element)
    {
        //TODO draw differently depending on if element is header or data
        if(viewType_ == HistoryAdapter.TYPE_HEADER)
        {
            dateTv.setText("DATE");
            nameTv.setText("NAME");
            priceTv.setText("PRICE");
        }
        if(viewType_ == HistoryAdapter.TYPE_DATA)
        {
            dateTv.setText(element.getDate());
            nameTv.setText(element.getName());
            priceTv.setText(element.getPrice());
        }
    }

}
