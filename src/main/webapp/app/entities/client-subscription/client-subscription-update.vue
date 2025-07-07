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
              v-text="t$('subscriptionManagementServiceApp.clientSubscription.startDttm')"
              for="client-subscription-startDttm"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="client-subscription-startDttm"
                  v-model="v$.startDttm.$model"
                  name="startDttm"
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
                id="client-subscription-startDttm"
                data-cy="startDttm"
                type="text"
                class="form-control"
                name="startDttm"
                :class="{ valid: !v$.startDttm.$invalid, invalid: v$.startDttm.$invalid }"
                v-model="v$.startDttm.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.startDttm.$anyDirty && v$.startDttm.$invalid">
              <small class="form-text text-danger" v-for="error of v$.startDttm.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.clientSubscription.endDttm')"
              for="client-subscription-endDttm"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="client-subscription-endDttm"
                  v-model="v$.endDttm.$model"
                  name="endDttm"
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
                id="client-subscription-endDttm"
                data-cy="endDttm"
                type="text"
                class="form-control"
                name="endDttm"
                :class="{ valid: !v$.endDttm.$invalid, invalid: v$.endDttm.$invalid }"
                v-model="v$.endDttm.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.endDttm.$anyDirty && v$.endDttm.$invalid">
              <small class="form-text text-danger" v-for="error of v$.endDttm.$errors" :key="error.$uid">{{ error.$message }}</small>
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
              v-text="t$('subscriptionManagementServiceApp.clientSubscription.subscriptionDetails')"
              for="client-subscription-subscriptionDetails"
            ></label>
            <select
              class="form-control"
              id="client-subscription-subscriptionDetails"
              data-cy="subscriptionDetails"
              name="subscriptionDetails"
              v-model="clientSubscription.subscriptionDetails"
            >
              <option :value="null"></option>
              <option
                :value="
                  clientSubscription.subscriptionDetails && subscriptionDetailsOption.id === clientSubscription.subscriptionDetails.id
                    ? clientSubscription.subscriptionDetails
                    : subscriptionDetailsOption
                "
                v-for="subscriptionDetailsOption in subscriptionDetails"
                :key="subscriptionDetailsOption.id"
              >
                {{ subscriptionDetailsOption.id }}
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
