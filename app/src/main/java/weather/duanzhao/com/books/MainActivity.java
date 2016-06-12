package weather.duanzhao.com.books;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    ListView listview;

    // 数据

    ArrayList<Book> list;

    // 适配器
    BookAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listView);
        // ButterKnife.bind(this);
        // new NetThread().start();
      //本地解析xml数据文件不一定非要开启新的线程，可以直接在主线程中操作数据，和网络获取数据不一样
        //这里是xml数据本地解析，使用pull方式解析.还有sax,dom两种解析方式
      AssetManager asset = getAssets();
        try {
            InputStream in = asset.open("data.xml");
            //========创建XmlPullParser,有两种方式=======
            //方式一:使用工厂类XmlPullParserFactory
            //  XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
            // XmlPullParser xpp = pullFactory.newPullParser();
            //方式二:使用Android提供的实用工具类android.util.Xml
            XmlPullParser xpp = Xml.newPullParser();
            //即解析器处理的数据从哪个流里面来
            //将数据传给了解析器,给解析器设置了输入流，即数据
            //获得本地的xml数据
            xpp.setInput(in, "utf-8");
            //解析事件
           list = new ArrayList<>();
            Book b = null;
            String text = null;
            int e;
            while (XmlPullParser.END_DOCUMENT != (e = xpp.next())) {
                switch (e) {
                    //开始标签
                    case XmlPullParser.START_TAG:
                        if (xpp.getName().equals(("book"))) {
                            b = new Book();

                        }
                        break;
                    //结束标签
                    case XmlPullParser.END_TAG:
                        if (xpp.getName().equals("book")) {
                            list.add(b);
                        } else if (xpp.getName().equals("title")) {
                            b.setTitle(text);
                        } else if (xpp.getName().equals("isbn")) {
                            b.setIsbn(text);
                        } else if (xpp.getName().equals("author")) {
                            b.setAuthor(text);
                        } else if (xpp.getName().equals("image")) {
                            b.setImage(text);
                        }
                        break;
                    //文本
                    case XmlPullParser.TEXT:
                        text = xpp.getText();
                        break;


                }
            }
            //验证
            for (Book book : list) {
                Log.d("XML", book.toString());

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        //加载数据
        adapter=new BookAdapter(this,list);
        listview.setAdapter(adapter);
    }


        //定义一个子线程，执行网络操作，获得网络上的xml数据，由于获得的文件不是文本资源文件，而是xmL数据文件，所以必须进行解析
        class NetThread extends Thread {
            @Override
            public void run() {
                super.run();
                //获得xml格式的数据
                try {
                    URL url = new URL(Api.GET_XML);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //当要发送数据的时候需要设置这两个方法
                    //数据的接收
                    conn.setDoInput(true);
                    //数据的发送，即代表不发送
                    conn.setDoOutput(false);
                    //这个方法默认是get方法，即获得数据，如果是发送的话，必须写上这个方法并标注post
                    conn.setRequestMethod("GET");
                    //设置超时操作，超过五秒，就不能建立连接
                    conn.setConnectTimeout(15000);
                    //读取超时
                    conn.setReadTimeout(15000);
                    //是否使用huancun
                    conn.setUseCaches(false);
                    //获得响应码
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        //根据连接获得输入流
                        InputStream in = conn.getInputStream();
                        //XML数据的解析
                        //DOM 给xml看做一个数，一个一个的解析出来
                        // SAX  XPP
                        //首先需要一个解析器,可以从输入流中拿出数据进行解析,读一部分解析一部分
                        XmlPullParser xpp = Xml.newPullParser();
                        //即解析器处理的数据从哪个流里面来
                        //将数据传给了解析器,给解析器设置了输入流，即数据
                        //获得本地的xml数据
                        xpp.setInput(in, "utf-8");
                        //解析事件
                        ArrayList<Book> list = new ArrayList<>();
                        Book b = null;
                        String text = null;
                        int e;
                        while (XmlPullParser.END_DOCUMENT != (e = xpp.next())) {
                            switch (e) {
                                //开始标签
                                case XmlPullParser.START_TAG:
                                    if (xpp.getName().equals(("book"))) {
                                        b = new Book();

                                    }
                                    break;
                                //结束标签
                                case XmlPullParser.END_TAG:
                                    if (xpp.getName().equals("book")) {
                                        list.add(b);
                                    } else if (xpp.getName().equals("title")) {
                                        b.setTitle(text);
                                    } else if (xpp.getName().equals("isbn")) {
                                        b.setIsbn(text);
                                    } else if (xpp.getName().equals("author")) {
                                        b.setAuthor(text);
                                    } else if (xpp.getName().equals("image")) {
                                        b.setImage(text);
                                    }
                                    break;
                                //文本
                                case XmlPullParser.TEXT:
                                    text = xpp.getText();
                                    break;


                            }
                        }
                        //验证
                        for (Book book : list) {
                            Log.d("XML", book.toString());

                        }
                        //加载数据
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }













