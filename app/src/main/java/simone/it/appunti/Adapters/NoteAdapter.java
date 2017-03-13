package simone.it.appunti.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import simone.it.appunti.Activities.MainActivity;
import simone.it.appunti.Models.Note;
import simone.it.appunti.R;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteVH> {


    private ArrayList<Note> dataSet = new ArrayList<>();
    private int position;

    public void addNote(Note item) {
        dataSet.add(0, item);
        notifyItemInserted(0);

    }

    public void updateNote(Note item, int position) {
        dataSet.set(position, item);
        notifyItemChanged(position);
    }

    public ArrayList<Note> getNotes() {
        return dataSet;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Note getNote(int position) {
        return dataSet.get(position);
    }


    public void setDataSet(ArrayList<Note> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public void deleteNote(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteVH(view);
    }

    @Override
    public void onBindViewHolder(NoteVH holder, int position) {
        Note note = dataSet.get(position);
        holder.titleTV.setText(note.getTitle());
        holder.dateTV.setText(note.getDate());
        holder.textTV.setText(note.getText());
    }

    public class NoteVH extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView titleTV, dateTV, textTV;

        public NoteVH(View itemView) {
            super(itemView);
            titleTV = (TextView) itemView.findViewById(R.id.titleTV);
            dateTV = (TextView) itemView.findViewById(R.id.dateTV);
            textTV = (TextView) itemView.findViewById(R.id.textTV);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    setPosition(getAdapterPosition());
                    return false;
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuInflater inflater = ((MainActivity)view.getContext()).getMenuInflater();
            inflater.inflate(R.menu.menu_options, contextMenu);
        }

    }
}