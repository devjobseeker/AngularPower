import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { DigitSpanComponent } from './components/digit-span/digit-span.component';
import { DigitSpanV2Component } from './components/digit-span-v2/digit-span-v2.component';
import { DigitSpanRunningComponent}  from './components/digit-span-running/digit-span-running.component';
import { UserManagementComponent}  from './components/admin/user-management/user-management.component';
import { AccessDeniedComponent } from './components/access-denied/access-denied.component';
import { LocationSpanComponent } from './components/location-span/location-span.component';
import { LocationSpanRunningComponent } from './components/location-span-running/location-span-running.component';
import { VisualSpanComponent } from './components/visual-span/visual-span.component';
import { VisualSpanRunningComponent } from './components/visual-span-running/visual-span-running.component';
import { VisualBindingSpanComponent } from './components/visual-binding-span/visual-binding-span.component';
import { VisualBindingSpanV2Component } from './components/visual-binding-span-v2/visual-binding-span-v2.component';
import { CrossModalBindingComponent } from './components/cross-modal-binding/cross-modal-binding.component';
import { PhonologicalBindingComponent } from './components/phonological-binding/phonological-binding.component';
import { NumberUpdateVisualComponent } from './components/number-update-visual/number-update-visual.component';
import { NumberUpdateVisualThreeBoxesComponent } from './components/number-update-visual-three-boxes/number-update-visual-three-boxes.component';
import { NumberUpdateAuditoryComponent } from './components/number-update-auditory/number-update-auditory.component';
import { NonWordComponent } from './components/non-word/non-word.component';
import { RepetitionAuditoryComponent } from './components/repetition-auditory/repetition-auditory.component';
import { RepetitionVisualComponent } from './components/repetition-visual/repetition-visual.component';

import { StoreComponent } from './components/store/store.component';

import { HelpComponent } from './components/help/help.component';
import { AudioRecordComponent } from './components/audio-record/audio-record.component';
import { DataUploadRedcapComponent } from './components/data-upload-redcap/data-upload-redcap.component';
import { ChildAuthService as childAuthService} from './services/auth/child-auth.service';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: '', component: HomeComponent}, //for typing childId and experiemter
  {path: 'app/dashboard', component: DashboardComponent, canActivate: [childAuthService] },
  {path: 'app/digitspan', component: DigitSpanComponent, canActivate: [childAuthService] },
  {path: 'app/digitspanv2', component: DigitSpanV2Component },
  {path: 'app/digitspanrunning', component: DigitSpanRunningComponent, canActivate: [childAuthService] },
  {path: 'app/locationspan', component: LocationSpanComponent, canActivate: [childAuthService] },
  {path: 'app/locationspanrunning', component: LocationSpanRunningComponent, canActivate: [childAuthService]},
  {path: 'app/visualspan', component: VisualSpanComponent, canActivate: [childAuthService]},
  {path: 'app/visualspanrunning', component: VisualSpanRunningComponent, canActivate: [childAuthService]},
  {path: 'app/visualbinding', component: VisualBindingSpanV2Component, canActivate: [childAuthService]},
  //{path: 'app/visualbindingv2', component: VisualBindingSpanComponent},
  {path: 'app/crossmodalbinding', component: CrossModalBindingComponent, canActivate: [childAuthService]},
  {path: 'app/phonological', component: PhonologicalBindingComponent, canActivate: [childAuthService]},
  {path: 'app/numberupdatevisual', component: NumberUpdateVisualComponent, canActivate: [childAuthService]},
  //{path: 'app/numberupdatevisualv2', component: NumberUpdateVisualThreeBoxesComponent, canActivate: [childAuthService]},
  {path: 'app/numberupdateauditory', component: NumberUpdateAuditoryComponent, canActivate: [childAuthService]},
  {path: 'app/nonword', component: NonWordComponent, canActivate: [childAuthService]},
  {path: 'app/repetitionauditory', component: RepetitionAuditoryComponent, canActivate: [childAuthService]},
  {path: 'app/repetitionvisual', component: RepetitionVisualComponent, canActivate: [childAuthService]},
  {path: 'noaccess', component: AccessDeniedComponent},

  {path: 'app/store', component: StoreComponent, canActivate: [childAuthService]},
  
  {path: 'admin/user', component: UserManagementComponent},

  {path: 'app/help', component: HelpComponent},
  {path: 'app/recording/test', component: AudioRecordComponent},
  {path: 'app/upload/redcap', component: DataUploadRedcapComponent},
  //{path: 'app/upload', component: DataUploadComponent},

  //otherwise, redirect to homeComponent
  {path: '**', redirectTo: '/login'}
  
];

@NgModule({
  exports: [ RouterModule ],
  imports: [ RouterModule.forRoot(routes)]
})

export class AppRoutingModule { }
