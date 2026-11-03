
/* RmcNrSwitchHandler::requestNrSwitch(android::sp<RfxMclMessage> const&) */

void __thiscall RmcNrSwitchHandler::requestNrSwitch(RmcNrSwitchHandler *this,sp *param_1)

{
  undefined4 uVar1;
  byte bVar2;
  long lVar3;
  uint uVar4;
  uint uVar5;
  int iVar6;
  RfxMclStatusManager *pRVar7;
  ulong uVar8;
  int iVar9;
  RfxAtResponse **ppRVar10;
  undefined1 uVar11;
  void *pvVar12;
  long lVar13;
  long *plVar14;
  RfxAtResponse *pRVar15;
  undefined1 uVar16;
  long lVar17;
  undefined4 uVar18;
  undefined7 in_stack_ffffffffffffff61;
  undefined1 in_stack_ffffffffffffff68;
  RfxAtResponse *local_90;
  RfxAtResponse *local_88;
  RfxAtResponse *local_80;
  ulong uStack_78;
  char acStack_70 [8];
  char acStack_68 [5];
  char local_63 [3];
  char acStack_60 [24];
  long local_48;
  
  lVar3 = tpidr_el0;
  local_48 = *(long *)(lVar3 + 0x28);
  local_88 = (RfxAtResponse *)0x0;
  uVar4 = getNrPsSlot(this);
  if (uVar4 == 0xffffffff) {
    UpdateAPNrSlot(this);
    uVar18 = 0;
LAB_004a0378:
    *(undefined4 *)(this + 0x18) = 1;
    pRVar7 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
    local_80 = (RfxAtResponse *)CONCAT44(local_80._4_4_,1);
    uStack_78 = uStack_78 & 0xffffffffffffff00;
    RfxMclStatusManager::setValue(pRVar7,0x56,(RfxVariant *)&local_80,false,false);
    RfxVariant::deinit((RfxVariant *)&local_80);
    local_90 = (RfxAtResponse *)0x0;
    uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxVoidData::RfxVoidData((RfxVoidData *)&local_80);
    plVar14 = *(long **)param_1;
    if (plVar14 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)plVar14 + *(long *)(*plVar14 + -0x18)));
    }
    RfxMclMessage::obtainResponse
              ((RfxMclMessage *)&local_90,uVar1,uVar18,&local_80,&stack0xffffffffffffff68,0);
    if (plVar14 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)plVar14 + *(long *)(*plVar14 + -0x18)),&stack0xffffffffffffff68);
    }
    RfxVoidData::~RfxVoidData((RfxVoidData *)&local_80);
    RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_90);
    if (local_90 == (RfxAtResponse *)0x0) goto LAB_004a0448;
    lVar17 = *(long *)local_90;
    ppRVar10 = &local_90;
    pRVar15 = local_90;
  }
  else {
    pRVar7 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
    local_80 = (RfxAtResponse *)CONCAT44(local_80._4_4_,1);
    uStack_78 = CONCAT71(uStack_78._1_7_,'\x01');
    RfxMclStatusManager::setValue(pRVar7,0x56,(RfxVariant *)&local_80,false,false);
    RfxVariant::deinit((RfxVariant *)&local_80);
    android::String8::format("AT+EPSCONFIG=1,\"nr_ps\",%d",&local_90,(ulong)(uVar4 + 1));
    RfxBaseHandler::atSendCommand((String8 *)this,&local_90);
    pRVar15 = local_88;
    if (local_88 != (RfxAtResponse *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)(local_88 + *(long *)(*(long *)local_88 + -0x18)),&local_88);
    }
    if (pRVar15 != local_88) {
      android::sp_report_race();
    }
    local_88 = local_80;
    local_80 = (RfxAtResponse *)0x0;
    android::String8::~String8((String8 *)&local_90);
    if ((*(int *)(local_88 + 0x28) < 0) || (*(int *)(local_88 + 0xc) == 0)) {
      uVar4 = *(uint *)(this + 0x18);
      uVar8 = RfxAtResponse::atGetCmeError(local_88);
      RfxBaseHandler::logE
                ((char *)this,"RmcNrSwHdlr","requestNrSwitch fail, count=%d, error=%d",(ulong)uVar4,
                 uVar8 & 0xffffffff);
      iVar6 = RfxAtResponse::atGetCmeError(local_88);
      if (iVar6 == 3) {
        RfxBaseHandler::logE
                  ((char *)this,"RmcNrSwHdlr","requestNrSwitch: fail, modem report no retry");
        uVar18 = 2;
        pRVar15 = local_88;
        goto joined_r0x004a0254;
      }
      iVar6 = *(int *)(this + 0x18);
      if (0xe1 < iVar6) {
        uStack_78._0_1_ = s_requestNrSwitch__retry_max_asser_0020e729[8];
        uStack_78._1_1_ = s_requestNrSwitch__retry_max_asser_0020e729[9];
        uStack_78._2_1_ = s_requestNrSwitch__retry_max_asser_0020e729[10];
        uStack_78._3_1_ = s_requestNrSwitch__retry_max_asser_0020e729[0xb];
        uStack_78._4_1_ = s_requestNrSwitch__retry_max_asser_0020e729[0xc];
        uStack_78._5_1_ = s_requestNrSwitch__retry_max_asser_0020e729[0xd];
        uStack_78._6_1_ = s_requestNrSwitch__retry_max_asser_0020e729[0xe];
        uStack_78._7_1_ = s_requestNrSwitch__retry_max_asser_0020e729[0xf];
        local_80 = (RfxAtResponse *)s_requestNrSwitch__retry_max_asser_0020e729._0_8_;
        acStack_68[0] = s_requestNrSwitch__retry_max_asser_0020e729[0x18];
        acStack_68[1] = s_requestNrSwitch__retry_max_asser_0020e729[0x19];
        acStack_68[2] = s_requestNrSwitch__retry_max_asser_0020e729[0x1a];
        acStack_68[3] = s_requestNrSwitch__retry_max_asser_0020e729[0x1b];
        acStack_68[4] = s_requestNrSwitch__retry_max_asser_0020e729[0x1c];
        acStack_70[0] = s_requestNrSwitch__retry_max_asser_0020e729[0x10];
        acStack_70[1] = s_requestNrSwitch__retry_max_asser_0020e729[0x11];
        acStack_70[2] = s_requestNrSwitch__retry_max_asser_0020e729[0x12];
        acStack_70[3] = s_requestNrSwitch__retry_max_asser_0020e729[0x13];
        acStack_70[4] = s_requestNrSwitch__retry_max_asser_0020e729[0x14];
        acStack_70[5] = s_requestNrSwitch__retry_max_asser_0020e729[0x15];
        acStack_70[6] = s_requestNrSwitch__retry_max_asser_0020e729[0x16];
        acStack_70[7] = s_requestNrSwitch__retry_max_asser_0020e729[0x17];
        local_63[0] = s_requestNrSwitch__retry_max_asser_0020e729[0x1d];
        local_63[1] = s_requestNrSwitch__retry_max_asser_0020e729[0x1e];
        local_63[2] = s_requestNrSwitch__retry_max_asser_0020e729[0x1f];
        acStack_60[0] = s_requestNrSwitch__retry_max_asser_0020e729[0x20];
        acStack_60[1] = s_requestNrSwitch__retry_max_asser_0020e729[0x21];
        acStack_60[2] = s_requestNrSwitch__retry_max_asser_0020e729[0x22];
        acStack_60[3] = s_requestNrSwitch__retry_max_asser_0020e729[0x23];
        acStack_60[4] = s_requestNrSwitch__retry_max_asser_0020e729[0x24];
        mtkAssert(&local_80);
        goto LAB_004a0344;
      }
      *(int *)(this + 0x18) = iVar6 + 1;
      lVar17 = *(long *)param_1;
      pvVar12 = (void *)0x17d78400;
      if (0x31 < iVar6) {
        pvVar12 = (void *)0x12a05f200;
      }
      uVar4 = *(uint *)(lVar17 + 0xc);
      iVar9 = (int)*(undefined8 *)(lVar17 + 0x10);
      uVar5 = *(uint *)(lVar17 + 0x1c);
      iVar6 = *(int *)(lVar17 + 0x24);
      bVar2 = *(byte *)(lVar17 + 0xa3);
      uVar11 = (undefined1)*(undefined8 *)(lVar17 + 0x60);
      lVar13 = *(long *)(lVar17 + 0xa8);
      uVar16 = *(undefined1 *)(lVar17 + 0xb0);
    }
    else {
      uVar8 = getNrPsSlot(this);
      RfxBaseHandler::logV
                ((char *)this,"RmcNrSwHdlr","requestNrSwitch success, slot:%d, getNrPsSlot:%d",
                 (ulong)uVar4,uVar8 & 0xffffffff);
      uVar5 = getNrPsSlot(this);
      if (uVar4 == uVar5) {
        UpdateAPNrSlot(this);
LAB_004a0344:
        uVar18 = 0;
        pRVar15 = local_88;
joined_r0x004a0254:
        local_88 = pRVar15;
        if (pRVar15 != (RfxAtResponse *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)(pRVar15 + *(long *)(*(long *)pRVar15 + -0x18)),&local_88);
        }
        if (pRVar15 != local_88) {
          android::sp_report_race();
        }
        local_88 = (RfxAtResponse *)0x0;
        goto LAB_004a0378;
      }
      lVar17 = *(long *)param_1;
      pvVar12 = (void *)0x989680;
      uVar4 = *(uint *)(lVar17 + 0xc);
      iVar9 = (int)*(undefined8 *)(lVar17 + 0x10);
      uVar5 = *(uint *)(lVar17 + 0x1c);
      iVar6 = *(int *)(lVar17 + 0x24);
      bVar2 = *(byte *)(lVar17 + 0xa3);
      uVar11 = (undefined1)*(undefined8 *)(lVar17 + 0x60);
      lVar13 = *(long *)(lVar17 + 0xa8);
      uVar16 = *(undefined1 *)(lVar17 + 0xb0);
    }
    local_80 = (RfxAtResponse *)0x0;
    RfxMclMessage::obtainRequest
              ((RfxMclMessage *)(ulong)uVar4,iVar9,(RfxBaseData *)(ulong)uVar5,iVar6,(uint)bVar2,
               (bool)uVar11,pvVar12,lVar13,CONCAT71(in_stack_ffffffffffffff61,uVar16),
               (bool)in_stack_ffffffffffffff68);
    RfxMclDispatcherThread::enqueueMclMessageDelay((sp *)&local_80);
    if (local_80 == (RfxAtResponse *)0x0) goto LAB_004a0448;
    lVar17 = *(long *)local_80;
    ppRVar10 = &local_80;
    pRVar15 = local_80;
  }
  android::RefBase::decStrong((RefBase *)(pRVar15 + *(long *)(lVar17 + -0x18)),ppRVar10);
LAB_004a0448:
  if (local_88 != (RfxAtResponse *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)(local_88 + *(long *)(*(long *)local_88 + -0x18)),&local_88);
  }
  if (*(long *)(lVar3 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


