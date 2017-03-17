package com.liwei.greendao.activity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.liwei.greendao.Customer;
import com.liwei.greendao.CustomerDao;
import com.liwei.greendao.DaoMaster;
import com.liwei.greendao.DaoSession;
import com.liwei.mystudy.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

public class GreenDaoActivity extends Activity {
    @BindView(R.id.add)
    public Button add;
    @BindView(R.id.delete)
    public Button delete;
    @BindView(R.id.aquery)
    public Button aquery;
    @BindView(R.id.modify)
    public Button modify;
    @BindView(R.id.recycler)
    public RecyclerView recycler;

    private CustomerDao customerDao;

    private GreenDaoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dao);

        ButterKnife.bind(this);

        //创建一个开发环境的Helper类，如果是正式环境调用DaoMaster.OpenHelper
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"androidXX",null);
        //通过Handler类获得数据库对象
        SQLiteDatabase readableDataBase = helper.getReadableDatabase();
        //通过数据库对象生成DaoMaster对象
        DaoMaster daoMaster = new DaoMaster(readableDataBase);
        //获取DaoSession对象
        DaoSession session = daoMaster.newSession();
        //通过DaoSeesion对象获得CustomerDao对象
        customerDao = session.getCustomerDao();

    }

    @OnClick({R.id.add,R.id.delete,R.id.aquery,R.id.modify})
    public void OnClick(View v){
        Customer customer = new Customer();
        List<Customer> allDatas;
        switch (v.getId()){
            case R.id.add:
                customer.setId(1L);
                customer.setCustomerName("Lee");
                customer.setCustomerPhone("10086");
                customer.setCustomerAge(22);

                long customerID = customerDao.insert(customer);
                if(customerID > 0){
                    Toast.makeText(GreenDaoActivity.this,"新增成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(GreenDaoActivity.this,"新增失败",Toast.LENGTH_SHORT).show();
                }

                allDatas = customerDao.loadAll();
                if(allDatas != null && allDatas.size() != 0){
                    adapter = new GreenDaoAdapter(allDatas);
                    LinearLayoutManager llm = new LinearLayoutManager(GreenDaoActivity.this,
                            LinearLayoutManager.VERTICAL,false);
                    recycler.setAdapter(adapter);
                    recycler.setLayoutManager(llm);
                }
                break;
            case R.id.delete:
                customerDao.deleteByKey(1L);
                adapter.notifyDataSetChanged();
                Toast.makeText(GreenDaoActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.aquery:
                allDatas = customerDao.loadAll();
                if(allDatas != null && allDatas.size() != 0){
                    adapter = new GreenDaoAdapter(allDatas);
                    LinearLayoutManager llm = new LinearLayoutManager(GreenDaoActivity.this,
                            LinearLayoutManager.VERTICAL,false);
                    recycler.setAdapter(adapter);
                    recycler.setLayoutManager(llm);
                }else{
                    Toast.makeText(GreenDaoActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.modify:
                QueryBuilder<Customer> builder = customerDao.queryBuilder();
                //添加where条件查询
                builder.where(CustomerDao.Properties.CustomerName.eq("Lee"));
                //添加and条件语句
                builder.and(CustomerDao.Properties.CustomerPhone.eq("10086"),CustomerDao.Properties.CustomerPhone.isNotNull());
                //建造一个Query对象
                Query<Customer> query = builder.build();
                //通过query对象获取结果集
                List<Customer> queryList = query.list();

                Customer queryCustomer = queryList.get(0);

                queryCustomer.setCustomerName("Well");
                queryCustomer.setCustomerPhone("1008611");
                queryCustomer.setCustomerAge(20);
                customerDao.update(queryCustomer);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @OnLongClick({R.id.add,R.id.delete,R.id.aquery,R.id.modify})
    public boolean OnLongClick(View v){
        List<Customer> allDatas;
        switch (v.getId()) {
            case R.id.add:
                List<Customer> datas = new ArrayList<>();
                Customer customer1 = new Customer();
                customer1.setId(1000L);
                customer1.setCustomerName("张三");
                customer1.setCustomerPhone("110");
                customer1.setCustomerAge(22);
                Customer customer2 = new Customer();
                customer2.setId(1001L);
                customer2.setCustomerName("李四");
                customer2.setCustomerPhone("120");
                customer2.setCustomerAge(23);
                Customer customer3 = new Customer();
                customer3.setId(1002L);
                customer3.setCustomerName("王武");
                customer3.setCustomerPhone("119");
                customer3.setCustomerAge(24);
                Customer customer4 = new Customer();
                customer4.setId(1003L);
                customer4.setCustomerName("孙六");
                customer4.setCustomerPhone("112");
                customer4.setCustomerAge(25);

                datas.add(customer1);
                datas.add(customer2);
                datas.add(customer3);
                datas.add(customer4);

                customerDao.insertInTx(datas);
                Toast.makeText(GreenDaoActivity.this,"批量新增成功",Toast.LENGTH_SHORT).show();

                allDatas = customerDao.loadAll();
                if(adapter == null){
                    if(allDatas != null && allDatas.size() != 0){
                        adapter = new GreenDaoAdapter(allDatas);
                        LinearLayoutManager llm = new LinearLayoutManager(GreenDaoActivity.this,
                                LinearLayoutManager.VERTICAL,false);
                        recycler.setAdapter(adapter);
                        recycler.setLayoutManager(llm);
                    }
                }else{
                    adapter.updataData(allDatas);
                }
                break;
            case R.id.delete:
                customerDao.deleteAll();
                Toast.makeText(GreenDaoActivity.this,"全部删除成功",Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                break;
            case R.id.aquery:
                allDatas = customerDao.loadAll();
                if(allDatas != null && allDatas.size() != 0){
                    adapter = new GreenDaoAdapter(allDatas);
                    LinearLayoutManager llm = new LinearLayoutManager(GreenDaoActivity.this,
                            LinearLayoutManager.VERTICAL,false);
                    recycler.setAdapter(adapter);
                    recycler.setLayoutManager(llm);
                }else{
                    Toast.makeText(GreenDaoActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.modify:

                break;
        }
        return false;
    }
}