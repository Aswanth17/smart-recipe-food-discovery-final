declare variable $ingredient external;

for $recipe in db:get("smart_recipes")/recipes/recipe
where some $item in $recipe/ingredients/ingredient/name
      satisfies lower-case(string($item)) = lower-case($ingredient)
return
    <recipe>
        <id>{data($recipe/@id)}</id>
        <name>{data($recipe/name)}</name>
        <cuisine>{data($recipe/cuisine)}</cuisine>
        <dietaryType>{data($recipe/dietaryType)}</dietaryType>
        <cookingTime>{data($recipe/cookingTime)}</cookingTime>
        <calories>{data($recipe/nutrition/calories)}</calories>
    </recipe>