package company.petrifaction.boss.bean;

public class BaseReturnData<T>
{
    private T data;
    private String msg;
    private String code;

    public BaseReturnData()
    {

    }

    public T getData()
    {
        return this.data;

    }

    public void setData(T data)
    {
        this.data = data;

    }

    public String getMsg()
    {
        return this.msg;

    }

    public void setMsg(String msg)
    {
        this.msg = msg;

    }

    public String getCode()
    {
        return this.code;

    }

    public void setCode(String code)
    {
        this.code = code;

    }
}