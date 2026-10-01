package com.project.recipe.xml;

import com.project.recipe.dto.RecipeResponse;
import com.project.recipe.dto.RecipeResponse.IngredientResponse;
import com.project.recipe.dto.RecipeResponse.NutritionResponse;

import org.springframework.stereotype.Component;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.StringReader;

import java.util.ArrayList;
import java.util.List;

@Component
public class RecipeXmlParser {

    public List<RecipeResponse> parseRecipes(
            String xml
    ) {

        if (xml == null
                || xml.isBlank()) {

            return List.of();
        }

        try {

            String wrappedXml =
                    "<results>"
                            + xml
                            + "</results>";

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            /*
             * Basic secure XML parser configuration.
             */

            factory.setFeature(
                    "http://apache.org/xml/features/disallow-doctype-decl",
                    true
            );

            factory.setFeature(
                    "http://xml.org/sax/features/external-general-entities",
                    false
            );

            factory.setFeature(
                    "http://xml.org/sax/features/external-parameter-entities",
                    false
            );

            factory.setXIncludeAware(false);

            factory.setExpandEntityReferences(false);


            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            Document document =
                    builder.parse(
                            new InputSource(
                                    new StringReader(
                                            wrappedXml
                                    )
                            )
                    );


            NodeList recipeNodes =
                    document.getElementsByTagName(
                            "recipe"
                    );


            List<RecipeResponse> recipes =
                    new ArrayList<>();


            for (
                    int i = 0;
                    i < recipeNodes.getLength();
                    i++
            ) {

                Node node =
                        recipeNodes.item(i);

                if (
                        node.getNodeType()
                                != Node.ELEMENT_NODE
                ) {

                    continue;
                }

                recipes.add(
                        parseRecipe(
                                (Element) node
                        )
                );
            }


            return recipes;

        } catch (Exception exception) {

            throw new IllegalStateException(
                    "Unable to parse recipe XML returned by BaseX.",
                    exception
            );
        }
    }


    private RecipeResponse parseRecipe(
            Element recipe
    ) {

        String recipeId =
                recipe.getAttribute("id");


        List<IngredientResponse> ingredients =
                parseIngredients(recipe);


        List<String> steps =
                parseSimpleList(
                        recipe,
                        "steps",
                        "step"
                );


        Element nutrition =
                getRequiredElement(
                        recipe,
                        "nutrition"
                );


        NutritionResponse nutritionResponse =
                new NutritionResponse(

                        getDouble(
                                nutrition,
                                "calories"
                        ),

                        getDouble(
                                nutrition,
                                "protein"
                        ),

                        getDouble(
                                nutrition,
                                "carbohydrates"
                        ),

                        getDouble(
                                nutrition,
                                "fat"
                        )
                );


        return new RecipeResponse(

                recipeId,

                getText(
                        recipe,
                        "name"
                ),

                getText(
                        recipe,
                        "description"
                ),

                getText(
                        recipe,
                        "cuisine"
                ),

                getText(
                        recipe,
                        "category"
                ),

                ingredients,

                steps,

                nutritionResponse,

                getInteger(
                        recipe,
                        "cookingTime"
                ),

                getText(
                        recipe,
                        "difficulty"
                ),

                getText(
                        recipe,
                        "dietaryType"
                ),

                parseSimpleList(
                        recipe,
                        "allergens",
                        "allergen"
                ),

                parseSimpleList(
                        recipe,
                        "tags",
                        "tag"
                )
        );
    }


    private List<IngredientResponse> parseIngredients(
            Element recipe
    ) {

        Element ingredientsElement =
                getRequiredElement(
                        recipe,
                        "ingredients"
                );


        NodeList nodes =
                ingredientsElement
                        .getElementsByTagName(
                                "ingredient"
                        );


        List<IngredientResponse> ingredients =
                new ArrayList<>();


        for (
                int i = 0;
                i < nodes.getLength();
                i++
        ) {

            Element ingredient =
                    (Element) nodes.item(i);


            ingredients.add(
                    new IngredientResponse(

                            getText(
                                    ingredient,
                                    "name"
                            ),

                            getDouble(
                                    ingredient,
                                    "quantity"
                            ),

                            getText(
                                    ingredient,
                                    "unit"
                            )
                    )
            );
        }


        return ingredients;
    }


    private List<String> parseSimpleList(

            Element parent,

            String containerName,

            String itemName

    ) {

        Element container =
                getRequiredElement(
                        parent,
                        containerName
                );


        NodeList nodes =
                container.getElementsByTagName(
                        itemName
                );


        List<String> result =
                new ArrayList<>();


        for (
                int i = 0;
                i < nodes.getLength();
                i++
        ) {

            result.add(
                    nodes.item(i)
                            .getTextContent()
                            .trim()
            );
        }


        return result;
    }


    private Element getRequiredElement(

            Element parent,

            String tagName

    ) {

        NodeList nodes =
                parent.getElementsByTagName(
                        tagName
                );


        if (
                nodes.getLength() == 0
        ) {

            throw new IllegalStateException(
                    "Missing XML element: "
                            + tagName
            );
        }


        return (Element) nodes.item(0);
    }


    private String getText(

            Element parent,

            String tagName

    ) {

        return getRequiredElement(
                parent,
                tagName
        )
                .getTextContent()
                .trim();
    }


    private int getInteger(

            Element parent,

            String tagName

    ) {

        return Integer.parseInt(
                getText(
                        parent,
                        tagName
                )
        );
    }


    private double getDouble(

            Element parent,

            String tagName

    ) {

        return Double.parseDouble(
                getText(
                        parent,
                        tagName
                )
        );
    }
}