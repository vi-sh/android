package test.example.com.verifierclient;


public class Empinfo
{
    private String _id;
    private String name;
    private String pwd;
    private String phno;
    private String locality;
    private String company;


////////////////////////////////////////////
    public String getId()
    {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }
/////////////////////////////////////////////
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
////////////////////////////////////////////////

    public String getPwd()
    {
        return pwd;
    }

    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }
////////////////////////////////////////////////




    public String getPhno()
    {
        return phno;
    }

    public void setPhno(String phno)
    {
        this.phno = phno;
    }
//////////////////////////////////////////////////
    public String getLocality()
    {
        return locality;
    }

    public void setLocality(String locality)
    {
        this.locality = locality;
    }
///////////////////////////////////////////////////
    public String getCompany()
    {
    return company;
    }

    public void setCompany(String company)

    {
        this.company = company;
    }
/////////////////////////////////////////////////////


    @Override
    public String toString() {
        return "[_id=" + _id + ", name=" + name +", phno=" + phno + ", locality=" + locality + ", company="
                + company + "]";
    }
}
