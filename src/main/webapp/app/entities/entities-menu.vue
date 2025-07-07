<template>
  <div>
    <b-dropdown-item to="/external-user">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.externalUser')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/client-subscription">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.clientSubscription')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/subscription-details">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.subscriptionDetails')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/referral-program">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.referralProgram')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/payment">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.payment')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/payment-system">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.paymentSystem')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/source-application">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.sourceApplication')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
