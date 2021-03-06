package com.lend.action;



import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.sun.xml.internal.ws.resources.SenderMessages;




@Controller  //普通spring mvc注解
@RequestMapping(value = "msg")
public class LineAction  {
	//private ConcurrentHashMap<String, WebSocketSession> wssMap = HashContaintManager.getWebSocketHash();
	//@RequestBody JSONObject  jsObj
	private final static String token = "lvrMNoiObD6rMom5FM7Z2zBT6ORTpvLczrVYts973kuHrKOei14+PkpNEbWulR+gudYHHZigww9gZ1fC1jn4CjtgsYh8W4jTYWE8o28JVNypDCOLDE1hZNoOD81efRyOpUdx7yx+gCB2XfNWBIMu0QdB04t89/1O/w1cDnyilFU=";
	private final static String channelSecret = "cc2523232b7a7b2f94ffeac686a761df"; // Channel secret string
	private int n=0;
	
	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	@ResponseBody
	public String link(HttpEntity<String> entity) {
		System.out.println(entity.getBody());
		System.out.println(entity.getHeaders().toString());
		System.out.println(entity.getHeaders().get("X-Line-Signature"));
		n++;
		//sendMsg(lineId, msg);
		return "successs";
	}
	
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public String test() {
		System.out.println("TEST  the server");
		return "successs Test:"+n;
	}
	
	
	/**
	 * 
	 * @param lineId
	 * @param msg
	 * "U92500e7829720eae8ae3c2385179a666",
	 */
	public static void sendMsg(String lineId,String msg){
		final LineMessagingClient client = LineMessagingClient
		        .builder(token)
		        .build();
		final PushMessage pushMessage = new PushMessage(lineId,new TextMessage(msg));
		try {
			final BotApiResponse  botApiResponse = client.pushMessage(pushMessage).get();
			System.out.println("--发送响应--:"+botApiResponse.toString());
		} catch (Exception  e) {
		    e.printStackTrace();
		    return;
		}
		
	}
	
	/**
	 * 信息较验
	 * @param body
	 * @param csignature
	 * @return
	 */
	private boolean validateMessage(String body,String csignature){
		try{
			
			String httpRequestBody = body; // Request body string
			SecretKeySpec key = new SecretKeySpec(channelSecret.getBytes(), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(key);
			byte[] source = httpRequestBody.getBytes("UTF-8");
			String signature = Base64.getEncoder().encodeToString(mac.doFinal(source));
			if(csignature.equals(signature)){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	

	
	/*public static void main(String[] args) {
		
		sendMsg("U92500e7829720eae8ae3c2385179a666","test msg777");
		
	}
	*/
	 
	 
	
}
