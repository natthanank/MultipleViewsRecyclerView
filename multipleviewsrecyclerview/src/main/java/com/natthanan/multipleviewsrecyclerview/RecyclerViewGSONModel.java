package com.natthanan.multipleviewsrecyclerview;


import java.util.ArrayList;

/**
 * Created by natthanan on 9/22/2017.
 */

public class RecyclerViewGSONModel {
    private int id;
    private LayoutManager layoutManager;
    private Swipe swipe;
    private Drag drag;
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

    public static class LayoutManager {

        private String type;
        private String orientation;
        private boolean reverseLayout;

        public boolean isReverseLayout() {
            return reverseLayout;
        }

        public void setReverseLayout(boolean reverseLayout) {
            this.reverseLayout = reverseLayout;
        }

        public String getOrientation() {
            return orientation;
        }

        public void setOrientation(String orientation) {
            this.orientation = orientation;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Swipe {

        private boolean isSwipe;
        private String swipeRight;
        private String swipeLeft;
        private String swipeUp;
        private String swipeDown;
        private SwipeFlag swipeFlag;

        public boolean isSwipe() {
            return isSwipe;
        }

        public void setSwipe(boolean swipe) {
            isSwipe = swipe;
        }

        public String getSwipeRight() {
            return swipeRight;
        }

        public void setSwipeRight(String swipeRight) {
            this.swipeRight = swipeRight;
        }

        public String getSwipeLeft() {
            return swipeLeft;
        }

        public void setSwipeLeft(String swipeLeft) {
            this.swipeLeft = swipeLeft;
        }

        public String getSwipeUp() {
            return swipeUp;
        }

        public void setSwipeUp(String swipeUp) {
            this.swipeUp = swipeUp;
        }

        public String getSwipeDown() {
            return swipeDown;
        }

        public void setSwipeDown(String swipeDown) {
            this.swipeDown = swipeDown;
        }

        public SwipeFlag getSwipeFlag() {
            return swipeFlag;
        }

        public void setSwipeFlag(SwipeFlag swipeFlag) {
            this.swipeFlag = swipeFlag;
        }

        public static class SwipeFlag {

            private boolean left;
            private boolean right;
            private boolean up;
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

    public static class Drag {

        private boolean isDrag;
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

    public static class ViewDataModel {

        private String viewHolderType;
        private String tag;
        private boolean isParent;
        private String groupName;
        private Model model;

        public String getViewHolderType() {
            return viewHolderType;
        }

        public void setViewHolderType(String viewHolderType) {
            this.viewHolderType = viewHolderType;
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

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }

        public static class Model {
            private String className;
            private Object data;

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public Object getData() {
                return data;
            }

            public void setData(Object data) {
                this.data = data;
            }
        }
    }


}
