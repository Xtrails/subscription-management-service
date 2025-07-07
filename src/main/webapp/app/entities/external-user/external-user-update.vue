<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="subscriptionManagementServiceApp.externalUser.home.createOrEditLabel"
          data-cy="ExternalUserCreateUpdateHeading"
          v-text="t$('subscriptionManagementServiceApp.externalUser.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="externalUser.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="externalUser.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.externalUser.externalUserId')"
              for="external-user-externalUserId"
            ></label>
            <input
              type="text"
              class="form-control"
              name="externalUserId"
              id="external-user-externalUserId"
              data-cy="externalUserId"
              :class="{ valid: !v$.externalUserId.$invalid, invalid: v$.externalUserId.$invalid }"
              v-model="v$.externalUserId.$model"
              required
            />
            <div v-if="v$.externalUserId.$anyDirty && v$.externalUserId.$invalid">
              <small class="form-text text-danger" v-for="error of v$.externalUserId.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.externalUser.referralCreator')"
              for="external-user-referralCreator"
            ></label>
            <select
              class="form-control"
              id="external-user-referralCreator"
              data-cy="referralCreator"
              name="referralCreator"
              v-model="externalUser.referralCreator"
            >
              <option :value="null"></option>
              <option
                :value="
                  externalUser.referralCreator && referralProgramOption.id === externalUser.referralCreator.id
                    ? externalUser.referralCreator
                    : referralProgramOption
                "
                v-for="referralProgramOption in referralPrograms"
                :key="referralProgramOption.id"
              >
                {{ referralProgramOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('subscriptionManagementServiceApp.externalUser.referralProgram')"
              for="external-user-referralProgram"
            ></label>
            <select
              class="form-control"
              id="external-user-referralProgram"
              data-cy="referralProgram"
              name="referralProgram"
              v-model="externalUser.referralProgram"
            >
              <option :value="null"></option>
              <option
                :value="
                  externalUser.referralProgram && referralProgramOption.id === externalUser.referralProgram.id
                    ? externalUser.referralProgram
                    : referralProgramOption
                "
                v-for="referralProgramOption in referralPrograms"
                :key="referralProgramOption.id"
              >
                {{ referralProgramOption.id }}
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
<script lang="ts" src="./external-user-update.component.ts"></script>
