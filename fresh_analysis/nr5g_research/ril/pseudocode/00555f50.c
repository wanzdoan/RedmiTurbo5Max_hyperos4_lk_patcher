
/* RmcPhbRequestHandler::requestReadUPBAnr(android::sp<RfxMclMessage> const&) */

void __thiscall RmcPhbRequestHandler::requestReadUPBAnr(RmcPhbRequestHandler *this,sp *param_1)

{
  long lVar1;
  long *plVar2;
  RfxAtResponse *pRVar3;
  int iVar4;
  undefined4 uVar5;
  RIL_PhbEntryStructure **__ptr;
  RIL_PhbEntryStructure *pRVar6;
  char *pcVar7;
  size_t sVar8;
  uint *puVar9;
  RfxAtLine *this_00;
  uint uVar10;
  char *__dest;
  char acStack_c0 [8];
  long *local_b8;
  long *local_b0;
  long *local_a8;
  long *local_a0;
  undefined8 local_98;
  int local_8c;
  RfxAtResponse *local_88;
  RfxAtResponse *local_80 [3];
  long local_68;
  
  lVar1 = tpidr_el0;
  local_68 = *(long *)(lVar1 + 0x28);
  local_88 = (RfxAtResponse *)0x0;
  local_8c = 0;
  local_98 = 0;
  android::String8::String8((String8 *)&local_98,"");
  local_a0 = (long *)0x0;
  puVar9 = *(uint **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  if (*(int *)(this + 0x14) != -1) {
    if (0 < *(int *)(this + 0x14)) goto LAB_00555fc8;
LAB_0055609c:
    __ptr = (RIL_PhbEntryStructure **)0x0;
    goto LAB_005560c8;
  }
  loadUPBCapability(this);
  if (*(int *)(this + 0x14) < 1) goto LAB_0055609c;
LAB_00555fc8:
  __ptr = calloc(1,8);
  if (__ptr == (RIL_PhbEntryStructure **)0x0) {
    pcVar7 = "entries calloc fail";
LAB_005560b8:
    RfxBaseHandler::logE((char *)this,"RmcPhbReq",pcVar7);
LAB_005560c8:
    uVar5 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxVoidData::RfxVoidData((RfxVoidData *)local_80);
    local_b8 = *(long **)param_1;
    if (local_b8 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_b8 + *(long *)(*local_b8 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_a8,uVar5,2,local_80,&local_b8,0);
    plVar2 = local_a0;
    if (local_a0 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_a0 + *(long *)(*local_a0 + -0x18)),&local_a0);
    }
    if (plVar2 != local_a0) {
      android::sp_report_race();
    }
    local_a0 = local_a8;
    local_a8 = (long *)0x0;
    if (local_b8 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_b8 + *(long *)(*local_b8 + -0x18)),&local_b8);
    }
    RfxVoidData::~RfxVoidData((RfxVoidData *)local_80);
    RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_a0);
    if (__ptr == (RIL_PhbEntryStructure **)0x0) goto LAB_00556190;
    pRVar6 = *__ptr;
  }
  else {
    pRVar6 = calloc(1,0x20);
    *__ptr = pRVar6;
    if (pRVar6 == (RIL_PhbEntryStructure *)0x0) {
      pcVar7 = "entry calloc fail";
      goto LAB_005560b8;
    }
    android::String8::format("AT+EPBUM=1,0,%d,%d",local_80,(ulong)*puVar9,(ulong)puVar9[1]);
    android::String8::append((String8 *)&local_98);
    android::String8::~String8((String8 *)local_80);
    RfxBaseHandler::atSendCommandSingleline((String8 *)this,(char *)&local_98,"+EPBUM:");
    pRVar3 = local_88;
    if (local_88 != (RfxAtResponse *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)(local_88 + *(long *)(*(long *)local_88 + -0x18)),&local_88);
    }
    if (pRVar3 != local_88) {
      android::sp_report_race();
    }
    local_88 = local_80[0];
    if ((local_80[0] == (RfxAtResponse *)0x0) || (*(int *)(local_80[0] + 0x28) < 0))
    goto LAB_005560c8;
    if (*(int *)(local_80[0] + 0xc) == 0) {
      iVar4 = RfxAtResponse::atGetCmeError(local_80[0]);
      if (iVar4 == 100) {
        RfxBaseHandler::logE((char *)this,"RmcPhbReq","p_response: CME_UNKNOWN");
      }
      uVar10 = 2;
    }
    else {
      uVar10 = 0;
    }
    RfxBaseHandler::logD((char *)this,"RmcPhbReq","requestReadUPBAnr ret: %d",(ulong)uVar10);
    if ((*(int *)(local_88 + 0xc) != 0) &&
       (this_00 = *(RfxAtLine **)(local_88 + 0x20), this_00 != (RfxAtLine *)0x0)) {
      RfxAtLine::atTokStart(this_00,&local_8c);
      if ((-1 < local_8c) &&
         (((RfxAtLine::atTokNextint(this_00,&local_8c), -1 < local_8c &&
           (RfxAtLine::atTokNextint(this_00,&local_8c), -1 < local_8c)) &&
          (RfxAtLine::atTokNextint(this_00,&local_8c), -1 < local_8c)))) {
        pcVar7 = (char *)RfxAtLine::atTokNextstr(this_00,&local_8c);
        sVar8 = strlen(pcVar7);
        __dest = acStack_c0 + -(sVar8 + 0x10 & 0xfffffffffffffff0);
        memset(__dest,0,sVar8 + 1);
        *(char **)(pRVar6 + 8) = __dest;
        sVar8 = strlen(pcVar7);
        strncpy(__dest,pcVar7,sVar8 + 1);
        if (-1 < local_8c) {
          uVar5 = RfxAtLine::atTokNextint(this_00,&local_8c);
          *(undefined4 *)(pRVar6 + 0x10) = uVar5;
          if (-1 < local_8c) {
            uVar5 = RfxAtLine::atTokNextint(this_00,&local_8c);
            uVar10 = 0;
            *(undefined1 **)(pRVar6 + 0x18) = &DAT_00207797;
            *(undefined4 *)pRVar6 = 0;
            *(undefined4 *)(pRVar6 + 4) = uVar5;
            goto LAB_00556334;
          }
        }
      }
      goto LAB_005560c8;
    }
LAB_00556334:
    uVar5 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxPhbEntriesData::RfxPhbEntriesData((RfxPhbEntriesData *)local_80,__ptr,1);
    local_b0 = *(long **)param_1;
    if (local_b0 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_b0 + *(long *)(*local_b0 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_a8,uVar5,uVar10,local_80,&local_b0,0);
    plVar2 = local_a0;
    if (local_a0 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_a0 + *(long *)(*local_a0 + -0x18)),&local_a0);
    }
    if (plVar2 != local_a0) {
      android::sp_report_race();
    }
    local_a0 = local_a8;
    local_a8 = (long *)0x0;
    if (local_b0 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_b0 + *(long *)(*local_b0 + -0x18)),&local_b0);
    }
    RfxPhbEntriesData::~RfxPhbEntriesData((RfxPhbEntriesData *)local_80);
    RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_a0);
    pRVar6 = *__ptr;
  }
  if (pRVar6 != (RIL_PhbEntryStructure *)0x0) {
    free(pRVar6);
  }
  free(__ptr);
LAB_00556190:
  if (local_a0 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_a0 + *(long *)(*local_a0 + -0x18)),&local_a0);
  }
  android::String8::~String8((String8 *)&local_98);
  if (local_88 != (RfxAtResponse *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)(local_88 + *(long *)(*(long *)local_88 + -0x18)),&local_88);
  }
  if (*(long *)(lVar1 + 0x28) == local_68) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


