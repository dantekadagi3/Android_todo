package com.example.mydoey1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydoey1.adapter.ToDoAdapter;

public class TouchHelper extends ItemTouchHelper.SimpleCallback {

    private ToDoAdapter adapter;
    private Drawable deleteIcon;
    private Drawable editIcon;
    private ColorDrawable deleteBackground;
    private ColorDrawable editBackground;

    public TouchHelper(ToDoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;

        // Initialize icons and backgrounds
        deleteIcon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_delete_24);
        editIcon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_edit_24);
        deleteBackground = new ColorDrawable(Color.RED);
        editBackground = new ColorDrawable(Color.BLUE);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();

        if (direction == ItemTouchHelper.RIGHT) { // Delete action
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setMessage("Are you sure you want to delete this task?")
                    .setTitle("Delete Task")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            adapter.deleteTask(position);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            adapter.notifyItemChanged(position);
                        }
                    }).show();
        } else if (direction == ItemTouchHelper.LEFT) { // Edit action
            // Trigger edit functionality
            adapter.editTask(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20; // Offset to prevent overlap

        Drawable icon = null;
        ColorDrawable background = null;

        if (dX > 0) { // Swiping to the right (Delete)
            icon = deleteIcon;
            background = deleteBackground;
            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left (Edit)
            icon = editIcon;
            background = editBackground;
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // No swipe
            if (background != null) {
                background.setBounds(0, 0, 0, 0);
            }
        }

        if (background != null) {
            background.draw(c);
        }

        if (icon != null) {
            int iconMargin = (((View) itemView).getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();

            if (dX > 0) { // Right swipe
                int iconLeft = itemView.getLeft() + iconMargin;
                int iconRight = iconLeft + icon.getIntrinsicWidth();
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            } else if (dX < 0) { // Left swipe
                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            }

            icon.draw(c);
        }
    }
}
