// File offset entry: 0xa2e70

undefined8 FUN_020a2d70(void)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  undefined8 uVar4;
  
  iVar3 = func_0x020a7efc();
  if (iVar3 == 0) {
    uVar1 = uRam000000001002d8f0 & 0x40000;
    uVar2 = uRam000000001002d8f0 & 0x80000;
    if ((uRam0000000013260058 & 3) != 0) {
      uVar1 = uRam0000000013260058 & 2;
      uVar2 = uRam0000000013260058 & 1;
    }
    if (uVar2 == 0 || uVar1 != 0) {
      uVar1 = uRam000000001002d8f0 & 0x40000;
      uVar2 = uRam000000001002d8f0 & 0x80000;
      if ((uRam0000000013260058 & 3) != 0) {
        uVar1 = uRam0000000013260058 & 2;
        uVar2 = uRam0000000013260058 & 1;
      }
      if ((uVar2 == 0) || (uVar1 == 0)) goto LAB_020a2d84;
    }
    uVar4 = 1;
  }
  else {
LAB_020a2d84:
    uVar4 = 0;
  }
  return uVar4;
}


