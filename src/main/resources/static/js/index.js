$(function () {
    $("#publishBtn").click(publish);
});

function publish() {
    $("#publishModal").modal("hide");

    // 应该在项目中所有ajax发送请求之前都加入这段逻辑，为了省事，直接禁用掉了Spring Security中防止CSRF攻击的措施。

    // 发送 AJAX 请求之前，将CSRF令牌设置到请求的消息头中
    // var token = $("meta[name='_csrf']").attr("content");
    // var header = $("meta[name='_csrf_header']").attr("content");
    // $(document).ajaxSend(function (e, xhr, options) {
    //     xhr.setRequestHeader(header, token);
    // });

    // 获取标题和内容
    var title = $("#recipient-name").val();
    var content = $("#message-text").val();
    // 发送异步请求(POST)
    $.post(
        CONTEXT_PATH + "/discuss/add",
        {"title": title, "content": content},
        function (data) {
            data = $.parseJSON(data);
            // 在提示框中显示返回消息
            $("#hintBody").text(data.msg);
            // 显示提示框
            $("#hintModal").modal("show");
            // 2秒后，自动隐藏提示框
            setTimeout(function () {
                $("#hintModal").modal("hide");
                // 发布成功后刷新页面
                if (data.code == 0) {
                    window.location.reload();
                }
            }, 2000);
        }
    );

}