declare variable $cuisine external;

for $recipe in db:get("smart_recipes")/recipes/recipe
where lower-case(string($recipe/cuisine)) = lower-case($cuisine)
return
    <recipe>
        <id>{data($recipe/@id)}</id>
        <name>{data($recipe/name)}</name>
        <cuisine>{data($recipe/cuisine)}</cuisine>
        <dietaryType>{data($recipe/dietaryType)}</dietaryType>
        <cookingTime>{data($recipe/cookingTime)}</cookingTime>
        <calories>{data($recipe/nutrition/calories)}</calories>
    </recipe>