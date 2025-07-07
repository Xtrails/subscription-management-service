<template>
  <div>
    <h2 id="page-heading" data-cy="ReferralProgramHeading">
      <span v-text="t$('subscriptionManagementServiceApp.referralProgram.home.title')" id="referral-program-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('subscriptionManagementServiceApp.referralProgram.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ReferralProgramCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-referral-program"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('subscriptionManagementServiceApp.referralProgram.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && referralPrograms && referralPrograms.length === 0">
      <span v-text="t$('subscriptionManagementServiceApp.referralProgram.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="referralPrograms && referralPrograms.length > 0">
      <table class="table table-striped" aria-describedby="referralPrograms">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.referralProgram.name')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.referralProgram.referralCode')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.referralProgram.description')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.referralProgram.startDttm')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.referralProgram.endDttm')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.referralProgram.rewardAmount')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.referralProgram.status')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.referralProgram.sourceApplication')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="referralProgram in referralPrograms" :key="referralProgram.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ReferralProgramView', params: { referralProgramId: referralProgram.id } }">{{
                referralProgram.id
              }}</router-link>
            </td>
            <td>{{ referralProgram.name }}</td>
            <td>{{ referralProgram.referralCode }}</td>
            <td>{{ referralProgram.description }}</td>
            <td>{{ referralProgram.startDttm }}</td>
            <td>{{ referralProgram.endDttm }}</td>
            <td>{{ referralProgram.rewardAmount }}</td>
            <td v-text="t$('subscriptionManagementServiceApp.ReferralStatus.' + referralProgram.status)"></td>
            <td>
              <div v-if="referralProgram.sourceApplication">
                <router-link
                  :to="{ name: 'SourceApplicationView', params: { sourceApplicationId: referralProgram.sourceApplication.id } }"
                  >{{ referralProgram.sourceApplication.id }}</router-link
                >
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ReferralProgramView', params: { referralProgramId: referralProgram.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ReferralProgramEdit', params: { referralProgramId: referralProgram.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(referralProgram)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="subscriptionManagementServiceApp.referralProgram.delete.question"
          data-cy="referralProgramDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-referralProgram-heading"
          v-text="t$('subscriptionManagementServiceApp.referralProgram.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-referralProgram"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeReferralProgram()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./referral-program.component.ts"></script>
