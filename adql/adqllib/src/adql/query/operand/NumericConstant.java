package adql.query.operand;

/*
 * This file is part of ADQLLibrary.
 * 
 * ADQLLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ADQLLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ADQLLibrary.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2012-2015 - UDS/Centre de Données astronomiques de Strasbourg (CDS),
 *                       Astronomisches Rechen Institut (ARI)
 */

import adql.query.ADQLIterator;
import adql.query.ADQLObject;
import adql.query.NullADQLIterator;
import adql.query.TextPosition;

/**
 * A numeric (integer, double, ...) constant.
 * 
 * @author Gr&eacute;gory Mantelet (CDS;ARI)
 * @version 1.4 (06/2015)
 */
public final class NumericConstant implements ADQLOperand {

	private String value;

	/** Position of this operand.
	 * @since 1.4 */
	private TextPosition position = null;

	/**
	 * The numeric value is saved as a string so that the exact user format can be saved.
	 * The given string must be convertible in a Double value.
	 * 
	 * @param value						The numeric value (in a String variable).
	 * @throws NumberFormatException	If it is impossible to convert the given value in a Double.
	 * 
	 * @see NumericConstant#setValue(String)
	 */
	public NumericConstant(String value) throws NumberFormatException{
		this(value, true);
	}

	/**
	 * The numeric value is saved as a string so that the exact user format can be saved.
	 * 
	 * @param val						The numeric value.
	 */
	public NumericConstant(long val){
		this("" + val, false);
	}

	/**
	 * The numeric value is saved as a string so that the exact user format can be saved.
	 * 
	 * @param val						The numeric value.
	 */
	public NumericConstant(double val){
		this("" + val, false);
	}

	/**
	 * The numeric value is saved as a string so that the exact user format can be saved.
	 * It is possible to force the value (no check to known whether this value is numeric or not is done) by setting the boolean
	 * parameter to <i>false</i>.
	 * 
	 * @param value						The numeric value (in a String variable).
	 * @param checkNumeric				<i>true</i> to check whether the given value is numeric, <i>false</i> otherwise.
	 * @throws NumberFormatException	If it is impossible to convert the given value in a Double.
	 * 
	 * @see NumericConstant#setValue(String, boolean)
	 */
	public NumericConstant(String value, boolean checkNumeric) throws NumberFormatException{
		setValue(value, checkNumeric);
	}

	/**
	 * Builds a NumericConstant by copying the given one.
	 * 
	 * @param toCopy	The NumericConstant to copy.
	 */
	public NumericConstant(NumericConstant toCopy){
		this.value = toCopy.value;
	}

	public final String getValue(){
		return value;
	}

	public final double getNumericValue(){
		try{
			return Double.parseDouble(value);
		}catch(NumberFormatException nfe){
			return Double.NaN;
		}
	}

	/**
	 * Sets the given value.
	 * 
	 * @param value		The numeric value.
	 */
	public final void setValue(long value){
		this.value = "" + value;
	}

	/**
	 * Sets the given value.
	 * 
	 * @param value		The numeric value.
	 */
	public final void setValue(double value){
		this.value = "" + value;
	}

	/**
	 * Sets the given value (it must be convertible in a Double).
	 * 
	 * @param value						The numeric value.
	 * @throws NumberFormatException	If it is impossible to convert the given value in a Double.
	 * 
	 * @see NumericConstant#setValue(String, boolean)
	 */
	public final void setValue(String value) throws NumberFormatException{
		setValue(value, true);
	}

	/**
	 * Sets the given value.
	 * It is possible to force the value (no check to known whether this value is numeric or not is done) by setting the boolean
	 * parameter to <i>false</i>.
	 * 
	 * @param value						The numeric value.
	 * @param checkNumeric				<i>true</i> to check whether the given value is numeric, <i>false</i> otherwise.
	 * @throws NumberFormatException	If the given value can not be converted in a Double.
	 */
	public final void setValue(String value, boolean checkNumeric) throws NumberFormatException{
		if (checkNumeric)
			Double.parseDouble(value);

		this.value = value;
	}

	/** Always returns <i>true</i>.
	 * @see adql.query.operand.ADQLOperand#isNumeric()
	 */
	@Override
	public final boolean isNumeric(){
		return true;
	}

	/** Always returns <i>false</i>.
	 * @see adql.query.operand.ADQLOperand#isString()
	 */
	@Override
	public final boolean isString(){
		return false;
	}

	@Override
	public final TextPosition getPosition(){
		return this.position;
	}

	/**
	 * Sets the position at which this {@link NumericConstant} has been found in the original ADQL query string.
	 * 
	 * @param position	Position of this {@link NumericConstant}.
	 * @since 1.4
	 */
	public final void setPosition(final TextPosition position){
		this.position = position;
	}

	/** Always returns <i>false</i>.
	 * @see adql.query.operand.ADQLOperand#isGeometry()
	 */
	@Override
	public final boolean isGeometry(){
		return false;
	}

	@Override
	public ADQLObject getCopy(){
		return new NumericConstant(this);
	}

	@Override
	public String getName(){
		return value;
	}

	@Override
	public ADQLIterator adqlIterator(){
		return new NullADQLIterator();
	}

	@Override
	public String toADQL(){
		return value;
	}

}
