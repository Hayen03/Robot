package hayen.robot.graphisme;

public class Vector2 {

	public static final Vector2 zero = new Vector2(0, 0);
	
	private double _x;
	private double _y;
	
	public Vector2(){
		this(0, 0);
	}
	public Vector2(double x, double y){
		_x = x; _y = y;
	}
	public Vector2(Vector2 b){
		_x = b._x;
		_y = b._y;
	}
	
	public Vector2 setX(double x){
		_x = x;
		return this;
	}
	public Vector2 setY(double y){
		_y = y;
		return this;
	}
	
	public double getX(){
		return _x;
	}
	public double getY(){
		return _y;
	}
	
	@Override
	public String toString(){
		return "(" + _x + ", " + _y + ")";
	}
	public double[] toArray(){
		return new double[]{_x, _y};
	}
	public Vector2 toIntVector(){
		return new Vector2((int)_x, (int)_y);
	}
	public int[] toIntArray(){
		return new int[]{(int)_x, (int)_y};
	}
	
	public Vector2 add(Vector2 b){
		_x += b._x;
		_y += b._y;
		return this;
	}
	public Vector2 substract(Vector2 b){
		_x -= b._x;
		_y -= b._y;
		return this;
	}
	public Vector2 multiply(double n){
		_x *= n;
		_y *= n;
		return this;
	}
	public Vector2 divide(double n){
		_x /= n;
		_y /= n;
		return this;
	}
	public boolean equals(Vector2 b){
		return _x == b._x && _y == b._y;
	}
	public boolean isGreaterThan(Vector2 b){
		return sqrMagnitude() > b.sqrMagnitude();
	}
	public boolean isLessThan(Vector2 b){
		return sqrMagnitude() < b.sqrMagnitude();
	}
	
	public double magnitude(){
		return Math.sqrt(_x*_x + _y*_y);
	}
	public double sqrMagnitude(){
		return _x*_x + _y*_y;
	}
	
	public static Vector2 Add(Vector2 a, Vector2 b){
		return new Vector2(a._x + b._x, a._y + b._y);
	}
	public static Vector2 Substract(Vector2 a, Vector2 b){
		return new Vector2(a._x - b._x, a._y - b._y);
	}
	public static Vector2 Multiply(Vector2 a, double n){
		return new Vector2(a._x * n, a._y * n);
	}
	public static Vector2 Divide(Vector2 a, double n){
		return new Vector2(a._x / n, a._y / n);
	}
	public static boolean equals(Vector2 a, Vector2 b){
		return a._x == b._x && a._y == b._y;
	}
	public static boolean isGreaterThan(Vector2 a, Vector2 b){
		return a.sqrMagnitude() > b.sqrMagnitude();
	}
	public static boolean isLessThan(Vector2 a, Vector2 b){
		return a.sqrMagnitude() < b.sqrMagnitude();
	}
	
	public static double Distance(Vector2 a){
		return a.magnitude();
	}
	public static double Distance(Vector2 a, Vector2 b){
		return Substract(a, b).magnitude();
	}
	public static double sqrDistance(Vector2 a){
		return a.sqrMagnitude();
	}
	public static double sqrDistance(Vector2 a, Vector2 b){
		return Substract(a, b).sqrMagnitude();
	}
}
