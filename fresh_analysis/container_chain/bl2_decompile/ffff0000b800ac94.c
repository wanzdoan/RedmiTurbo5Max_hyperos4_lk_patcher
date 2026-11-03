
undefined8 FUN_ffff0000b800ac94(long param_1,uint param_2)

{
  char cVar1;
  undefined8 uVar2;
  ulong uVar3;
  undefined *puVar4;
  int iVar5;
  
  puVar4 = PTR_DAT_ffff0000b8108680;
  uVar2 = daif;
  if (((uint)uVar2 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 | 0x80;
  }
  cVar1 = *(char *)(param_1 + 4);
  *(undefined8 *)PTR_DAT_ffff0000b8108680 = 1;
  if (cVar1 == '\0') {
    if ((*(byte *)(param_1 + 8) & 1) == 0) {
      *(undefined1 *)(param_1 + 4) = 1;
      FUN_ffff0000b800b500(param_1 + 0x10,param_2 & 1,0);
    }
    else {
      iVar5 = FUN_ffff0000b800bb9c(param_1 + 0x10,param_2 & 1,0);
      if (iVar5 < 1) {
        *(undefined1 *)(param_1 + 4) = 1;
      }
    }
  }
  *(undefined8 *)puVar4 = 0;
  if (((uint)uVar2 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 & 0xffffffffffffff7f;
  }
  return 0;
}


