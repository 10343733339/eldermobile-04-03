package com.eryansky.common.web.springmvc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eryansky.common.excel.ExportExcel;
import com.eryansky.common.exception.ActionException;
import com.eryansky.common.model.Result;
import com.eryansky.common.orm.Page;
import com.eryansky.common.orm.PropertyFilter;
import com.eryansky.common.orm.hibernate.EntityManager;
import com.eryansky.common.orm.hibernate.HibernateWebUtils;
import com.eryansky.common.utils.StringUtils;
import com.eryansky.common.utils.reflection.MyBeanUtils;
import com.eryansky.common.utils.reflection.ReflectionUtils;
import com.eryansky.common.web.utils.WebUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hooro.ri.model.Menu;
import com.hooro.ri.service.impl.MenuServiceImpl;
import com.hooro.ri.util.PageUtil1;
import com.hooro.ri.util.PageUtils;


/**
 * 控制器支持类基类
 * @param <T> 实体类
 * @param <PK> 主键
 */
public abstract class BaseController<T, PK extends Serializable> extends SimpleController {

	@Resource
	protected MenuServiceImpl menuServiceImpl;
    protected Class<T> entityClass;
    protected BaseController() {
        this.entityClass = ReflectionUtils.getClassGenricType(getClass());
    }

    /**
     * EntityManager.
     */
    public abstract EntityManager<T, PK> getEntityManager();
     
    @SuppressWarnings("unchecked")
	@ModelAttribute
    public T getModel(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "id", required = false) PK id, Model model) {
        //如果是保存对象不需要再次查询   引起覆盖
    	if(request.getRequestURI().indexOf("save")!=-1)  
        	return null;
    	T cloneEntity = null;
    	boolean flag = (id != null);
    	if(id != null && id instanceof String){
    		flag = StringUtils.isNotBlank((String)id);
    	}
    	if (flag) {
    		T entity = getEntityManager().getById(id);
    		//对象拷贝
    		if(entity != null){
    			try {
    				cloneEntity = (T) MyBeanUtils.cloneBean(entity);
    			} catch (Exception e) {
    				cloneEntity = entity;
    				e.printStackTrace();
    				logger.error(e.getMessage(),e);
    			}
    		}else{
    			throw new ActionException("ID为["+id+"]的记录不存在或已被其他用户删除！");
    		}
    		model.addAttribute("model", cloneEntity);
    	}
    	return cloneEntity;
    }


    /**
     * 新增或修改.
     *
     * @param model
     * @return
     * @throws ClassNotFoundException 
     */
    @RequestMapping(value = {"save"})
    @ResponseBody
    public Result save(@ModelAttribute("model")T model)  {
        getEntityManager().saveEntity(model);
        return Result.successResult();
    }

    /**
     * 根据ID删除
     *
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = {"delete/{id}"})
    @ResponseBody
    public Result delete(@PathVariable PK id) {
        getEntityManager().deleteById(id);
        return Result.successResult();
    }

    /**
     * 根据ID集合批量删除.
     *
     * @param ids 主键ID集合
     * @return
     */
    @RequestMapping(value = {"remove"})
    @ResponseBody
    public Result remove(@RequestParam(value = "ids", required = false) List<PK> ids) {
        getEntityManager().deleteByIds(ids);
        return Result.successResult();
    }

    
    /**
     * DATATABLES 列表数据
     * @return
     */
    @RequestMapping(value = {"search"})
    @ResponseBody
    public PageUtils search() {
        HttpServletRequest request = SpringMVCHolder.getRequest();
        // 自动构造属性过滤器
        List<PropertyFilter> filters = HibernateWebUtils.buildPropertyFilters(request);
        Page<T> p = new Page<T>(request);
        p = getEntityManager().findPage(p, filters,false);
   	 	PageUtils pageUtils = new PageUtils(p.getTotalCount(), p.getTotalCount(), 0, p.getResult());
   	 	return pageUtils;
    }
    /**
     * bootstrap-table 列表数据
     * @return
     */
    @RequestMapping(value = {"searchForBootstraptable"})
    @ResponseBody
    public PageUtil1 searchForBootstraptable() {
        HttpServletRequest request = SpringMVCHolder.getRequest();
        // 自动构造属性过滤器
        List<PropertyFilter> filters = HibernateWebUtils.buildPropertyFilters(request);
        Page<T> p = new Page<T>(request);
        p = getEntityManager().findPage(p, filters,false);
   	 	PageUtil1 pageUtil = new PageUtil1(p.getTotalCount(), p.getResult());
   	 	return pageUtil;
    }

    /**
     * 根据ID获取记录
     * @return
     */
	@RequestMapping(value={"getByOneEntity"})
	@ResponseBody
	public Result getByOneEntity(PK idd) {
		try {
			T entity = getEntityManager().getById(idd);
			if (entity == null)
				return Result.errorResult().setMsg("没有此条数据");
			else
				return Result.successResult().setObj(entity);

		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.toString());
			return Result.errorResult().setMsg("请检查传入参数");
		}

	}
	
	/** 
	* <p>Title: 逻辑删除数据  </p> 
	* <p>Description:参数名改为IDD  避免重复查询 </p> 
	* @param id      唯一ID主键
	* @param value   标识逻辑删除的值，默认2
	* @param field   标识逻辑删除的字段，默认status
	* @return 
	*/
	@RequestMapping("logicallyDelete")
	@ResponseBody
	public Result logicallyDelete(PK idd,@RequestParam(defaultValue="2") 
	Integer value,@RequestParam(defaultValue="status") String field) {
    	boolean flag = (idd != null);
    	if(idd != null && idd instanceof String){
    		flag = StringUtils.isNotBlank((String)idd);
    	}
    	if (flag) {
    		T entity = getEntityManager().getById(idd);
    		if(entity != null){
    			try {
    				//反射
    				if(value==(Integer)ReflectionUtils.invokeGetter(entity, field))
    				{
    					return Result.errorResult().setMsg("【修改前与修改后一致，操作失败】");
    				}
    				ReflectionUtils.invokeSetter(entity, field, value);
    				getEntityManager().update(entity);
    				return  Result.successResult().setMsg("【删除成功】");
    			} catch (Exception e) {
    				e.printStackTrace();
    				logger.error(e.getMessage(),e);
    			}
    		}else{
    			return  Result.errorResult().setMsg("ID为["+idd+"]的记录不存在或已被其他用户删除！");
    		}
    	}
    	return  Result.successResult().setMsg("【未找到需要删除的数据】");

	}
	
	/** 
	* <p>Title: 排序  </p> 
	* <p>Description:  排序 </p> 
	* @param sortId      唯一ID主键
	* @param field   标识需要排序的字段，默认sort
	* @return 
	*/
	@RequestMapping("sort")
	@ResponseBody
	public Result sort(PK sortId,@RequestParam(defaultValue="sort") String field,Integer value) {
    	boolean flag = (sortId != null);
    	if(sortId != null && sortId instanceof String){
    		flag = StringUtils.isNotBlank((String)sortId);
    	}
    	if (flag) {
    		T entity = getEntityManager().getById(sortId);
    		if(entity != null){
    			try {
    				ReflectionUtils.invokeSetter(entity, field, value);
    				getEntityManager().update(entity);
    				return  Result.successResult().setMsg("【修改成功】");
    			} catch (Exception e) {
    				e.printStackTrace();
    				logger.error(e.getMessage(),e);
    			}
    		}else{
    			return  Result.errorResult().setMsg("ID为["+sortId+"]的记录不存在或已被其他用户删除！");
    		}
    	}
    	return  Result.successResult().setMsg("【未找到需要排序的数据】");

	}
	  
	
	/** 
	* <p>Title:getMenu </p> 
	* <p>Description:获取按钮菜单 </p> 
	* @param request
	* @param response
	* @param parentId
	* @param model 
	*/
	@ModelAttribute
    public void getMenu(HttpServletRequest request,@RequestParam(defaultValue="0") Integer parentId, Model model) {
		Object  userId=request.getSession().getAttribute("userId");
		
		if(userId!=null&&parentId!=0)
			{
			List<Menu> menus=menuServiceImpl.getButtonUrl(parentId,(Integer)userId);
			String munuString="<div class='cl pd-5 bg-1 bk-gray mt-20'> <span class='l'> ";
			for(Menu menu:menus)
				{
				munuString+=menu.getUrl()+"&nbsp;&nbsp;";
				}
			if(menus!=null&&menus.size()>0)
				munuString+="</span></div>";
			else 
				munuString="";
			
			model.addAttribute("menus",munuString ) ;
			}
    }


	 /**
     * Excel导出
     */
    @RequestMapping(value = {"exportExcel"})
    public void exportExcel(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
        // 生成提示信息，
        final String fileName = "导出信息.xls";
        OutputStream outStream = null;
        try {
            //设置文件类型
            response.setContentType(WebUtils.EXCEL_TYPE);
            //设置下载弹出对话框
            WebUtils.setDownloadableHeader(request, response, fileName);
            //获取查询参数
            List<PropertyFilter> filters = HibernateWebUtils.buildPropertyFilters(request);
            List<T> lists = null;
            if (filters != null) {
            	lists = getEntityManager().find(filters, "id", Page.ASC);
            } else {
            	lists = getEntityManager().getAll("id", Page.ASC);
            }
            HSSFWorkbook workbook = new ExportExcel<T>().exportExcel("导出信息",entityClass, lists);
            outStream = response.getOutputStream();
            workbook.write(outStream);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outStream.flush();
                outStream.close();
            } catch (IOException e) {

            }
        }
    }
    /**
     * 初始化数据绑定
     * 1. 设置被排除的属性 不自动绑定
     * 2. 将所有传递进来的String进行HTML编码，防止XSS攻击
     * 3. 将字段中Date类型转换为String类型
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        super.initBinder(binder);
        //设置被排除的属性 不自动绑定
        Object annotationValue = AnnotationUtils.getValue(AnnotationUtils.findAnnotation(entityClass, JsonIgnoreProperties.class));
        if (annotationValue != null) {
            String[] jsonIgnoreProperties = (String[]) annotationValue;
            binder.setDisallowedFields(jsonIgnoreProperties);
        }
    }
    

}
