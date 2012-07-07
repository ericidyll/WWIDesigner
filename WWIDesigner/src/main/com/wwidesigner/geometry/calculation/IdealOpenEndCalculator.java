/**
 * 
 */
package com.wwidesigner.geometry.calculation;

import org.apache.commons.math3.complex.Complex;

import com.wwidesigner.geometry.Termination;
import com.wwidesigner.geometry.TerminationCalculator;
import com.wwidesigner.math.StateVector;
import com.wwidesigner.util.PhysicalParameters;

/**
 * @author kort
 *
 */
public class IdealOpenEndCalculator extends TerminationCalculator
{

	public IdealOpenEndCalculator(Termination termination)
	{
		super(termination);
	}

	/* (non-Javadoc)
	 * @see com.wwidesigner.geometry.TerminationCalculator#calcStateVector(double, com.wwidesigner.util.PhysicalParameters)
	 */
	@Override
	public StateVector calcStateVector(double wave_number,
			PhysicalParameters params)
	{
		return new StateVector(Complex.ZERO, Complex.ONE);
	}

}
