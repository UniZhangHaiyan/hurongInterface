package demo;

/*
 *  北京互融时代软件有限公司 OA办公自动管理系统   -- http://www.hurongtime.com
 *  Copyright (C) 2008-2011 Hurong Software Company
 */
import java.math.BigDecimal;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class BeanUtil {
	
	private static Log logger = LogFactory.getLog(BeanUtil.class);
	/**
	 * BeanUtil类型转换器
	 */
	public static ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
	
	static{
		convertUtilsBean.register(new LongConverter(null), Long.class);
		convertUtilsBean.register(new BigDecimalConverter(null), BigDecimal.class);
		convertUtilsBean.register(new IntegerConverter(null), Integer.class);
	}
	
	public static void transMap2Bean(TreeMap<String,String> map, Object obj) {  
        if (map == null || obj == null) {  
            return;  
        }  
        try {  
            BeanUtils.populate(obj, map);  
        } catch (Exception e) {  
        	logger.error(e.getMessage());
        }  
    }  
	
}