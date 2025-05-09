package no.ntnu.idatt2106.krisefikser.model;

/**
 * Container class for enumeration types used in the model.
 * <p>
 * Holds roles and icon identifiers for use throughout the application.
 */
public class Enums {

    /**
     * Enumeration of user roles within the system.
     */
    public enum RoleEnum {
        /**
         * Standard user with limited access.
         */
        normal,
        /**
         * Administrator with elevated privileges.
         */
        admin,
        /**
         * Super administrator with full system control.
         */
        super_admin
    }

    /**
     * Enumeration of available icon types for UI elements.
     */
    public enum IconEnum {
        /**
         * No icon.
         */
        none,
        /**
         * Point icon, typically representing a location.
         */
        point,
        /**
         * Standard icon for general use.
         */
        normal,
        /**
         * Danger icon, used to indicate hazards or warnings.
         */
        danger,
        /**
         * Assembly point icon for evacuation or meeting points.
         */
        assembly_point,
        /**
         * Medical icon indicating healthcare or first aid.
         */
        medical,
        /**
         * Shelter icon indicating safe zones or shelters.
         */
        shelter
    }
}
