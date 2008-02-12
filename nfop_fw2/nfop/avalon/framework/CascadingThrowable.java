/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.avalon.framework;

/**
 * Interface which all cascadign throwables should implement.
 * Allows recording of nested exceptions.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public interface CascadingThrowable
{
    Throwable getCause();
}
