package uk.ac.ebi.fgpt.zooma.service;

import uk.ac.ebi.fgpt.zooma.model.Property;

import java.util.Collection;
import java.util.Map;

/**
 * A ZOOMA service that allows searching over the set of {@link Property}s known to ZOOMA.  Two fundamental types of
 * search are allowed: prefix searches, to support autocomplete scenarios, and string based matching using an algorithm
 * defined by implementing classes.
 * <p/>
 * Both basic types can be modified by supplying typing information, but note that if typing information is supplied it
 * should match exactly.
 *
 * @author Tony Burdett
 * @date 08/03/12
 */
public interface PropertySearchService {
    /**
     * Search the set of properties known to ZOOMA for those which have a value that matches the supplied pattern.
     * <p/>
     * Implementations are free to define their own search algorithms for text matching.
     *
     * @param propertyValuePattern the property value that should be searched for
     * @return a collection of matching properties
     */
    Collection<Property> search(String propertyValuePattern);

    /**
     * Search the set of properties known to ZOOMA for those which have a value that matches the supplied pattern. The
     * set of results should only include those which exactly map the supplied type.  To expand the search tbased on
     * typing information, first use a {@link PropertyTypeSearchService} to find possible matches for the property
     * type.
     * <p/>
     * Implementations are free to define their own search algorithms for text matching.
     *
     * @param propertyType         the property type that should be searched for
     * @param propertyValuePattern the property value that should be searched for
     * @return a collection of matching properties
     */
    Collection<Property> search(String propertyType, String propertyValuePattern);

    /**
     * Search the set of properties known to ZOOMA for those which have a value that matches the supplied prefix
     * <p/>
     * Results should always match based on an exact prefix match (essentially, {@link String#startsWith(String)}).
     *
     * @param propertyValuePrefix the property value that should be searched for
     * @return a collection of matching properties
     */
    Collection<Property> searchByPrefix(String propertyValuePrefix);

    /**
     * Search the set of properties known to ZOOMA for those which have a value that matches the supplied prefix. The
     * set of results should only include those which exactly map the supplied type.  To expand the search tbased on
     * typing information, first use a {@link PropertyTypeSearchService} to find possible matches for the property
     * type.
     * <p/>
     * Results should always match based on an exact prefix match (essentially, {@link String#startsWith(String)}).
     *
     * @param propertyType        the property type that should be searched for
     * @param propertyValuePrefix the property value that should be searched for
     * @return a collection of matching properties
     */
    Collection<Property> searchByPrefix(String propertyType, String propertyValuePrefix);

    /**
     * Search the set of properties known to ZOOMA for those which have a value that matches the supplied pattern.
     * <p/>
     * Implementations are free to define their own search algorithms for text matching.
     * <p/>
     * This form returns a map of matching properties linked to a metric that describes the quality of the match.  You
     * may need to sort results based on their score to determine the best match order
     *
     * @param propertyValuePattern the property value that should be searched for
     * @return a collection of matching properties
     */
    Map<Property, Float> searchAndScore(String propertyValuePattern);

    /**
     * Search the set of properties known to ZOOMA for those which have a value that matches the supplied pattern. The
     * set of results should only include those which exactly map the supplied type.  To expand the search tbased on
     * typing information, first use a {@link PropertyTypeSearchService} to find possible matches for the property
     * type.
     * <p/>
     * Implementations are free to define their own search algorithms for text matching.
     * <p/>
     * This form returns a map of matching properties linked to a metric that describes the quality of the match.  You
     * may need to sort results based on their score to determine the best match order
     *
     * @param propertyType         the property type that should be searched for
     * @param propertyValuePattern the property value that should be searched for
     * @return a collection of matching properties
     */
    Map<Property, Float> searchAndScore(String propertyType, String propertyValuePattern);

    /**
     * Search the set of properties known to ZOOMA for those which have a value that matches the supplied prefix
     * <p/>
     * Results should always match based on an exact prefix match (essentially, {@link String#startsWith(String)}).
     * <p/>
     * This form returns a map of matching properties linked to a metric that describes the quality of the match.  You
     * may need to sort results based on their score to determine the best match order
     *
     * @param propertyValuePrefix the property value that should be searched for
     * @return a collection of matching properties
     */
    Map<Property, Float> searchAndScoreByPrefix(String propertyValuePrefix);

    /**
     * Search the set of properties known to ZOOMA for those which have a value that matches the supplied prefix. The
     * set of results should only include those which exactly map the supplied type.  To expand the search tbased on
     * typing information, first use a {@link PropertyTypeSearchService} to find possible matches for the property
     * type.
     * <p/>
     * Results should always match based on an exact prefix match (essentially, {@link String#startsWith(String)}).
     * <p/>
     * This form returns a map of matching properties linked to a metric that describes the quality of the match.  You
     * may need to sort results based on their score to determine the best match order
     *
     * @param propertyType        the property type that should be searched for
     * @param propertyValuePrefix the property value that should be searched for
     * @return a collection of matching properties
     */
    Map<Property, Float> searchAndScoreByPrefix(String propertyType, String propertyValuePrefix);
}
