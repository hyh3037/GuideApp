package com.jyyl.guideapp.dao;
import java.util.List;


/**
 * 该类是Dao层的抽象接口，抽象定义了Dao类共有的一些方法
 * @param <T>
 * @Author: Shang
 * @Date: 2016/5/13  9:48
 */
public interface BaseDao<T> {

	/**
	 * 获取某个数据库表里所有数据
	 * @return List<T> 实体类的集合，每个实体都对应数据库里一行记录
     */
	List<T> queryList();

	/**
	 * 获取某个数据库表里一行数据
	 * @param id 主键
	 * @return 返回根据主键查询到的实体对象
	 */
	T queryById(Object id);

	boolean del(T t);
	boolean delById(Object id);
	boolean delAll();
	boolean upd(T t);
	boolean add(T t);
	boolean addAll(List<T> list);

}
