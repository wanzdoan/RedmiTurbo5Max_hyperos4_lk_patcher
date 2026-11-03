// File offset entry: 0xa6d9c

undefined4 FUN_020a6c9c(byte param_1,undefined8 *param_2,uint *param_3,int *param_4,int param_5)

{
  undefined4 uVar1;
  byte bVar2;
  uint uVar3;
  int iVar4;
  
  if ((int)uRam0000000002123ae8 < 1) {
    return 0x101012;
  }
  FUN_020a0fec(pbRam0000000002123ae0,0x10);
  iVar4 = *param_4;
  *param_3 = 0;
  *param_4 = 0;
  func_0x020a6f34(pbRam0000000002123ae0,param_3,param_4);
  bVar2 = *pbRam0000000002123ae0;
  uVar1 = 0;
  if (bVar2 != param_1) {
    uVar1 = 0x10100d;
  }
  if ((iVar4 != 0) && (iVar4 != *param_4)) {
    uVar1 = 0x10100e;
  }
  if (param_5 == 0) {
    uVar3 = *param_3 + 1;
    if ((bVar2 >> 5 & 1) == 0) {
      iVar4 = *param_4;
      goto LAB_020a6d88;
    }
  }
  else if (param_5 == 1) {
    uVar3 = *param_3;
    iVar4 = 1;
    if (bVar2 == 3) {
      iVar4 = 2;
    }
LAB_020a6d88:
    uVar3 = uVar3 + iVar4;
  }
  else {
    if (param_5 != 2) {
      uVar3 = 0;
      uVar1 = 0x10100f;
      goto LAB_020a6db0;
    }
    uVar3 = *param_3 + *param_4 + 1;
  }
  if (uRam0000000002123ae8 < uVar3) {
    uRam0000000002123ae8 = 0;
    return 0x101028;
  }
LAB_020a6db0:
  *param_2 = pbRam0000000002123ae0;
  uRam0000000002123ae8 = uRam0000000002123ae8 - uVar3;
  pbRam0000000002123ae0 = pbRam0000000002123ae0 + uVar3;
  return uVar1;
}


