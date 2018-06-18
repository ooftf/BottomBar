package com.ooftf.bottombar.holder;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ooftf.bottombar.BottomBar;
import com.ooftf.bottombar.OnItemRepeatListener;
import com.ooftf.bottombar.OnItemSelectChangedListener;
import com.ooftf.bottombar.OnItemSelectIInterceptor;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    BottomBar bottomBar;
    TextView textView;
    Boolean isIntercept = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        textView = findViewById(R.id.text);
        bottomBar.setOnItemSelectChangedListener(new OnItemSelectChangedListener() {
            @Override
            public void onItemSelectChanged(int oldIndex, int newIndex) {
                if (newIndex == 0) {
                    textView.setText("First");
                } else if (newIndex == 1) {
                    textView.setText("Second");
                }
            }
        });
        bottomBar.setOnItemSelectIInterceptor(new OnItemSelectIInterceptor() {
            @Override
            public boolean onIntercept(int oldIndex, int newIndex) {
                return isIntercept;
            }
        });
        bottomBar.setOnItemRepeatListener(new OnItemRepeatListener() {
            @Override
            public void onItemRepeat(int index) {
                isIntercept = !isIntercept;
            }
        });
        bottomBar.setAdapter(new BottomBar.Adapter<ViewHolder>() {
            @Override
            public void onBindViewHolder(@NotNull ViewHolder holder, int position, int selectedPosition) {
                if (position == selectedPosition) {
                    holder.title.setTextColor(Color.parseColor("#2196F3"));
                    switch (position) {
                        case 0:
                            holder.title.setText("First");
                            holder.icon.setImageResource(R.drawable.ic_app_selected_24dp);
                            break;
                        case 1:
                            holder.title.setText("Second");
                            holder.icon.setImageResource(R.drawable.ic_debug_selected_24dp);
                            break;
                    }
                } else {
                    holder.title.setTextColor(Color.parseColor("#000000"));
                    switch (position) {
                        case 0:
                            holder.title.setText("First");
                            holder.icon.setImageResource(R.drawable.ic_app_24dp);
                            break;
                        case 1:
                            holder.title.setText("Second");
                            holder.icon.setImageResource(R.drawable.ic_debug_24dp);
                            break;
                    }

                }
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(getLayoutInflater().inflate(R.layout.item_bottombar, parent, false));
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });
        bottomBar.setSelectIndex(0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
