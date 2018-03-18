# MultipleViewsRecyclerView
MultipleViewsRecyclerView is library to help you create recyclerview that have multiple viewholder types in one recyclerview. 

### Setup
1. Gradle
```
compile 'com.mo0on:multipleviewsrecyclerview:1.0.3'
```
2. Maven
```
<dependency>
  <groupId>com.mo0on</groupId>
  <artifactId>multipleviewsrecyclerview</artifactId>
  <version>1.0.3</version>
  <type>pom</type>
</dependency>
```

### How to use it (Simple)
1. Extends BaseViewHolder, this example is ItemViewHolderClass and HeaderViewHolderClass
```
@LayoutID(R.layout.footer)
public class FooterViewHolder extends BaseViewHolder {

    @ViewID(R.id.item)
    public TextView item;


    public FooterViewHolder(View itemView) {
        super(itemView);
    }
    
    @Override
    public void bind(Object data, final String tag) {

        item.setText(((String) data));

    }

}
```
```
@LayoutID(R.layout.header)
public class HeaderViewHolder extends BaseViewHolder<String> {


    @ViewID(R.id.item)
    TextView item;


    public HeaderViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(String data, final String tag) {
        item.setText(data);
    }
}
```
**Note** the important things you have to do is adding @LayoutID annotation. Unless it will be error.

2. In your Activity recyclerview and layoutmanager ordinarily but use BaseAdapter of this library as the adapter of recyclerview. When you traversal in your dataset pass them to ViewDataModel constructor.
the first parameter is ViewHolderClass you use, the second is your data, and the last is tag, tag can be null.
```
public class RecyclerViewTest extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);
        
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        
        BaseAdapter baseAdapter = new BaseAdapter();
        recyclerView.setAdapter(baseAdapter);
        
      
        for (int j = 0; j < 5; j++) {
            new ViewDataModel(HeaderViewHolder.class, "Group" + j + " HEADER", "HEADER");
            for (int i = 0; i < 2; i++) {
                new ViewDataModel(FooterViewHolder.class, "Group" + j + " number " + i, "FOOTER");
            }
        }
    }
}
```

### How to use it (Group)
if you want to create group of item in recyclerview (when you drag and drop or swipe parent of group, that group will follow the parent) you can add more parameter in ViewDataModel constructor like this. the forth parameter is isParent, only one parent per group, if you want that item to be parent pass true. If not, false. and the last paramter is groupName. Item in the same group have to has same groupName.
```
public class RecyclerViewTest extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);
        
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        
        BaseAdapter baseAdapter = new BaseAdapter();
        recyclerView.setAdapter(baseAdapter);
        
      
        for (int j = 0; j < 5; j++) {
            new ViewDataModel(HeaderViewHolder.class, "Group" + j + " HEADER", "HEADER", true, "Group" + j);
            for (int i = 0; i < 2; i++) {
                new ViewDataModel(FooterViewHolder.class, "Group" + j + " number " + i, "FOOTER", false, "Group" + j);
            }
        }
    }
}
```

### Drag and Drop
if you want to use drag and drop function, just use this code. the viewDataModels is list of viewDataModel you create after that sort after your drop action. you can get you data from it.
```
        new Drag(recyclerView) {
            @Override
            public void onItemDropped(List<ViewDataModel> viewDataModels) {

            }
        };
```

### Swipe
if you want to use swipe function, you can use this code. the second parameter of constructor is swipeFlag it can be right or left if your layoutmanager orientation is vertical. and can be up or down if your layoutmanager orientation is horizontal. 
```
new Swipe(recyclerView, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            protected void onChildSwiped(final int position, ViewDataModel viewDataModel, int direction) {

            }

            @Override
            protected void onParentSwiped(final int position, ArrayList<ViewDataModel> group, int direction) {

            }

            @Override
            protected void afterParentSwiped(int position, ArrayList<ViewDataModel> group, int direction) {

            }

            @Override
            protected void afterChildSwiped(int position, ViewDataModel viewDataModel, int direction) {

            }
        };
```

### Load more data when scroll
just implement LoadMoreListener like this example.
```
recyclerView.addOnScrollListener(new LoadMoreListener(recyclerView) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                new ViewDataModel(HeaderViewHolder.class, "LoadMore"+ page, "LoadMore" , true, "OnLoadMore" + page);
                for (int i = 0; i < 5; i++) {
                    new ViewDataModel(FooterViewHolder.class, "page " + page + " " + Integer.toString((page - 1) * 5 + i), "LoadMore", false, "OnLoadMore" + page);
                    baseAdapter.notifyItemInserted(BaseAdapter.getViewDataModels().size());
                }

                if (page == 4) {
                    stopLoading();
                }

            }
        });
```

### Notify when data has changed
you can get notification when data in view has been change by implement DataChangedCallback in your activity.
and in your ViewHolder class, call the onDataChanged method from your activity and pass parameter like this example.
```
@LayoutID(R.layout.footer)
public class FooterViewHolder extends BaseViewHolder {

    @ViewID(R.id.item)
    public TextView item;


    public FooterViewHolder(View itemView) {
        super(itemView);
    }
    
    @Override
    public void bind(Object data, final String tag) {
        item.setText(((String) data));
        item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                
                // call onDataChanged method
                ((RecyclerViewTest) itemView.getContext()).onDataChanged(tag, getViewHolder(), item, s.toString());
            }
        });

    }

}
```
