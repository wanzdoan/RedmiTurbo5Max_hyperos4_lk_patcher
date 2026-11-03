
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* RmcNetworkNrtRequestHandler::handleSimulatedNetworkScanResultByRat(int) */

undefined4 __thiscall
RmcNetworkNrtRequestHandler::handleSimulatedNetworkScanResultByRat
          (RmcNetworkNrtRequestHandler *this,int param_1)

{
  int iVar1;
  char cVar2;
  long lVar3;
  bool bVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  long *plVar9;
  undefined4 uVar10;
  int iVar11;
  undefined4 *__ptr;
  size_t sVar12;
  void *pvVar13;
  long lVar14;
  char *__nptr;
  ulong extraout_x1;
  RfxBaseData *pRVar15;
  uint uVar16;
  uint uVar17;
  ulong uVar18;
  int iVar19;
  undefined4 uVar20;
  RfxAtLine *this_00;
  char *pcVar21;
  long lVar22;
  long *local_188;
  int local_17c;
  long *local_178;
  long *local_170;
  long *local_168 [3];
  undefined8 local_150;
  undefined8 uStack_148;
  undefined8 local_140;
  undefined8 uStack_138;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 uStack_110;
  undefined4 uStack_108;
  undefined4 local_104;
  undefined4 uStack_100;
  undefined8 uStack_fc;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined4 uStack_a8;
  undefined4 local_a4;
  undefined4 uStack_a0;
  undefined8 uStack_9c;
  undefined8 local_90;
  long local_88;
  
  lVar3 = tpidr_el0;
  local_88 = *(long *)(lVar3 + 0x28);
  local_170 = (long *)0x0;
  __ptr = calloc(1,0x10);
  if (__ptr == (undefined4 *)0x0) {
    RfxBaseHandler::logE
              ((char *)this,"RmcNwReqHdlr","handleSimulatedNetworkScanResultByRat result = null");
    uVar20 = 0;
    goto LAB_00494cc0;
  }
  *__ptr = 1;
  local_178 = (long *)0x0;
  local_17c = 0;
  local_90 = 0;
  uStack_9c = 0;
  uStack_a0 = 0;
  uStack_d8 = 0;
  local_e0 = 0;
  uStack_c8 = 0;
  local_d0 = 0;
  uStack_b8 = 0;
  local_c0 = 0;
  uStack_a8 = 0;
  local_a4 = 0;
  uStack_b0 = 0;
  uStack_e8 = 0;
  local_f0 = 0;
  uStack_fc = 0;
  uStack_100 = 0;
  uStack_138 = 0;
  local_140 = 0;
  uStack_128 = 0;
  local_130 = 0;
  uStack_118 = 0;
  local_120 = 0;
  uStack_108 = 0;
  local_104 = 0;
  uStack_110 = 0;
  uStack_148 = 0;
  local_150 = 0;
  android::String8::format("AT+EPLWSS=%d, 50, 0",&local_188,(ulong)(uint)param_1);
  pRVar15 = (RfxBaseData *)0x0;
  RfxBaseHandler::atSendCommandSingleline((String8 *)this,(char *)&local_188,"+EPLWSS:");
  plVar9 = local_178;
  if (local_178 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_178 + *(long *)(*local_178 + -0x18)),&local_178);
  }
  if (plVar9 != local_178) {
    android::sp_report_race();
  }
  local_178 = local_168[0];
  local_168[0] = (long *)0x0;
  android::String8::~String8((String8 *)&local_188);
  local_17c = (int)local_178[5];
  if ((local_17c < 0) || (*(int *)((long)local_178 + 0xc) == 0)) {
LAB_00494be0:
    uVar20 = 0;
    uVar16 = __ptr[1];
  }
  else {
    this_00 = (RfxAtLine *)local_178[4];
    pcVar21 = *(char **)this_00;
    sVar12 = strlen(pcVar21);
    if ((int)sVar12 < 1) {
      uVar16 = 0;
    }
    else {
      bVar4 = false;
      uVar16 = 0;
      uVar18 = sVar12 & 0xffffffff;
      do {
        cVar2 = *pcVar21;
        if (cVar2 == '\"') {
          bVar4 = (bool)(bVar4 ^ 1);
        }
        else if (bVar4) {
          bVar4 = true;
        }
        else {
          if (cVar2 == ',') {
            if (pcVar21[1] == ',') break;
          }
          else if (cVar2 == '(') {
            bVar4 = false;
            uVar16 = uVar16 + 1;
            goto LAB_004946e4;
          }
          bVar4 = false;
        }
LAB_004946e4:
        uVar18 = uVar18 - 1;
        pcVar21 = pcVar21 + 1;
      } while (uVar18 != 0);
    }
    __ptr[1] = uVar16;
    pvVar13 = calloc((long)(int)uVar16,0x90);
    *(void **)(__ptr + 2) = pvVar13;
    if (pvVar13 == (void *)0x0) goto LAB_00494be0;
    pcVar21 = (char *)((long)(int)uVar16 * 0x90);
    memset(pvVar13,0,(size_t)pcVar21);
    RfxAtLine::atTokStart(this_00,&local_17c);
    uVar8 = _UNK_002565e8;
    uVar7 = _DAT_002565e0;
    uVar6 = _UNK_00256578;
    uVar5 = _DAT_00256570;
    if (local_17c < 0) goto LAB_00494be0;
    if (0 < (int)uVar16) {
      lVar22 = 0;
      do {
        local_90 = 0;
        uStack_e8 = 0;
        local_f0 = 0;
        uStack_d8 = 0;
        local_e0 = 0;
        uStack_c8 = 0;
        local_d0 = 0;
        uStack_b8 = 0;
        local_c0 = 0;
        uStack_a8 = 0;
        uStack_b0 = 0;
        uStack_9c = 0;
        local_a4 = 0;
        uStack_a0 = 0;
        uStack_148 = 0;
        local_150 = 0;
        uStack_138 = 0;
        local_140 = 0;
        uStack_128 = 0;
        local_130 = 0;
        uStack_118 = 0;
        local_120 = 0;
        uStack_108 = 0;
        uStack_110 = 0;
        uStack_fc = 0;
        local_104 = 0;
        uStack_100 = 0;
        lVar14 = RfxAtLine::atTokNextstr(this_00,&local_17c);
        if (local_17c < 0) goto LAB_00494be0;
        uVar17 = (uint)(*(char *)(lVar14 + 1) == '2');
        *(uint *)((long)pvVar13 + lVar22 + 4) = uVar17;
        pvVar13 = *(void **)(__ptr + 2);
        *(uint *)((long)pvVar13 + lVar22 + 0x88) = uVar17;
        RfxAtLine::atTokNextstr(this_00,&local_17c);
        if (((local_17c < 0) || (RfxAtLine::atTokNextstr(this_00,&local_17c), local_17c < 0)) ||
           (RfxAtLine::atTokNextstr(this_00,&local_17c), local_17c < 0)) goto LAB_00494be0;
        local_17c = ::snprintf((char *)&local_90,extraout_x1,pcVar21);
        if (local_17c < 0) {
          RfxBaseHandler::logE((char *)this,"RmcNwReqHdlr","snprintf mccmnc fail");
        }
        __nptr = (char *)RfxAtLine::atTokNextstr(this_00,&local_17c);
        if (local_17c < 0) {
          pcVar21 = "No <lac> in +EPLWSS response";
LAB_00494bd8:
          RfxBaseHandler::logE((char *)this,"RmcNwReqHdlr",pcVar21);
          goto LAB_00494be0;
        }
        pcVar21 = (char *)0x10;
        uVar18 = strtoul(__nptr,(char **)0x0,0x10);
        iVar11 = RfxAtLine::atTokNextint(this_00,&local_17c);
        if (local_17c < 0) goto LAB_00494be0;
        if (iVar11 == 0) {
          uVar20 = 1;
        }
        else if (iVar11 == 7) {
          uVar20 = 3;
        }
        else {
          if (iVar11 != 2) {
            uVar20 = 0;
            *(undefined4 *)((long)pvVar13 + lVar22) = 0;
            uVar16 = __ptr[1];
            uVar10 = 0;
            if (uVar16 == 0) goto LAB_00494c74;
            goto LAB_00494bec;
          }
          uVar20 = 4;
        }
        *(undefined4 *)((long)pvVar13 + lVar22) = uVar20;
        iVar11 = __strlen_chk(&local_90,8);
        if (iVar11 == 6) {
          iVar19 = (uint)(byte)local_90 * 100 + (uint)local_90._1_1_ * 10 + (uint)local_90._2_1_;
          iVar11 = (uint)local_90._4_1_ * 10 + (uint)local_90._3_1_ * 100 +
                   (local_90._5_1_ - 0x14d0);
          uVar20 = 3;
        }
        else {
          if (iVar11 != 5) {
            pcVar21 = "The length of the numeric code is incorrect";
            goto LAB_00494bd8;
          }
          iVar19 = (uint)(byte)local_90 * 100 + (uint)local_90._1_1_ * 10 + (uint)local_90._2_1_;
          uVar20 = 2;
          iVar11 = (uint)local_90._4_1_ + (uint)local_90._3_1_ * 10 + -0x210;
        }
        iVar1 = *(int *)((long)pvVar13 + lVar22);
        iVar19 = iVar19 + -0x14d0;
        uVar17 = (uint)uVar18;
        if (iVar1 == 4) {
          pRVar15 = (RfxBaseData *)&local_f0;
          *(int *)((long)pvVar13 + lVar22 + 0x18) = iVar19;
          *(int *)((long)pvVar13 + lVar22 + 0x1c) = iVar11;
          *(undefined4 *)((long)pvVar13 + lVar22 + 0x20) = uVar20;
          *(uint *)((long)pvVar13 + lVar22 + 0x24) = uVar17;
          RmcNetworkHandler::getOperatorNamesFromNumericCode
                    ((RmcNetworkHandler *)this,(char *)&local_90,uVar17,(char *)pRVar15,
                     (char *)&local_150,0x5c,0);
          asprintf((char **)((long)pvVar13 + lVar22 + 0x38),"%s",&local_f0);
          pvVar13 = *(void **)(__ptr + 2);
          pcVar21 = (char *)&local_150;
          asprintf((char **)((long)pvVar13 + lVar22 + 0x40),"%s");
          *(undefined8 *)((long)pvVar13 + lVar22 + 0x50) = uVar6;
          *(undefined8 *)((long)pvVar13 + lVar22 + 0x48) = uVar5;
        }
        else if (iVar1 == 3) {
          pRVar15 = (RfxBaseData *)&local_f0;
          *(int *)((long)pvVar13 + lVar22 + 0x18) = iVar19;
          *(int *)((long)pvVar13 + lVar22 + 0x1c) = iVar11;
          *(undefined4 *)((long)pvVar13 + lVar22 + 0x20) = uVar20;
          *(uint *)((long)pvVar13 + lVar22 + 0x2c) = uVar17;
          RmcNetworkHandler::getOperatorNamesFromNumericCode
                    ((RmcNetworkHandler *)this,(char *)&local_90,uVar17,(char *)pRVar15,
                     (char *)&local_150,0x5c,0);
          asprintf((char **)((long)pvVar13 + lVar22 + 0x38),"%s",&local_f0);
          pvVar13 = *(void **)(__ptr + 2);
          pcVar21 = (char *)&local_150;
          asprintf((char **)((long)pvVar13 + lVar22 + 0x40),"%s");
          *(undefined8 *)((long)pvVar13 + lVar22 + 0x60) = 0x7fffffff7fffffff;
          *(undefined8 *)((long)pvVar13 + lVar22 + 0x58) = uVar8;
          *(undefined8 *)((long)pvVar13 + lVar22 + 0x50) = uVar7;
          *(undefined4 *)((long)pvVar13 + lVar22 + 0x68) = 0x7fffffff;
        }
        else if (iVar1 == 1) {
          pRVar15 = (RfxBaseData *)&local_f0;
          *(int *)((long)pvVar13 + lVar22 + 0x18) = iVar19;
          *(int *)((long)pvVar13 + lVar22 + 0x1c) = iVar11;
          *(undefined4 *)((long)pvVar13 + lVar22 + 0x20) = uVar20;
          *(uint *)((long)pvVar13 + lVar22 + 0x24) = uVar17;
          RmcNetworkHandler::getOperatorNamesFromNumericCode
                    ((RmcNetworkHandler *)this,(char *)&local_90,uVar17,(char *)pRVar15,
                     (char *)&local_150,0x5c,0);
          asprintf((char **)((long)pvVar13 + lVar22 + 0x38),"%s",&local_f0);
          pvVar13 = *(void **)(__ptr + 2);
          pcVar21 = (char *)&local_150;
          asprintf((char **)((long)pvVar13 + lVar22 + 0x40),"%s");
          *(undefined8 *)((long)pvVar13 + lVar22 + 0x48) = 0x6300000063;
          *(undefined4 *)((long)pvVar13 + lVar22 + 0x50) = 0x7fffffff;
        }
        lVar22 = lVar22 + 0x90;
      } while (((ulong)uVar16 + (ulong)uVar16 * 8) * 0x10 != lVar22);
      if (0 < (int)uVar16) {
        iVar11 = *(int *)(this + 0x10);
        RfxNetworkScanResultData::RfxNetworkScanResultData
                  ((RfxNetworkScanResultData *)local_168,__ptr,0x10);
        RfxMclMessage::obtainUrc((RfxMclMessage *)0xc7bb,iVar11,(int)local_168,pRVar15);
        plVar9 = local_170;
        if (local_170 != (long *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)((long)local_170 + *(long *)(*local_170 + -0x18)),&local_170);
        }
        if (plVar9 != local_170) {
          android::sp_report_race();
        }
        local_170 = local_188;
        local_188 = (long *)0x0;
        RfxNetworkScanResultData::~RfxNetworkScanResultData((RfxNetworkScanResultData *)local_168);
        RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_170);
      }
    }
    uVar20 = 1;
    uVar16 = __ptr[1];
  }
  uVar10 = uVar20;
  if (uVar16 != 0) {
LAB_00494bec:
    uVar20 = uVar10;
    if ((*(long *)(__ptr + 2) != 0) && (0 < (int)uVar16)) {
      lVar22 = 0;
      do {
        lVar14 = *(long *)(__ptr + 2);
        iVar11 = *(int *)(lVar14 + lVar22);
        if (((iVar11 == 4) || (iVar11 == 3)) || (iVar11 == 1)) {
          pvVar13 = *(void **)(lVar14 + lVar22 + 0x38);
          if (pvVar13 != (void *)0x0) {
            free(pvVar13);
            lVar14 = *(long *)(__ptr + 2);
          }
          pvVar13 = *(void **)(lVar14 + lVar22 + 0x40);
          if (pvVar13 != (void *)0x0) {
            free(pvVar13);
          }
        }
        lVar22 = lVar22 + 0x90;
      } while ((ulong)uVar16 * 0x90 - lVar22 != 0);
    }
  }
LAB_00494c74:
  if (*(void **)(__ptr + 2) != (void *)0x0) {
    free(*(void **)(__ptr + 2));
  }
  free(__ptr);
  if (local_178 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_178 + *(long *)(*local_178 + -0x18)),&local_178);
  }
  if (local_170 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_170 + *(long *)(*local_170 + -0x18)),&local_170);
  }
LAB_00494cc0:
  if (*(long *)(lVar3 + 0x28) == local_88) {
    return uVar20;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


