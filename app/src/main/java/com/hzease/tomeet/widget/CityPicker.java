package com.hzease.tomeet.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hzease.tomeet.R;
import com.hzease.tomeet.data.CityInfoBean;
import com.hzease.tomeet.utils.CitycodeUtil;
import com.hzease.tomeet.utils.FileUtil;

/**
 * 城市Picker
 * 
 * @author zihao
 * 
 */
public class CityPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker cityPicker;
//	private ScrollerNumberPicker counyPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempProvinceIndex = -1;
	private int temCityIndex = -1;
	private Context context;
	private List<CityInfoBean> province_list = new ArrayList<CityInfoBean>();
	private HashMap<String, List<CityInfoBean>> city_map = new HashMap<String, List<CityInfoBean>>();

	private CitycodeUtil citycodeUtil;
	private String city_code_string;
	private String city_string;
	public testCity city;

	public CityPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		getaddressinfo();
		// TODO Auto-generated constructor stub
	}

	public CityPicker(Context context) {
		super(context);
		this.context = context;
		getaddressinfo();
		// TODO Auto-generated constructor stub
	}
	
	public void setCity(testCity testCityWbb){
		this.city=testCityWbb;
	}

	// 获取城市信息
	private void getaddressinfo() {
		// TODO Auto-generated method stub
		// 读取城市信息string
		JSONParser parser = new JSONParser();
		String area_str = FileUtil.readAssets(context, "gamelist.json");
		province_list = parser.getJSONParserResult(area_str, "area0");
		city_map = parser.getJSONParserResultArray(area_str, "area1");
	}

	public static class JSONParser {
		public ArrayList<String> province_list_code = new ArrayList<String>();
		public ArrayList<String> city_list_code = new ArrayList<String>();

		public List<CityInfoBean> getJSONParserResult(String JSONString, String key) {
			List<CityInfoBean> list = new ArrayList<CityInfoBean>();
			JsonObject result = new JsonParser().parse(JSONString)
					.getAsJsonObject().getAsJsonObject(key);

			Iterator<?> iterator = result.entrySet().iterator();
			while (iterator.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator
						.next();
				CityInfoBean cityinfo = new CityInfoBean();

				cityinfo.setCity_name(entry.getValue().getAsString());
				cityinfo.setId(entry.getKey());
				province_list_code.add(entry.getKey());
				list.add(cityinfo);
			}
			return list;
		}

		public HashMap<String, List<CityInfoBean>> getJSONParserResultArray(
                String JSONString, String key) {
			HashMap<String, List<CityInfoBean>> hashMap = new HashMap<String, List<CityInfoBean>>();
			JsonObject result = new JsonParser().parse(JSONString)
					.getAsJsonObject().getAsJsonObject(key);

			Iterator<?> iterator = result.entrySet().iterator();
			while (iterator.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator
						.next();
				List<CityInfoBean> list = new ArrayList<CityInfoBean>();
				JsonArray array = entry.getValue().getAsJsonArray();
				for (int i = 0; i < array.size(); i++) {
					CityInfoBean cityinfo = new CityInfoBean();
					cityinfo.setCity_name(array.get(i).getAsJsonArray().get(0)
							.getAsString());
					cityinfo.setId(array.get(i).getAsJsonArray().get(1)
							.getAsString());
					city_list_code.add(array.get(i).getAsJsonArray().get(1)
							.getAsString());
					list.add(cityinfo);
				}
				hashMap.put(entry.getKey(), list);
			}
			return hashMap;
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
		citycodeUtil = CitycodeUtil.getSingleton();
		// 获取控件引用
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);

		cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
//		counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);
		provincePicker.setData(citycodeUtil.getProvince(province_list));
		provincePicker.setDefault(1);
		cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil
				.getProvince_list_code().get(1)));
		cityPicker.setDefault(0);
//		counyPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil
//				.getCity_list_code().get(1)));
//		counyPicker.setDefault(1);
		provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				if (text.equals("") || text == null)
					return;
				if (tempProvinceIndex != id) {
					String selectDay = cityPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
//					String selectMonth = counyPicker.getSelectedText();
//					if (selectMonth == null || selectMonth.equals(""))
//						return;
					// 城市数组
					cityPicker.setData(citycodeUtil.getCity(city_map,
							citycodeUtil.getProvince_list_code().get(id)));
					cityPicker.setDefault(0);
//					counyPicker.setData(citycodeUtil.getCouny(couny_map,
//							citycodeUtil.getCity_list_code().get(1)));
//					counyPicker.setDefault(1);
					//得到市级
					String shi=cityPicker.getSelectedText();
//					String quxian=citycodeUtil.getCouny(couny_map,
//							citycodeUtil.getCity_list_code().get(cityPicker.getSelected())).get(1);
					//设置 值
					city.cityAll(text,shi);

					int lastDay = Integer.valueOf(provincePicker.getListSize());
					if (id > lastDay) {
						provincePicker.setDefault(lastDay - 1);
					}
				}
				tempProvinceIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub
			}
		});
		cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				if (text.equals("") || text == null)
					return;
				if (temCityIndex != id) {
					
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
//					String selectMonth = counyPicker.getSelectedText();
//					if (selectMonth == null || selectMonth.equals(""))
//						return;
//					counyPicker.setData(citycodeUtil.getCouny(couny_map,
//							citycodeUtil.getCity_list_code().get(id)));
//					counyPicker.setDefault(1);
					//得到 区县
//					ArrayList<String> quxians=citycodeUtil.getCouny(couny_map,
//							citycodeUtil.getCity_list_code().get(id));
//					String quxian=quxians.get(1);
					//设置 值
					city.cityAll(selectDay,text);
					
					int lastDay = Integer.valueOf(cityPicker.getListSize());
					if (id > lastDay) {
						cityPicker.setDefault(lastDay - 1);
					}
				}
				temCityIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub

			}
		});
//		counyPicker.setOnSelectListener(new OnSelectListener() {
//
//			@Override
//			public void endSelect(int id, String text) {
//				// TODO Auto-generated method stub
//				if (text.equals("") || text == null)
//					return;
//				if (tempCounyIndex != id) {
//					String selectDay = provincePicker.getSelectedText();
//					if (selectDay == null || selectDay.equals(""))
//						return;
//					String selectMonth = cityPicker.getSelectedText();
//					if (selectMonth == null || selectMonth.equals(""))
//						return;
//					
//					//设置 值
//					cityWbb.cityAll(selectDay,selectMonth,text);
//					
//					// 城市数组
//					city_code_string = citycodeUtil.getCouny_list_code()
//							.get(id);
//					int lastDay = Integer.valueOf(counyPicker.getListSize());
//					if (id > lastDay) {
//						counyPicker.setDefault(lastDay - 1);
//					}
//				}
//				tempCounyIndex = id;
//				Message message = new Message();
//				message.what = REFRESH_VIEW;
//				handler.sendMessage(message);
//			}
//
//			@Override
//			public void selecting(int id, String text) {
//				// TODO Auto-generated method stub
//
//			}
//		});
	}

	@SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public String getCity_code_string() {
		return city_code_string;
	}

	public String getCity_string() {
		city_string = provincePicker.getSelectedText()
				+" "+ cityPicker.getSelectedText();
		return city_string;
	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}
	
	public interface testCity {
		
		public void cityAll(String sheng, String shi);
		
	}
}
