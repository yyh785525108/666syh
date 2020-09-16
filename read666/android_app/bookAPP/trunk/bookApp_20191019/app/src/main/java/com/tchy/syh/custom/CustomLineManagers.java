package com.tchy.syh.custom;

import androidx.recyclerview.widget.RecyclerView;

import com.tchy.syh.custom.toolbar.CommonDivider;

import me.goldze.mvvmhabit.binding.viewadapter.recyclerview.DividerLine;

public class CustomLineManagers {
    protected CustomLineManagers() {
    }

    public static CustomLineManagers.LineManagerFactory both() {
        return new CustomLineManagers.LineManagerFactory() {
            public RecyclerView.ItemDecoration create(RecyclerView recyclerView) {
                return new CommonDivider(recyclerView.getContext(), DividerLine.LineDrawMode.BOTH);
            }
        };
    }

    public static CustomLineManagers.LineManagerFactory horizontal() {
        return new CustomLineManagers.LineManagerFactory() {
            public RecyclerView.ItemDecoration create(RecyclerView recyclerView) {
                return new CommonDivider(recyclerView.getContext(), DividerLine.LineDrawMode.HORIZONTAL);
            }
        };
    }

    public static CustomLineManagers.LineManagerFactory vertical() {
        return new CustomLineManagers.LineManagerFactory() {
            public RecyclerView.ItemDecoration create(RecyclerView recyclerView) {
                return new CommonDivider(recyclerView.getContext(), DividerLine.LineDrawMode.VERTICAL);
            }
        };
    }

    public interface LineManagerFactory {
        RecyclerView.ItemDecoration create(RecyclerView var1);
    }
}
