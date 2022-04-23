package test.example.com.verifiersvalidator;


public class verifierdata
{
    public String name;
    public String _id;
    public String _rev;
    public String password;
    public String place;
    public String status;
    public String phno;


    public String getId()
    {
        return _id;
    }
    public void setId(String _id) {
        this._id = _id;
    }
////////////////////////////////////////////////////////////////////
    public String getName()
    {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    /////////////////////////////////////////////////////////////
    public String getRev()
    {
        return _rev;
    }
    public void setRev(String _rev)
    {
        this._rev=_rev;
    }
    /////////////////////////////////////////////////////////////
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password=password;
    }

    /////////////////////////////////////////////////////////

    public String getPhno()
    {
        return phno;
    }

    public void setPhno(String phno)
    {
        this.phno = phno;
    }


    /////////////////////////////////////////////////////////////
    public String getPlace()
    {
        return place;
    }

    public void setPlace(String place)
    {
        this.place=place;
    }



    /////////////////////////////////////////////////////////////
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status=status;
    }

    /////////////////////////////////////////////////////////////


    @Override
    public String toString() {
        return "[_id=" + _id + ", _rev=" + _rev + ", name=" + name + ", password=" + password + ", phno=" + phno + ", place=" + place + ", status=" + status + "]";
    }

}
