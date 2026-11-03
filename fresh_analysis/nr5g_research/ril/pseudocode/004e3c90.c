
/* RmcCapabilitySwitchRequestHandler::requestSetRadioCapability(android::sp<RfxMclMessage> const&)
    */

void __thiscall
RmcCapabilitySwitchRequestHandler::requestSetRadioCapability
          (RmcCapabilitySwitchRequestHandler *this,sp *param_1)

{
  int iVar1;
  long lVar2;
  undefined *__mutex;
  long *plVar3;
  long *plVar4;
  byte bVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  int iVar9;
  uint uVar10;
  int iVar11;
  undefined4 uVar12;
  RfxMclStatusManager *pRVar13;
  long lVar14;
  uint uVar15;
  uint uVar16;
  ulong uVar17;
  uint *puVar18;
  long *local_110;
  long *local_108;
  long *local_100;
  long *local_f8;
  long *local_f0;
  long *local_e8;
  long *local_e0;
  uint local_d4;
  long *local_d0;
  ulong uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined4 uStack_88;
  undefined4 local_84;
  undefined4 uStack_80;
  undefined8 uStack_7c;
  long local_70;
  
  lVar2 = tpidr_el0;
  local_70 = *(long *)(lVar2 + 0x28);
  local_e8 = (long *)0x0;
  puVar18 = *(uint **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  pRVar13 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
  uVar6 = RfxMclStatusManager::getIntValue(pRVar13,0x34,0);
  iVar11 = *(int *)(this + 8);
  iVar1 = *(int *)(this + 0xc);
  local_f0 = (long *)0x0;
  bVar5 = RmcCapabilitySwitchUtil::isDssNoResetSupport();
  local_d4 = *puVar18;
  uVar7 = RfxChannelManager::getSupportChannels();
  RfxBaseHandler::logI
            ((char *)this,"RmcCapa","RadioCapability old_major_slot=%d, new_major_slot=%d",
             (ulong)uVar6,(ulong)*puVar18);
  if ((bVar5 & 1) == 0) {
    uVar17 = isVsimEnabled(this);
    if ((uVar17 & 1) != 0) {
LAB_004e4690:
      android::String8::format
                ("AT+ES3G = %d,%d",&local_d0,(ulong)(uint)(1 << (ulong)(*puVar18 & 0x1f)),3);
      RfxBaseHandler::atSendCommand((String8 *)this,&local_d0);
      if (local_110 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_110 + *(long *)(*local_110 + -0x18)),&local_110);
      }
      android::String8::~String8((String8 *)&local_d0);
      uVar7 = *puVar18;
      switchCapability(this,uVar6,uVar7);
      android::String8::format("%d",&local_d0,(ulong)(uVar7 + 1));
      rfx_property_set("persist.vendor.radio.simswitch",local_d0);
      android::String8::~String8((String8 *)&local_d0);
      pRVar13 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
      uStack_c8 = CONCAT44(uStack_c8._4_4_,uVar7);
      local_d0 = (long *)CONCAT44(local_d0._4_4_,2);
      RfxMclStatusManager::setValue(pRVar13,0x34,(RfxVariant *)&local_d0,false,false);
      RfxVariant::deinit((RfxVariant *)&local_d0);
      rfx_property_set("vendor.gsm.ril.eboot",&DAT_002240c5);
      resetRadio(this);
      goto joined_r0x004e4974;
    }
    uStack_7c = 0;
    uStack_80 = 0;
    uStack_b8 = 0;
    local_c0 = 0;
    uStack_a8 = 0;
    uStack_b0 = 0;
    uStack_98 = 0;
    local_a0 = 0;
    uStack_88 = 0;
    local_84 = 0;
    uStack_90 = 0;
    uStack_c8 = 0;
    local_d0 = (long *)0x0;
    iVar9 = RfxRilUtils::rfxGetSimCount();
    if (iVar9 != 0) {
      uVar8 = 0;
      do {
        getMSimProperty(uVar8,"persist.vendor.radio.external.sim",&local_d0);
        iVar9 = atoi((char *)&local_d0);
        if (0 < iVar9) {
          RfxBaseHandler::logD((char *)this,"RmcCapa","isPersistVsim=%d",1);
          goto LAB_004e4690;
        }
        uVar8 = uVar8 + 1;
        uVar10 = RfxRilUtils::rfxGetSimCount();
      } while (uVar8 < uVar10);
    }
    RfxBaseHandler::logD((char *)this,"RmcCapa","isPersistVsim=%d",0);
    uVar8 = RfxRilUtils::rfxGetSimCount();
    if (0 < (int)uVar8) {
      uVar10 = 0;
      do {
        pRVar13 = (RfxMclStatusManager *)
                  RfxBaseHandler::getMclStatusManager((RfxBaseHandler *)this,uVar10);
        local_d0 = (long *)CONCAT44(local_d0._4_4_,2);
        uStack_c8 = uStack_c8 & 0xffffffff00000000;
        RfxMclStatusManager::setValue(pRVar13,0x4d,(RfxVariant *)&local_d0,false,false);
        RfxVariant::deinit((RfxVariant *)&local_d0);
        uVar10 = uVar10 + 1;
      } while (uVar8 != uVar10);
    }
    android::String8::format("AT+EFUN=0,%d",&local_f8,3);
    RfxBaseHandler::atSendCommand((String8 *)this,&local_f8);
    plVar3 = local_f0;
    if (local_f0 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_f0 + *(long *)(*local_f0 + -0x18)),&local_f0);
    }
    if (plVar3 != local_f0) {
      android::sp_report_race();
    }
    local_f0 = local_d0;
    local_d0 = (long *)0x0;
    android::String8::~String8((String8 *)&local_f8);
    if ((local_f0 != (long *)0x0) && (*(int *)((long)local_f0 + 0xc) == 0)) {
      uVar12 = *(undefined4 *)(*(long *)param_1 + 0xc);
      RfxIntsData::RfxIntsData((RfxIntsData *)&local_d0,(int *)&local_d4,1);
      local_100 = *(long **)param_1;
      if (local_100 != (long *)0x0) {
        android::RefBase::incStrong((void *)((long)local_100 + *(long *)(*local_100 + -0x18)));
      }
      RfxMclMessage::obtainResponse((RfxMclMessage *)&local_f8,uVar12,2,&local_d0,&local_100,1);
      plVar3 = local_e8;
      if (local_e8 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_e8 + *(long *)(*local_e8 + -0x18)),&local_e8);
      }
      if (plVar3 != local_e8) {
        android::sp_report_race();
      }
      local_e8 = local_f8;
      local_f8 = (long *)0x0;
      if (local_100 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_100 + *(long *)(*local_100 + -0x18)),&local_100);
      }
      RfxIntsData::~RfxIntsData((RfxIntsData *)&local_d0);
      RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_e8);
      goto joined_r0x004e4974;
    }
    RfxBaseHandler::atSendCommand((char *)this,"AT+EBOOT=1");
    plVar3 = local_f0;
    if (local_f0 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_f0 + *(long *)(*local_f0 + -0x18)),&local_f0);
    }
    if (plVar3 != local_f0) {
      android::sp_report_race();
    }
    local_f0 = local_d0;
  }
  else {
    uVar8 = RfxRilUtils::rfxGetSimCount();
  }
  uVar10 = iVar1 + iVar11 * 0xc;
  iVar11 = RfxRilUtils::getRilRunMode();
  if (iVar11 != 1) {
    if (7 < (int)uVar7) {
      uVar17 = 7;
      do {
        RfxBaseHandler::logD((char *)this,"RmcCapa","lock mutex %d!",uVar17);
        lVar14 = RfxChannelManager::getChannel((int)uVar17);
        lVar14 = *(long *)(lVar14 + 0x20);
        *(undefined1 *)(lVar14 + 0xe8) = 1;
        pthread_mutex_lock((pthread_mutex_t *)(lVar14 + 0x50));
        pthread_mutex_lock((pthread_mutex_t *)(lVar14 + 0x28));
        uVar16 = (int)uVar17 + 0xc;
        uVar17 = (ulong)uVar16;
      } while ((int)uVar16 < (int)uVar7);
    }
    if ((int)uVar7 < 1) {
      if ((bVar5 & 1) == 0) goto LAB_004e4078;
    }
    else {
      if (uVar7 != 1) {
        if (uVar10 != 1) {
          RfxBaseHandler::logI((char *)this,"RmcCapa","lock mutex %d!",1);
          lVar14 = RfxChannelManager::getChannel(1);
          lVar14 = *(long *)(lVar14 + 0x20);
          *(undefined1 *)(lVar14 + 0xe8) = 1;
          pthread_mutex_lock((pthread_mutex_t *)(lVar14 + 0x50));
          pthread_mutex_lock((pthread_mutex_t *)(lVar14 + 0x28));
        }
        if (uVar7 != 2) {
          uVar16 = 2;
          do {
            if ((((uVar10 != uVar16) &&
                 (uVar15 = uVar16 / 0xc, iVar11 = uVar16 + uVar15 * -0xc + 2,
                 iVar11 != 2 && iVar11 != 0xd)) && (uVar16 + uVar15 * -0xc + -5 != 2)) &&
               ((bVar5 & uVar16 + uVar15 * -0xc + -8 == 2) == 0)) {
              RfxBaseHandler::logD((char *)this,"RmcCapa","lock mutex %d!",(ulong)uVar16);
              lVar14 = RfxChannelManager::getChannel(uVar16);
              lVar14 = *(long *)(lVar14 + 0x20);
              *(undefined1 *)(lVar14 + 0xe8) = 1;
              pthread_mutex_lock((pthread_mutex_t *)(lVar14 + 0x50));
              pthread_mutex_lock((pthread_mutex_t *)(lVar14 + 0x28));
            }
            uVar16 = uVar16 + 1;
          } while (uVar7 != uVar16);
        }
      }
      if ((bVar5 & 1) == 0) {
        if (0 < (int)uVar7) {
          iVar11 = 0;
          do {
            lVar14 = RfxChannelManager::getChannel(iVar11);
            lVar14 = *(long *)(lVar14 + 0x20);
            *(undefined1 *)(lVar14 + 0xe8) = 1;
            pthread_mutex_lock((pthread_mutex_t *)(lVar14 + 0x50));
            pthread_mutex_lock((pthread_mutex_t *)(lVar14 + 0x28));
            iVar11 = iVar11 + 0xc;
          } while (iVar11 < (int)uVar7);
        }
LAB_004e4078:
        RfxBaseHandler::logI((char *)this,"RmcCapa","lock mutex done!");
        switchChannel(0,uVar6,*puVar18);
        goto LAB_004e40a8;
      }
    }
    RfxBaseHandler::logI((char *)this,"RmcCapa","lock mutex done!");
    pRVar13 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
    local_d0 = (long *)CONCAT44(local_d0._4_4_,1);
    uStack_c8 = CONCAT71(uStack_c8._1_7_,1);
    RfxMclStatusManager::setValue(pRVar13,100,(RfxVariant *)&local_d0,false,false);
    RfxVariant::deinit((RfxVariant *)&local_d0);
  }
LAB_004e40a8:
  local_f8 = (long *)0x0;
  android::String8::String8((String8 *)&local_f8);
  __mutex = PTR_s_sim_init_state_mutex_005d41e8;
  pthread_mutex_lock((pthread_mutex_t *)PTR_s_sim_init_state_mutex_005d41e8);
  *(undefined4 *)PTR_s_sim_init_state_005d41f0 = 0;
  pthread_mutex_unlock((pthread_mutex_t *)__mutex);
  if ((int)uVar8 < 3) {
    android::String8::format("AT+ESIMMAP=%d",&local_d0,(ulong)(uint)(1 << (ulong)(*puVar18 & 0x1f)))
    ;
    android::String8::setTo((String8 *)&local_f8);
    android::String8::~String8((String8 *)&local_d0);
  }
  else {
    android::String8::format("AT+EPSMAP=%d,%d",&local_d0,(ulong)uVar8,(ulong)(*puVar18 + 1));
    android::String8::setTo((String8 *)&local_f8);
    android::String8::~String8((String8 *)&local_d0);
    uVar16 = 0;
    do {
      uVar15 = uVar16 + 1;
      if (uVar16 != *puVar18) {
        android::String8::appendFormat((char *)&local_f8,&DAT_002497d2,(ulong)uVar15);
      }
      uVar16 = uVar15;
    } while (uVar8 != uVar15);
  }
  RfxBaseHandler::atSendCommand((String8 *)this,&local_f8);
  plVar3 = local_f0;
  if (local_f0 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_f0 + *(long *)(*local_f0 + -0x18)),&local_f0);
  }
  plVar4 = local_d0;
  if (plVar3 != local_f0) {
    android::sp_report_race();
    plVar4 = local_d0;
  }
  while ((local_f0 = plVar4, local_d0 = local_f0, local_f0 != (long *)0x0 &&
         (*(int *)((long)local_f0 + 0xc) == 0))) {
    RfxBaseHandler::logE
              ((char *)this,"RmcCapa","at cmd returns ERROR:%d, retry",
               (ulong)*(uint *)(local_f0 + 5));
    usleep(200000);
    RfxBaseHandler::atSendCommand((String8 *)this,&local_f8);
    plVar3 = local_f0;
    if (local_f0 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_f0 + *(long *)(*local_f0 + -0x18)),&local_f0);
    }
    plVar4 = local_d0;
    if (plVar3 != local_f0) {
      android::sp_report_race();
      plVar4 = local_d0;
    }
  }
  while( true ) {
    pRVar13 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
    uVar17 = RfxMclStatusManager::getBoolValue(pRVar13,100,false);
    if ((uVar17 & 1) == 0) break;
    usleep(100000);
  }
  iVar11 = RfxRilUtils::getRilRunMode();
  if (iVar11 == 1) {
    uVar7 = *puVar18;
    switchCapability(this,uVar6,uVar7);
    android::String8::format("%d",&local_d0,(ulong)(uVar7 + 1));
    rfx_property_set("persist.vendor.radio.simswitch",local_d0);
    android::String8::~String8((String8 *)&local_d0);
    pRVar13 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
    uStack_c8 = CONCAT44(uStack_c8._4_4_,uVar7);
    local_d0 = (long *)CONCAT44(local_d0._4_4_,2);
    RfxMclStatusManager::setValue(pRVar13,0x34,(RfxVariant *)&local_d0,false,false);
    RfxVariant::deinit((RfxVariant *)&local_d0);
LAB_004e4528:
    uVar12 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxIntsData::RfxIntsData((RfxIntsData *)&local_d0,(int *)&local_d4,1);
    local_108 = *(long **)param_1;
    if (local_108 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_108 + *(long *)(*local_108 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_e0,uVar12,0,&local_d0,&local_108,1);
    plVar3 = local_e8;
    if (local_e8 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_e8 + *(long *)(*local_e8 + -0x18)),&local_e8);
    }
    if (plVar3 != local_e8) {
      android::sp_report_race();
    }
    local_e8 = local_e0;
    local_e0 = (long *)0x0;
    if (local_108 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_108 + *(long *)(*local_108 + -0x18)),&local_108);
    }
    RfxIntsData::~RfxIntsData((RfxIntsData *)&local_d0);
    RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_e8);
    if (0 < (int)uVar8) {
      uVar6 = 0;
      do {
        local_e0 = (long *)((ulong)local_e0 & 0xffffffff00000000);
        pRVar13 = (RfxMclStatusManager *)
                  RfxBaseHandler::getMclStatusManager((RfxBaseHandler *)this,uVar6);
        uVar12 = RfxMclStatusManager::getIntValue(pRVar13,0x2d,0);
        local_e0 = (long *)CONCAT44(local_e0._4_4_,uVar12);
        RfxIntsData::RfxIntsData((RfxIntsData *)&local_d0,(int *)&local_e0,1);
        RfxBaseHandler::sendEvent
                  ((RfxBaseHandler *)this,0xcb26,&local_d0,8,uVar6,0xffffffff,0xffffffff,0,1);
        RfxIntsData::~RfxIntsData((RfxIntsData *)&local_d0);
        uVar6 = uVar6 + 1;
      } while (uVar8 != uVar6);
    }
  }
  else {
    queryActiveMode(this);
    uVar16 = *puVar18;
    switchCapability(this,uVar6,uVar16);
    android::String8::format("%d",&local_d0,(ulong)(uVar16 + 1));
    rfx_property_set("persist.vendor.radio.simswitch",local_d0);
    android::String8::~String8((String8 *)&local_d0);
    pRVar13 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
    uStack_c8 = CONCAT44(uStack_c8._4_4_,uVar16);
    local_d0 = (long *)CONCAT44(local_d0._4_4_,2);
    RfxMclStatusManager::setValue(pRVar13,0x34,(RfxVariant *)&local_d0,false,false);
    RfxVariant::deinit((RfxVariant *)&local_d0);
    if (((bVar5 & 1) == 0) || (uVar17 = waitSIMInitDone(this), (uVar17 & 1) != 0)) {
      switchChannel(1,uVar6,*puVar18);
      switchChannel(2,uVar6,*puVar18);
      switchChannel(3,uVar6,*puVar18);
      switchChannel(4,uVar6,*puVar18);
      switchChannel(5,uVar6,*puVar18);
      switchChannel(6,uVar6,*puVar18);
      switchChannel(7,uVar6,*puVar18);
      switchChannel(8,uVar6,*puVar18);
      switchChannel(9,uVar6,*puVar18);
      if ((bVar5 & 1) == 0) {
        switchChannel(10,uVar6,*puVar18);
      }
      RfxBaseHandler::logD
                ((char *)this,"RmcCapa","switchChannel done! old:%d,new:%d",(ulong)uVar6,
                 (ulong)*puVar18);
      if (0 < (int)uVar7) {
        uVar6 = 0;
        do {
          uVar16 = uVar6 % 0xc;
          if (((uVar16 != 0 && uVar16 != 0xb) && (uVar10 != uVar6)) && ((bVar5 & uVar16 == 10) == 0)
             ) {
            lVar14 = RfxChannelManager::getChannel(uVar6);
            lVar14 = *(long *)(lVar14 + 0x20);
            *(undefined1 *)(lVar14 + 0xe8) = 0;
            pthread_mutex_unlock((pthread_mutex_t *)(lVar14 + 0x28));
            pthread_cond_signal((pthread_cond_t *)(lVar14 + 0xa8));
            pthread_mutex_unlock((pthread_mutex_t *)(lVar14 + 0x50));
          }
          uVar6 = uVar6 + 1;
        } while (uVar7 != uVar6);
        if ((bVar5 & 1) == 0) {
          iVar11 = 0;
          do {
            lVar14 = RfxChannelManager::getChannel(iVar11);
            lVar14 = *(long *)(lVar14 + 0x20);
            *(undefined1 *)(lVar14 + 0xe8) = 0;
            pthread_mutex_unlock((pthread_mutex_t *)(lVar14 + 0x28));
            pthread_cond_signal((pthread_cond_t *)(lVar14 + 0xa8));
            pthread_mutex_unlock((pthread_mutex_t *)(lVar14 + 0x50));
            iVar11 = iVar11 + 0xc;
          } while (iVar11 < (int)uVar7);
        }
      }
      goto LAB_004e4528;
    }
  }
  android::String8::~String8((String8 *)&local_f8);
joined_r0x004e4974:
  if (local_f0 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_f0 + *(long *)(*local_f0 + -0x18)),&local_f0);
  }
  if (local_e8 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_e8 + *(long *)(*local_e8 + -0x18)),&local_e8);
  }
  if (*(long *)(lVar2 + 0x28) == local_70) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


