<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="subscriptionManagementServiceApp.subscriptionAccess.home.createOrEditLabel"
          data-cy="SubscriptionAccessCreateUpdateHeading"
          v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="subscriptionAccess.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="subscriptionAccess.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.name')"
              for="subscription-access-name"
            ></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="subscription-access-name"
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
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.description')"
              for="subscription-access-description"
            ></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="subscription-access-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.order')"
              for="subscription-access-order"
            ></label>
            <input
              type="number"
              class="form-control"
              name="order"
              id="subscription-access-order"
              data-cy="order"
              :class="{ valid: !v$.order.$invalid, invalid: v$.order.$invalid }"
              v-model.number="v$.order.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.role')"
              for="subscription-access-role"
            ></label>
            <input
              type="text"
              class="form-control"
              name="role"
              id="subscription-access-role"
              data-cy="role"
              :class="{ valid: !v$.role.$invalid, invalid: v$.role.$invalid }"
              v-model="v$.role.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.roleGroup')"
              for="subscription-access-roleGroup"
            ></label>
            <input
              type="text"
              class="form-control"
              name="roleGroup"
              id="subscription-access-roleGroup"
              data-cy="roleGroup"
              :class="{ valid: !v$.roleGroup.$invalid, invalid: v$.roleGroup.$invalid }"
              v-model="v$.roleGroup.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.active')"
              for="subscription-access-active"
            ></label>
            <input
              type="checkbox"
              class="form-check"
              name="active"
              id="subscription-access-active"
              data-cy="active"
              :class="{ valid: !v$.active.$invalid, invalid: v$.active.$invalid }"
              v-model="v$.active.$model"
              required
            />
            <div v-if="v$.active.$anyDirty && v$.active.$invalid">
              <small class="form-text text-danger" v-for="error of v$.active.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.subscriptionDetails')"
              for="subscription-access-subscriptionDetails"
            ></label>
            <select
              class="form-control"
              id="subscription-access-subscriptionDetails"
              data-cy="subscriptionDetails"
              multiple
              name="subscriptionDetails"
              v-if="subscriptionAccess.subscriptionDetails !== undefined"
              v-model="subscriptionAccess.subscriptionDetails"
              required
            >
              <option
                :value="getSelected(subscriptionAccess.subscriptionDetails, subscriptionDetailsOption, 'id')"
                v-for="subscriptionDetailsOption in subscriptionDetails"
                :key="subscriptionDetailsOption.id"
              >
                {{ subscriptionDetailsOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.subscriptionDetails.$anyDirty && v$.subscriptionDetails.$invalid">
            <small class="form-text text-danger" v-for="error of v$.subscriptionDetails.$errors" :key="error.$uid">{{
              error.$message
            }}</small>
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
<script lang="ts" src="./subscription-access-update.component.ts"></script>
