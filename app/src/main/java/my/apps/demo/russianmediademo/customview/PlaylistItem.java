package my.apps.demo.russianmediademo.customview;

public class PlaylistItem {
    private String _id;
    private String _name;
    private String _image;
    private String _high;
    private String _data;
    private int _duration;
    private int _position;

    public PlaylistItem(String id, String name, String image, String high, String data) {
        _id = id;
        _name = name;
        _high = high;
        _image = image;
        _data = data;
    }

    public String Name() {
        return _name;
    }

    public String Url() {
        return _high;
    }

    public String ImageUrl() {
        return _image;
    }

    public String Data() {
        return _data;
    }

    public void SetData(String data){_data = data;}

    public void SetTitle(String title){_name = title;}

    public void SetPosition(int position, int duration) {
        _duration = duration;
        _position = position;
    }

    public int GetPosition()
    {
        return _position;
    }

    public int GetDuration()
    {
        return _duration;
    }
}
