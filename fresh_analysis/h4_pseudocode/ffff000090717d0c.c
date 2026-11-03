
undefined8 FUN_ffff000090717d0c(undefined4 *param_1,undefined4 *param_2,undefined4 param_3)

{
  undefined8 *puVar1;
  ulong uVar2;
  undefined8 uVar3;
  uint uVar4;
  
  uVar3 = daif;
  uVar4 = (uint)uVar3;
  if ((uVar4 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 | 0x80;
    if ((*(byte *)(param_1 + 0xd) & 1) != 0) {
      uVar2 = daif;
      daif = uVar2 & 0xffffffffffffff7f;
      DAT_ffff0000908436b0 = 0;
      return 0xffffffde;
    }
  }
  else if ((*(byte *)(param_1 + 0xd) & 1) != 0) {
    DAT_ffff0000908436b0 = 0;
    return 0xffffffde;
  }
  DAT_ffff0000908436b0 = 1;
  if ((param_1[0xb] == 5) || (uVar3 = FUN_ffff000090717e20(param_1 + 0xa4,param_3), -1 < (int)uVar3)
     ) {
    if (param_2 != (undefined4 *)0x0) {
      *param_2 = param_1[0xa2];
    }
    puVar1 = *(undefined8 **)(param_1 + 4);
    *param_1 = 0;
    DAT_ffff0000908436b0 = 0;
    *puVar1 = *(undefined8 *)(param_1 + 2);
    *(undefined8 **)(*(long *)(param_1 + 2) + 8) = puVar1;
    *(undefined8 *)(param_1 + 2) = 0;
    *(undefined8 *)(param_1 + 4) = 0;
    if ((uVar4 >> 7 & 1) == 0) {
      uVar2 = daif;
      daif = uVar2 & 0xffffffffffffff7f;
    }
    uVar4 = param_1[0xd];
    if (((uVar4 >> 1 & 1) != 0) && (*(long *)(param_1 + 0x9a) != 0)) {
      thunk_FUN_ffff0000907c314c();
      uVar4 = param_1[0xd];
    }
    if ((uVar4 >> 2 & 1) != 0) {
      thunk_FUN_ffff0000907c314c(param_1);
    }
    uVar3 = 0;
  }
  else {
    DAT_ffff0000908436b0 = 0;
    if ((uVar4 >> 7 & 1) == 0) {
      uVar2 = daif;
      daif = uVar2 & 0xffffffffffffff7f;
    }
  }
  return uVar3;
}


