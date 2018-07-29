//存放主要交互逻辑的js代码
//javascript很容易写乱， 需要模块化
//下列方法跟seckill.detail.init(params);  包名.类名.方法 类似
var seckill = {
    //封装秒杀相关ajax的url
    URL : {
        now : function () {
            return '/seckill/time/now';
        },
        exposer : function (seckillId) {
            return '/seckill/'+seckillId+'/exposer';
        },
        execution : function (seckillId, md5) {
            return '/seckill/'+ seckillId + '/' + md5 + '/execution';
        }
    },

    handleSeckillkill : function(seckillId, node) {
        //处理秒杀逻辑, 控制显示逻辑，执行秒杀
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>'); //按钮
        $.post(seckill.URL.exposer(seckillId), {}, function (result) { //post请求，url，参数，回调函数
            //在回调函数中，执行交互流程
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //开启秒杀
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killUrl:"+killUrl);
                    //.one作用绑定一次点击事件，减少发送请求的次数
                    $('#killBtn').one('click', function () { //点按钮处理的操作
                        //执行秒杀请求
                        //1.禁用按钮
                        $(this).addClass('disabled');
                        //2：发送秒杀请求，执行秒杀
                        $.post(killUrl, {}, function (result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //3:显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                } else {
                    //未开启秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新计算计时逻辑
                    seckill.countDown(seckillId, now, start, end);
                }
            }else {
                console.log('result:' + result);
            }
        });
    },

    //验证手机号，可能有多个地方需要这个功能，所以放在上层
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) { //isNaN是否是非数字，不是数字返回true
            return true;
        } else {
            return false;
        }
    },

    countDown : function(seckillId, nowTime, startTime, endTime) {
        //alert("nt"+nowTime + "ID" + seckillId);
        var seckillBox = $('#seckill-box');
        //时间的判断
        if(nowTime > endTime) {
            //秒杀结束
            seckillBox.html("秒杀结束！");
        }else if(nowTime < startTime) {
            //秒杀未开始，计时事件绑定
            var killTime = new Date(startTime + 1000); //TODO 1000为了防止时间偏移
            seckillBox.countdown(killTime,function (event) { //每一次事件变化都会回调这个函数
                //事件格式
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown',function () { //时间完成后回调事件
                //获取秒杀地址， 控制显示逻辑，执行秒杀
                seckill.handleSeckillkill(seckillId, seckillBox);
            });

        }else {
            //秒杀开始
            seckill.handleSeckillkill(seckillId, seckillBox);
        }
    },

    //详情页秒杀逻辑
    detail : {
        //详情页初始化
        init : function(params) {
            //手机验证和登录，计时交互
            //规划我们的交互流程
            //在Cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            var startTime = params['startTime']; //javascript访问json的一种方式
            var seckillId = params['seckillId'];
            var endTime = params['endTime'];
            //验证手机号
            if(!seckill.validatePhone(killPhone)) {
                //绑定phone
                //控制输出
                var killPhoneModal = $('#killPhoneModal');
                //显示弹出层
                killPhoneModal.modal({ //传入json
                    show : true, //显示弹出层
                    backdrop : 'static', //禁止位置关闭
                    keyboard : false //关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if(seckill.validatePhone(inputPhone)) {
                        //电话写入cookie
                        $.cookie('killPhone', inputPhone, {expires : 7, path : '/seckill'});
                        //刷新页面,刷新页面后会重新走detail.init()
                        window.location.reload();
                    }else {
                        //先影藏后显示比较好看
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
                    }
                });
            }
            //已经登录
            //计时交互
            $.get(seckill.URL.now(), {}, function (result) { //get请求
                console.log(result);
                if(result && result['success']) {
                    var nowTime = result['data'];
                    //时间判断,计时交互
                    seckill.countDown(seckillId, nowTime, startTime, endTime);
                }else {
                    console.log('result:' + result);
                }
            });
        }
    }
}