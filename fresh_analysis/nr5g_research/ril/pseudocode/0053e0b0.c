
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* RmcCallControlCommonRequestHandler::RmcCallControlCommonRequestHandler(int, int) */

void __thiscall
RmcCallControlCommonRequestHandler::RmcCallControlCommonRequestHandler
          (RmcCallControlCommonRequestHandler *this,int param_1,int param_2)

{
  long lVar1;
  long lVar2;
  long *plVar3;
  RmcCallControlImsRequestHandler *this_00;
  long *local_188;
  long *local_180;
  long *local_178;
  long *local_170;
  long *local_168;
  long *local_160;
  long *local_158;
  long *local_150;
  long *local_148;
  long *local_140;
  long *local_138;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 uStack_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 uStack_f0;
  undefined4 uStack_e8;
  undefined4 local_e4;
  undefined4 uStack_e0;
  undefined8 uStack_dc;
  int local_c8 [2];
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  long local_48;
  
  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  RmcCallControlBaseHandler::RmcCallControlBaseHandler
            ((RmcCallControlBaseHandler *)this,param_1,param_2);
  *(undefined **)this = PTR_vtable_005d4430 + 0x10;
  memset(this + 0x10,0,0x2298);
  uStack_78 = _UNK_00293e60;
  local_80 = _DAT_00293e58;
  uStack_68 = _UNK_00293e70;
  uStack_70 = _DAT_00293e68;
  uStack_58 = _UNK_00293e80;
  local_60 = _DAT_00293e78;
  uStack_b8 = _UNK_00293e20;
  local_c0 = _DAT_00293e18;
  uStack_a8 = _UNK_00293e30;
  uStack_b0 = _DAT_00293e28;
  uStack_dc = 0;
  uStack_e0 = 0;
  uStack_f8 = 0;
  local_100 = 0;
  uStack_e8 = 0;
  local_e4 = 0;
  uStack_f0 = 0;
  uStack_118 = 0;
  local_120 = 0;
  uStack_108 = 0;
  uStack_110 = 0;
  uStack_128 = 0;
  local_130 = 0;
  local_c8[0] = 0xc931;
  local_c8[1] = 0xc932;
  uStack_98 = _UNK_00293e40;
  local_a0 = _DAT_00293e38;
  uStack_88 = _UNK_00293e50;
  uStack_90 = _DAT_00293e48;
  mtkGetFeature(3,&local_130);
  if ((short)local_130 == 0x31) {
    RfxBaseHandler::atSendCommand((char *)this,"AT+ECFGSET=\"disable_cs_call\",\"0\"");
    if (local_140 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_140 + *(long *)(*local_140 + -0x18)),&local_140);
    }
    RfxBaseHandler::registerToHandleRequest((RfxBaseHandler *)this,(int *)&local_c0,0x1c);
    RfxBaseHandler::registerToHandleEvent((RfxBaseHandler *)this,local_c8,2);
    lVar2 = RfxGwsdUtils::getCallControlHandler();
    if (lVar2 != 0) {
      plVar3 = (long *)RfxGwsdUtils::getCallControlHandler();
      (**(code **)(*plVar3 + 0x10))(plVar3,this);
    }
    lVar2 = RfxGwsdUtils::getCallControlHandler();
    if (lVar2 != 0) {
      plVar3 = (long *)RfxGwsdUtils::getCallControlHandler();
      (**(code **)(*plVar3 + 0x18))(plVar3,this);
    }
    queryEmciSupport(this);
    this_00 = operator_new(0x10);
    RmcCallControlImsRequestHandler::RmcCallControlImsRequestHandler(this_00,param_1,param_2);
    *(RmcCallControlImsRequestHandler **)(this + 0x22a0) = this_00;
    RfxBaseHandler::atSendCommand((char *)this,"AT+CMOD=0");
    if (local_148 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_148 + *(long *)(*local_148 + -0x18)),&local_148);
    }
    RfxBaseHandler::atSendCommand((char *)this,"ATS0=0");
    if (local_150 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_150 + *(long *)(*local_150 + -0x18)),&local_150);
    }
    RfxBaseHandler::atSendCommand((char *)this,"AT+CCWA=1");
    if (local_158 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_158 + *(long *)(*local_158 + -0x18)),&local_158);
    }
    RfxBaseHandler::atSendCommand((char *)this,"AT+CMUT=0");
    if (local_160 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_160 + *(long *)(*local_160 + -0x18)),&local_160);
    }
    RfxBaseHandler::atSendCommand((char *)this,"AT+ECPI=4294967295");
    if (local_168 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_168 + *(long *)(*local_168 + -0x18)),&local_168);
    }
    RfxBaseHandler::atSendCommand((char *)this,"AT+EAIC=2");
    if (local_170 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_170 + *(long *)(*local_170 + -0x18)),&local_170);
    }
    RfxBaseHandler::atSendCommand((char *)this,"AT+EVOCD=1");
    if (local_178 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_178 + *(long *)(*local_178 + -0x18)),&local_178);
    }
    RfxBaseHandler::atSendCommand((char *)this,"AT+ECCP=1");
    if (local_180 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_180 + *(long *)(*local_180 + -0x18)),&local_180);
    }
    RfxBaseHandler::atSendCommand((char *)this,"AT+EECCFC=1");
    if (local_188 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_188 + *(long *)(*local_188 + -0x18)),&local_188);
    }
    *(undefined8 *)(this + 0x2298) = 0;
  }
  else {
    RfxBaseHandler::logE
              ((char *)this,"RmcCCReqHandler","CONFIG_CC is set to 0, disable voice call feature!");
    RfxBaseHandler::atSendCommand((char *)this,"AT+ECFGSET=\"disable_cs_call\",\"1\"");
    if (local_138 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_138 + *(long *)(*local_138 + -0x18)),&local_138);
    }
  }
  if (*(long *)(lVar1 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


