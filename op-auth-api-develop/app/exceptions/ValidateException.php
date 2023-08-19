<?php

namespace app\exceptions;

use Exception;

class ValidateException extends Exception
{

    /**
     * ValidateException constructor.
     */
    public function __construct($key, $value)
    {
        $this->message = 'validation error. ' . $key . '=' . $value;
    }
}