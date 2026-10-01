declare variable $ingredients external := "";
declare variable $cuisine external := "";
declare variable $diet external := "";
declare variable $maxCalories external := "";
declare variable $maxTime external := "";
declare variable $difficulty external := "";

let $ingredientList :=
    for $item in tokenize($ingredients, ",")
    let $clean := normalize-space($item)
    where $clean != ""
    return lower-case($clean)

for $recipe in db:get("smart_recipes")/recipes/recipe

let $recipeIngredients :=
    for $name in $recipe/ingredients/ingredient/name
    return lower-case(normalize-space(string($name)))

let $ingredientMatch :=
    if (empty($ingredientList))
    then true()
    else every $requested in $ingredientList
         satisfies
         some $available in $recipeIngredients
         satisfies $available = $requested

let $cuisineMatch :=
    if (normalize-space($cuisine) = "")
    then true()
    else lower-case(string($recipe/cuisine)) = lower-case($cuisine)

let $dietMatch :=
    if (normalize-space($diet) = "")
    then true()
    else lower-case(string($recipe/dietaryType)) = lower-case($diet)

let $calorieMatch :=
    if (normalize-space($maxCalories) = "")
    then true()
    else xs:decimal($recipe/nutrition/calories) <= xs:decimal($maxCalories)

let $timeMatch :=
    if (normalize-space($maxTime) = "")
    then true()
    else xs:integer($recipe/cookingTime) <= xs:integer($maxTime)

let $difficultyMatch :=
    if (normalize-space($difficulty) = "")
    then true()
    else lower-case(string($recipe/difficulty)) = lower-case($difficulty)

where
    $ingredientMatch
    and $cuisineMatch
    and $dietMatch
    and $calorieMatch
    and $timeMatch
    and $difficultyMatch

order by
    xs:integer($recipe/cookingTime),
    xs:decimal($recipe/nutrition/calories)

return
    <recipe>
        <id>{data($recipe/@id)}</id>
        <name>{data($recipe/name)}</name>
        <description>{data($recipe/description)}</description>
        <cuisine>{data($recipe/cuisine)}</cuisine>
        <category>{data($recipe/category)}</category>
        <dietaryType>{data($recipe/dietaryType)}</dietaryType>
        <difficulty>{data($recipe/difficulty)}</difficulty>
        <cookingTime>{data($recipe/cookingTime)}</cookingTime>
        <calories>{data($recipe/nutrition/calories)}</calories>
    </recipe>