package com.java.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java.common.service.JwtService;
import com.java.service.UserService;
import com.java.vo.BlacklistVO;
import com.java.vo.UserVO;

/*
 * 
 * https://jwt.io/ ȩ������ Debugger�� ���ڵ��� ��ū�� �Է��ϸ� ���ڵ� �ż� ��ū�� ������ ��Ÿ��.
 * 
 * https://github.com/viviennes7/luvook/tree/master/src/main/java/com/ms/luvook
 */

/*
 * # ������Ʈ ����� ��ɺ��� ������ �����. �� �Ʒ��� ���� ����.
 * com.java.kr �Ʒ���
 * 
 * -HomeController
 * -user
 * 	- service
 *  - controller
 *  - dao
 *  - vo(dto)
 *  
 * -board
 *  - service
 *  - controller
 *  - dao
 *  - vo(dto)
 * 
 * -common
 *  - service (JwtService, JwtServiceImpl)
 *  - config 
 *  - interceptor
 *  - exception
 *  - ���
 * 
 */

/*
 * 
 ���ڵ� ����
+ JwtService�������̽� Autowired�ؼ� ������
+ ��ū������ ��ū����ð���� �߰���
- ��ū���� Ȥ�� �α׾ƿ��� BlackList DB ������ū���� ���?
 */


/*
 * @Controller�� @RestController�� ���� -> HTTP Response Body�� �����Ǵ� ����� ����.
	������ MVC @Controller�� View ����� ���������, @RestController�� ��ü�� ��ȯ�Ҷ� ��ü �����ʹ� �ٷ� JSON/XML Ÿ���� HTTP ������ ���� �����ϰ� �ȴ�.
 */

@Controller
@RequestMapping("/jwtTest")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;
	
	private Map<String,String> tokenMap = new LinkedHashMap<String, String>(); //��ū�ӽ����� (�α׾ƿ����)

	@ResponseBody
	@RequestMapping(value = "/signup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public Integer signup(@RequestBody UserVO vo) throws Exception {

		if (userService.signupcheckid(vo.getUserId()) == 0) { //���̵� �ߺ�Ȯ��
			try {
				userService.signup(vo);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("�������� ȸ������");
			}
			return 1; // ȸ������ ������ 1�� ����
		} else {
			System.out.println("���̵� �ߺ�");
			return 0; // ȸ������ ���н�(���̵��ߺ�) 0�� ����
		}

	}


	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public JSONObject login(@RequestBody UserVO vo, HttpServletResponse response) throws Exception {
		
		/*
		 * ���� �ȵ���̵�� ��� ���̶� Postman���� ���Ƿ� DB�� �ִ� ����������� �Է��Ͽ� ��ū�� �����Ͽ����� 
		 * �ȵ���̵�� ������ �Ǹ� �ȵ���̵忡�� ������ �Ѿ���� ȸ�� ������ �Ķ���ͷ� �޾Ƽ� DB ���翩�ε� �˻� �� ��ū�� �����Ͽ� �ڵ带 �����ؾ���
		 */
				
		JSONObject json = new JSONObject();
		
		if (userService.login(vo) != 0) {

			String token = jwtService.create(vo.getUserId(), vo, "user" , "lookid_api"); // key, data, subject(��ū����), issuer(����������) 
			
			tokenMap.put(vo.getUserId(), token); // �α׾ƿ� ������ ���� �ʿ� ��ū �ӽ�����
			
			response.setHeader("Authorization", token); // HTTP Authorization ����� JWT(��ū)�� ��Ƽ� ����.
			
			if (jwtService.isUsable(token)) {
				System.out.println("token : ");
				System.out.println("[ " + token + " ]");
				
				json.put("token", token);
				
			}
			System.out.println("�α��μ���");
			
			//return 1; // �α��� ������ 1�� ����
			
		} else if (userService.login(vo) == 0) {
			if (userService.signupcheckid(vo.getUserId()) == 0) {
				System.out.println("�������� �ʴ� ���̵��Դϴ�.");
				json.put("status", "invalid userId");

			} else {
				System.out.println("�߸��� ��й�ȣ�Դϴ�.");
				json.put("status", "invalid userPw");

			}
			//return 0; // �α��� ���н� 0�� ����.

		} else {
			System.out.println("db����");
			json.put("status", "Database Error");
			//return -1; // db error.
		}
		
		return json;

	}

	//�α׾ƿ�
	//�α׾ƿ���Ų ��ū�� blacklist ���̺��� �����ǵ��� �Ѵ�.
	@ResponseBody
	@RequestMapping(value = "/logout" , method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public JSONObject logout(@RequestBody UserVO userVo) throws Exception{
		
		JSONObject json = new JSONObject(); 
	
		String tempId = userVo.getUserId();
		String tempToken = tokenMap.get(tempId); // �������� �ʿ� ����Ǿ��ִ� ��ū����
		
		if(tempToken != null) {
			
			if(jwtService.isUsable(tempToken)) {
			
				if(userService.tokenCount(tempToken) == 0) { //blacklist ���̺� ������ ��ū�� �̹� �����ϸ� �ߺ���� ����
					
					BlacklistVO blacklistVo = new BlacklistVO();
					blacklistVo.setLogoutId(tempId); //�α׾ƿ� ��ų ���̵� ����
					blacklistVo.setLogoutToken(tempToken); //�α׾ƿ� ��ų ��ū ����
					userService.logout(blacklistVo); //�α׾ƿ� �� ��ū�� blacklist ���̺� �����Ͽ� ����.
					System.out.println("�α׾ƿ�����");
				
				}
				
				json.put("status", "logout is completed!!");
				//return -1 // �α׾ƿ��� �˸�
			}
			
		}else {
			json.put("status", "token does not exist...");
		}
		
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "/listAll", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public List<UserVO> listAll() throws Exception {

		return userService.listAll();

	}
}
