package com.LibraryManager.andres.librarymanager;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Locale;

        import android.content.Context;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.Filter;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.view.View.OnClickListener;

        import com.bumptech.glide.Glide;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;


public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<BookList> worldpopulationlist = null;
    private ArrayList<BookList> arraylist;

    public ListViewAdapter(Context context, List<BookList> worldpopulationlist) {
        mContext = context;
        this.worldpopulationlist = worldpopulationlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<BookList>();
        this.arraylist.addAll(worldpopulationlist);
    }

    public class ViewHolder {
        TextView rank;
        TextView country;
        TextView population;
        ImageView flag;
        ImageView marker;
    }

    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public BookList getItem(int position) {
        return worldpopulationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);

            // Locate the TextViews in listview_item.xml
            holder.rank = (TextView) view.findViewById(R.id.rank);
            holder.country = (TextView) view.findViewById(R.id.country);
            holder.population = (TextView) view.findViewById(R.id.population);
            // Locate the ImageView in listview_item.xml
            holder.flag = (ImageView) view.findViewById(R.id.flag);
            holder.marker = (ImageView) view.findViewById(R.id.checkmark);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.rank.setText(worldpopulationlist.get(position).getBookName());
        holder.country.setText(worldpopulationlist.get(position).getAuthor());
        holder.population.setText(worldpopulationlist.get(position)
                .getGenre());
        // Set the results into ImageView
        holder.marker.setBackgroundResource(R.color.trans);
        if(worldpopulationlist.get(position).getAvailabilty())
            holder.marker.setVisibility(View.GONE);
        else
        {
            holder.marker.setImageResource(R.drawable.checkoutmarker);
            holder.marker.setVisibility(View.VISIBLE);
        }
        Glide.with(mContext).load(worldpopulationlist.get(position).getImage()).into(holder.flag);



        return view;
    }

        // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        worldpopulationlist.clear();
        if (charText.length() == 0 || charText.equals("") || charText.equals(null)) {
            worldpopulationlist.addAll(arraylist);
        } else {
            for (BookList wp : arraylist) {
                if (wp.getBookName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    worldpopulationlist.add(wp);
                }
                if (wp.getAuthor().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    worldpopulationlist.add(wp);
                }
                if (wp.getGenre().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    worldpopulationlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void Inverse(){
        for (int i = 0; i<worldpopulationlist.size() / 2; i++)
        {
            BookList temp = new BookList(worldpopulationlist.get(i).getBookName(),
                    worldpopulationlist.get(i).getAuthor(),
                    worldpopulationlist.get(i).getGenre(),
                    worldpopulationlist.get(i).getImage(),
                    worldpopulationlist.get(i).getAvailabilty());
            worldpopulationlist.get(i).SetValues(worldpopulationlist.get(worldpopulationlist.size() - i - 1).getBookName(),
                    worldpopulationlist.get(worldpopulationlist.size() - i - 1).getAuthor(),
                    worldpopulationlist.get(worldpopulationlist.size() - i - 1).getGenre(),
                    worldpopulationlist.get(worldpopulationlist.size() - i - 1).getImage(),
                    worldpopulationlist.get(worldpopulationlist.size()-i-1).getAvailabilty());
            worldpopulationlist.get(worldpopulationlist.size() - i - 1).SetValues(temp.getBookName(),
                    temp.getAuthor(), temp.getGenre(), temp.getImage(), temp.getAvailabilty());
        }
    }
    public void SortAuthor() {

        int n = worldpopulationlist.size();
        BookList temp;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {

                if ( worldpopulationlist.get(j - 1).getAuthor().compareToIgnoreCase(worldpopulationlist.get(j).getAuthor()) > 0 ) {
                    //swap elements
                    temp = new BookList(worldpopulationlist.get(j - 1).getBookName(),
                            worldpopulationlist.get(j -1).getAuthor(),
                            worldpopulationlist.get(j - 1).getGenre(),
                            worldpopulationlist.get(j - 1).getImage(),
                            worldpopulationlist.get(j - 1).getAvailabilty());
                    //temp = arr[j - 1];
                    worldpopulationlist.get(j - 1).SetValues(worldpopulationlist.get(j).getBookName(),
                            worldpopulationlist.get(j).getAuthor(),
                            worldpopulationlist.get(j).getGenre(),
                            worldpopulationlist.get(j).getImage(),
                            worldpopulationlist.get(j).getAvailabilty());
                    //arr[j - 1] = arr[j];
                    worldpopulationlist.get(j).SetValues(temp.getBookName(),
                            temp.getAuthor(), temp.getGenre(), temp.getImage(), temp.getAvailabilty());
                    //arr[j] = temp;
                }

            }
        }


    }
    public void SortGenre() {

        int n = worldpopulationlist.size();
        BookList temp;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {

                if ( worldpopulationlist.get(j - 1).getGenre().compareToIgnoreCase(worldpopulationlist.get(j).getGenre()) > 0 ) {
                    //swap elements
                    temp = new BookList(worldpopulationlist.get(j - 1).getBookName(),
                            worldpopulationlist.get(j -1).getAuthor(),
                            worldpopulationlist.get(j - 1).getGenre(),
                            worldpopulationlist.get(j - 1).getImage(),
                            worldpopulationlist.get(j - 1).getAvailabilty());
                    //temp = arr[j - 1];
                    worldpopulationlist.get(j - 1).SetValues(worldpopulationlist.get(j).getBookName(),
                            worldpopulationlist.get(j).getAuthor(),
                            worldpopulationlist.get(j).getGenre(),
                            worldpopulationlist.get(j).getImage(),
                            worldpopulationlist.get(j).getAvailabilty());
                    //arr[j - 1] = arr[j];
                    worldpopulationlist.get(j).SetValues(temp.getBookName(),
                            temp.getAuthor(), temp.getGenre(), temp.getImage(), temp.getAvailabilty());
                    //arr[j] = temp;
                }

            }
        }


    }

}