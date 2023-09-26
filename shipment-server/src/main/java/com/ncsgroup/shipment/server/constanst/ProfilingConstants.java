package com.ncsgroup.shipment.server.constanst;

public class ProfilingConstants {

    public static class CommonConstants {
        public static final String ENCODING_UTF_8 = "UTF-8";
        public static final String LANGUAGE = "Accept-Language";
        public static final String DEFAULT_LANGUAGE = "en";
    }

    public static class AuditorConstant {
        public static final String ANONYMOUS = "anonymousUser";
        public static final String SYSTEM = "SYSTEM";
    }

    public static class StatusException {
        public static final Integer NOT_FOUND = 404;
        public static final Integer CONFLICT = 409;
        public static final Integer BAD_REQUEST = 400;
    }

    public static class MessageException {

    }

    public static class AuthConstant {
        public static String TYPE_TOKEN = "Bear ";
        public static String AUTHORIZATION = "Authorization";
    }
}
