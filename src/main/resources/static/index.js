$(document).ready(function () {
    //获取浏览器参数
    function GetQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
    let collection = GetQueryString('type') || 1;//1：A馆 ，2：B馆
    // let text=collection===1?'A':'B';
    $(".collectType").html(collection === 1 ? 'A' : 'B');
    $(".collectType2").html(collection === 1 ? 'B' : 'A');
    $(".totalData").html(collection === 1 ? '300' : '2000');
    $(".downNum").html(collection === 1 ? '2000' : '300');
    if (collection === '2') {
        $(".mess").html('B馆总建筑面积为8572平方米，共分三层。首层布置入口门厅和农产品创意展厅，首层建筑面积为3828平方米；二层布置农产品科普展厅和农产品教育展厅，二层建筑面积为2140平方米；三层布置农产品体验区和一个多功能厅，三层建筑面积为2604平方米。')
    }
    // $(".collectType").html(text);
    console.log("coll", collection)
    console.log(window.location);
    //天气
    const weather = {
        height: 30,
        low: 27,
        detail: '多云'
    }
    let personTotalLogo = {};
    /** 人数拥挤程度统计规则如下 **/
    // 1:空闲，2：适中，3拥挤
    const mapPerson = {
        1: 'person4.png',
        2: 'person2.png',
        3: 'person3.png'
    }
    // 1:空闲，2：适中，3拥挤
    const mapTitle = {
        1: '空闲',
        2: '舒适',
        3: '拥挤'
    }
    function planA_Rule(params) {
        if (params <= 35) {
            return {
                type: '1',
                number: 4
            }
        }
        else if (params <= 70 && params > 35) {
            return {
                type: '1',
                number: 7
            }
        }
        else if (params <= 100 && params > 70) {
            return {
                type: '1',
                number: 10
            }
        }
        else if (params <= 135 && params > 100) {
            return {
                type: '2',
                number: 4
            }
        }
        else if (params <= 170 && params > 135) {
            return {
                type: '2',
                number: 7
            }
        }
        else if (params <= 200 && params > 170) {
            return {
                type: '2',
                number: 10
            }
        }
        else if (params <= 235 && params > 200) {
            return {
                type: '3',
                number: 4
            }
        }
        else if (params <= 270 && params > 235) {
            return {
                type: '3',
                number: 7
            }
        }
        else if (params <= 300 && params > 270) {
            return {
                type: '3',
                number: 10
            }
        } else {
            return {
                type: '1',
                number: 5
            }

        }
    }
    function planB_Rule(params) {
        if (params <= 250) {
            return {
                type: '1',
                number: 4
            }
        }
        else if (params <= 500 && params > 250) {
            return {
                type: '1',
                number: 7
            }
        }
        else if (params <= 700 && params > 500) {
            return {
                type: '1',
                number: 10
            }
        }
        else if (params <= 950 && params > 700) {
            return {
                type: '2',
                number: 4
            }
        }
        else if (params <= 1200 && params > 950) {
            return {
                type: '2',
                number: 7
            }
        }
        else if (params <= 1400 && params > 1200) {
            return {
                type: '2',
                number: 10
            }
        }
        else if (params <= 1650 && params > 1400) {
            return {
                type: '3',
                number: 4
            }
        }
        else if (params <= 1900 && params > 1650) {
            return {
                type: '3',
                number: 7
            }
        }
        else if (params <= 2000 && params > 1900) {
            return {
                type: '3',
                number: 10
            }
        } else {
            return {
                type: '1',
                number: 5
            }

        }
    }

    //getNumberTYPE
    function getNumberType(type, number) {
        let var1 = '';
        let var2 = '';
        for (let i = 1; i <= number; i++) {
            var1 += `<li><image class="person" src="./images/${mapPerson[type]}"></image></li>`;
        }
        for (let j = 1; j <= 10 - number; j++) {
            var2 += `<li><image class="person" src="./images/person.png"></image></li>`;
        }
        return var1 + var2
    }
    /** 人数拥挤程度统计规则结束 **/
    setInterval(function () { getWeatherData(); }, 3600000);
    setInterval(function () { getPersonData(); }, 60000);
    function getWeatherData(params) {
        $.ajax({
            //请求方式
            type: 'GET',
            //发送请求的地址
            //url: './getWeather',
            url: 'http://wthrcdn.etouch.cn/weather_mini?city=怀仁',
            //服务器返回的数据类型
            // crossDomain: true,
            dataType: 'json',
            //发送到服务器的数据，对象必须为key/value的格式，jquery会自动转换为字符串格式
            data: {},
            success: function (res) {
                console.log("------------data", res);
                //请求成功函数内容
                const result = res.data.forecast[0];
                // 天气数据
                $(".hotH").html(result.high.split(" ")[1]);
                $(".hotL").html(result.low.split(" ")[1]);
                $(".hotD").html(result.type);
            },
            error: function (jqXHR) {
                //请求失败函数内容
                console.log("接口请求失败")
            }
        })
    }
    function getPersonData(params) {
        $.ajax({
            //请求方式
            type: 'GET',
            //发送请求的地址
            url: './passengerFlowgroups/1',
            //服务器返回的数据类型
            // crossDomain: true,
            dataType: 'json',
            //发送到服务器的数据，对象必须为key/value的格式，jquery会自动转换为字符串格式
            data: {},
            success: function (res) {
                console.log("------------data1", res);
                const result = res.data;
                // 人数统计情况

                //300为例 总数0-10  {type: 1 , number: 1}  高亮一个
                if (collection === 1) {
                    personTotalLogo = planA_Rule(result.holdValue);
                    $(".numberLogo").html(
                        getNumberType(personTotalLogo.type, personTotalLogo.number)
                    )
                }
                //1为a馆主数据 2为b馆为主数据
                if (collection === 1) {
                    $(".now").html(result.holdValue);
                    $(".total1").html(result.flowInNum);
                    $(".total2").html(result.flowOutNum);
                    $(".downStatus").html(mapTitle[personTotalLogo.type]);
                } else {
                    $(".bTotal1").html(result.flowInNum);
                    $(".bTotal2").html(result.flowOutNum);
                    $(".bTotal").html(result.holdValue);
                    $(".statusData").html(mapTitle[personTotalLogo.type]);
                }
                //请求服务器日期数据
                $(".time").html(result.date);
                $(".week").html(result.week);
            },
            error: function (jqXHR) {
                //请求失败函数内容
            }
        })
        $.ajax({
            //请求方式
            type: 'GET',
            //发送请求的地址
            url: './passengerFlowgroups/2',
            //服务器返回的数据类型
            // crossDomain: true,
            dataType: 'json',
            //发送到服务器的数据，对象必须为key/value的格式，jquery会自动转换为字符串格式
            data: {},
            success: function (res) {
                console.log("------------data2", res);
                const result = res.data;
                // 人数统计情况

                //300为例 总数0-10  {type: 1 , number: 1}  高亮一个
                if (collection === '2') {
                    personTotalLogo = planB_Rule(result.holdValue);
                    $(".numberLogo").html(
                        getNumberType(personTotalLogo.type, personTotalLogo.number)
                    )
                }
                //1为a馆主数据 2为b馆为主数据
                if (collection === 1) {
                    $(".bTotal1").html(result.flowInNum);
                    $(".bTotal2").html(result.flowOutNum);
                    $(".bTotal").html(result.holdValue);
                    $(".statusData").html(mapTitle[personTotalLogo.type]);
                } else {
                    $(".now").html(result.holdValue);
                    $(".total1").html(result.flowInNum);
                    $(".total2").html(result.flowOutNum);
                    $(".downStatus").html(mapTitle[personTotalLogo.type]);
                }

                //请求成功函数内容
            },
            error: function (jqXHR) {
                //请求失败函数内容
            }
        })
    }
    getWeatherData();
    getPersonData();
})
