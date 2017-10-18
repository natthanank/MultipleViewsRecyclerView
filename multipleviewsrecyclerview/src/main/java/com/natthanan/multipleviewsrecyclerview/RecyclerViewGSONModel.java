package com.natthanan.multipleviewsrecyclerview;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by natthanan on 9/22/2017.
 */

public class RecyclerViewGSONModel {
    @SerializedName("id")
    private int id;
    @SerializedName("layoutManager")
    private LayoutManager layoutManager;
    @SerializedName("swipe")
    private Swipe swipe;
    @SerializedName("drag")
    private Drag drag;
    @SerializedName("viewDataModels")
    private ArrayList<ViewDataModel> viewDataModels;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public Swipe getSwipe() {
        return swipe;
    }

    public void setSwipe(Swipe swipe) {
        this.swipe = swipe;
    }

    public Drag getDrag() {
        return drag;
    }

    public void setDrag(Drag drag) {
        this.drag = drag;
    }

    public ArrayList<ViewDataModel> getViewDataModels() {
        return viewDataModels;
    }

    public void setViewDataModels(ArrayList<ViewDataModel> viewDataModels) {
        this.viewDataModels = viewDataModels;
    }

    public class LayoutManager {

        @SerializedName("type")
        private String type;
        @SerializedName("orientation")
        private String orientation;
        @SerializedName("reverseLayout")
        private boolean reverseLayout;
        @SerializedName("spanCount")
        private int spanCount;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOrientation() {
            return orientation;
        }

        public void setOrientation(String orientation) {
            this.orientation = orientation;
        }

        public boolean isReverseLayout() {
            return reverseLayout;
        }

        public void setReverseLayout(boolean reverseLayout) {
            this.reverseLayout = reverseLayout;
        }

        public int getSpanCount() {
            return spanCount;
        }

        public void setSpanCount(int spanCount) {
            this.spanCount = spanCount;
        }
    }

    public class Swipe {

        @SerializedName("isSwipe")
        private boolean isSwipe;
        @SerializedName("swipeFlag")
        private SwipeFlag swipeFlag;
        @SerializedName("swipeRight")
        private SwipeAction swipeRight;
        @SerializedName("swipeLeft")
        private SwipeAction swipeLeft;
        @SerializedName("swipeUp")
        private SwipeAction swipeUp;
        @SerializedName("swipeDown")
        private SwipeAction swipeDown;

        public boolean isSwipe() {
            return isSwipe;
        }

        public void setSwipe(boolean swipe) {
            isSwipe = swipe;
        }

        public SwipeFlag getSwipeFlag() {
            return swipeFlag;
        }

        public void setSwipeFlag(SwipeFlag swipeFlag) {
            this.swipeFlag = swipeFlag;
        }

        public SwipeAction getSwipeRight() {
            return swipeRight;
        }

        public void setSwipeRight(SwipeAction swipeRight) {
            this.swipeRight = swipeRight;
        }

        public SwipeAction getSwipeLeft() {
            return swipeLeft;
        }

        public void setSwipeLeft(SwipeAction swipeLeft) {
            this.swipeLeft = swipeLeft;
        }

        public SwipeAction getSwipeUp() {
            return swipeUp;
        }

        public void setSwipeUp(SwipeAction swipeUp) {
            this.swipeUp = swipeUp;
        }

        public SwipeAction getSwipeDown() {
            return swipeDown;
        }

        public void setSwipeDown(SwipeAction swipeDown) {
            this.swipeDown = swipeDown;
        }

        public class SwipeAction {
            @SerializedName("action")
            private String action;
            @SerializedName("undo")
            private boolean undo;

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
            }

            public boolean isUndo() {
                return undo;
            }

            public void setUndo(boolean undo) {
                this.undo = undo;
            }
        }
        public class SwipeFlag {

            @SerializedName("left")
            private boolean left;
            @SerializedName("right")
            private boolean right;
            @SerializedName("up")
            private boolean up;
            @SerializedName("down")
            private boolean down;

            public boolean isLeft() {
                return left;
            }

            public void setLeft(boolean left) {
                this.left = left;
            }

            public boolean isRight() {
                return right;
            }

            public void setRight(boolean right) {
                this.right = right;
            }

            public boolean isUp() {
                return up;
            }

            public void setUp(boolean up) {
                this.up = up;
            }

            public boolean isDown() {
                return down;
            }

            public void setDown(boolean down) {
                this.down = down;
            }
        }
    }

    public class Drag {

        @SerializedName("isDrag")
        private boolean isDrag;
        @SerializedName("onItemDrop")
        private String onItemDrop;

        public boolean isDrag() {
            return isDrag;
        }

        public void setDrag(boolean drag) {
            isDrag = drag;
        }

        public String getOnItemDrop() {
            return onItemDrop;
        }

        public void setOnItemDrop(String onItemDrop) {
            this.onItemDrop = onItemDrop;
        }
    }

    public class ViewDataModel {

        @SerializedName("viewHolderType")
        private String viewHolderType;
        @SerializedName("model")
        private Model model;
        @SerializedName("tag")
        private String tag;
        @SerializedName("isParent")
        private boolean isParent;
        @SerializedName("groupName")
        private String groupName;

        public String getViewHolderType() {
            return viewHolderType;
        }

        public void setViewHolderType(String viewHolderType) {
            this.viewHolderType = viewHolderType;
        }

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public boolean isParent() {
            return isParent;
        }

        public void setParent(boolean parent) {
            isParent = parent;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }


        public class Model {
            @SerializedName("className")
            private String className;
            @SerializedName("data")
            private String data;

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }
        }
    }


}
