package projecto;


	public class Wall<X,Y> {
	    
		X x;
	    Y y;
	    
	    public Wall(X x, Y y)
	    {
	        this.x = x;
	        this.y = y;
	    }
	    
	    public X getX()
	    {
	    	return x;
	    }
	    
	    public Y getY()
	    {
	    	return y;
	    }
	    
	    @Override
	    public boolean equals(Object object)
	    {
	        boolean isEqual= false;

	        if (object != null && object instanceof Coord<?,?>)
	        {
	            isEqual = (this.x.equals(((Coord<?,?>) object).x) && (int) this.y == (int) ((Coord<?,?>) object).y);
	        }

	        return isEqual;
	    }

	}
