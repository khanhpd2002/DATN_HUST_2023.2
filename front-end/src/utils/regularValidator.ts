const validatorPositiveInteger = (rule: any, value: any, callback: any) => {
    if (/(^[1-9]\d*$)/.test(value)) {
        callback();
    } else {
        callback(new Error("请输入正整数"));
    }
};

const validatorMaxCountFunction = (errorMsg = "最多三个", count = 3) => {
    return (rule: any, value: any, callback: any) => {
        if (!value || !value.length) {
            callback(new Error("不能为空"));
        } else if (value.length > count) {
            callback(new Error(errorMsg));
        } else {
            callback();
        }
    };
};

export { validatorPositiveInteger, validatorMaxCountFunction };
