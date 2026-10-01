for $recipe in db:get("smart_recipes")/recipes/recipe
order by string($recipe/name)
return
    <recipeSummary>
        <id>{data($recipe/@id)}</id>
        <name>{data($recipe/name)}</name>
        <cuisine>{data($recipe/cuisine)}</cuisine>
        <category>{data($recipe/category)}</category>
        <dietaryType>{data($recipe/dietaryType)}</dietaryType>
        <difficulty>{data($recipe/difficulty)}</difficulty>
        <cookingTime>{data($recipe/cookingTime)}</cookingTime>
        <calories>{data($recipe/nutrition/calories)}</calories>
    </recipeSummary>