<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="subscriptionManagementServiceApp.clientSubscription.home.createOrEditLabel"
          data-cy="ClientSubscriptionCreateUpdateHeading"
          v-text="t$('subscriptionManagementServiceApp.clientSubscription.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="clientSubscription.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="clientSubscription.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.clientSubscription.startDate')"
              for="client-subscription-startDate"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="client-subscription-startDate"
                  v-model="v$.startDate.$model"
                  name="startDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="client-subscription-startDate"
                data-cy="startDate"
                type="text"
                class="form-control"
                name="startDate"
                :class="{ valid: !v$.startDate.$invalid, invalid: v$.startDate.$invalid }"
                v-model="v$.startDate.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.startDate.$anyDirty && v$.startDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.startDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.clientSubscription.endDate')"
              for="client-subscription-endDate"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="client-subscription-endDate"
                  v-model="v$.endDate.$model"
                  name="endDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="client-subscription-endDate"
                data-cy="endDate"
                type="text"
                class="form-control"
                name="endDate"
                :class="{ valid: !v$.endDate.$invalid, invalid: v$.endDate.$invalid }"
                v-model="v$.endDate.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.endDate.$anyDirty && v$.endDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.endDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.clientSubscription.status')"
              for="client-subscription-status"
            ></label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              id="client-subscription-status"
              data-cy="status"
              required
            >
              <option
                v-for="subscriptionStatus in subscriptionStatusValues"
                :key="subscriptionStatus"
                :value="subscriptionStatus"
                :label="t$('subscriptionManagementServiceApp.SubscriptionStatus.' + subscriptionStatus)"
              >
                {{ subscriptionStatus }}
              </option>
            </select>
            <div v-if="v$.status.$anyDirty && v$.status.$invalid">
              <small class="form-text text-danger" v-for="error of v$.status.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.clientSubscription.user')"
              for="client-subscription-user"
            ></label>
            <select class="form-control" id="client-subscription-user" data-cy="user" name="user" v-model="clientSubscription.user">
              <option :value="null"></option>
              <option
                :value="
                  clientSubscription.user && externalUserOption.id === clientSubscription.user.id
                    ? clientSubscription.user
                    : externalUserOption
                "
                v-for="externalUserOption in externalUsers"
                :key="externalUserOption.id"
              >
                {{ externalUserOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.clientSubscription.subscriptionType')"
              for="client-subscription-subscriptionType"
            ></label>
            <select
              class="form-control"
              id="client-subscription-subscriptionType"
              data-cy="subscriptionType"
              name="subscriptionType"
              v-model="clientSubscription.subscriptionType"
            >
              <option :value="null"></option>
              <option
                :value="
                  clientSubscription.subscriptionType && subscriptionTypeOption.id === clientSubscription.subscriptionType.id
                    ? clientSubscription.subscriptionType
                    : subscriptionTypeOption
                "
                v-for="subscriptionTypeOption in subscriptionTypes"
                :key="subscriptionTypeOption.id"
              >
                {{ subscriptionTypeOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.clientSubscription.sourceApplication')"
              for="client-subscription-sourceApplication"
            ></label>
            <select
              class="form-control"
              id="client-subscription-sourceApplication"
              data-cy="sourceApplication"
              name="sourceApplication"
              v-model="clientSubscription.sourceApplication"
            >
              <option :value="null"></option>
              <option
                :value="
                  clientSubscription.sourceApplication && sourceApplicationOption.id === clientSubscription.sourceApplication.id
                    ? clientSubscription.sourceApplication
                    : sourceApplicationOption
                "
                v-for="sourceApplicationOption in sourceApplications"
                :key="sourceApplicationOption.id"
              >
                {{ sourceApplicationOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./client-subscription-update.component.ts"></script>
