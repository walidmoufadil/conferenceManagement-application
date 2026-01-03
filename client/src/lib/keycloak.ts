import Keycloak from 'keycloak-js';

const keycloakConfig = {
  url: 'http://localhost:8080',
  realm: 'bank-application',
  clientId: 'bank-app',
};

const keycloak = new Keycloak(keycloakConfig);

export const initKeycloak = async () => {
  try {
    const authenticated = await keycloak.init({
      onLoad: 'login-required',
      checkLoginIframe: false,
    });
    return authenticated;
  } catch (error) {
    console.error('Failed to initialize Keycloak', error);
    throw error;
  }
};

export const getToken = () => {
  return keycloak.token;
};

export const logout = () => {
  keycloak.logout();
};

export const getUsername = () => {
  return keycloak.tokenParsed?.preferred_username || 'User';
};

export default keycloak;
