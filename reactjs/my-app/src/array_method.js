const myArray = ['apple', 'banana', 'orange']
const myList = myArray.map((item) => item + " fruit")

console.log(myList)


const vehicles = ['mustang', 'f-150', 'expedition']
const [car,, suv] = vehicles

console.log(car)
console.log(suv)

console.log("========")

const vehicleOne = {
    brand: 'Ford',
    model: 'Mustang',
    type: 'car',
    year: 2021,
    color: 'red'
}

const {brand,type} = vehicleOne
console.log(brand)
console.log(type)


console.log("===========")
const numbersOne = [1, 2, 3]
const numbersTwo = [4, 5, 6]

const numbersCombined = [...numbersOne, ...numbersTwo]
console.log(numbersCombined)

const [one, two, ...rest] = numbersCombined
console.log(one)
console.log(two)
console.log(rest)


console.log("=============")
const myVehicle = {
    brand: 'Ford',
    model: 'Mustang',
    color: 'red'
}

const updateMyVehicle = {
    type: 'car',
    year: 2021, 
    color: 'yellow'
}

const myUpdatedVehicle = {...myVehicle, ...updateMyVehicle}
console.log(myUpdatedVehicle)