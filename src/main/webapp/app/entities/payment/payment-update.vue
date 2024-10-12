<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="subscriptionManagementServiceApp.payment.home.createOrEditLabel"
          data-cy="PaymentCreateUpdateHeading"
          v-text="t$('subscriptionManagementServiceApp.payment.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="payment.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="payment.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('subscriptionManagementServiceApp.payment.amount')" for="payment-amount"></label>
            <input
              type="number"
              class="form-control"
              name="amount"
              id="payment-amount"
              data-cy="amount"
              :class="{ valid: !v$.amount.$invalid, invalid: v$.amount.$invalid }"
              v-model.number="v$.amount.$model"
              required
            />
            <div v-if="v$.amount.$anyDirty && v$.amount.$invalid">
              <small class="form-text text-danger" v-for="error of v$.amount.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('subscriptionManagementServiceApp.payment.status')" for="payment-status"></label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              id="payment-status"
              data-cy="status"
              required
            >
              <option
                v-for="paymentStatus in paymentStatusValues"
                :key="paymentStatus"
                :value="paymentStatus"
                :label="t$('subscriptionManagementServiceApp.PaymentStatus.' + paymentStatus)"
              >
                {{ paymentStatus }}
              </option>
            </select>
            <div v-if="v$.status.$anyDirty && v$.status.$invalid">
              <small class="form-text text-danger" v-for="error of v$.status.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.payment.paymentDate')"
              for="payment-paymentDate"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="payment-paymentDate"
                  v-model="v$.paymentDate.$model"
                  name="paymentDate"
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
                id="payment-paymentDate"
                data-cy="paymentDate"
                type="text"
                class="form-control"
                name="paymentDate"
                :class="{ valid: !v$.paymentDate.$invalid, invalid: v$.paymentDate.$invalid }"
                v-model="v$.paymentDate.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.paymentDate.$anyDirty && v$.paymentDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.paymentDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('subscriptionManagementServiceApp.payment.user')" for="payment-user"></label>
            <select class="form-control" id="payment-user" data-cy="user" name="user" v-model="payment.user">
              <option :value="null"></option>
              <option
                :value="payment.user && externalUserOption.id === payment.user.id ? payment.user : externalUserOption"
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
              v-text="t$('subscriptionManagementServiceApp.payment.clietntSubscription')"
              for="payment-clietntSubscription"
            ></label>
            <select
              class="form-control"
              id="payment-clietntSubscription"
              data-cy="clietntSubscription"
              name="clietntSubscription"
              v-model="payment.clietntSubscription"
            >
              <option :value="null"></option>
              <option
                :value="
                  payment.clietntSubscription && clientSubscriptionOption.id === payment.clietntSubscription.id
                    ? payment.clietntSubscription
                    : clientSubscriptionOption
                "
                v-for="clientSubscriptionOption in clientSubscriptions"
                :key="clientSubscriptionOption.id"
              >
                {{ clientSubscriptionOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.payment.paymentSystem')"
              for="payment-paymentSystem"
            ></label>
            <select
              class="form-control"
              id="payment-paymentSystem"
              data-cy="paymentSystem"
              name="paymentSystem"
              v-model="payment.paymentSystem"
            >
              <option :value="null"></option>
              <option
                :value="
                  payment.paymentSystem && paymentSystemOption.id === payment.paymentSystem.id ? payment.paymentSystem : paymentSystemOption
                "
                v-for="paymentSystemOption in paymentSystems"
                :key="paymentSystemOption.id"
              >
                {{ paymentSystemOption.id }}
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
<script lang="ts" src="./payment-update.component.ts"></script>
