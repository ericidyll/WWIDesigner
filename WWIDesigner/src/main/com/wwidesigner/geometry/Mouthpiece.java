/**
 * 
 */
package com.wwidesigner.geometry;

import com.wwidesigner.math.TransferMatrix;
import com.wwidesigner.util.PhysicalParameters;

/**
 * @author kort
 * 
 */
public class Mouthpiece implements ComponentInterface, MouthpieceInterface
{
	protected double position;
	protected Double beta;
	protected Mouthpiece.EmbouchureHole embouchureHole;
	protected Mouthpiece.Fipple fipple;
	protected MouthpieceCalculator mouthpieceCalculator;

	/**
	 * Gets the value of the position property.
	 * 
	 */
	public double getPosition()
	{
		return position;
	}

	/**
	 * Sets the value of the position property.
	 * 
	 * @param value
	 *            allowed object is {@link XmlZeroOrMore }
	 * 
	 */
	public void setPosition(double value)
	{
		this.position = value;
	}

	/**
	 * @return the beta
	 */
	public Double getBeta()
	{
		return beta;
	}

	/**
	 * @param beta
	 *            the beta to set
	 */
	public void setBeta(Double beta)
	{
		this.beta = beta;
	}

	/**
	 * Gets the value of the embouchureHole property.
	 * 
	 * @return possible object is {@link XmlMouthpiece.EmbouchureHole }
	 * 
	 */
	public Mouthpiece.EmbouchureHole getEmbouchureHole()
	{
		return embouchureHole;
	}

	/**
	 * Sets the value of the embouchureHole property.
	 * 
	 * @param value
	 *            allowed object is {@link XmlMouthpiece.EmbouchureHole }
	 * 
	 */
	public void setEmbouchureHole(Mouthpiece.EmbouchureHole value)
	{
		this.embouchureHole = value;
	}

	/**
	 * Gets the value of the fipple property.
	 * 
	 * @return possible object is {@link XmlMouthpiece.Fipple }
	 * 
	 */
	public Mouthpiece.Fipple getFipple()
	{
		return fipple;
	}

	/**
	 * Sets the value of the fipple property.
	 * 
	 * @param value
	 *            allowed object is {@link XmlMouthpiece.Fipple }
	 * 
	 */
	public void setFipple(Mouthpiece.Fipple value)
	{
		this.fipple = value;
	}

	/**
	 * @param mouthpieceCalculator
	 *            the mouthpieceCalculator to set
	 */
	public void setCalculator(
			MouthpieceCalculator mouthpieceCalculator)
	{
		this.mouthpieceCalculator = mouthpieceCalculator;
	}

	/**
	 * <p>
	 * Java class for anonymous complex type.
	 * 
	 */
	public static class EmbouchureHole
	{

		protected double innerDiameter;
		protected double outerDiameter;
		protected double height;

		/**
		 * Gets the value of the innerDiameter property.
		 * 
		 */
		public double getInnerDiameter()
		{
			return innerDiameter;
		}

		/**
		 * Sets the value of the innerDiameter property.
		 * 
		 */
		public void setInnerDiameter(double value)
		{
			this.innerDiameter = value;
		}

		/**
		 * Gets the value of the outerDiameter property.
		 * 
		 */
		public double getOuterDiameter()
		{
			return outerDiameter;
		}

		/**
		 * Sets the value of the outerDiameter property.
		 * 
		 */
		public void setOuterDiameter(double value)
		{
			this.outerDiameter = value;
		}

		/**
		 * @return the height
		 */
		public double getHeight()
		{
			return height;
		}

		/**
		 * @param height
		 *            the height to set
		 */
		public void setHeight(double height)
		{
			this.height = height;
		}

	}

	/**
	 * <p>
	 * Java class for anonymous complex type.
	 * 
	 */
	public static class Fipple
	{

		protected double windowWidth;
		protected double windowLength;
		protected Double fippleFactor;
		protected Double windowHeight;
		protected Double windwayLength;
		protected Double windwayHeight;

		/**
		 * @return the windowWidth
		 */
		public double getWindowWidth()
		{
			return windowWidth;
		}

		/**
		 * @param windowWidth
		 *            the windowWidth to set
		 */
		public void setWindowWidth(double windowWidth)
		{
			this.windowWidth = windowWidth;
		}

		/**
		 * @return the windowLength
		 */
		public double getWindowLength()
		{
			return windowLength;
		}

		/**
		 * @param windowLength
		 *            the windowLength to set
		 */
		public void setWindowLength(double windowLength)
		{
			this.windowLength = windowLength;
		}

		/**
		 * @return the fippleFactor
		 */
		public Double getFippleFactor()
		{
			return fippleFactor;
		}

		/**
		 * @param fippleFactor
		 *            the fippleFactor to set
		 */
		public void setFippleFactor(Double fippleFactor)
		{
			this.fippleFactor = fippleFactor;
		}

		/**
		 * @return the windowHeight
		 */
		public Double getWindowHeight()
		{
			return windowHeight;
		}

		/**
		 * @param windowHeight
		 *            the windowHeight to set
		 */
		public void setWindowHeight(Double windowHeight)
		{
			this.windowHeight = windowHeight;
		}

		/**
		 * @return the windwayLength
		 */
		public Double getWindwayLength()
		{
			return windwayLength;
		}

		/**
		 * @param windwayLength
		 *            the windwayLength to set
		 */
		public void setWindwayLength(Double windwayLength)
		{
			this.windwayLength = windwayLength;
		}

		/**
		 * @return the windwayHeight
		 */
		public Double getWindwayHeight()
		{
			return windwayHeight;
		}

		/**
		 * @param windwayHeight
		 *            the windwayHeight to set
		 */
		public void setWindwayHeight(Double windwayHeight)
		{
			this.windwayHeight = windwayHeight;
		}

	}

	@Override
	public TransferMatrix calcTransferMatrix(double waveNumber,
			PhysicalParameters parameters)
	{
		TransferMatrix result = mouthpieceCalculator.calcTransferMatrix(
				waveNumber, parameters);

		return result;
	}

	@Override
	public int calcReflectanceMultiplier()
	{
		int result = mouthpieceCalculator.calcReflectanceMultiplier();

		return result;
	}

}
