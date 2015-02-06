package hayen.robot.graphisme;

public class Transform {
	
	private Vector2 _position;
	private double _rotation;
	private Vector2 _scale;
	
	public Transform(){
		_position = new Vector2(0, 0);
		_rotation = 0;
		_scale = new Vector2(1, 1);
	}
	public Transform(Vector2 pos, double rot, Vector2 scale){
		_position = pos;
		_rotation = rot;
		_scale = scale;
	}
	
	public Transform setPosition(Vector2 newPos){
		_position = newPos;
		return this;
	}
	public Transform setRotation(double newRot){
		_rotation = newRot;
		return this;
	}
	public Transform setScale(Vector2 newScale){
		_scale = newScale;
		return this;
	}
	
	public Vector2 getPosition(){
		return _position;
	}
	public double getRotation(){
		return _rotation;
	}
	public Vector2 getScale(){
		return _scale;
	}
	
	public Transform rotate(double theta){
		_rotation += theta;
		return this;
	}
	public Transform translate(Vector2 dep){
		_position.add(dep);
		return this;
	}
	public Transform scale(Vector2 s){
		_scale.add(s);
		return this;
	}
	
}
