
undefined8 FUN_ffff0000b800ae28(long param_1)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  undefined *puVar4;
  
  puVar4 = PTR_DAT_ffff0000b8108680;
  uVar2 = daif;
  if (((uint)uVar2 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 | 0x80;
  }
  *(undefined8 *)(param_1 + 8) = 0;
  iVar1 = *(int *)(param_1 + 0x10);
  *(undefined8 *)puVar4 = 1;
  *(int *)(param_1 + 0x10) = iVar1 + -1;
  if (iVar1 < 2) {
    *(undefined8 *)puVar4 = 0;
  }
  else {
    FUN_ffff0000b800bb9c(param_1 + 0x18,1,0);
    *(undefined8 *)puVar4 = 0;
  }
  if (((uint)uVar2 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 & 0xffffffffffffff7f;
  }
  return 0;
}


