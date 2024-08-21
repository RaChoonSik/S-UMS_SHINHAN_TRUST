/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.12
 * 설명 : 에러 화면 Controller
 */
package kr.co.sict.ums.err;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/err")
public class ErrorController {
	/**
	 * 404 Not Found 오류를 처리한다.
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/error404")
	public String getErrorPage404(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		model.addAttribute("status_code",request.getAttribute("javax.servlet.error.status_code"));
		model.addAttribute("message",request.getAttribute("javax.servlet.error.message"));
		model.addAttribute("exception_code",request.getAttribute("javax.servlet.error.exception_code"));
		model.addAttribute("exception",request.getAttribute("javax.servlet.error.exception"));
		model.addAttribute("request_url",request.getAttribute("javax.servlet.error.request_url"));
		model.addAttribute("servlet_name",request.getAttribute("javax.servlet.error.servlet_name"));
		
		response.setStatus(HttpServletResponse.SC_OK);
		
		return "err/error404";
	}
	
	/**
	 * 500 프로그램 오류를 처리한다.
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/error500")
	public String getErrorPage500(Model model, HttpServletRequest request, HttpServletResponse response) {
			
		model.addAttribute("status_code",request.getAttribute("javax.servlet.error.status_code"));
		model.addAttribute("message",request.getAttribute("javax.servlet.error.message"));
		model.addAttribute("exception_code",request.getAttribute("javax.servlet.error.exception_code"));
		model.addAttribute("exception",request.getAttribute("javax.servlet.error.exception"));
		model.addAttribute("request_url",request.getAttribute("javax.servlet.error.request_url"));
		model.addAttribute("servlet_name",request.getAttribute("javax.servlet.error.servlet_name"));
			
		response.setStatus(HttpServletResponse.SC_OK);
		
		return "err/error500";
	}
	
	/**
	 * 사용자 접근 권한 오류
	 * @return
	 */
	@RequestMapping(value="/access")
	public String getAccessError() {
		return "err/access";
	}

}
