/*
 * Copyright (c) 2002-2024, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.identitystore.modules.test.business;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Date;
import javax.validation.constraints.NotNull;

/**
 * This is the business class for the object TestIdentityAttribute
 */
public class TestIdentityAttribute implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations
    private int _nId;

    @NotEmpty( message = "#i18n{module.identitystore.test.validation.testidentityattribute.Key.notEmpty}" )
    private String _strKey;

    @NotEmpty( message = "#i18n{module.identitystore.test.validation.testidentityattribute.Value.notEmpty}" )
    private String _strValue;

    private String _strCertifier;
    @NotNull( message = "#i18n{portal.validation.message.notEmpty}" )
    private Date _dateCertificationDate;

    private int _nCertificationLevel;

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * 
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Key
     * 
     * @return The Key
     */
    public String getKey( )
    {
        return _strKey;
    }

    /**
     * Sets the Key
     * 
     * @param strKey
     *            The Key
     */
    public void setKey( String strKey )
    {
        _strKey = strKey;
    }

    /**
     * Returns the Value
     * 
     * @return The Value
     */
    public String getValue( )
    {
        return _strValue;
    }

    /**
     * Sets the Value
     * 
     * @param strValue
     *            The Value
     */
    public void setValue( String strValue )
    {
        _strValue = strValue;
    }

    /**
     * Returns the Certifier
     * 
     * @return The Certifier
     */
    public String getCertifier( )
    {
        return _strCertifier;
    }

    /**
     * Sets the Certifier
     * 
     * @param strCertifier
     *            The Certifier
     */
    public void setCertifier( String strCertifier )
    {
        _strCertifier = strCertifier;
    }

    /**
     * Returns the CertificationDate
     * 
     * @return The CertificationDate
     */
    public Date getCertificationDate( )
    {
        return _dateCertificationDate;
    }

    /**
     * Sets the CertificationDate
     * 
     * @param dateCertificationDate
     *            The CertificationDate
     */
    public void setCertificationDate( Date dateCertificationDate )
    {
        _dateCertificationDate = dateCertificationDate;
    }

    /**
     * Returns the CertificationLevel
     * 
     * @return The CertificationLevel
     */
    public int getCertificationLevel( )
    {
        return _nCertificationLevel;
    }

    /**
     * Sets the CertificationLevel
     * 
     * @param nCertificationLevel
     *            The CertificationLevel
     */
    public void setCertificationLevel( int nCertificationLevel )
    {
        _nCertificationLevel = nCertificationLevel;
    }

}
