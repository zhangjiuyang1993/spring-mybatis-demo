package com.ssm.controller;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ssm.domain.ItemsCustom;
import com.ssm.domain.ItemsCustomVo;
import com.ssm.exception.CustomException;
import com.ssm.service.ItemsService;

@Controller
public class ItemsController {
	
	@Autowired
	private ItemsService itemsService;
	
	@RequestMapping(value = "/queryItems", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView queryItems(){
		List<ItemsCustom> itemsList = itemsService.findItemsList(null);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("itemsList", itemsList);
		modelAndView.setViewName("items/itemsList");
		return modelAndView;
	}
	
	@RequestMapping("editUI")
	public ModelAndView editUI(@RequestParam(value = "id", defaultValue = "") Integer id){
		ItemsCustom itemsCustom = itemsService.findItemsById(id);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("itemsCustom", itemsCustom);
		modelAndView.setViewName("items/editUI");
		return modelAndView;
	}
	
	@RequestMapping(value = "/edit", method = {RequestMethod.GET, RequestMethod.POST })
	public String edit(HttpServletRequest request, Integer id, String name, ItemsCustomVo itemsCustomVo){
		if(itemsCustomVo != null){
			System.out.println(itemsCustomVo.getItemsCustom().getName());
		}
		return "redirect:queryItems.action";
	}
	
	/*
	 * ɾ��������id[],spring�Զ�ע��ֵ�ķ�ʽ
	 */
	@RequestMapping(value = "/delete", method = {RequestMethod.GET, RequestMethod.POST })
	public String edit(Integer[] itemsIds){
		for(Integer integer : itemsIds){
			System.out.println(integer);
		}
		return "redirect:queryItems.action";
	}
	
	/*
	 * list���Զ����
	 */
	@RequestMapping(value = "/deleteList", method = {RequestMethod.GET, RequestMethod.POST })
	public String edit(ItemsCustomVo itemsCustomVo){
		List<ItemsCustom> itemsCustomList = itemsCustomVo.getItemsCustomList();
		for(ItemsCustom itemsCustom : itemsCustomList){
			System.out.println(itemsCustom.getId());
		}
		return "redirect:queryItems.action";
	}
	
	/*
	 * list���Զ����
	 */
	@RequestMapping(value = "/deleteMap", method = {RequestMethod.GET, RequestMethod.POST })
	public String editMap(ItemsCustomVo itemsCustomVo){
		Map<String, ItemsCustom> itemsCustomMap = itemsCustomVo.getItemsCustomMap();
		Set<String> set = itemsCustomMap.keySet();
		for(String str : set){
			System.out.println(itemsCustomMap.get(str).getId());
		}
		return "redirect:queryItems.action";
	}
	
	/*
	 * list���Զ����
	 */
	@RequestMapping(value = "/editItemsValitation", method = {RequestMethod.GET, RequestMethod.POST })
	public String editValitation(Model model, @Validated ItemsCustom itemsCustom, BindingResult bindingResult) throws CustomException{
		System.out.println(itemsCustom.getName());
		System.out.println(itemsCustom.getId());
		System.out.println(itemsCustom.getDetail());
		if(bindingResult.hasErrors()){
			List<ObjectError> errorList = bindingResult.getAllErrors();
			for(ObjectError objectError : errorList){
				System.out.println("������Ϣ��" + objectError.getDefaultMessage());
			}
			if(model != null)
				model.addAttribute("errorList", errorList);
			else
				throw new CustomException("error");
		}
		return "redirect:queryItems.action";
	}
	
	//list���Զ����
	@RequestMapping(value = "/editSubmit", method = {RequestMethod.GET, RequestMethod.POST })
	public String editSubmit(Model model, @Validated ItemsCustom itemsCustom, BindingResult bindingResult, MultipartFile items_pic) throws Exception{
		System.out.println(itemsCustom.getName());
		System.out.println(itemsCustom.getId());
		System.out.println(itemsCustom.getDetail());
		
		String originalFileName = items_pic.getOriginalFilename();
		if(items_pic != null && originalFileName != null && originalFileName.length() > 0){
			String pic_path = "F:\\temp\\";
			String newFileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf('.'), originalFileName.length());
			File file = new File(pic_path + newFileName);
			if(!file.exists())
				file.mkdirs();
			items_pic.transferTo(file);
			itemsCustom.setPic(newFileName);
		}
		if(bindingResult.hasErrors()){
			List<ObjectError> errorList = bindingResult.getAllErrors();
			for(ObjectError objectError : errorList){
				System.out.println("������Ϣ�� " + objectError.getDefaultMessage());
			}
			if(model != null)
				model.addAttribute("errorList", errorList);
			else
				throw new CustomException("error");
		}
		itemsService.updateItemsCustom(itemsCustom);
		return "forward:queryItems.action";
	}
}
