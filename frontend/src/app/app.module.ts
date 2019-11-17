import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { DragDropModule } from '@angular/cdk/drag-drop';

import { AppConfig } from './app.config';
import { AppComponent } from './app.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { DigitSpanComponent } from './components/digit-span/digit-span.component';
import { AppRoutingModule } from './app-routing.module';
import { LoginComponent } from './components/login/login.component';
import { DigitSpanRunningComponent } from './components/digit-span-running/digit-span-running.component';
import { HomeComponent } from './components/home/home.component';
import { UserManagementComponent } from './components/admin/user-management/user-management.component';
import { AccessDeniedComponent } from './components/access-denied/access-denied.component';
import { LocationSpanComponent } from './components/location-span/location-span.component';
import { HeaderComponent } from './components/layouts/header/header.component';
import { LocationSpanRunningComponent } from './components/location-span-running/location-span-running.component';
import { VisualSpanComponent } from './components/visual-span/visual-span.component';
import { VisualSpanRunningComponent } from './components/visual-span-running/visual-span-running.component';
import { VisualBindingSpanComponent } from './components/visual-binding-span/visual-binding-span.component';
import { PhonologicalBindingComponent } from './components/phonological-binding/phonological-binding.component';
import { CrossModalBindingComponent } from './components/cross-modal-binding/cross-modal-binding.component';
import { NumberUpdateVisualComponent } from './components/number-update-visual/number-update-visual.component';
import { AudioRecordComponent } from './components/audio-record/audio-record.component';
import { DataUploadComponent } from './components/data-upload/data-upload.component';
import { NonWordComponent } from './components/non-word/non-word.component';
import { RepetitionAuditoryComponent } from './components/repetition-auditory/repetition-auditory.component';
import { NumberUpdateVisualThreeBoxesComponent } from './components/number-update-visual-three-boxes/number-update-visual-three-boxes.component';
import { RepetitionVisualComponent } from './components/repetition-visual/repetition-visual.component';
import { NumberUpdateAuditoryComponent } from './components/number-update-auditory/number-update-auditory.component';
import { StoreComponent } from './components/store/store.component';
import { HelpComponent } from './components/help/help.component';
import { DataUploadRedcapComponent } from './components/data-upload-redcap/data-upload-redcap.component';
import { VisualBindingSpanV2Component } from './components/visual-binding-span-v2/visual-binding-span-v2.component';
import { DigitSpanV2Component } from './components/digit-span-v2/digit-span-v2.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    DigitSpanComponent,
    LoginComponent,
    DigitSpanRunningComponent,
    HomeComponent,
    UserManagementComponent,
    AccessDeniedComponent,
    LocationSpanComponent,
    HeaderComponent,
    LocationSpanRunningComponent,
    VisualSpanComponent,
    VisualSpanRunningComponent,
    VisualBindingSpanComponent,
    PhonologicalBindingComponent,
    CrossModalBindingComponent,
    NumberUpdateVisualComponent,
    AudioRecordComponent,
    DataUploadComponent,
    NonWordComponent,
    RepetitionAuditoryComponent,
    NumberUpdateVisualThreeBoxesComponent,
    RepetitionVisualComponent,
    NumberUpdateAuditoryComponent,
    StoreComponent,
    HelpComponent,
    DataUploadRedcapComponent,
    VisualBindingSpanV2Component,
    DigitSpanV2Component,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    HttpModule,
    DragDropModule
  ],
  providers: [
    //{ provide: AppConfig, useValue: AppConfig }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
