// File offset entry: 0xa7248

ulong FUN_020a7148(undefined4 param_1)

{
  uint uVar1;
  int *piVar2;
  undefined4 uVar3;
  int iVar4;
  ulong uVar5;
  undefined8 uVar6;
  
  uVar3 = FUN_020a7cec();
  FUN_0208ada0(0x20d01dd,uVar3);
  iVar4 = FUN_020a7cec();
  if (iVar4 == 0) {
    iVar4 = FUN_020a0e58(*puRam0000000002103820,0x21240ec,uRam00000000021242ec);
    uVar1 = 0;
    if (iVar4 != 0) {
      uVar1 = 0x101017;
    }
    uVar5 = (ulong)uVar1;
  }
  else {
    FUN_0208ada0(0x20cefb0);
    piVar2 = piRam0000000002103810;
    uVar5 = FUN_0209c58c(0x21244f4,param_1,0x2123a5c,*piRam0000000002103810);
    if ((int)uVar5 == 0) {
      if (*piVar2 - 1U < 2) {
        uVar5 = func_0x020a7d10(0x2123a9c,0x30,*puRam0000000002103830);
        if ((int)uVar5 != 0) {
          return uVar5;
        }
        uVar6 = 0x30;
      }
      else {
        if (*piVar2 != 0) {
          return 0x10101b;
        }
        uVar5 = func_0x020a7d10(0x2123a9c,0x20,*puRam0000000002103830);
        if ((int)uVar5 != 0) {
          return uVar5;
        }
        uVar6 = 0x20;
      }
      iVar4 = FUN_020a0e58(0x2123a5c,0x2123a9c,uVar6);
      uVar1 = 0;
      if (iVar4 != 0) {
        uVar1 = 0x101017;
      }
      uVar5 = (ulong)uVar1;
    }
  }
  return uVar5;
}


