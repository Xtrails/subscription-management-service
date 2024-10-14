<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="subscriptionManagementServiceApp.paymentSystem.home.createOrEditLabel"
          data-cy="PaymentSystemCreateUpdateHeading"
          v-text="t$('subscriptionManagementServiceApp.paymentSystem.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="paymentSystem.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="paymentSystem.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.paymentSystem.name')"
              for="payment-system-name"
            ></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="payment-system-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              v-text="t$('subscriptionManagementServiceApp.paymentSystem.sourceApplications')"
              for="payment-system-sourceApplications"
            ></label>
            <select
              class="form-control"
              id="payment-system-sourceApplications"
              data-cy="sourceApplications"
              multiple
              name="sourceApplications"
              v-if="paymentSystem.sourceApplications !== undefined"
              v-model="paymentSystem.sourceApplications"
            >
              <option
                :value="getSelected(paymentSystem.sourceApplications, sourceApplicationOption, 'id')"
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
<script lang="ts" src="./payment-system-update.component.ts"></script>
