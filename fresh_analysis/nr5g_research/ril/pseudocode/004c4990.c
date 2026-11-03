
/* RmcCommSimRequestHandler::getPersoRemainRetry(int, RIL_SimMeLockInfo*) */

ulong __thiscall
RmcCommSimRequestHandler::getPersoRemainRetry
          (RmcCommSimRequestHandler *this,int param_1,RIL_SimMeLockInfo *param_2)

{
  long lVar1;
  long *plVar2;
  undefined4 uVar3;
  char *pcVar4;
  char *pcVar5;
  ulong uVar6;
  ulong uVar7;
  RfxAtLine *this_00;
  undefined4 *puVar8;
  long *local_190;
  long *local_188;
  int local_17c;
  undefined8 local_178;
  undefined8 local_170;
  undefined8 uStack_168;
  undefined8 uStack_160;
  undefined8 uStack_158;
  undefined8 local_150;
  undefined8 uStack_148;
  undefined8 uStack_140;
  undefined8 uStack_138;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 uStack_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 uStack_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 uStack_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined8 uStack_78;
  long local_68;
  
  lVar1 = tpidr_el0;
  local_68 = *(long *)(lVar1 + 0x28);
  local_178 = 0;
  android::String8::String8((String8 *)&local_178,"");
  local_188 = (long *)0x0;
  local_17c = -1;
  if (6 < (uint)param_1) {
    RfxBaseHandler::logE
              ((char *)this,*(char **)(this + 0x10),"getPersoRemainRetry: invalid cat %d",
               (ulong)(uint)param_1);
    uVar7 = 0xffffffff;
    goto LAB_004c4c28;
  }
  uStack_168 = 0;
  local_170 = 0;
  uStack_158 = 0;
  uStack_160 = 0;
  uStack_148 = 0;
  local_150 = 0;
  uStack_138 = 0;
  uStack_140 = 0;
  uStack_128 = 0;
  local_130 = 0;
  uStack_118 = 0;
  uStack_120 = 0;
  uStack_108 = 0;
  local_110 = 0;
  uStack_f8 = 0;
  uStack_100 = 0;
  uStack_e8 = 0;
  local_f0 = 0;
  uStack_d8 = 0;
  uStack_e0 = 0;
  uStack_c8 = 0;
  local_d0 = 0;
  uStack_b8 = 0;
  uStack_c0 = 0;
  uStack_a8 = 0;
  local_b0 = 0;
  uStack_98 = 0;
  uStack_a0 = 0;
  uStack_88 = 0;
  local_90 = 0;
  uStack_78 = 0;
  uStack_80 = 0;
  android::String8::format("AT+ESMLCK",&local_190);
  android::String8::append((String8 *)&local_178);
  android::String8::~String8((String8 *)&local_190);
  RfxBaseHandler::atSendCommandSingleline((String8 *)this,(char *)&local_178,"+ESMLCK:");
  plVar2 = local_188;
  if (local_188 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_188 + *(long *)(*local_188 + -0x18)),&local_188);
  }
  if (plVar2 != local_188) {
    android::sp_report_race();
  }
  local_188 = local_190;
  android::String8::clear();
  local_17c = (int)local_188[5];
  if ((local_17c < 0) || (*(int *)((long)local_188 + 0xc) == 0)) {
    pcVar4 = *(char **)(this + 0x10);
    pcVar5 = "getPersoRemainRetry AT+ESMLCK error!";
LAB_004c4bd8:
    RfxBaseHandler::logE((char *)this,pcVar4,pcVar5);
LAB_004c4be0:
    uVar7 = 0xffffffff;
  }
  else {
    this_00 = (RfxAtLine *)local_188[4];
    RfxAtLine::atTokStart(this_00,&local_17c);
    if (local_17c < 0) {
      pcVar4 = *(char **)(this + 0x10);
      pcVar5 = "getPersoRemainRetry atTokStart error!";
      goto LAB_004c4bd8;
    }
    uVar6 = (ulong)(param_1 + 1);
    uVar7 = 0;
    if (param_2 != (RIL_SimMeLockInfo *)0x0) {
      uVar6 = 7;
    }
    puVar8 = (undefined4 *)((ulong)&local_170 | 0xc);
    do {
      RfxAtLine::atTokChar(this_00,&local_17c);
      if (local_17c < 0) {
LAB_004c4c80:
        RfxBaseHandler::logE
                  ((char *)this,*(char **)(this + 0x10),"getPersoRemainRetry parse entry %d error!",
                   uVar7 & 0xffffffff);
        if (local_17c < 0) goto LAB_004c4be0;
        break;
      }
      uVar3 = RfxAtLine::atTokNextint(this_00,&local_17c);
      puVar8[-3] = uVar3;
      if (local_17c < 0) goto LAB_004c4c80;
      uVar3 = RfxAtLine::atTokNextint(this_00,&local_17c);
      puVar8[-2] = uVar3;
      if (local_17c < 0) goto LAB_004c4c80;
      uVar3 = RfxAtLine::atTokNextint(this_00,&local_17c);
      puVar8[-1] = uVar3;
      if (local_17c < 0) goto LAB_004c4c80;
      uVar3 = RfxAtLine::atTokNextint(this_00,&local_17c);
      *puVar8 = uVar3;
      if (local_17c < 0) goto LAB_004c4c80;
      uVar3 = RfxAtLine::atTokNextint(this_00,&local_17c);
      puVar8[1] = uVar3;
      if (local_17c < 0) goto LAB_004c4c80;
      uVar3 = RfxAtLine::atTokNextint(this_00,&local_17c);
      puVar8[2] = uVar3;
      if (local_17c < 0) goto LAB_004c4c80;
      uVar3 = RfxAtLine::atTokNextint(this_00,&local_17c);
      puVar8[3] = uVar3;
      if (local_17c < 0) goto LAB_004c4c80;
      uVar7 = uVar7 + 1;
      puVar8 = puVar8 + 7;
    } while (uVar6 != uVar7);
    uVar7 = (ulong)*(uint *)((long)&uStack_168 + (ulong)(uint)param_1 * 0x1c);
    RfxBaseHandler::logD
              ((char *)this,*(char **)(this + 0x10),"getPersoRemainRetry retryCount = %d",uVar7);
  }
  if (param_2 != (RIL_SimMeLockInfo *)0x0) {
    *(undefined8 *)(param_2 + 200) = uStack_a8;
    *(undefined8 *)(param_2 + 0xc0) = local_b0;
    *(undefined8 *)(param_2 + 0xd8) = uStack_98;
    *(undefined8 *)(param_2 + 0xd0) = uStack_a0;
    *(undefined8 *)(param_2 + 0xe8) = uStack_88;
    *(undefined8 *)(param_2 + 0xe0) = local_90;
    *(undefined8 *)(param_2 + 0xf8) = uStack_78;
    *(undefined8 *)(param_2 + 0xf0) = uStack_80;
    *(undefined8 *)(param_2 + 0x88) = uStack_e8;
    *(undefined8 *)(param_2 + 0x80) = local_f0;
    *(undefined8 *)(param_2 + 0x98) = uStack_d8;
    *(undefined8 *)(param_2 + 0x90) = uStack_e0;
    *(undefined8 *)(param_2 + 0xa8) = uStack_c8;
    *(undefined8 *)(param_2 + 0xa0) = local_d0;
    *(undefined8 *)(param_2 + 0xb8) = uStack_b8;
    *(undefined8 *)(param_2 + 0xb0) = uStack_c0;
    *(undefined8 *)(param_2 + 0x48) = uStack_128;
    *(undefined8 *)(param_2 + 0x40) = local_130;
    *(undefined8 *)(param_2 + 0x58) = uStack_118;
    *(undefined8 *)(param_2 + 0x50) = uStack_120;
    *(undefined8 *)(param_2 + 0x68) = uStack_108;
    *(undefined8 *)(param_2 + 0x60) = local_110;
    *(undefined8 *)(param_2 + 0x78) = uStack_f8;
    *(undefined8 *)(param_2 + 0x70) = uStack_100;
    *(undefined8 *)(param_2 + 8) = uStack_168;
    *(undefined8 *)param_2 = local_170;
    *(undefined8 *)(param_2 + 0x18) = uStack_158;
    *(undefined8 *)(param_2 + 0x10) = uStack_160;
    *(undefined8 *)(param_2 + 0x28) = uStack_148;
    *(undefined8 *)(param_2 + 0x20) = local_150;
    *(undefined8 *)(param_2 + 0x38) = uStack_138;
    *(undefined8 *)(param_2 + 0x30) = uStack_140;
  }
LAB_004c4c28:
  if (local_188 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_188 + *(long *)(*local_188 + -0x18)),&local_188);
  }
  android::String8::~String8((String8 *)&local_178);
  if (*(long *)(lVar1 + 0x28) == local_68) {
    return uVar7;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


