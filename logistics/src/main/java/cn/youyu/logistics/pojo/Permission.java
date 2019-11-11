package cn.youyu.logistics.pojo;

import java.io.Serializable;

public class Permission implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8378355359666229199L;

	//@JsonProperty("id")可以通过此种方式修改传入字段的名称，使用ztree的树形结构需要字段为id和pId
    private Long permissionId;
    
    private String name;

    private String type;

    private String url;

    private String expression;

    private Long parentId;
    
    //新增一列
    private String parentName;

    @Override
	public String toString() {
		return "Permission [permissionId=" + permissionId + ", name=" + name + ", type=" + type + ", url=" + url
				+ ", expression=" + expression + ", parentId=" + parentId + ", parentName=" + parentName + ", sort="
				+ sort + "]";
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	private Integer sort;

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}