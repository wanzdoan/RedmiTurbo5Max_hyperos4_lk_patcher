
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff0000b8068ec0(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  int iVar1;
  uint uVar2;
  int iVar3;
  uint uVar4;
  ulong uVar5;
  
  uVar4 = _DAT_ffff00001002d8f0 & 0x80000;
  uVar2 = _DAT_ffff00001002d8f0 & 0x40000;
  if ((_DAT_ffff000013260058 & 3) != 0) {
    uVar4 = _DAT_ffff000013260058 & 1;
    uVar2 = _DAT_ffff000013260058 & 2;
  }
  iVar3 = 1;
  if (uVar2 != 0) {
    iVar3 = 2;
  }
  iVar1 = 0;
  if (uVar4 != 0) {
    iVar1 = iVar3;
  }
  if (iVar1 == 0) {
    if (((((_DAT_ffff00001800d688 >> 3 & 1) != 0) && (_DAT_ffff000018010000 == 0x20000)) &&
        ((_DAT_ffff0000180100a0 & 1) != 0)) && ((_DAT_ffff0000180100a0 >> 2 & 1) == 0)) {
LAB_ffff0000b8068fe4:
      FUN_ffff0000b80075ac(param_2,0x200);
      FUN_ffff0000b80075ac(param_1,0x200);
      FUN_ffff0000b80075ac(param_3,0x200);
      uVar4 = FUN_ffff0000b8068d14(param_1,param_2,param_3);
      if (uVar4 != 0) {
        FUN_ffff0000b804c33c(s_hwrot_mailbox_rsa_fail_ffff0000b808b3cf);
      }
      FUN_ffff0000b80075cc(param_3,0x200);
      return (ulong)uVar4;
    }
  }
  else {
    if (iVar1 != 1) goto LAB_ffff0000b8068fa0;
    if ((_DAT_ffff000018010000 == 0x20000) && ((_DAT_ffff0000180100a0 & 1) != 0))
    goto LAB_ffff0000b8068fe4;
  }
  iVar3 = FUN_ffff0000b800e378();
  if (iVar3 == 1) {
    uVar5 = FUN_ffff0000b8061130(param_1,param_2,param_3);
    return uVar5;
  }
LAB_ffff0000b8068fa0:
  uVar5 = FUN_ffff0000b805f888(param_1,param_2,param_3);
  return uVar5;
}


