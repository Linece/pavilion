// $()

$(document).ready(function () {
    // setInterval(function(){ getData() ; }, 3000);
    function getData(params) {
        // const {type,url,payload}=params;
        $.ajax({
            //请求方式
            type: 'GET',
            //发送请求的地址
            url: 'http://106.55.158.81:8080/passengerFlowgroups',
            //服务器返回的数据类型
            crossDomain: true,
            dataType: 'jsonp',
            //发送到服务器的数据，对象必须为key/value的格式，jquery会自动转换为字符串格式
            data: {},
            success: function (data) {
                console.log("------------data", data);
                //请求成功函数内容
            },
            error: function (jqXHR) {
                //请求失败函数内容
            }
        })
    }
    getData() ; 
})
