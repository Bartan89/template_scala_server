


val anIncrementer = (x : Int) => x + 1
val anIncrementerTwo = (_ : Int) + 1

anIncrementer(1)
anIncrementerTwo(1)


def anIncrementerAlt(y : Int): Int  = {
  y + 1
}

anIncrementerAlt(1)

List(1,2,3,4).foreach(x => println("hello" + x))


case class Person(id: Int, name : String, age: Int)

val people = List(Person(1, "Bart", 31), Person(2, "Tom", 31))


val newList = people.map((_.copy(id = 1)))

println(newList)


//double copy

case class CarMaker(yearOfOrigin: Int, name: String)
case class Car(id: Int, brand: CarMaker, model : String)

val cars = List(
  Car(1, CarMaker(1876, "Volkswagen"), "Golf"),
  Car(2, CarMaker(2006, "Tesla"), "Model-t")
)

// in an imaginary world Tesla bought Volkswagen change Volkswagen to Tesla

//cars.map(_.copy(brand.name = "Tesla"))
val renewedList = cars.map(car => car.copy(brand = car.brand.copy(name = "Tesla")))

case class Fruit(name: String = "fruit", vitamins : List[String])

val fruits = List(
  Fruit("Apple", List("A", "C")),
  Fruit("Peer", List("D", "E")),
  Fruit("Banana", List("F", "E+")),
  Fruit(vitamins = List("F", "E+"))
)

//fruit also has Codestar power add Vitamin Codestar
fruits.map(fruit => fruit.copy(vitamins = "vitamin codestar" :: fruit.vitamins))



val combinations = ("green", "blue")

println(combinations == ("green", "blue"))

