<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="subscriptionManagementServiceApp.sourceApplication.home.createOrEditLabel"
          data-cy="SourceApplicationCreateUpdateHeading"
          v-text="t$('subscriptionManagementServiceApp.sourceApplication.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="sourceApplication.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="sourceApplication.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.sourceApplication.applicationName')"
              for="source-application-applicationName"
            ></label>
            <input
              type="text"
              class="form-control"
              name="applicationName"
              id="source-application-applicationName"
              data-cy="applicationName"
              :class="{ valid: !v$.applicationName.$invalid, invalid: v$.applicationName.$invalid }"
              v-model="v$.applicationName.$model"
              required
            />
            <div v-if="v$.applicationName.$anyDirty && v$.applicationName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.applicationName.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              v-text="t$('subscriptionManagementServiceApp.sourceApplication.paymentSystems')"
              for="source-application-paymentSystems"
            ></label>
            <select
              class="form-control"
              id="source-application-paymentSystems"
              data-cy="paymentSystems"
              multiple
              name="paymentSystems"
              v-if="sourceApplication.paymentSystems !== undefined"
              v-model="sourceApplication.paymentSystems"
            >
              <option
                :value="getSelected(sourceApplication.paymentSystems, paymentSystemOption, 'id')"
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
<script lang="ts" src="./source-application-update.component.ts"></script>
