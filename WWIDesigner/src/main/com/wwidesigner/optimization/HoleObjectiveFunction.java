package com.wwidesigner.optimization;

import com.wwidesigner.modelling.EvaluatorInterface;
import com.wwidesigner.modelling.InstrumentCalculator;
import com.wwidesigner.note.TuningInterface;
import com.wwidesigner.optimization.HolePositionObjectiveFunction.BoreLengthAdjustmentType;

/**
 * Optimization objective function for hole positions and diameters:
 * <ul>
 * <li>Position of end of bore,</li>
 * <li>For each hole, spacing to next hole, ending with spacing from last hole
 * to end of bore.</li>
 * <li>For each hole, hole diameter.</li>
 * </ul>
 * 
 * @author Edward Kort, Burton Patkau
 * 
 */
public class HoleObjectiveFunction extends MergedObjectiveFunction
{
	public static final String DISPLAY_NAME = "Hole position and size optimizer";

	public HoleObjectiveFunction(InstrumentCalculator calculator,
			TuningInterface tuning, EvaluatorInterface evaluator,
			BoreLengthAdjustmentType lengthAdjustmentMode)
	{
		super(calculator, tuning, evaluator);
		this.components = new BaseObjectiveFunction[2];
		this.components[0] = new HolePositionObjectiveFunction(calculator,
				tuning, evaluator, lengthAdjustmentMode);
		this.components[1] = new HoleSizeObjectiveFunction(calculator, tuning,
				evaluator);
		optimizerType = OptimizerType.BOBYQAOptimizer; // MultivariateOptimizer
		sumDimensions();
		maxEvaluations = 20000 + (getNrDimensions() - 1) * 5000;
		constraints.setObjectiveDisplayName(DISPLAY_NAME);
		constraints.setObjectiveFunctionName(this.getClass().getSimpleName());
		constraints.setConstraintsName("Default");
	}

	public HoleObjectiveFunction(InstrumentCalculator calculator,
			TuningInterface tuning, EvaluatorInterface evaluator)
	{
		this(calculator, tuning, evaluator, BoreLengthAdjustmentType.PRESERVE_TAPER);
	}
}
