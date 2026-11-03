
/* RmcNrSwitchHandler::RmcNrSwitchHandler(int, int) */

void __thiscall
RmcNrSwitchHandler::RmcNrSwitchHandler(RmcNrSwitchHandler *this,int param_1,int param_2)

{
  uint uVar1;
  undefined *puVar2;
  long lVar3;
  int iVar4;
  int iVar5;
  String8 aSStack_e0 [8];
  long *local_d8;
  char local_d0 [96];
  char local_70 [5];
  char acStack_6b [3];
  char acStack_68 [8];
  char local_60 [8];
  char cStack_58;
  char acStack_57 [7];
  char cStack_50;
  char acStack_4f [23];
  long local_38;
  
  cStack_58 = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[8];
  local_60 = (char  [8])s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0._0_8_;
  lVar3 = tpidr_el0;
  local_38 = *(long *)(lVar3 + 0x28);
  puVar2 = PTR_vtable_005d3fc8 + 0x10;
  *(int *)(this + 8) = param_1;
  *(int *)(this + 0xc) = param_2;
  *(int *)(this + 0x10) = param_1;
  *(int *)(this + 0x14) = param_2;
  *(undefined **)this = puVar2;
  local_70[0] = s_5G_SUPPORTED_0021e236[0];
  local_70[1] = s_5G_SUPPORTED_0021e236[1];
  local_70[2] = s_5G_SUPPORTED_0021e236[2];
  local_70[3] = s_5G_SUPPORTED_0021e236[3];
  local_70[4] = s_5G_SUPPORTED_0021e236[4];
  acStack_6b[0] = s_5G_SUPPORTED_0021e236[5];
  acStack_6b[1] = s_5G_SUPPORTED_0021e236[6];
  acStack_6b[2] = s_5G_SUPPORTED_0021e236[7];
  acStack_68[0] = s_5G_SUPPORTED_0021e236[8];
  acStack_68[1] = s_5G_SUPPORTED_0021e236[9];
  acStack_68[2] = s_5G_SUPPORTED_0021e236[10];
  acStack_68[3] = s_5G_SUPPORTED_0021e236[0xb];
  acStack_68[4] = s_5G_SUPPORTED_0021e236[0xc];
  acStack_4f[0] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0x11];
  acStack_4f[1] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0x12];
  acStack_4f[2] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0x13];
  acStack_4f[3] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0x14];
  acStack_4f[4] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0x15];
  acStack_4f[5] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0x16];
  acStack_4f[6] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0x17];
  acStack_4f[7] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0x18];
  acStack_57[0] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[9];
  acStack_57[1] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[10];
  acStack_57[2] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0xb];
  acStack_57[3] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0xc];
  acStack_57[4] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0xd];
  acStack_57[5] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0xe];
  acStack_57[6] = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0xf];
  cStack_50 = s_GEMINI_NR_PLUS_SUPPORTED_0020a7f0[0x10];
  *(undefined4 *)(this + 0x18) = 1;
  RfxBaseHandler::registerToHandleRequest((RfxBaseHandler *)this,(int *)&DAT_00293220,1);
  iVar4 = RfxBaseHandler::getFeatureVersion((RfxBaseHandler *)this,local_70,0);
  if ((iVar4 == 1) &&
     (iVar4 = RfxBaseHandler::getFeatureVersion((RfxBaseHandler *)this,local_60,0), iVar4 == 0)) {
    local_d0[0x54] = '\0';
    local_d0[0x55] = '\0';
    local_d0[0x56] = '\0';
    local_d0[0x57] = '\0';
    local_d0[0x58] = '\0';
    local_d0[0x59] = '\0';
    local_d0[0x5a] = '\0';
    local_d0[0x5b] = '\0';
    local_d0[0x50] = '\0';
    local_d0[0x51] = '\0';
    local_d0[0x52] = '\0';
    local_d0[0x53] = '\0';
    local_d0[0x18] = '\0';
    local_d0[0x19] = '\0';
    local_d0[0x1a] = '\0';
    local_d0[0x1b] = '\0';
    local_d0[0x1c] = '\0';
    local_d0[0x1d] = '\0';
    local_d0[0x1e] = '\0';
    local_d0[0x1f] = '\0';
    local_d0[0x10] = '\0';
    local_d0[0x11] = '\0';
    local_d0[0x12] = '\0';
    local_d0[0x13] = '\0';
    local_d0[0x14] = '\0';
    local_d0[0x15] = '\0';
    local_d0[0x16] = '\0';
    local_d0[0x17] = '\0';
    local_d0[0x28] = '\0';
    local_d0[0x29] = '\0';
    local_d0[0x2a] = '\0';
    local_d0[0x2b] = '\0';
    local_d0[0x2c] = '\0';
    local_d0[0x2d] = '\0';
    local_d0[0x2e] = '\0';
    local_d0[0x2f] = '\0';
    local_d0[0x20] = '\0';
    local_d0[0x21] = '\0';
    local_d0[0x22] = '\0';
    local_d0[0x23] = '\0';
    local_d0[0x24] = '\0';
    local_d0[0x25] = '\0';
    local_d0[0x26] = '\0';
    local_d0[0x27] = '\0';
    local_d0[0x38] = '\0';
    local_d0[0x39] = '\0';
    local_d0[0x3a] = '\0';
    local_d0[0x3b] = '\0';
    local_d0[0x3c] = '\0';
    local_d0[0x3d] = '\0';
    local_d0[0x3e] = '\0';
    local_d0[0x3f] = '\0';
    local_d0[0x30] = '\0';
    local_d0[0x31] = '\0';
    local_d0[0x32] = '\0';
    local_d0[0x33] = '\0';
    local_d0[0x34] = '\0';
    local_d0[0x35] = '\0';
    local_d0[0x36] = '\0';
    local_d0[0x37] = '\0';
    local_d0[0x48] = '\0';
    local_d0[0x49] = '\0';
    local_d0[0x4a] = '\0';
    local_d0[0x4b] = '\0';
    local_d0[0x4c] = '\0';
    local_d0[0x4d] = '\0';
    local_d0[0x4e] = '\0';
    local_d0[0x4f] = '\0';
    local_d0[0x40] = '\0';
    local_d0[0x41] = '\0';
    local_d0[0x42] = '\0';
    local_d0[0x43] = '\0';
    local_d0[0x44] = '\0';
    local_d0[0x45] = '\0';
    local_d0[0x46] = '\0';
    local_d0[0x47] = '\0';
    local_d0[8] = '\0';
    local_d0[9] = '\0';
    local_d0[10] = '\0';
    local_d0[0xb] = '\0';
    local_d0[0xc] = '\0';
    local_d0[0xd] = '\0';
    local_d0[0xe] = '\0';
    local_d0[0xf] = '\0';
    local_d0[0] = '\0';
    local_d0[1] = '\0';
    local_d0[2] = '\0';
    local_d0[3] = '\0';
    local_d0[4] = '\0';
    local_d0[5] = '\0';
    local_d0[6] = '\0';
    local_d0[7] = '\0';
    rfx_property_get("persist.vendor.radio.simswitch",local_d0,&DAT_002240c5);
    iVar4 = atoi(local_d0);
    iVar5 = getNrPsSlot(this);
    if (*(int *)(this + 0x10) == iVar4 + -1) {
      uVar1 = iVar5 + 1;
      if (iVar5 == -1) {
        uVar1 = 1;
      }
      android::String8::format("AT+EPSCONFIG=1,\"nr_ps\",%d",aSStack_e0,(ulong)uVar1);
      RfxBaseHandler::atSendCommand((String8 *)this,aSStack_e0);
      if (local_d8 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_d8 + *(long *)(*local_d8 + -0x18)),&local_d8);
      }
      android::String8::~String8(aSStack_e0);
    }
  }
  if (*(long *)(lVar3 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


