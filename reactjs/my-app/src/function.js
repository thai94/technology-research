hello = function() {
    return "hello world"
}

console.log(hello())

hello1 = () => {
    return "hello world"
}

console.log(hello1())

hello2 = () => "hello world"
console.log(hello2())

hello3 = (name) => "hello " + name
console.log(hello3("Thai"))