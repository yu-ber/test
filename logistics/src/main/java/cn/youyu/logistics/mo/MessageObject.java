package cn.youyu.logistics.mo;
/**
 * 消息对象
 * 给ajax增删改操作返回json消息
 */
public class MessageObject {
	
	private Integer code;//返回1成功，返回0失败
	private String msg;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public MessageObject(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	public MessageObject() {
	}
	@Override
	public String toString() {
		return "MessageObject [code=" + code + ", msg=" + msg + "]";
	}
}
