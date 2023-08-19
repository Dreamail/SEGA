@extends('admin.common.layout')

@section('content')
    <!--/span-->
    <div id="content" class="span10">
        <ul class="breadcrumb">
            <li class="active">
                TOP
            </li>
        </ul>

        <h2>TOP</h2><br>


        @if (count($errors) > 0)
            <div class="alert alert-error">
                @foreach ($errors->all() as $error)
                    <span class="badge badge-important">error</span>&nbsp{{ $error }}</br>
                    @break
                @endforeach
            </div>
        @endif

    </div>
    <!--/span-->
@stop