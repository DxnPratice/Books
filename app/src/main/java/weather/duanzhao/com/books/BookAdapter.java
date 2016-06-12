package weather.duanzhao.com.books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windows on 2016/6/6.
 */
public class BookAdapter extends BaseAdapter {
    /**
     * 数据（依赖数据）
     */
    private ArrayList<Book> data;

    /**
     * 上下文（访问应用程序中各种资源）
     */
    private Context context;

    /**
     * 布局实例化服务
     */
    private LayoutInflater layoutInflater;

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param data    数据
     */
    public BookAdapter(Context context, ArrayList<Book> data) {
        this.data = data;
        this.context = context;

        // 获得布局实例化服务
        layoutInflater = LayoutInflater.from(context);

        // layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Book getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // 没有可复用
            // 创建一个视图项
            convertView = layoutInflater.inflate(R.layout.book_item, parent, false);
            // 当前的 ViewHolder 保存视图的结构
            holder = new ViewHolder(convertView);
            // 视图绑定一个对象
            convertView.setTag(holder);
        } else {
            // 获得视图绑定的对象
            holder = (ViewHolder) convertView.getTag();
        }
        // 绑定数据（数据项）
        Book book = data.get(position);
        // 在视图项绑定数据项
        holder.bindData(book);

//        Log.d("getView", "holder: " + holder.id + ", position: " + position);
        return convertView;
    }

    class ViewHolder {


      //  @Bind(R.id.textView_title)
        TextView title;
        //@Bind(R.id.textView_author)
        TextView author;
      //  @Bind(R.id.textView_isbn)
        TextView isbn;
       // @Bind(R.id.imageView_cover)

      ImageView image;

        public ViewHolder(View v) {
            //ButterKnife.bind(this, v);
            title= (TextView) v.findViewById(R.id.textView_title);
            author= (TextView) v.findViewById(R.id.textView_author);
            isbn= (TextView) v.findViewById(R.id.textView_isbn);
            image= (ImageView) v.findViewById(R.id.imageView_cover);
        }

        public void bindData(Book book) {
         author.setText(book.getAuthor());
            isbn.setText(book.getIsbn());
            title.setText(book.getTitle());
            image.setImageResource(R.drawable.ic_face_24dp);

        }
    }
}
